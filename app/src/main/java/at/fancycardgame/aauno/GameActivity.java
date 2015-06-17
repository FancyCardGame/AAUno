package at.fancycardgame.aauno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// import com.app.appwarplisterner.WarpListener;
import com.shephertz.app42.gaming.multiplayer.client.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import at.fancycardgame.aauno.adapters.JoinedPlayersAdapter;
import at.fancycardgame.aauno.toolbox.GameState;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Christian on 26.05.2015.
 */

public class GameActivity extends Activity {

    private final Context context = this;

    // the logical density of the display
    private static float density;

    public ViewGroup gameBoard;
    public ViewGroup game_activity_start;
    public ViewGroup game_activity_creategame;
    public ViewGroup game_activity_joingame;
    public ViewGroup game_activity_startedGameLobby;
    private ViewGroup deckPosition;

    private UnoCardDeck cardDeck;
    // Workaround for disappearing player cards, use another card deck for cards from others
    private UnoCardDeck opCardDeck;
    //List of lists of player cards
    //private ArrayList<ArrayList<UnoCard>> playerCards
    private ArrayList<UnoCard> playerCards;
    private ArrayList<UnoCard> playedCards;

    private String chosenColor = "";
    private int cardsToDraw = 0;

    private boolean madeTurn = false;
    private boolean hasDrawnCard = false;
    private boolean yourTurn = false;

    private static int nextPlayer;
    private static int currPlayer;

    private static TextView colorTxt;
    private static TextView currPlayerTxt;
    private static TextView turnOrderTxt;

    // true = normal, false = reversed
    private static boolean turnOrder;
    private static boolean skip = false;

    private Point res;

    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

