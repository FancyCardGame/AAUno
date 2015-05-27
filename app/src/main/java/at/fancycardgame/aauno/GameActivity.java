package at.fancycardgame.aauno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteTableLockedException;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import com.shephertz.app42.paas.sdk.android.App42API;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Christian on 26.05.2015.
 */

// TODO: Use this new Activity to clean up the code (e.g. move methods out of startGame() to make them public)
// move implemented methods from interfaces to extra classes to have a more managed code !
public class GameActivity extends Activity implements View.OnClickListener, ZoneRequestListener, ConnectionRequestListener, RoomRequestListener, NotifyListener {


    public ViewGroup gameBoard;
    // the card deck
    private UnoCardDeck cardDeck;
    // the logical density of the display
    private static float density;
    // Loginstatus
    private static boolean isUserLoggedIn = false;
    //test button
    private Button testBtn;
    public View playedCard;

    private Display display;
    private ViewGroup game_activity_start;

    // Done by Thomas
    private ViewGroup game_activity_creategame;
    private  ViewGroup game_activity_joingame;
    private ViewGroup game_activity_startedGameLobby;

    private View.OnClickListener gameActivityListener = this;

    // App42 API key / Secret key
    private static final String API_KEY = Constants.API_KEY;
    private static final String SECRET_KEY = Constants.SECRET_KEY;
    private WarpClient theClient;

    private static Context appContext;

    private String gamename;
    private int maxUsers;
    private String[] allRoomIDs;
    private String currentRoom;
    private ArrayList<String> allRoomNamesList = new ArrayList<>();
    private ArrayList<String> joinedPlayers = new ArrayList<>();

    private final Context context = this;


    private static Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(GameActivity.appContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

        GameActivity.appContext = this.getApplicationContext();

        this.game_activity_start = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_start, null);
        setContentView(this.game_activity_start);

        findViewById(R.id.createGameMP).setOnClickListener(this);
        findViewById(R.id.joinGameMP).setOnClickListener(this);


