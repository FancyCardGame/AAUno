package at.fancycardgame.aauno;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class MainActivity extends Activity implements View.OnClickListener{

    // define font name, can be changed later on here
    private static final String menu_font = "Comic Book Bold.ttf";
    // the whole display container where the content is shown
    private ViewGroup screen_container;
    // the whole container where the game takes place
    private ViewGroup gameBoard;

    // the logical density of the display
    private static float density;

    private Set<UnoCard> cardDeck = new HashSet<UnoCard>();
    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set menu typeface
        this.setMenuTypeface();
        // get density of display (to scale images later)
        MainActivity.density = getResources().getDisplayMetrics().density;
        // get container where game content is shown later
        this.screen_container = (ViewGroup)findViewById(R.id.screens);
        // get current display
        this.display = ((WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // set onclick listener to make navigation in menue possible
        findViewById(R.id.startGameMP).setOnClickListener(this);
        findViewById(R.id.optionsMP).setOnClickListener(this);
        findViewById(R.id.helpMP).setOnClickListener(this);
        findViewById(R.id.quitMP).setOnClickListener(this);
    }



    // method that takes *.ttf file, creates a typeface and applies it to the menu TextViews
    private void setMenuTypeface() {
        Typeface menu = Typeface.createFromAsset(getAssets(), menu_font);
        ((TextView)findViewById(R.id.startGameMP)).setTypeface(menu);
        ((TextView)findViewById(R.id.optionsMP)).setTypeface(menu);
        ((TextView)findViewById(R.id.helpMP)).setTypeface(menu);
        ((TextView)findViewById(R.id.quitMP)).setTypeface(menu);
    }

    @Override
    public void onClick(View clickedView) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = clickedView.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);


        if(clickedID==R.id.startGameMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // remove everything that is in screen_container
                    screen_container.removeAllViews();
                    // create gameboard from layout ...
                    gameBoard = (ViewGroup)getLayoutInflater().inflate(R.layout.game_field, null);
                    // ... and add it to the screen_container
                    screen_container.addView(gameBoard);

                    startGame();
                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedID==R.id.optionsMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    //
                    // VIEW OPTIONS MENU
                    //
                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedID==R.id.helpMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    //
                    // VIEW HELP MENU?/DIALOG?/TUTORIAL?
                    //
                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedID==R.id.quitMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    finish();
                    System.exit(0);
                }
            } );
            clickedView.startAnimation(a);
        }
        // if the same OnClickListener is used continue here with else if(...)
    }

    private void startGame() {

        ViewGroup deckPosition = ((ViewGroup)findViewById(R.id.cardDeckPosition));
        // create card deck and set where to put it
        this.createCardDeck((FrameLayout) deckPosition);

        // add OnDragListener to playCardsPosition where player can drag&drop their cards
        findViewById(R.id.playCardsPosition).setOnDragListener(new View.OnDragListener() {
            //Drawable enterShape = getResources().getDrawable(entershape);
            //Drawable normalShape = getResources().getDrawable(normalshape);
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                View view = (View)event.getLocalState();

                // switch user action
                switch(action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // show user where to put the card
                        //v.setBackgroundDrawable(enterShape);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        view.setVisibility(View.VISIBLE);
                        //view.setBackgroundDrawable(normalShape);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // show user where to put the card
                        //v.setBackgroundDrawable(enterShape);
                        break;
                    case DragEvent.ACTION_DROP:
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        view.setX(event.getX() - (view.getWidth() / 2));
                        view.setY(event.getY()-(view.getHeight()/2));
                        ((ViewGroup)findViewById(R.id.playCardsPosition)).addView(view);
                        view.setVisibility(View.VISIBLE);
                        view.setOnTouchListener(null);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        // mix deck
        //this.mixDeck();
        // deal out cards to user (each user gets 7 from the mixed deck)
        // set on drag listener to null when creating deck

        // TEST STUFF ******************************************************
        // *****************************************************************
            Display display = getWindowManager().getDefaultDisplay();
            Point res = new Point();
            display.getSize(res);


            UnoCard test2 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-50, res.y-130), getResources().getDrawable(R.drawable.blue_2), "Blue 2", "", "2", "Blue");
            UnoCard test3 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-100, res.y-130), getResources().getDrawable(R.drawable.red_6), "Red 6", "", "6", "Red");
            UnoCard test4 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-150, res.y-130), getResources().getDrawable(R.drawable.green_9), "Green 9", "", "9", "Green");
        // TEST STUFF ******************************************************
        // *****************************************************************
    }

    private void mixDeck() {
       // TODO
    }

    /**
     * Method that creates UNO card deck.
     * 1) drawables are initialized
     * 2) UnoCards objects are created with the initialized drawables
     * 3) simultaneously they are added to the cardDeck Hashmap
     *
     * INFO: point is created with -999, -999 (x,y) coordinates to move deck out of screen
     * TODO: backside of card, init drawable and extend constructor (in class UnoCard and on object creation)
     */
    private void createCardDeck(FrameLayout deckPos) {


        // create drawables for  normal cards BLUE
        Drawable blue_0 = getResources().getDrawable(R.drawable.blue_0);
        Drawable blue_1 = getResources().getDrawable(R.drawable.blue_1);
        Drawable blue_2 = getResources().getDrawable(R.drawable.blue_2);
        Drawable blue_3 = getResources().getDrawable(R.drawable.blue_3);
        Drawable blue_4 = getResources().getDrawable(R.drawable.blue_4);
        Drawable blue_5 = getResources().getDrawable(R.drawable.blue_5);
        Drawable blue_6 = getResources().getDrawable(R.drawable.blue_6);
        Drawable blue_7 = getResources().getDrawable(R.drawable.blue_7);
        Drawable blue_8 = getResources().getDrawable(R.drawable.blue_8);
        Drawable blue_9 = getResources().getDrawable(R.drawable.blue_9);
        // create drawables for  special cards BLUE
        Drawable blue_skip = getResources().getDrawable(R.drawable.blue_skip);
        Drawable blue_plus2 = getResources().getDrawable(R.drawable.blue_plus2);
        Drawable blue_turn = getResources().getDrawable(R.drawable.blue_turn);
        
        // create drawables for  normal cards RED
        Drawable red_0 = getResources().getDrawable(R.drawable.red_0);
        Drawable red_1 = getResources().getDrawable(R.drawable.red_1);
        Drawable red_2 = getResources().getDrawable(R.drawable.red_2);
        Drawable red_3 = getResources().getDrawable(R.drawable.red_3);
        Drawable red_4 = getResources().getDrawable(R.drawable.red_4);
        Drawable red_5 = getResources().getDrawable(R.drawable.red_5);
        Drawable red_6 = getResources().getDrawable(R.drawable.red_6);
        Drawable red_7 = getResources().getDrawable(R.drawable.red_7);
        Drawable red_8 = getResources().getDrawable(R.drawable.red_8);
        Drawable red_9 = getResources().getDrawable(R.drawable.red_9);
        // create drawables for  special cards red
        Drawable red_skip = getResources().getDrawable(R.drawable.red_skip);
        Drawable red_plus2 = getResources().getDrawable(R.drawable.red_plus2);
        Drawable red_turn = getResources().getDrawable(R.drawable.red_turn);
        
        // create drawables for  normal cards GREEN
        Drawable green_0 = getResources().getDrawable(R.drawable.green_0);
        Drawable green_1 = getResources().getDrawable(R.drawable.green_1);
        Drawable green_2 = getResources().getDrawable(R.drawable.green_2);
        Drawable green_3 = getResources().getDrawable(R.drawable.green_3);
        Drawable green_4 = getResources().getDrawable(R.drawable.green_4);
        Drawable green_5 = getResources().getDrawable(R.drawable.green_5);
        Drawable green_6 = getResources().getDrawable(R.drawable.green_6);
        Drawable green_7 = getResources().getDrawable(R.drawable.green_7);
        Drawable green_8 = getResources().getDrawable(R.drawable.green_8);
        Drawable green_9 = getResources().getDrawable(R.drawable.green_9);
        // create drawables for  special cards GREEN
        Drawable green_skip = getResources().getDrawable(R.drawable.green_skip);
        Drawable green_plus2 = getResources().getDrawable(R.drawable.green_plus2);
        Drawable green_turn = getResources().getDrawable(R.drawable.green_turn);
        
        // create drawables for  normal cards YELLOW
        Drawable yellow_0 = getResources().getDrawable(R.drawable.yellow_0);
        Drawable yellow_1 = getResources().getDrawable(R.drawable.yellow_1);
        Drawable yellow_2 = getResources().getDrawable(R.drawable.yellow_2);
        Drawable yellow_3 = getResources().getDrawable(R.drawable.yellow_3);
        Drawable yellow_4 = getResources().getDrawable(R.drawable.yellow_4);
        Drawable yellow_5 = getResources().getDrawable(R.drawable.yellow_5);
        Drawable yellow_6 = getResources().getDrawable(R.drawable.yellow_6);
        Drawable yellow_7 = getResources().getDrawable(R.drawable.yellow_7);
        Drawable yellow_8 = getResources().getDrawable(R.drawable.yellow_8);
        Drawable yellow_9 = getResources().getDrawable(R.drawable.yellow_9);
        // create drawables for  special cards YELLOW
        Drawable yellow_skip = getResources().getDrawable(R.drawable.yellow_skip);
        Drawable yellow_plus2 = getResources().getDrawable(R.drawable.yellow_plus2);
        Drawable yellow_turn = getResources().getDrawable(R.drawable.yellow_turn);
        
        // create drawables for  special cards
        Drawable color_change = getResources().getDrawable(R.drawable.color_change);
        Drawable color_change_plus4 = getResources().getDrawable(R.drawable.color_change_plus4);

        // blue
        // 0 only once
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_0, "Blue 0", "", "0", "Blue"));
        for(int twice=0;twice<2;twice++) {
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_1, "Blue 1", "", "1", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_2, "Blue 2", "", "2", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_3, "Blue 3", "", "3", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_4, "Blue 4", "", "4", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_5, "Blue 5", "", "5", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_6, "Blue 6", "", "6", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_7, "Blue 7", "", "7", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_8, "Blue 8", "", "8", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_9, "Blue 9", "", "9", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_skip, "Blue Skip", "", "SKIP", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_turn, "Blue Turn", "", "TURN", "Blue"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), blue_plus2, "Blue Plus 2", "", "PLUS 2", "Blue"));
        }
        
        // red
        // 0 only once
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_0, "red 0", "", "0", "red"));
        for(int twice=0;twice<2;twice++) {
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_1, "Red 1", "", "1", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_2, "Red 2", "", "2", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_3, "Red 3", "", "3", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_4, "Red 4", "", "4", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_5, "Red 5", "", "5", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_6, "Red 6", "", "6", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_7, "Red 7", "", "7", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_8, "Red 8", "", "8", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_9, "Red 9", "", "9", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_skip, "Red Skip", "", "SKIP", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_turn, "Red Turn", "", "TURN", "Red"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), red_plus2, "Red Plus 2", "", "PLUS 2", "Red"));
        }
        
        // green
        // o only once
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_0, "Green 0", "", "0", "Green"));
        for(int twice=0;twice<2;twice++) {
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_1, "Green 1", "", "1", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_2, "Green 2", "", "2", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_3, "Green 3", "", "3", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_4, "Green 4", "", "4", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_5, "Green 5", "", "5", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_6, "Green 6", "", "6", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_7, "Green 7", "", "7", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_8, "Green 8", "", "8", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_9, "Green 9", "", "9", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_skip, "Green Skip", "", "SKIP", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_turn, "Green Turn", "", "TURN", "Green"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), green_plus2, "Green Plus 2", "", "PLUS 2", "Green"));
        }

        // yellow
        // o only once
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_0, "Yellow 0", "", "0", "Yellow"));
        for(int twice=0;twice<2;twice++) {
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_1, "Yellow 1", "", "1", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_2, "Yellow 2", "", "2", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_3, "Yellow 3", "", "3", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_4, "Yellow 4", "", "4", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_5, "Yellow 5", "", "5", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_6, "Yellow 6", "", "6", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_7, "Yellow 7", "", "7", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_8, "Yellow 8", "", "8", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_9, "Yellow 9", "", "9", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_skip, "Yellow Skip", "", "SKIP", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_turn, "Yellow Turn", "", "TURN", "Yellow"));
            this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), yellow_plus2, "Yellow Plus 2", "", "PLUS 2", "Yellow"));
        }

        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));

        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cardDeck.add(new UnoCard(getApplicationContext(), deckPos, new Point(20,20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));

        // LOGGING ONLY
        Toast.makeText(getApplicationContext(), "CardDeck created. Size=" + this.cardDeck.size(), Toast.LENGTH_LONG).show();
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
        return (int)MainActivity.density * v;
    }


}