        this.game_activity_start = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_start, null);
        setContentView(this.game_activity_start);

        findViewById(R.id.createGameMP).setOnClickListener(Tools.gameOnClickListner);
        findViewById(R.id.joinGameMP).setOnClickListener(Tools.gameOnClickListner);

        //preparing views
        this.game_activity_creategame = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_creategame, null);
        this.game_activity_joingame = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_joingame, null);
        this.game_activity_startedGameLobby = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_gamelobby, null);

        Tools.init(this.getApplicationContext());
        Tools.game = this;
    }

    public void startGame() {

        Button drawBtn = (Button) findViewById(R.id.drawBtn);
        drawBtn.setTypeface(Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book.ttf"));
        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasDrawnCard && !madeTurn && yourTurn) {
                    hasDrawnCard = true;
                    if (cardsToDraw == 0) {
                        drawCards(1);
                    } else {
                        drawCards(cardsToDraw);
                        cardsToDraw = 0;
                    }
                } else if (!yourTurn) {
                    Tools.showToast("It's not your turn!", Toast.LENGTH_SHORT);
                } else if(madeTurn) {
                    Tools.showToast("Your turn is over!", Toast.LENGTH_SHORT);
                } else {
                    Tools.showToast("You already drew a card!", Toast.LENGTH_SHORT);
                }
            }
        });

        Button endTurnButton = (Button) findViewById(R.id.endTurnBtn);
        endTurnButton.setTypeface(Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book.ttf"));
        endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((hasDrawnCard || madeTurn) && yourTurn) {
                    Tools.showToast("Ending turn ...", Toast.LENGTH_SHORT);
                    String msg = "NEXT" + Util.userName + "#" + chosenColor + "@" + cardsToDraw + "_" + turnOrder + "+" + skip;
                    //TODO: Use sendUpdate of GameActivity for this?
                    Tools.wClient.sendUpdatePeers(msg.getBytes());
                } else if (!yourTurn) {
                    Tools.showToast("It's not your turn!", Toast.LENGTH_SHORT);
                } else {
                    Tools.showToast("You have to play or draw a card!", Toast.LENGTH_SHORT);
                }
            }
        });

        // Button for testing
        Button testBtn = (Button) findViewById(R.id.testBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "yourTurn: " + isYourTurn(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, Tools.joinedPlayers.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "played cards: " + playedCards.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        // TextViews for chosen color and current player
        colorTxt = (TextView) findViewById(R.id.colorTxt);
        colorTxt.setTypeface(Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book.ttf"));
        currPlayerTxt = (TextView) findViewById(R.id.currPlayerTxt);
        currPlayerTxt.setTypeface(Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book.ttf"));
        turnOrderTxt = (TextView) findViewById(R.id.turnOrderTxt);
        turnOrderTxt.setTypeface(Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book.ttf"));

        Display display = getWindowManager().getDefaultDisplay();
        this.res = new Point();
        display.getSize(res);

        // Create a card deck and set it's position
        this.deckPosition = ((ViewGroup) findViewById(R.id.cardDeckPosition));
        this.cardDeck = new UnoCardDeck(this.getApplicationContext(), (FrameLayout) deckPosition);
        // Workaround card deck for cards from other players
        this.opCardDeck = new UnoCardDeck(this.getApplicationContext(), (FrameLayout) deckPosition);

        // Cards that have been played
        this.playedCards = new ArrayList<>();
        // Cards of the player
        this.playerCards = new ArrayList<>();

        dealCards();

        // TODO: Draw cards by dragging from the stack to the player card position
        // Add OnDragListener to playerCardsPosition for this

        // Add onDragListener to playCardsPosition to allow drag & drop of cards
        findViewById(R.id.playCardsPosition).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                final View view = (View) event.getLocalState();

                // switch user action
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // show user where to put the card
                        //v.setBackgroundDrawable(enterShape);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setVisibility(View.VISIBLE);
                            }
                        });
                        //view.setBackgroundDrawable(normalShape);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // show user where to put the card
                        //v.setBackgroundDrawable(enterShape);
                        break;
                    case DragEvent.ACTION_DROP:
                        //if (playedCards.size() > 0 && (playedCards.get(playedCards.size() - 1).getColor() != cardToBePlayed(view).getColor())){
                        if ((!madeTurn && yourTurn && playedCards.size() > 0 && validPlay(playedCards.get(playedCards.size() - 1), cardToBePlayed(view))) || (playedCards.size() == 0 && yourTurn && !madeTurn)) {
                            // remove from current owner
                            //ViewGroup owner = (ViewGroup) view.getParent();
                            // owner.removeView(view);
                            playCard(view);
                            view.setVisibility(View.VISIBLE);
                            // delete touchlistener
                            // Problem with deleting Touch Listener: cards use same view, if you have played one card you cannot touch the next card if you draw it
                            //view.setOnTouchListener(null);
                            Log.d("ImgView dropped:", "" + view);
                            break;
                        } else if (!yourTurn) {
                            Tools.showToast("It's not your turn!", Toast.LENGTH_SHORT);
                            //Toast.makeText(context, "It's not your turn!", Toast.LENGTH_SHORT).show();
                        } else if (madeTurn){
                            Tools.showToast("Your turn is over!", Toast.LENGTH_SHORT);
                            //Toast.makeText(context, "Your turn is over!", Toast.LENGTH_SHORT).show();
                        } else {
                            // not a valid play
                        }
                    default:
                        // nothing
                        break;
                }
                return true;
            }
        });
    }

    public String findCardByView(View card){
        String cardPlayed = "";
        for (int i=0;i<playerCards.size();i++) {
            if (playerCards.get(i).getImageView() == card){
                cardPlayed = playerCards.get(i).getName();
            }
        }
        return cardPlayed;
    }

    public void playCardByName(final String cardName){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnoCard cardFromOtherPlayer = opCardDeck.getCardByName(cardName);
                cardFromOtherPlayer.setLocation(res.x / 2 - (cardFromOtherPlayer.getImageView().getWidth() + 50), res.y / 2 - cardFromOtherPlayer.getImageView().getHeight() / 2);
                cardFromOtherPlayer.viewFront();
                cardFromOtherPlayer.setContainer((FrameLayout) findViewById(R.id.container));
                playedCards.add(cardFromOtherPlayer);
            }
        });

    }

    public void playCard(View playedCard) {
        madeTurn = true;
        String sendCard = findCardByView(playedCard);
        if (cardToBePlayed(playedCard) != null) {
            cardToBePlayed(playedCard).setLocation(res.x / 2 - (playedCard.getWidth() + 50), res.y / 2 - playedCard.getHeight() / 2);
            cardToBePlayed(playedCard).viewFront();
        }
        for (int i=0;i<playerCards.size();i++){
            if (playerCards.get(i).getImageView() == playedCard){
                Log.d("Played Card:", playerCards.get(i).getName());
                playedCards.add(playerCards.get(i));
                switch (playerCards.get(i).getValue()) {
                    case "COLOR CHANGE":
                        // Player played a color chooser, let him choose a color
                        this.chosenColor = chooseColor();
                        break;
                    case "COLOR CHANGE PLUS 4":
                        //Player played a color chooser + 4, let him choose a color and increase draw stack
                        cardsToDraw += 4;
                        this.chosenColor = chooseColor();
                        break;
                    case "SKIP":
                        // Next player has to skip his turn
                        Tools.showToast("Next player has to skip his turn!", Toast.LENGTH_SHORT);
                        skip = true;
                        //Toast.makeText(context, "Next player has to skip his turn!", Toast.LENGTH_SHORT).show();
                        break;
                    case "TURN":
                        // Turn order has to be reversed
                        Tools.showToast("Turn order has been reversed!", Toast.LENGTH_SHORT);
                        turnOrder = !turnOrder;

                        //Toast.makeText(context, "Turn order has been reversed!", Toast.LENGTH_SHORT).show();
                        break;
                    case "PLUS 2":
                        // Increase draw stack
                        cardsToDraw += 2;
                        break;
                    default:
                        // No special card has been played
                        break;
                }
                playerCards.remove(i);
                sendUpdateEvent("PLAY", sendCard);
                break;
            }
        }
    }

    private UnoCard cardToBePlayed(View selectedCard){
        for (int i=0;i<playerCards.size();i++){
            if (playerCards.get(i).getImageView() == selectedCard){
                Log.d("toBePlayed", playerCards.get(i).getName());
                return playerCards.get(i);
            }
        }
        return null;
    }

    private void drawCards(int count){
        Tools.showToast("Drawing " + count + " card(s)", Toast.LENGTH_SHORT);
        //Toast.makeText(context, "Drawing " + count + " card(s)", Toast.LENGTH_SHORT).show();
        for (int i=0;i<count;i++){
            if (cardDeck.getSize() > 0){
                playerCards.add(cardDeck.getCard());
                cardDeck.removeCard(playerCards.get(playerCards.size() - 1));
            } else {
                cardDeck = new UnoCardDeck(context, (FrameLayout)deckPosition);
                playerCards.add(cardDeck.getCard());
                cardDeck.removeCard(playerCards.get(playerCards.size() - 1));
            }
        }
        // Reposition cards
        for (int i=0;i<playerCards.size();i++){
            playerCards.get(i).setLocation(res.x / (playerCards.size()) + (i * 50), res.y - 130);
            playerCards.get(i).viewFront();
            playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
        }
    }

    private boolean validPlay(UnoCard played, UnoCard toBePlayed){
        if (played.getValue().equals(toBePlayed.getValue())){
            switch (played.getValue()){
                case "PLUS 2":
                    if (!toBePlayed.getValue().equals("PLUS 2")) {
                        // If no +2 card is played on top of a +2 card player has to draw cards
                        drawCards(cardsToDraw);
                        cardsToDraw = 0;
                    }
                    break;
                default:
                    break;
            }
            return true;
        } else if ((played.getColor().equals(toBePlayed.getColor()))) {
            switch (played.getValue()){
                case "PLUS 2":
                    if (!toBePlayed.getValue().equals("PLUS 2")) {
                        // If no +2 card is played on top of a +2 card player has to draw cards
                        drawCards(cardsToDraw);
                        cardsToDraw = 0;
                    }
                    break;
                default:
                    break;
            }
            return true;
        } else if (toBePlayed.getValue().equals("COLOR CHANGE")) {
            // This card can be played on top of every other card
            switch (played.getValue()){
                case "PLUS 2":
                case "COLOR CHANGE PLUS 4":
                    drawCards(cardsToDraw);
                    cardsToDraw = 0;
                    break;
                default:
                    break;
            }
            return true;
        } else if (played.getValue().equals("COLOR CHANGE")) {
            // Enforce chosen color
            if (toBePlayed.getColor().equals(this.chosenColor)){
                this.chosenColor = "";
                return true;
            } else if (toBePlayed.getValue().equals("COLOR CHANGE PLUS 4")){
                this.chosenColor = "";
                return true;
            } else {
                return false;
            }
        } else if (toBePlayed.getValue().equals("COLOR CHANGE PLUS 4")) {
            return true;
        } else if (played.getValue().equals("COLOR CHANGE PLUS 4")){
            if (toBePlayed.getColor().equals(this.chosenColor)){
                switch (toBePlayed.getValue()) {
                    case "PLUS 2":
                        //cardsToDraw += 2;
                        break;
                    case "COLOR CHANGE PLUS 4":
                        //cardsToDraw += 4;
                        break;
                    default:
                        drawCards(cardsToDraw);
                        cardsToDraw = 0;
                        break;
                }
                this.chosenColor = "";
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private String chooseColor(){
        final String[] colorArray = {"Blue", "Green", "Red", "Yellow"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Choose a color!");
        alertDialogBuilder.setItems(colorArray,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenColor = colorArray[which];
                    }
                });
        alertDialogBuilder.show();
        return chosenColor;
    }

    public void setChosenColor(String color){
        this.chosenColor = color;
    }

    public String getChosenColor(){
        return this.chosenColor;
    }

    public void setCardsToDraw(String count){
        this.cardsToDraw = Integer.parseInt(count);
    }

    public int getCardsToDraw(){
        return this.cardsToDraw;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public int getNextPlayer(){
        return nextPlayer;
    }

    public void setNextPlayer(int next){
        nextPlayer = next;
    }

    public int getCurrPlayer(){
        return currPlayer;
    }

    public void setCurrPlayer(int curr){
        currPlayer = curr;
    }

    public void setMadeTurn(boolean madeTurn){
        this.madeTurn = madeTurn;
    }

    public void setHasDrawnCard(boolean hasDrawnCard){
        this.hasDrawnCard = hasDrawnCard;
    }

    public void setColorTxt(final String color){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (color.equals("")) {
                    colorTxt.setText("Chosen Color: -");
                } else {
                    colorTxt.setText("Chosen Color: " + color);
                }

            }
        });

    }

    public void setCurrPlayerTxt(final String currPlayer){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currPlayerTxt.setText("Current Player: " + currPlayer);
            }
        });
    }

    public void setTurnOrderTxt(final String turnOrder){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                turnOrderTxt.setText("Turn Order: " + turnOrder);
            }
        });
    }

    public void dealCards(){
        for (int i = 0; i < 7; i++) {
            // Give cards to the player, remove given cards from draw stack
            // TODO: Distribution of cards (i.e. cards are limited, e.g. there are no more than 4 color choosers)
            playerCards.add(i, cardDeck.getCard());
            playerCards.get(i).setLocation(res.x / 8 + (i * 50), res.y - 130);
            playerCards.get(i).viewFront();
            playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
            cardDeck.removeCard(playerCards.get(i));
        }
    }

    public void sendUpdateEvent(String msg, String card){
        try{
            String message = msg + "#" + Util.userName + "@" + card + "_" + turnOrder;
            Tools.wClient.sendUpdatePeers(message.getBytes());
        } catch (Exception e){
            Log.d("Exc: sendUpdateEvent", e.getMessage());
        }
    }

    public boolean getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(boolean turnOrder) {
        GameActivity.turnOrder = turnOrder;
    }

    public ArrayList<UnoCard> getPlayedCards(){
        return this.playedCards;
    }

    public boolean isSkip(){
        return skip;
    }

    public void setSkip(boolean skip) {
        GameActivity.skip = skip;
    }

    public ArrayList<UnoCard> getPlayerCards(){
        return this.playerCards;
    }

    public static int scale(int v) {
        return (int) GameActivity.density * v;
    }


    @Override
    protected void onPause() {

        // unregister if needed(to save battery)
        //

        // HINT: with savedInstanceState, currently not working
        if(GameState.gameCondition!=null)
            if(GameState.gameCondition.equals(GameState.LOBBY)) {
                Tools.wClient.leaveRoom(Tools.currentRoom);
                updateJoinedPlayersListView();
            }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // HINT: maybe try with savedInstanceState, next codelines currently not working
        if(GameState.gameCondition!=null)
            if(GameState.gameCondition.equals(GameState.LOBBY)) {
                Tools.wClient.joinRoom(Tools.currentRoom);
                updateJoinedPlayersListView();

            }
    }

    public void updateJoinedPlayersListView() {
        // next line was implemented because of an error
        //if(Tools.game.getWindow().getDecorView()==Tools.game.game_activity_startedGameLobby)
        // workaround: check if game is not started (problem was tried to access view elements which are not on display anymore
        //if(GameState.gameCondition.equals(GameState.STARTED))
            Tools.game.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Tools.getUsersInRoom(Tools.currentRoom);
                    ((TextView) Tools.game.findViewById(R.id.lobbyInfo)).setText("Players online (" + Tools.joinedPlayers.size() + "/" + Tools.maxPlayersInRoom + ") in Lobby \"" + Tools.currentRoomName + "\"");

                    Tools.game.findViewById(R.id.btnPlay).setOnClickListener(Tools.gameOnClickListner);

                    ListView listViewConPlayers = ((ListView) findViewById(R.id.listViewConnectedPlayers));

                    JoinedPlayersAdapter customAdpt = new JoinedPlayersAdapter(Tools.appContext, Tools.joinedPlayers);

                    listViewConPlayers.setAdapter(customAdpt);
                    customAdpt.notifyDataSetChanged();

                    //ArrayAdapter<String> adapter = new ArrayAdapter<>(Tools.appContext, android.R.layout.simple_list_item_1, Tools.joinedPlayers);

                }
            });
    }

    public void updateChatView() {
        //if(GameState.gameCondition.equals(GameState.STARTED))
            Tools.game.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListView chat = ((ListView) findViewById(R.id.listViewChatMsgs));
                    JoinedPlayersAdapter adp = new JoinedPlayersAdapter(Tools.appContext, Tools.chatQueue);
                    chat.setAdapter(adp);
                    adp.notifyDataSetChanged();

                    chat.setSelection(adp.getCount() - 1);
                    chat.setDivider(null);
                }
            });
    }

    public void updateOpenGamesList() {

        Tools.showToast("Update!", Toast.LENGTH_SHORT);

        ((ListView)findViewById(R.id.listViewAvailGame)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tools.currentRoom = ((TextView) view).getText().toString().split("ID:")[1];

                setContentView(game_activity_startedGameLobby);

                // now it should work no matter what
                Tools.game.findViewById(R.id.btnSendChatMsg).setOnClickListener(Tools.gameOnClickListner);

                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tools.wClient.joinRoom(Tools.currentRoom);
                        Tools.wClient.subscribeRoom(Tools.currentRoom);
                        Tools.wClient.getLiveRoomInfo(Tools.currentRoom);
                    }
                }, 2000);


                // must be placed here tried also to set code here but it seems
                // that the view update process is faster than the getLiveRoomInfo(...) method --> NullPointerE.
                Handler h2 = new Handler();
                h2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tools.playerArrayToList();
                        updateJoinedPlayersListView();

                        // only admin sees the play button
                        if(Tools.roomOwner.equals(User.getUsername()))
                            (Tools.game.findViewById(R.id.btnPlay)).setVisibility(View.VISIBLE);
                    }
                }, 3000);


            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tools.appContext, android.R.layout.simple_list_item_1, Tools.allRoomNamesList);
        ((ListView)findViewById(R.id.listViewAvailGame)).setAdapter(adapter);
        ((ListView)findViewById(R.id.listViewAvailGame)).setDivider(null);
        adapter.notifyDataSetChanged();
    }
}
