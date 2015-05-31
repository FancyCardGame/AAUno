package at.fancycardgame.aauno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
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

import java.util.ArrayList;

import at.fancycardgame.aauno.adapters.JoinedPlayersAdapter;
import at.fancycardgame.aauno.toolbox.GameState;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Christian on 26.05.2015.
 */

public class GameActivity extends Activity {


    public ViewGroup gameBoard;
    // the card deck
    private UnoCardDeck cardDeck;
    // the logical density of the display
    private static float density;

    //test button
    private Button testBtn;
    public View playedCard;

    private Display display;

    private ViewGroup game_activity_start;
    public ViewGroup game_activity_creategame;
    public  ViewGroup game_activity_joingame;
    public ViewGroup game_activity_startedGameLobby;






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


        // Toast.makeText(getApplicationContext(), "before room creation", Toast.LENGTH_SHORT).show();
        //if(at.fancycardgame.aauno.User.getUsername()!=null)
            //theClient.connectWithUserName(at.fancycardgame.aauno.User.getUsername());
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



    public void startGame() {
        GameState.gameCondition = GameState.STARTED;

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
                                Toast.makeText(Tools.appContext, "Next player has to skip his turn!", Toast.LENGTH_SHORT).show();
                                break;
                            case "TURN":
                                Toast.makeText(Tools.appContext, "Turn order has been reversed!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Tools.appContext, "Drawing " + count + " cards", Toast.LENGTH_SHORT).show();
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


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Tools.appContext);
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
                    cardDeck = new UnoCardDeck(Tools.appContext, (FrameLayout)deckPosition);
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





    public static int scale(int v) {
        return (int) GameActivity.density * v;
    }


    @Override
    protected void onPause() {
        super.onPause();

        // HINT: with savedInstanceState, currently not working
        if(GameState.gameCondition!=null)
            if(GameState.gameCondition.equals(GameState.LOBBY)) {
                Tools.wClient.leaveRoom(Tools.currentRoom);
                updateJoinedPlayersListView();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // HINT: with savedInstanceState,currently not working
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
                    ((TextView)Tools.game.findViewById(R.id.lobbyInfo)).setText("Players online (" + Tools.joinedPlayers.size() + "/"+ Tools.maxPlayersInRoom +") in Lobby \"" + Tools.currentRoomName + "\"");

                    Tools.game.findViewById(R.id.btnPlay).setOnClickListener(Tools.gameOnClickListner);

                    ListView listViewConPlayers = ((ListView)findViewById(R.id.listViewConnectedPlayers));

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
                    ListView chat = ((ListView)findViewById(R.id.listViewChatMsgs));
                    JoinedPlayersAdapter adp = new JoinedPlayersAdapter(Tools.appContext, Tools.chatQueue);
                    chat.setAdapter(adp);
                    adp.notifyDataSetChanged();

                    chat.setSelection(adp.getCount()-1);
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
