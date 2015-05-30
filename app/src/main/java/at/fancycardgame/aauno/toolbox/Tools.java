package at.fancycardgame.aauno.toolbox;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.paas.sdk.android.App42API;

import java.util.ArrayList;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.MainActivity;
import at.fancycardgame.aauno.R;
import at.fancycardgame.aauno.User;
import at.fancycardgame.aauno.listeners.*;

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
    public static View.OnClickListener gameOnClickListner = new GameOnClickListener();

    // useful class variables
    public static String[] allRoomIDs;
    public static String currentRoom;
    public static ArrayList<String> allRoomNamesList = new ArrayList<>();
    public static ArrayList<String> joinedPlayers = new ArrayList<>();
    public static String[] playersInRoom;



    public static ConnectionRequestListener crl = new ConnectionRequestListener();
    public static NotifyListener nl = new NotifyListener();
    public static RoomRequestListener rrl = new RoomRequestListener();
    public static ZoneRequestListener zrl = new ZoneRequestListener();


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
                Tools.joinedPlayers.add(s + " (You)");
            else
                Tools.joinedPlayers.add(s);
    }

    public static void executeFromRemote(String command) {
        switch(command) {
            case Constants.PREP_TO_PLAY:
                Tools.startGameCountDown();
                break;

            case Constants.STARTGAME:
                Tools.startGame();
                break;
        }
    }

    public static void startGame(){
        Tools.game.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Tools.game.setContentView(R.layout.game_field);
                Tools.game.gameBoard = (ViewGroup)Tools.game.getLayoutInflater().inflate(R.layout.game_field, null);
                Tools.game.startGame();
            }
        });
    }

    public static void startGameCountDown() {
        Tools.game.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final TextView countdown = new TextView(Tools.appContext);
                countdown.setGravity(Gravity.CENTER);
                countdown.setTextSize(40);
                countdown.setTextColor(Color.BLACK);
                countdown.setText("4");

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
