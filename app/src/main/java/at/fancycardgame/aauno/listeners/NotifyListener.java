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
        //Tools.showToast("Hello", 5);
        String message = new String(updateEvent.getUpdate());

        if (message.startsWith("TEST#")){
            String sender = message.substring(message.indexOf("#")+1, message.indexOf("@")).trim();
            String card =  message.substring(message.indexOf("@") + 1, message.indexOf("_"));
            if (!sender.equals(Util.userName)){
                String test = message.substring(0, message.indexOf("#")).trim();
                //String card =  message.substring(message.indexOf("@")+1, message.indexOf("_"));
                String chosenColor =  message.substring(message.indexOf("_")+1, message.indexOf("*"));
                String cardsToDraw =  message.substring(message.indexOf("*")+1, message.length());
                Log.d("updateEvent Sender", sender);
                Log.d("updateEvent Card", card);
                Log.d("updateEvent chosenColor", chosenColor);
                Log.d("updateEvent cardsToDraw", cardsToDraw);

                //Tools.game.playCard((View) card);
                //Tools.game.playSomeCard();

                Tools.game.setChosenColor(chosenColor);
                Tools.game.setCardsToDraw(cardsToDraw);
                Tools.game.playCardByName(card);
            }
/*            if (card.contains("Skip")){
                if (Tools.game.getCurrPlayer() == Tools.joinedPlayers.size() - 1){
                    // If last player in turn order has played a skip card
                    Tools.game.setNextPlayer(1);
                } else if (Tools.game.getCurrPlayer() == Tools.joinedPlayers.size() - 2){
                    // If second to last player in turn order has played a skip card
                    Tools.game.setNextPlayer(0);
                } else {
                    // If first or second player in turn order has played a skip card
                    Tools.game.setNextPlayer(Tools.game.getNextPlayer() + 1);
                }
            }*/

            // currPlayer == Tools.joinedPlayers.indexOf(sender)

            if (card.contains("Skip")){
                if (Tools.joinedPlayers.indexOf(sender) == Tools.joinedPlayers.size() - 1){
                    // If last player in turn order has played a skip card
                    Tools.game.setNextPlayer(1);
                } else if (Tools.joinedPlayers.indexOf(sender) == Tools.joinedPlayers.size() - 2){
                    // If second to last player in turn order has played a skip card
                    Tools.game.setNextPlayer(0);
                } else {
                    // If first or second player in turn order has played a skip card
                    Tools.game.setNextPlayer(Tools.game.getNextPlayer() + 1);
                }
            }
        }

        if (message.startsWith("NEXT")){
            //Tools.playersInRoom
            //Tools.game.setYourTurn(true);

            Tools.game.setYourTurn(false);

            String chosenColor = message.substring(message.indexOf("#") + 1, message.length()).trim();
            Tools.game.setChosenColor(chosenColor);

            //String currPlayer = "";

            if (Util.userName.equals(Tools.joinedPlayers.get(Tools.game.getNextPlayer()))) {
                Tools.game.setYourTurn(true);
                //currPlayer = Util.userName;
                //Tools.game.setCurrPlayer(Tools.joinedPlayers.indexOf(Util.userName));
            }

            //Tools.game.setCurrPlayer(Tools.joinedPlayers.indexOf(currPlayer));

            if (Tools.game.getNextPlayer() < Tools.joinedPlayers.size() - 1){
                //Tools.nextPlayer++;
                Tools.game.setNextPlayer(Tools.game.getNextPlayer() + 1);
            } else {
                //Tools.nextPlayer = 0;
                Tools.game.setNextPlayer(0);
            }

            Tools.game.setMadeTurn(false);
            // TODO: Implement currPlayer stuff (current player, is this needed?)
            /*if (Tools.game.getCurrPlayer() < Tools.joinedPlayers.size() - 1){
                //Tools.CurrPlayer++;
                Tools.game.setCurrPlayer(Tools.game.getNextPlayer() - 1);
            } else {
                //Tools.CurrPlayer = 0;
                Tools.game.setCurrPlayer(0);
            }*/

        }

        /*if (message.startsWith("TEST#")){
            String test = message.substring(0, message.indexOf("#")).trim();
            String card =  message.substring(message.indexOf('#')+1, message.length());
            Log.d("updateEvent Message", "" + card);
            //Tools.game.playCard((View) card);
            Tools.game.playSomeCard();
        }*/
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