        //preparing views
        this.game_activity_creategame = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_creategame, null);
        this.game_activity_joingame = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_joingame, null);
        this.game_activity_startedGameLobby = (ViewGroup)getLayoutInflater().inflate(R.layout.game_activity_gamelobby, null);

        Toast.makeText(getApplicationContext(), "before init app42", Toast.LENGTH_SHORT).show();
        this.initApp42SDK();
        Toast.makeText(getApplicationContext(), "after init app42", Toast.LENGTH_SHORT).show();

        //prepare spinner in creategame view
       // Spinner maxUsers = (Spinner)findViewById(R.id.spinnerMaxUsers);
       // ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.maxPlayersSelection, android.R.layout.simple_spinner_dropdown_item);
       // spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // maxUsers.setAdapter(spnAdapter);


        //old
        //setContentView(R.layout.game_field);
        //this.gameBoard = (ViewGroup)getLayoutInflater().inflate(R.layout.game_field, null);
        //startGame();

    }


    private void initApp42SDK() {
        App42API.initialize(getApplicationContext(), Constants.API_KEY, Constants.SECRET_KEY);
        WarpClient.initialize(Constants.API_KEY, Constants.SECRET_KEY);


        try {
            theClient = WarpClient.getInstance();
            WarpClient.enableTrace(true);
        } catch (Exception ex) {
            Toast.makeText(this, "Exception in Initilization", Toast.LENGTH_LONG).show();
        }
        theClient.addConnectionRequestListener(this);
        theClient.addZoneRequestListener(this);
        theClient.addRoomRequestListener(this);
        theClient.addNotificationListener(this);
    }


    private void startGame() {

        final ViewGroup deckPosition = ((ViewGroup)findViewById(R.id.cardDeckPosition));
        // create card deck and set where to put it
        this.cardDeck = new UnoCardDeck(this.getApplicationContext(), (FrameLayout)deckPosition);

        // test button
        this.testBtn = (Button) findViewById(R.id.testBtn);

        //Store played cards somewhere
        final ArrayList<UnoCard> playedCards = new ArrayList<>();

        //Give the player 7 cards from the deck

        Display display = getWindowManager().getDefaultDisplay();
        Point res = new Point();
        display.getSize(res);

        final ArrayList<UnoCard> playerCards = new ArrayList<>();


        for (int i=0;i<8;i++){
            // Give cards to the player, remove given cards from draw stack
            playerCards.add(i, cardDeck.getCard());
            playerCards.get(i).setLocation(res.x/8 + (i * 50), res.y - 130);
            playerCards.get(i).viewFront();
            playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
            cardDeck.removeCard(playerCards.get(i));
        }

/*        UnoCard testCard = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-50, res.y-130), getResources().getDrawable(R.drawable.color_change), getResources().getDrawable(R.drawable.card_back), "Color Change", "", "COLOR CHANGE", "COLOR CHANGE");
        testCard.viewFront();
        testCard.setContainer((FrameLayout) findViewById(R.id.container));
        playerCards.add(playedCards.size(), testCard);

        UnoCard testPlus4 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(200, 200), getResources().getDrawable(R.drawable.color_change_plus4), getResources().getDrawable(R.drawable.card_back), "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4");
        UnoCard testPlus4_1 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(210, 210), getResources().getDrawable(R.drawable.color_change_plus4), getResources().getDrawable(R.drawable.card_back), "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4");

        testPlus4.viewFront();
        testPlus4.setContainer((FrameLayout) findViewById(R.id.container));
        testPlus4_1.viewFront();
        testPlus4_1.setContainer((FrameLayout) findViewById(R.id.container));

        playedCards.add(playedCards.size(), testPlus4);
        playedCards.add(playedCards.size(), testPlus4_1);*/
        final Point finalRes1 = res;
        // Draw cards by dragging from the stack to the player card position
        // add OnDragListener to playCardsPosition where player can drag&drop their cards

        findViewById(R.id.playCardsPosition).setOnDragListener(new View.OnDragListener() {
            //Drawable enterShape = getResources().getDrawable(entershape);
            //Drawable normalShape = getResources().getDrawable(normalshape);
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                final View view = (View)event.getLocalState();

                // switch user action
                switch(action) {
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
                        if ((playedCards.size() > 0 && validPlay(playedCards.get(playedCards.size() - 1), cardToBePlayed(view))) || playedCards.size() == 0){
                            // remove from current owner
                            ViewGroup owner = (ViewGroup) view.getParent();
                            // owner.removeView(view);
                            // get current X and Y coordinates from drop event
                            // view.setX(event.getX() - (view.getWidth() / 2));
                            //view.setY(event.getY() - (view.getHeight() / 2));
                            if (cardToBePlayed(view) != null){
                                cardToBePlayed(view).setLocation(finalRes1.x/2 - (view.getWidth() + 50), finalRes1.y/2 - view.getHeight()/2);
                                cardToBePlayed(view).viewFront();
                            }
                            // add dropped view to new parent (playCardsPosition)
                            //((ViewGroup) findViewById(R.id.playCardsPosition)).addView(view);
                            // make original view visible again
                            view.setVisibility(View.VISIBLE);
                            // delete touchlistener
                            // Problem with deleting Touch Listener:
                            // same cards use same view, if you have played one card you cannot touch the next card if you draw it
                            // add a view for each card?
                            //view.setOnTouchListener(null);
                            Log.d("ImgView dropped:", "" + view);
                            playCard(view);
                            break;
                        } else {
                            //not a valid play
                        }
                    default:
                        // nothing
                        break;
                }
                return true;
            }

            int cardsToDraw = 0;
            //played card logic goes here, e.g. color chooser, turn turner, etc.??
            private void playCard(View playedCard) {
                for (int i=0;i<playerCards.size();i++){
                    if (playerCards.get(i).getImageView() == playedCard){
                        Log.d("Played Card:", playerCards.get(i).getName());
                        playedCards.add(playerCards.get(i));
                        String card = playerCards.get(i).getValue();
                        switch (card) {
                            case "COLOR CHANGE":
                                chosenColor = chooseColor();
                                break;
                            case "COLOR CHANGE PLUS 4":
                                //Player has played color chooser, let him choose a color and enforce it
                                cardsToDraw += 4;
                                chosenColor = chooseColor();
                                break;
                            case "SKIP":
                                Toast.makeText(context, "Next player has to skip his turn!", Toast.LENGTH_SHORT).show();
                                break;
                            case "TURN":
                                Toast.makeText(context, "Turn order has been reversed!", Toast.LENGTH_SHORT).show();
                                break;
                            case "PLUS 2":
                                cardsToDraw += 2;
                                break;
                            default:
                                break;
                        }
                        playerCards.remove(i);
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
                Toast.makeText(context, "Drawing " + count + " cards", Toast.LENGTH_SHORT).show();
                // This should go somewhere else to be accessible
                for (int i=0;i<count;i++){
                    // Give cards to the player, remove given cards from draw stack
                    playerCards.add(i, cardDeck.getCard());
                    cardDeck.removeCard(playerCards.get(i));
                }
                // reposition current cards
                for (int i=0;i<playerCards.size();i++){
                    playerCards.get(i).setLocation(finalRes1.x / (playerCards.size()) + (i * 50), finalRes1.y - 130);
                    playerCards.get(i).viewFront();
                    playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
                }
            }

            String chosenColor = "";

            private String chooseColor(){
                final String[] colorArray = {"Blue", "Green", "Red", "Yellow"};


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Choose a color!");
                alertDialogBuilder.setItems(colorArray,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(context, "Which: " + which, Toast.LENGTH_LONG).show();
                                chosenColor = colorArray[which];
                            }
                        });
                alertDialogBuilder.show();
                return chosenColor;
            }

            private boolean validPlay(UnoCard played, UnoCard toBePlayed){
                // Rules of play go here
                // (Next) player has to skip his turn after a "Skip" card has been played
                // (Next) Player has to X draw cards if a "draw X cards" card is played
                // Player has to select a color if he/she plays a color change card
                // Turn order has to be reversed if a player plays a turn card
                // TODO: Color Change card gets played on top of a special card
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
                    if (toBePlayed.getColor().equals(chosenColor)){
                        chosenColor = "";
                        return true;
                    } else if (toBePlayed.getValue().equals("COLOR CHANGE PLUS 4")){
                        chosenColor = "";
                        return true;
                    } else {
                        return false;
                    }
                } else if (toBePlayed.getValue().equals("COLOR CHANGE PLUS 4")) {
                    return true;
                } else if (played.getValue().equals("COLOR CHANGE PLUS 4")){
                    if (toBePlayed.getColor().equals(chosenColor)){
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
                        chosenColor = "";
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });




        // mix deck
        //this.mixDeck();

        // deal out cards to user (each user gets 7 from the mixed deck)
        // set on drag listener to null when creating deck

        // TEST STUFF ******************************************************
        // *****************************************************************
        display = getWindowManager().getDefaultDisplay();
        res = new Point();
        display.getSize(res);

            /*
            UnoCard test2 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-50, res.y-130), getResources().getDrawable(R.drawable.blue_2), getResources().getDrawable(R.drawable.card_back), "Blue 2", "", "2", "Blue");
            UnoCard test3 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-100, res.y-130), getResources().getDrawable(R.drawable.red_6), getResources().getDrawable(R.drawable.card_back),"Red 6", "", "6", "Red");
            UnoCard test4 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-150, res.y-130), getResources().getDrawable(R.drawable.green_9), getResources().getDrawable(R.drawable.card_back),"Green 9", "", "9", "Green");

           test2.viewFront();
           test3.viewFront();
           test4.viewFront();*/

        final Point finalRes = res;
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Draw card, refresh position of all cards

                if (cardDeck.getSize() > 0){
                    playerCards.add(cardDeck.getCard());
                    cardDeck.removeCard(playerCards.get(playerCards.size() - 1));
                } else {
                    cardDeck = new UnoCardDeck(context, (FrameLayout)deckPosition);
                    playerCards.add(cardDeck.getCard());
                    cardDeck.removeCard(playerCards.get(playerCards.size() - 1));
                }

                Log.d("Size matters", "" + cardDeck.getSize());
                Log.d("Player Size", "" + playerCards.size());
                Log.d("Drawn card", playerCards.get(playerCards.size() - 1).getName());

                for (int i=0;i<playerCards.size();i++){
                    playerCards.get(i).setLocation(finalRes.x / (playerCards.size()) + (i * 50), finalRes.y - 130);
                    playerCards.get(i).viewFront();
                    playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
                }
            }
        });

        // TEST STUFF ******************************************************
        // TEST STUFF ******************************************************
        // *****************************************************************
    }



    // method that sets a view invisible which is specified with a parameter
    protected void hideView(int view) {
        findViewById(view).setVisibility(View.INVISIBLE);
    }

    // method that sets a view visible which is specified with a parameter
    protected void showView(int view) {
        findViewById(view).setVisibility(View.VISIBLE);
    }

    public static int scale(int v) {
        return (int) GameActivity.density * v;
    }

    @Override
    public void onClick(View v) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = v.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);

        if(clickedID==R.id.createGameMP) {

            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    setContentView(game_activity_creategame);

                    findViewById(R.id.btnStartGameLobby).setOnClickListener(gameActivityListener);

                    //prepare spinner in creategame view
                    Spinner maxUsers = (Spinner)findViewById(R.id.spinnerMaxUsers);
                    ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.maxPlayersSelection, android.R.layout.simple_spinner_dropdown_item);
                    spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    maxUsers.setAdapter(spnAdapter);

                }
            });
            v.startAnimation(a);

        } else if(clickedID==R.id.joinGameMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    setContentView(game_activity_joingame);

                    theClient.getAllRooms();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    for(String id : allRoomIDs) {
                        theClient.getLiveRoomInfo(id);
                    }


                    // wait at least 3 seconds until data is ready (maybe can be reduced)
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }




                    ListView availableGames = ((ListView)findViewById(R.id.listViewAvailGame));
                    availableGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String room = ((TextView)view).getText().toString().split("ID:")[1];

                            Message msg1 = new Message();
                            msg1.obj = room;
                            toastHandler.sendMessage(msg1);

                            theClient.joinRoom(room);
                            setContentView(game_activity_startedGameLobby);
                        }
                    });
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(appContext, android.R.layout.simple_list_item_1, allRoomNamesList);
                    availableGames.setAdapter(adapter);


                    //findViewById(R.id.btnStartGameLobby).setOnClickListener(gameActivityListener);
                }
            });
            v.startAnimation(a);
        } else if(clickedID==R.id.btnStartGameLobby) {

            // getting entered info & create room in cloud
            this.gamename = ((TextView)findViewById(R.id.txtBoxGameName)).getText().toString();
            this.maxUsers = Integer.parseInt(((Spinner)findViewById(R.id.spinnerMaxUsers)).getSelectedItem().toString());

            theClient.createRoom(this.gamename, User.getUsername(), this.maxUsers, null);

            Message msg1 = new Message();
            msg1.obj = "Room should be created.";
            toastHandler.sendMessage(msg1);

            setContentView(game_activity_startedGameLobby);
            findViewById(R.id.btnStartGameFromRoom).setOnClickListener(gameActivityListener);

            this.joinedPlayers.add(User.getUsername());

            theClient.subscribeRoom(this.currentRoom);

            this.updateJoinedPlayersListView();

            // for what?
            //theClient.getAllRooms();

        } else if(clickedID==R.id.btnStartGameFromRoom) {

            // check if e.g. 2 of 2 users are connected, 4 of 4 ...

            setContentView(R.layout.game_field);
            this.gameBoard = (ViewGroup)getLayoutInflater().inflate(R.layout.game_field, null);
            startGame();
        }
    }

    public void updateJoinedPlayersListView() {
        ListView listViewConPlayers = ((ListView)findViewById(R.id.listViewConnectedPlayers));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(appContext, android.R.layout.simple_list_item_1, joinedPlayers);
        listViewConPlayers.setAdapter(adapter);
    }

    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {

        // ID IS RECEIVED
       // Message msg = new Message();
       // msg.obj = (allRoomsEvent.[0]);
        //toastHandler.sendMessage(msg);
        this.allRoomIDs = allRoomsEvent.getRoomIds();
    }

    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {
        // room created message
    }

    @Override
    public void onGetOnlineUsersDone(AllUsersEvent allUsersEvent) {

    }

    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent) {

    }

    @Override
    public void onConnectDone(ConnectEvent connectEvent) {
        Message msg = new Message();
        msg.obj = "Successfully connected to the cloud.";
        toastHandler.sendMessage(msg);
    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onInitUDPDone(byte b) {

    }

    @Override
    public void onSubscribeRoomDone(RoomEvent roomEvent) {
       // Message msg1 = new Message();
        //msg1.obj = "Subscribed to room "+ roomEvent.getData().getName();
        //toastHandler.sendMessage(msg1);
    }

    @Override
    public void onUnSubscribeRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onJoinRoomDone(RoomEvent roomEvent) {
        Message msg1 = new Message();
        msg1.obj = "You joined the room " + roomEvent.getData().getName();
        toastHandler.sendMessage(msg1);
    }

    @Override
    public void onLeaveRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {
        this.allRoomNamesList.add(liveRoomInfoEvent.getData().getName() + " - ID:" + liveRoomInfoEvent.getData().getId());
    }

    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onLockPropertiesDone(byte b) {

    }

    @Override
    public void onUnlockPropertiesDone(byte b) {

    }

    @Override
    public void onRoomCreated(RoomData roomData) {
        this.currentRoom = roomData.getId();

    }

    @Override
    public void onRoomDestroyed(RoomData roomData) {

    }

    @Override
    public void onUserLeftRoom(RoomData roomData, String s) {
        this.joinedPlayers.remove(s);
        this.updateJoinedPlayersListView();
    }

    @Override
    public void onUserJoinedRoom(RoomData roomData, String s) {
        //this.joinedPlayers.add(s);
        //this.updateJoinedPlayersListView();
        Message msg1 = new Message();
        msg1.obj = "Someone joined!";
        toastHandler.sendMessage(msg1);
    }

    @Override
    public void onUserLeftLobby(LobbyData lobbyData, String s) {

    }

    @Override
    public void onUserJoinedLobby(LobbyData lobbyData, String s) {

    }

    @Override
    public void onChatReceived(ChatEvent chatEvent) {

    }

    @Override
    public void onPrivateChatReceived(String s, String s2) {

    }

    @Override
    public void onPrivateUpdateReceived(String s, byte[] bytes, boolean b) {

    }

    @Override
    public void onUpdatePeersReceived(UpdateEvent updateEvent) {

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

    //List available Bluetooth devices
    /*public void showPlayers() {

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        final ArrayList<String> list = new ArrayList<String>();
        final ListView playersList = (ListView) findViewById(R.id.listPlayers);


        If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(device.getName() + "\n" + device.getAddress());
                playersList.setAdapter(adapter);
            }
        }

        bluetoothAdapter.startDiscovery();

        // Create a BroadcastReceiver for ACTION_FOUND
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    list.add(device.getName() + "\n" + device.getAddress());
                    ArrayAdapter adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_list_item_1, list);
                    playersList.setAdapter(adapter);
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

    }*/
/*
    public void loadFragment(){

       final Fragment f = new Fragment();
         getSupportFragmentManager().beginTransaction()
                .add(R.id.screens, f,"screens")
                .addToBackStack("tag")
                .commit();

    }

    public void onBackPressed(){

}
  */

}
