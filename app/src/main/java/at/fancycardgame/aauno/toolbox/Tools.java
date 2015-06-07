package at.fancycardgame.aauno.toolbox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.paas.sdk.android.App42API;

import java.util.ArrayList;
import java.util.Arrays;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.MainActivity;
import at.fancycardgame.aauno.R;
import at.fancycardgame.aauno.User;
import at.fancycardgame.aauno.listeners.ConnectionRequestListener;
import at.fancycardgame.aauno.listeners.GameOnClickListener;
import at.fancycardgame.aauno.listeners.NotifyListener;
import at.fancycardgame.aauno.listeners.RoomRequestListener;
import at.fancycardgame.aauno.listeners.ShakeEventListener;
import at.fancycardgame.aauno.listeners.WarpListener;
import at.fancycardgame.aauno.listeners.ZoneRequestListener;

/**
 * Created by Thomas on 27.05.2015.
 */
public class Tools {
    // context of app
    public static Context appContext;

    // the main warp client
    public static WarpClient wClient;

    public static MainActivity mainActivity;

    public static GameActivity game;
    public static TextView shakeCardDeckHint;

    private static ShakeEventListener mShakeDetector;
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;


    public static View.OnClickListener gameOnClickListner = new GameOnClickListener();

    // it's all about the room
    public static String[] allRoomIDs;
    public static String currentRoom;
    public static String currentRoomName;
    public static String roomOwner;
    public static int maxPlayersInRoom;
    public static ArrayList<String> joinedPlayers = new ArrayList<>();
    public static String[] playersInRoom;
    public static ArrayList<String> chatQueue = new ArrayList<>();

    public static ArrayList<String> allRoomNamesList = new ArrayList<>();

    public static ConnectionRequestListener crl = new ConnectionRequestListener();
    public static NotifyListener nl = new NotifyListener();
    public static RoomRequestListener rrl = new RoomRequestListener();
    public static ZoneRequestListener zrl = new ZoneRequestListener();

    //public static boolean isSender = false;
    public static int nextPlayer = 1;
    public static int currPlayer = 0;

    public static int testCounter = 0;


    public static void init(Context ac) {
            Tools.appContext = ac;

            App42API.initialize(Tools.appContext, Constants.API_KEY, Constants.SECRET_KEY);
            WarpClient.initialize(Constants.API_KEY, Constants.SECRET_KEY);

            try {
                Tools.wClient = WarpClient.getInstance();
                // WarpClient.enableTrace(true);
            } catch (Exception ex) {
                Tools.showToast("Exception in Initialization.", Toast.LENGTH_SHORT);
            }

            Tools.wClient.addConnectionRequestListener(Tools.crl);
            Tools.wClient.addNotificationListener(Tools.nl);
            Tools.wClient.addRoomRequestListener(Tools.rrl);
            Tools.wClient.addZoneRequestListener(Tools.zrl);

           // Tools.wClient.addNotificationListener(eventHandler);
            //Tools.wClient.addRoomRequestListener(eventHandler);


    }


    // methods to show short toasts, no matter where you are
    private static Handler shortToastHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            String text = msg.obj.toString().split("§")[0];
            int duration = Integer.parseInt(msg.obj.toString().split("§")[1]);
            switch (duration) {
                case Toast.LENGTH_LONG:
                    Toast.makeText(Tools.appContext, text, Toast.LENGTH_LONG).show();
                    break;
                case Toast.LENGTH_SHORT:
                    Toast.makeText(Tools.appContext, text, Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };
    public static void showToast(String msg, int toastLength) {
        Message m = new Message();
        m.obj = msg + "§" + toastLength;
        shortToastHandler.sendMessage(m);
    }

    public static void playerArrayToList() {
        for(String s : Tools.playersInRoom)
            if(s.equals(User.getUsername()))
                //Tools.joinedPlayers.add(s + " (You)");
                Tools.joinedPlayers.add(s);
            else
                Tools.joinedPlayers.add(s);
    }

    public static void executeFromRemote(String command) {


        switch(command) {
            //Multiplayer Test
            case "TEST":
                Tools.game.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Tools.game, "TEST", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "NEXT":
                Tools.game.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Tools.game, "nextPlayer: " + Tools.game.getNextPlayer(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Tools.game, "currPlayer: " + Tools.game.getCurrPlayer(), Toast.LENGTH_SHORT).show();
                    }
                });
                /*Tools.game.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Tools.game, "Players: " + Tools.joinedPlayers.toString(), Toast.LENGTH_SHORT).show();
                        Tools.game.setYourTurn(false);
                        for (String player : Tools.joinedPlayers){
                            Tools.game.setYourTurn(false);
                        }
                        for (String player : Tools.joinedPlayers){
                            //Tools.game.setYourTurn(false);
                            if (player.equals(Tools.joinedPlayers.get(Tools.nextPlayer))){
                                testCounter ++;
                                Toast.makeText(Tools.game, "player: " + player + " joinedPlayers.get(nextPlayer):" + Tools.joinedPlayers.get(nextPlayer), Toast.LENGTH_LONG).show();
                                Tools.game.setYourTurn(true);
                                if (Tools.nextPlayer < Tools.joinedPlayers.size() - 1){
                                    Tools.nextPlayer++;
                                } else {
                                    Tools.nextPlayer = 0;
                                }

                                if (Tools.currPlayer < Tools.joinedPlayers.size() - 1){
                                    Tools.currPlayer++;
                                } else {
                                    Tools.currPlayer = 0;
                                }
                            break;
                            }
                        }
                        Toast.makeText(Tools.game, "testCounter:" + testCounter, Toast.LENGTH_SHORT).show();
                    }
                });*/
                break;
            case Constants.PREP_TO_PLAY:
                Tools.startGameCountDown();
                break;

