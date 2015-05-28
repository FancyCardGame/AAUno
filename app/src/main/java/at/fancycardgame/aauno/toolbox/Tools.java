package at.fancycardgame.aauno.toolbox;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.paas.sdk.android.App42API;

import java.util.ArrayList;

import at.fancycardgame.aauno.GameActivity;
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
            String text = msg.obj.toString().split(":")[0];
            int duration = Integer.parseInt(msg.obj.toString().split(":")[1]);
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
        m.obj = msg + ":" + toastLength;
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
            case "START":
                Tools.game.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Tools.game.setContentView(R.layout.game_field);
                        Tools.game.gameBoard = (ViewGroup)Tools.game.getLayoutInflater().inflate(R.layout.game_field, null);
                        Tools.game.startGame();
                    }
                });
                break;
        }
    }
}
