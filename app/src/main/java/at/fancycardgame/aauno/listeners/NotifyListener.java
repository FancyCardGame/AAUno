package at.fancycardgame.aauno.listeners;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.User;
import at.fancycardgame.aauno.toolbox.Constants;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 27.05.2015.
 */
public class NotifyListener implements com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener {
    @Override
    public void onRoomCreated(RoomData roomData) {
        Tools.currentRoom = roomData.getId();

    }

    @Override
    public void onRoomDestroyed(RoomData roomData) {

    }

    @Override
    public void onUserLeftRoom(RoomData roomData, String s) {
        Tools.joinedPlayers.remove(s);

        Tools.showToast(s + " left the room.", Toast.LENGTH_SHORT);
        Tools.game.updateJoinedPlayersListView();
        //list view in game activity should be updated
        //GameActivity.updateJoinedPlayersListView();
    }

    @Override
    public void onUserJoinedRoom(RoomData roomData, String s) {
        Tools.joinedPlayers.add(s);
        //this.updateJoinedPlayersListView();
        Tools.showToast("Someone joined!", Toast.LENGTH_SHORT);
        Tools.game.updateJoinedPlayersListView();

       if(roomData.getMaxUsers()==Tools.joinedPlayers.size() && roomData.getRoomOwner().equals(User.getUsername())) {
           // last user entered the room, room owner starts the game, get ready to play!
           Tools.wClient.sendUpdatePeers(Constants.PREP_TO_PLAY.getBytes());
           Tools.startGameCountDown();

           Tools.game.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Handler h = new Handler();
                   h.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Tools.wClient.sendUpdatePeers(Constants.WAIT_FOR_MIX.getBytes());
                       }
                   }, 4000);
               }
           });
       }
    }

    @Override
    public void onUserLeftLobby(LobbyData lobbyData, String s) {

    }

    @Override
    public void onUserJoinedLobby(LobbyData lobbyData, String s) {

    }

    @Override
    public void onChatReceived(ChatEvent chatEvent) {
        String sender = chatEvent.getSender();
        String recMsg = chatEvent.getMessage();

        Tools.chatQueue.add(" " + sender + ": " + recMsg);
        Tools.game.updateChatView();
    }

    @Override
    public void onPrivateChatReceived(String s, String s2) {

    }

    @Override
    public void onPrivateUpdateReceived(String s, byte[] bytes, boolean b) {

    }

    @Override
    public void onUpdatePeersReceived(UpdateEvent updateEvent) {
        String s = "";

        String message = new String(updateEvent.getUpdate());


        // Action if current player play a card
        if (message.startsWith("PLAY#")){
            boolean turnOrder = Boolean.parseBoolean(message.substring(message.indexOf("_") + 1, message.length()));
            String sender = message.substring(message.indexOf("#")+1, message.indexOf("@")).trim();
            String card =  message.substring(message.indexOf("@") + 1, message.indexOf("_")).trim();
            if (!sender.equals(Util.userName)){

                Log.d("updateEvent Sender", sender);
                Log.d("updateEvent Card", card);
                Log.d("updateEvent TO", "" + turnOrder);
                Tools.game.setTurnOrder(turnOrder);
                Tools.game.playCardByName(card);
            }
        }

        // Action if end turn button is pressed
        if (message.startsWith("NEXT")){

            boolean turnOrder = Tools.game.getTurnOrder();
            String sender = message.substring(message.indexOf("T") + 1, message.indexOf("#")).trim();
            boolean skip = Boolean.parseBoolean(message.substring(message.indexOf("+") + 1, message.length()).trim());


            if (turnOrder){
                // normal turn order
                Tools.game.setTurnOrderTxt("Normal");
                if (skip){
                    if (Tools.joinedPlayers.indexOf(sender) == Tools.joinedPlayers.size() - 1){
                        // If last player in turn order has played a skip card
                        Tools.game.setNextPlayer(1);
                    } else if (Tools.joinedPlayers.indexOf(sender) == Tools.joinedPlayers.size() - 2){
                        // If second to last player in turn order has played a skip card
                        Tools.game.setNextPlayer(0);
                    } else {
                        // If first or second player in turn order has played a skip card
                        Tools.game.setNextPlayer(Tools.joinedPlayers.indexOf(sender) + 2);
                    }
                    Tools.game.setSkip(false);
                } else {
                    if (Tools.joinedPlayers.indexOf(sender) < Tools.joinedPlayers.size() - 1){
                        Tools.game.setNextPlayer(Tools.joinedPlayers.indexOf(sender) + 1);
                    } else {
                        Tools.game.setNextPlayer(0);
                    }
                }
            } else {
                // reversed turn order
                Tools.game.setTurnOrderTxt("Reversed");
                if (skip){
                    if (Tools.joinedPlayers.indexOf(sender) == 0){
                        // If last player in reversed turn order has played a skip card
                        Tools.game.setNextPlayer(Tools.joinedPlayers.size() - 2);
                    } else if (Tools.joinedPlayers.indexOf(sender) == 1){
                        // If second to last player in reversed turn order has played a skip card
                        Tools.game.setNextPlayer(Tools.joinedPlayers.size() - 1);
                    } else {
                        // If first or second player in reversed turn order has played a skip card
                        Tools.game.setNextPlayer(Tools.game.getNextPlayer() - 1);
                    }
                    Tools.game.setSkip(false);
                } else {
                    if (Tools.joinedPlayers.indexOf(sender) > 0){
                        Tools.game.setNextPlayer(Tools.joinedPlayers.indexOf(sender) - 1);
                    } else {
                        Tools.game.setNextPlayer(Tools.joinedPlayers.size() - 1);
                    }
                }
            }

            // Allow next player to make his turn, disable actions for other players
            Tools.game.setYourTurn(false);
            Tools.game.setCurrPlayerTxt(Tools.joinedPlayers.get(Tools.game.getNextPlayer()));

            if (Util.userName.equals(Tools.joinedPlayers.get(Tools.game.getNextPlayer()))){
                Tools.game.setYourTurn(true);
                Tools.showToast("It's your turn!", Toast.LENGTH_SHORT);
            }

            // Chosen color has to be set here to ensure that there is input from the color chooser dialog
            String chosenColor = message.substring(message.indexOf("#") + 1, message.indexOf("@")).trim();
            Tools.game.setChosenColor(chosenColor);
            Tools.game.setColorTxt(chosenColor);
            String cardsToDraw = message.substring(message.indexOf("@") + 1, message.indexOf("_")).trim();
            Tools.game.setCardsToDraw(cardsToDraw);

            // Allow players to make a turn again
            Tools.game.setMadeTurn(false);
        }

        try {
            s = new String(updateEvent.getUpdate(), "UTF-8");
            Log.d("updateEvent", "" + s);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       //Tools.executeFromRemote("TEST")
       Tools.executeFromRemote(s);
    }

    @Override
    public void onUserChangeRoomProperty(RoomData roomData, String s, HashMap<String, Object> stringObjectHashMap, HashMap<String, String> stringStringHashMap) {

    }

    @Override
    public void onMoveCompleted(MoveEvent moveEvent) {

    }

    @Override
    public void onGameStarted(String s, String s2, String s3) {

    }

    @Override
    public void onGameStopped(String s, String s2) {

    }

    @Override
    public void onUserPaused(String s, boolean b, String s2) {

    }

    @Override
    public void onUserResumed(String s, boolean b, String s2) {

    }

    @Override
    public void onNextTurnRequest(String s) {

    }
}
