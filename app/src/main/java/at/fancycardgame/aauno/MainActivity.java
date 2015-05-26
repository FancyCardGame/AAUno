package at.fancycardgame.aauno;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import java.util.Set;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    // App42 API key / Secret key
    private static final String API_KEY = "c908e0df7084fad2caab981905cf15d77943912511d6550747e46d7dd5e665ce";
    private static final String SECRET_KEY = "02fd16bc09eb0b836d3794b3d6dfcac6c010e12e08ef454c0fc91f6a77ec1249";

    // define font name, can be changed later on here
    private static final String menu_font = "Comic Book Bold.ttf";
    // the whole display container where the content is shown
    private ViewGroup screen_container;
    // the whole container where the game takes place
    private ViewGroup gameBoard;
    // the viewgroups for the different options
    private ViewGroup optionsMenu;
    private ViewGroup userMgmtMenu;
    private ViewGroup createUserMenu;
    private ViewGroup changePwdMenu;


    private View.OnClickListener mainOnClickListener = this;

    private static Context appContext;
    private static Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(MainActivity.appContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    // the card deck
    private UnoCardDeck cardDeck;

    // the logical density of the display
    private static float density;

    // Loginstatus
    private static boolean isUserLoggedIn = false;

    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init App42 SDK
        this.initApp42SDK();

        // set menu typeface
        this.setMenuTypeface();

        MainActivity.appContext = this.getApplicationContext();
        // get density of display (to scale images later)
        MainActivity.density = getResources().getDisplayMetrics().density;
        // get container where game content is shown later
        this.screen_container = (ViewGroup)findViewById(R.id.screens);

        this.userMgmtMenu = (ViewGroup)getLayoutInflater().inflate(R.layout.menu_usermgmt_page, null);
        this.optionsMenu = (ViewGroup)getLayoutInflater().inflate(R.layout.menu_options_page, null);
        this.gameBoard = (ViewGroup)getLayoutInflater().inflate(R.layout.game_field, null);
        this.createUserMenu = (ViewGroup)getLayoutInflater().inflate(R.layout.menu_createuser_page, null);
        this.changePwdMenu = (ViewGroup)getLayoutInflater().inflate(R.layout.menu_changepwd_page, null);

        // get current display
        this.display = ((WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        // ***** PREPARE WHOLE MENU *******
        // startpage
        findViewById(R.id.startGameMP).setOnClickListener(this.mainOnClickListener);
        findViewById(R.id.optionsMP).setOnClickListener(this.mainOnClickListener);
        findViewById(R.id.helpMP).setOnClickListener(this.mainOnClickListener);
        findViewById(R.id.quitMP).setOnClickListener(this.mainOnClickListener);
    }

    private void initApp42SDK() {
        App42API.initialize(getApplicationContext(),MainActivity.API_KEY , MainActivity.SECRET_KEY);
    }


    // method that takes *.ttf file, creates a typeface and applies it to the menu TextViews
    private void setMenuTypeface() {
        Typeface menu = Typeface.createFromAsset(getAssets(), menu_font);
        setStringTypeface(R.id.startGameMP);
        setStringTypeface(R.id.optionsMP);
        setStringTypeface(R.id.helpMP);
        setStringTypeface(R.id.quitMP);
    }

    private void setStringTypeface(int textview) {
        Typeface menu = Typeface.createFromAsset(getAssets(), menu_font);
        ((TextView)findViewById(textview)).setTypeface(menu);
    }

    // main onclick method
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
                    if(isUserLoggedIn == false) {
                        DialogFragment loginDialog = new LoginDialogFragment();
                        loginDialog.show(getSupportFragmentManager(), "login");
                    }

                    else if(isUserLoggedIn == true) {
                        screen_container.removeAllViews();
                        // create gameboard from layout ...
                        // ... and add it to the screen_container
                        screen_container.addView(gameBoard);
                        startGame();
                    }

                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedID==R.id.optionsMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    // remove everything that is in screen_container
                    screen_container.removeAllViews();
                    // create options menue from layout ...


                    // ... and add it to the screen_container
                    screen_container.addView(optionsMenu);

                    setStringTypeface(R.id.userMgmtMP);
                    setStringTypeface(R.id.musicOnOffMP);
                    setStringTypeface(R.id.effectsOnOffMP);


                    findViewById(R.id.userMgmtMP).setOnClickListener(mainOnClickListener);
                    findViewById(R.id.musicOnOffMP).setOnClickListener(mainOnClickListener);
                    findViewById(R.id.effectsOnOffMP).setOnClickListener(mainOnClickListener);
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
        // ************************ USER MANAGEMENT MENU ****************************
        else if (clickedID == R.id.userMgmtMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // access user mgmt
                    screen_container.removeAllViews();
                    screen_container.addView(userMgmtMenu);

                    setStringTypeface(R.id.createUserMP);
                    setStringTypeface(R.id.changePwdMP);


                    findViewById(R.id.createUserMP).setOnClickListener(mainOnClickListener);
                    findViewById(R.id.changePwdMP).setOnClickListener(mainOnClickListener);
                }
            });
            clickedView.startAnimation(a);

        } else if (clickedID == R.id.musicOnOffMP) {
            //music on/off
        } else if (clickedID == R.id.effectsOnOffMP) {
            // effects on/off
        } // TODO: implement return arrow + add if(..) here
        // ************************ CREATE USER MENU *******************************
        else if(clickedID == R.id.createUserMP) {

            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // access user mgmt
                    screen_container.removeAllViews();
                    screen_container.addView(createUserMenu);

                    setStringTypeface(R.id.createUserHeadline);
                    setStringTypeface(R.id.createUserUsernameStr);
                    setStringTypeface(R.id.createUserPasswordStr);
                    setStringTypeface(R.id.createUserMailStr);

                    findViewById(R.id.btnCreateUser).setOnClickListener(mainOnClickListener);

                    // assign buttonlistener
                    //findViewById(R.id.createUserMP).setOnClickListener(this.mainOnClickListener);
                    //findViewById(R.id.changePwdMP).setOnClickListener(this.mainOnClickListener);
                }
            });
            clickedView.startAnimation(a);
        }
        // ************************ CHANGE PWD MENU *****************************
        else if(clickedID==R.id.changePwdMP) {

            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    screen_container.removeAllViews();
                    screen_container.addView(changePwdMenu);

                    setStringTypeface(R.id.changePwdHeadline);
                    setStringTypeface(R.id.oldPwdStr);
                    setStringTypeface(R.id.newPwdStr);

                    findViewById(R.id.btnChangePWD).setOnClickListener(mainOnClickListener);
                }
            });
            clickedView.startAnimation(a);
        }
        // *********************** CREATE USER BTN CLICKED **********************
        else if(clickedID == R.id.btnCreateUser) {

            String username = ((TextView)findViewById(R.id.txtBoxUsername)).getText().toString();
            String password = ((TextView)findViewById(R.id.txtBoxPwd)).getText().toString();
            String email = ((TextView)findViewById(R.id.txtBoxMail)).getText().toString();
            this.createUser(username, password, email);

        }
        else if(clickedID == R.id.btnChangePWD) {

            String oldPwd = ((TextView)findViewById(R.id.txtBoxOldPwd)).getText().toString();
            String newPwd = ((TextView)findViewById(R.id.txtBoxNewPwd)).getText().toString();

            //Session needed for logged in User
            //this.changePassword(Username, oldPwd, newPwd);
        }
        // if the same OnClickListener is used continue here with else if(...)
    }

    private void createUser(String username, String password, String email) {

        UserService userService = App42API.buildUserService();
        userService.createUser(username, password, email, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {

                Message msg = new Message();
                msg.obj = "User successfully created.";
                toastHandler.sendMessage(msg);
                //User user = (User)response;
                //Toast.makeText(getApplicationContext(),"Successfully created User.", Toast.LENGTH_SHORT);

                //
            }

            @Override
            public void onException(Exception ex) {
                //Toast.makeText(getApplicationContext(),"Error creating User.", Toast.LENGTH_SHORT);
                Message msg = new Message();
                msg.obj = "Error creating user. ERROR: " + ex.getMessage();
                toastHandler.sendMessage(msg);
            }
        });
    }

    private void changePassword(String username, String oldPwd, String newPwd) {

        UserService userService = App42API.buildUserService();
        userService.changeUserPassword(username, oldPwd, newPwd, new App42CallBack() {

            @Override
            public void onSuccess(Object o) {

                //show User a message that password has changed successfully
            }

            @Override
            public void onException(Exception e) {

                //show User a message that password has not changed
            }
        });
    }

    public static void login(String username, String password) {

        UserService userService = App42API.buildUserService();
        userService.authenticate(username, password, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                User user = (User)response;
                isUserLoggedIn = true;

                Message msg = new Message();
                msg.obj = "User successfully logged in.";
                toastHandler.sendMessage(msg);

            }

            @Override
            public void onException(Exception ex) {
                System.out.println("Exception Message : "+ ex.getMessage());
            }
        });
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


            UnoCard test2 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-50, res.y-130), getResources().getDrawable(R.drawable.blue_2), getResources().getDrawable(R.drawable.card_back), "Blue 2", "", "2", "Blue");
            UnoCard test3 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-100, res.y-130), getResources().getDrawable(R.drawable.red_6), getResources().getDrawable(R.drawable.card_back),"Red 6", "", "6", "Red");
            UnoCard test4 = new UnoCard(getApplicationContext(), (FrameLayout)((ViewGroup)findViewById(R.id.container)), new Point(res.x/2-150, res.y-130), getResources().getDrawable(R.drawable.green_9), getResources().getDrawable(R.drawable.card_back),"Green 9", "", "9", "Green");

           test2.viewFront();
           test3.viewFront();
           test4.viewFront();
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
}
