package at.fancycardgame.aauno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import at.fancycardgame.aauno.listeners.AbstractAnimationListener;
import at.fancycardgame.aauno.tasks.EnablingWLANTask;
import at.fancycardgame.aauno.toolbox.Tools;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static  final String PREFS_NAME = "My Preferences";
    Context savePrefs;

    // the whole display container where the content is shown
    private ViewGroup screen_container;

    // the viewgroups for the different options
    private ViewGroup optionsMenu;
    private ViewGroup userMgmtMenu;
    private ViewGroup createUserMenu;
    private ViewGroup changePwdMenu;

    private ProgressDialog progressDialog;

    private View.OnClickListener mainOnClickListener = this;

    // the card deck
    private UnoCardDeck cardDeck;

    // the logical density of the display
    private static float density;

    //test button
    private Button testBtn;
    private View playedCard;

    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!at.fancycardgame.aauno.User.isLoggedIn()) {
            DialogFragment loginDialog = new LoginDialogFragment();

            loginDialog.show(getSupportFragmentManager(), "login");
        }

        //SharedPrefernces: Save Login data

        Tools.init(this.getApplicationContext());
                    Tools.mainActivity = this;

                    // set menu typeface
                    this.setMenuTypeface();

                    // get density of display (to scale images later)
                    MainActivity.density = getResources().getDisplayMetrics().density;
                    // get container where game content is shown later
                    this.screen_container = (ViewGroup) findViewById(R.id.screens);

                    this.userMgmtMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.menu_usermgmt_page, null);
                    this.optionsMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.menu_options_page, null);

                    this.createUserMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.menu_createuser_page, null);
                    this.changePwdMenu = (ViewGroup) getLayoutInflater().inflate(R.layout.menu_changepwd_page, null);

                    // get current display
                    this.display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


                    // ***** PREPARE WHOLE MENU *******
                    // startpage
                    findViewById(R.id.startGameMP).setOnClickListener(this.mainOnClickListener);
                    findViewById(R.id.optionsMP).setOnClickListener(this.mainOnClickListener);
                    findViewById(R.id.helpMP).setOnClickListener(this.mainOnClickListener);
                    findViewById(R.id.quitMP).setOnClickListener(this.mainOnClickListener);
    }

    // method that takes *.ttf file, creates a typeface and applies it to the menu TextViews
    private void setMenuTypeface() {
        Tools.setStringTypeface(this, R.id.startGameMP);
        Tools.setStringTypeface(this, R.id.optionsMP);
        Tools.setStringTypeface(this, R.id.helpMP);
        Tools.setStringTypeface(this, R.id.quitMP);
    }

    // main onclick method
    @Override
    public void onClick(View clickedView) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = clickedView.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);

        if(clickedID==R.id.startGameMP) {
              // first check internet connection

             // checking internet connection
             if(!Tools.checkInternetConnection(this)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Network Connection");
                alertDialogBuilder.setMessage("Sorry, but you have to be connected to the internet when you want to play this game. Do you want to turn on WLAN?");
                alertDialogBuilder.setPositiveButton("Yeah, I want to play!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnablingWLANTask t = new EnablingWLANTask(MainActivity.this);
                        t.execute();
                    }
                }).setNegativeButton("No, thanks.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel
                        // TODO: do nothing? stay on menu? quit?
                    }
                });
                alertDialogBuilder.show();
             } else {
                 a.setAnimationListener(new AbstractAnimationListener() {
                     @Override
                     public void onAnimationEnd(Animation animation) {
                         // remove everything that is in screen_container
                        if (!at.fancycardgame.aauno.User.isLoggedIn()) {
                             DialogFragment loginDialog = new LoginDialogFragment();
                             loginDialog.show(getSupportFragmentManager(), "login");
                         } else if (at.fancycardgame.aauno.User.isLoggedIn()) {
                             //screen_container.removeAllViews();
                             // create gameboard from layout ...
                             // ... and add it to the screen_container
                             //screen_container.addView(gameBoard);

                             // start new gameActivity
                             startActivity(new Intent(MainActivity.this, GameActivity.class));

                             // "cleanup" TODO: check if necessary, because of back press button
                             //Tools.mainActivity = null;
                             //finish();
                         }
                     }
                 });
                 clickedView.startAnimation(a);
             }
        } else if(clickedID==R.id.optionsMP) {
           a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    //startOptionsMenu
                    startActivity(new Intent(MainActivity.this,OptionsActivity.class));
                }
            } );
            clickedView.startAnimation(a);
        } else if(clickedID==R.id.helpMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    //StartHelp/rules
                    startActivity(new Intent(getApplicationContext(), HelpActivity.class));
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