            case Constants.STARTGAME:
                // vibration feedback when device was shaked
                Vibrator vib = (Vibrator)Tools.game.getSystemService(Context.VIBRATOR_SERVICE);
                if(vib!=null)
                    vib.vibrate(100);

                Tools.game.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // no matter who you are just hide the mixText txt on screen
                        Tools.shakeCardDeckHint.setVisibility(View.GONE); // <-- not working, but also no exeception
                        //Tools.shakeCardDeckHint.setText("");
                        //Tools.shakeCardDeckHint.invalidate(); // <-- not working, but also no exeception


                        //Tools.showToast(v.toString(), Toast.LENGTH_LONG);
                        // not working -->((View)mixText.getParent()).invalidate();
                        // not working --> Tools.game.getWindow().getDecorView().findViewById(mixText.getId()).setVisibility(View.GONE);
                        //((FrameLayout)mixText.getParent()).invalidate();
                        //((FrameLayout)mixText.getParent()).removeView(mixText);
                        if (!Tools.roomOwner.equals(User.getUsername())){
                            Tools.game.setYourTurn(false);
                        } else {
                            Tools.game.setYourTurn(true);
                        }
                        Tools.game.setNextPlayer(1);
                        Tools.game.startGame();
                    }
                });
                break;

            case Constants.WAIT_FOR_MIX:
                // show gamefield but start game not yet

                Tools.showGameField();

                if(!Tools.roomOwner.equals(User.getUsername()))
                // if you're not the admin
                    Tools.game.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // For now, admin will be the first player
                            //Tools.game.setYourTurn(false);

                            Tools.shakeCardDeckHint= new TextView(Tools.game.getApplicationContext());
                            Typeface font = Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book Bold.ttf");
                            Tools.shakeCardDeckHint.setTypeface(font);
                            Tools.shakeCardDeckHint.setGravity(Gravity.CENTER);
                            Tools.shakeCardDeckHint.setTextSize(50);
                            Tools.shakeCardDeckHint.setTextColor(Color.YELLOW);
                            Tools.shakeCardDeckHint.setText("Wait until match maker mixes the card deck!");

                            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            Tools.game.addContentView(Tools.shakeCardDeckHint, p);

                            Tools.shakeCardDeckHint.invalidate();
                        }
                    });
                else
                // if you're the admin
                // Put the STARTGAME command outside of the Shake Listener to start it in the emulator
                    //Tools.game.setYourTurn(true);
                    Tools.wClient.sendUpdatePeers(Constants.STARTGAME.getBytes());
                    Tools.game.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Tools.shakeCardDeckHint= new TextView(Tools.game.getApplicationContext());
                            Typeface font = Typeface.createFromAsset(Tools.game.getAssets(), "Comic Book Bold.ttf");
                            Tools.shakeCardDeckHint.setTypeface(font);
                            Tools.shakeCardDeckHint.setGravity(Gravity.CENTER);
                            Tools.shakeCardDeckHint.setTextSize(50);
                            Tools.shakeCardDeckHint.setTextColor(Color.YELLOW);
                            Tools.shakeCardDeckHint.setText("Mix the card deck by shaking your device!");

                            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            Tools.game.addContentView(Tools.shakeCardDeckHint, p);
                            Tools.shakeCardDeckHint.invalidate();

                            // shake listener init
                            mSensorManager = (SensorManager)Tools.game.getSystemService(Context.SENSOR_SERVICE);
                            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                            mShakeDetector = new ShakeEventListener(new ShakeEventListener.OnShakeListener() {
                                @Override
                                public void onShake() {
                                    Tools.showToast("Mix it!", Toast.LENGTH_SHORT);

                                    // send start command
                                    Tools.wClient.sendUpdatePeers(Constants.STARTGAME.getBytes());

                                    // only use it once then unregister
                                    mSensorManager.unregisterListener(mShakeDetector);
                                }
                            });
                            // register after init, it will unregister itself in onShake method
                            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                        }
                    });
                break;
        }
    }

    public static void showGameField(){
        Tools.game.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Tools.game.setContentView(R.layout.game_field);
                Tools.game.gameBoard = (ViewGroup)Tools.game.getLayoutInflater().inflate(R.layout.game_field, null);
            }
        });
    }

    public static void startGameCountDown() {
        Tools.game.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView countdown = new TextView(Tools.appContext);
                countdown.setGravity(Gravity.CENTER);
                countdown.setTextSize(70);
                countdown.setTextColor(Color.YELLOW);
                countdown.setText("3");

                ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                Tools.game.addContentView(countdown, p);

                new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        countdown.setText(""+millisUntilFinished/1000);
                        countdown.invalidate();
                    }
                    @Override
                    public void onFinish() {
                        countdown.setVisibility(View.INVISIBLE);
                       //Tools.hideView(countdown.getId());
                    }
                }.start();
            }
        });
    }

    public static boolean checkInternetConnection(Activity activity){
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Tools.appContext.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(!(netInfo!=null && netInfo.isConnected()))
            return false;
        else
            return true;
    }

    // method that sets a view invisible which is specified with a parameter
    public static void hideView(final int view) {
        Tools.game.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Tools.game.findViewById(view).setVisibility(View.INVISIBLE);
            }
        });
    }

    // method that sets a view visible which is specified with a parameter
    public static void showView(int view) {
        Tools.game.findViewById(view).setVisibility(View.VISIBLE);
    }
}
