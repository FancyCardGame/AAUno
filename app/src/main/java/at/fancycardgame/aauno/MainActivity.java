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



public class MainActivity extends Activity implements View.OnClickListener{

    // define font name, can be changed later on here
    private static final String menu_font = "Comic Book Bold.ttf";
    // the whole display container where the content is shown
    private ViewGroup screen_container;
    // the whole container where the game takes place
    private ViewGroup gameBoard;
    // the card deck
    private UnoCardDeck cardDeck;

    // the logical density of the display
    private static float density;


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
        this.cardDeck = new UnoCardDeck(this.getApplicationContext(), (FrameLayout)deckPosition);

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
                        // remove from current owner
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        // get current X and Y coordinates from drop event
                        view.setX(event.getX() - (view.getWidth() / 2));
                        view.setY(event.getY()-(view.getHeight()/2));

                        // add dropped view to new parent (playCardsPosition)
                        ((ViewGroup)findViewById(R.id.playCardsPosition)).addView(view);
                        // make original view visible again
                        view.setVisibility(View.VISIBLE);
                        // delete touchlistener
                        view.setOnTouchListener(null);
                        break;
                    default:
                        // nothing
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
