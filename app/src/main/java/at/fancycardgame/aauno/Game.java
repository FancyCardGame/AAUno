package at.fancycardgame.aauno;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by Christian on 26.05.2015.
 */
public class Game extends Activity {


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



    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

        setContentView(R.layout.game_field);
        this.gameBoard = (ViewGroup)getLayoutInflater().inflate(R.layout.game_field, null);
        startGame();
    }


    private void startGame() {

        ViewGroup deckPosition = ((ViewGroup)findViewById(R.id.cardDeckPosition));
        // create card deck and set where to put it
        this.cardDeck = new UnoCardDeck(this.getApplicationContext(), (FrameLayout)deckPosition);

        // test button
        this.testBtn = (Button) findViewById(R.id.testBtn);

        //Store played cards somewhere
        final ArrayList<UnoCard> playedCards = new ArrayList<>();

        //Give the player 7 cards from the deck

        Display display = getWindowManager().getDefaultDisplay();
        final Point res = new Point();
        display.getSize(res);

        final ArrayList<UnoCard> playerCards = new ArrayList<>();


        for (int i=0;i<8;i++){
            // Give cards to the player, remove given cards from draw stack
            playerCards.add(i, cardDeck.getCard());
            playerCards.get(i).setLocation(res.x/3 + (i * 50), res.y - 130);
            playerCards.get(i).viewFront();
            playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
            cardDeck.removeCard(playerCards.get(i));
        }

        // add OnDragListener to playCardsPosition where player can drag&drop their cards
        // TODO: Create method for checking if a play is valid and clean this mess up
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
                            cardToBePlayed(view).setLocation(res.x/2 - (view.getWidth() + 50), res.y/2 - view.getHeight()/2);
                            cardToBePlayed(view).viewFront();


                            // add dropped view to new parent (playCardsPosition)
                            //((ViewGroup) findViewById(R.id.playCardsPosition)).addView(view);
                            // make original view visible again
                            view.setVisibility(View.VISIBLE);
                            // delete touchlistener
                            view.setOnTouchListener(null);
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

            private void playCard(View playedCard) {
                for (int i=0;i<playerCards.size();i++){
                    if (playerCards.get(i).getImageView() == playedCard){
                        Log.d("Played Card:", playerCards.get(i).getName());
                        playedCards.add(playerCards.get(i));
                        playerCards.remove(i);
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
                // This should go somewhere else to be accessible
                for (int i=0;i<count;i++){
                    // Give cards to the player, remove given cards from draw stack
                    playerCards.add(i, cardDeck.getCard());
                    playerCards.get(i).setLocation(res.x / 3 + (playerCards.size() * 50), res.y - 130);
                    playerCards.get(i).viewFront();
                    playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));
                    cardDeck.removeCard(playerCards.get(i));
                }
            }

            private boolean validPlay(UnoCard played, UnoCard toBePlayed){
                // Rules of play go here
                // (Next) player has to skip his turn after a "Stop" card has been played
                // (Next) Player has to X draw cards if a "draw X cards" card is played
                // Player has to select a color if he/she plays a color change card
                // Turn order has to be reversed if a player plays a turn card
                // TODO: Color Change card gets played on top of a special card
                if (played.getValue() == toBePlayed.getValue()){
                    switch (played.getValue()){
                        case "PLUS 2":
                            drawCards(2);
                            break;
                        default:
                            break;
                    }
                    return true;
                } else if (played.getColor() == toBePlayed.getColor()) {
                    switch (played.getValue()){
                        case "PLUS 2":
                            drawCards(2);
                            break;
                        default:
                            break;
                    }
                    return true;
                } else if (toBePlayed.getValue() == "COLOR CHANGE") {
                    // This card can be played on top of every other card
                    // TODO: Let player choose a color
                    return true;
                } else if (played.getValue() == "COLOR CHANGE") {
                    // TODO: Enforce chosen color
                    return true;
                } else if (toBePlayed.getValue() == "COLOR CHANGE PLUS 4") {
                    return true;
                } else if (played.getValue() == "COLOR CHANGE PLUS 4"){
                    drawCards(4);
                    return true;
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
        //res = new Point();
        display.getSize(res);

            /*
            UnoCard test2 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-50, res.y-130), getResources().getDrawable(R.drawable.blue_2), getResources().getDrawable(R.drawable.card_back), "Blue 2", "", "2", "Blue");
            UnoCard test3 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-100, res.y-130), getResources().getDrawable(R.drawable.red_6), getResources().getDrawable(R.drawable.card_back),"Red 6", "", "6", "Red");
            UnoCard test4 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-150, res.y-130), getResources().getDrawable(R.drawable.green_9), getResources().getDrawable(R.drawable.card_back),"Green 9", "", "9", "Green");

           test2.viewFront();
           test3.viewFront();
           test4.viewFront();*/

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Draw a card
                // Refresh the position of the other cards
                // Add new card to the left of the other cards
                // Remove the drawn card from the draw stack
                for (int i=0;i<playerCards.size();i++){
                    playerCards.get(i).setLocation(res.x / 3 + (i * 50), res.y - 130);
                    playerCards.get(i).viewFront();
                    playerCards.get(i).setContainer((FrameLayout) findViewById(R.id.container));

                }
                playerCards.add(cardDeck.getCard());
                playerCards.get(playerCards.size() - 1).setLocation(res.x / 3 + (playerCards.size() * 50), res.y - 130);
                playerCards.get(playerCards.size() - 1).viewFront();
                playerCards.get(playerCards.size() - 1).setContainer((FrameLayout) findViewById(R.id.container));
                cardDeck.removeCard(playerCards.get(playerCards.size() - 1));
                Log.d("Size matters", "" + cardDeck.getSize());
                Log.d("Player SIze", "" + playerCards.size());
                Log.d("Drawn card", playerCards.get(playerCards.size() - 1).getName());

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
        return (int)Game.density * v;
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
