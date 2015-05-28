package at.fancycardgame.aauno.listeners;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import at.fancycardgame.aauno.R;
import at.fancycardgame.aauno.User;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 28.05.2015.
 */
public class GameOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = v.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(Tools.appContext, R.anim.pulse);

        if(clickedID==R.id.createGameMP) {
                a.setAnimationListener(new AbstractAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Tools.game.setContentView(Tools.game.game_activity_creategame);


                        Tools.game.findViewById(R.id.btnStartGameLobby).setOnClickListener(Tools.gameOnClickListner);

                        //prepare spinner in creategame view
                        Spinner maxUsers = (Spinner)Tools.game.findViewById(R.id.spinnerMaxUsers);
                        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(Tools.appContext, R.array.maxPlayersSelection, android.R.layout.simple_spinner_dropdown_item);
                        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        maxUsers.setAdapter(spnAdapter);

                    }
                });
                v.startAnimation(a);
        } else if(clickedID==R.id.joinGameMP) {
                a.setAnimationListener(new AbstractAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Tools.game.setContentView(Tools.game.game_activity_joingame);

                        // clearing roomList because it's being refilled
                        Tools.allRoomNamesList.clear();

                        // must be placed here, tried also outside after v.startAnim(a);
                        Tools.wClient.getAllRooms();
                        Tools.showToast("Wait some seconds...", Toast.LENGTH_LONG);
                    }
                });
                v.startAnimation(a);

                // TODO: time maybe can be reduced
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Tools.game.updateOpenGamesList();
                    }
                }, 5000);

        } else if(clickedID==R.id.btnStartGameLobby) {

            // getting entered info & create room in cloud
            String gamename = ((TextView)Tools.game.findViewById(R.id.txtBoxGameName)).getText().toString();
            int maxUsers = Integer.parseInt(((Spinner)Tools.game.findViewById(R.id.spinnerMaxUsers)).getSelectedItem().toString());

            Tools.wClient.createRoom(gamename, User.getUsername(), maxUsers, null);

            Tools.game.setContentView(Tools.game.game_activity_startedGameLobby);

            Tools.joinedPlayers.add(User.getUsername() + " (You)");

            // worked with waiting 2 seconds after room was created
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Tools.wClient.joinRoom(Tools.currentRoom);
                    Tools.wClient.subscribeRoom(Tools.currentRoom);
                }
            }, 2000);



            Tools.game.updateJoinedPlayersListView();



        } else if(clickedID==R.id.btnPlay) {
            Tools.showToast("game should start!", Toast.LENGTH_SHORT);
            // check if e.g. 2 of 2 users are connected, 4 of 4 ...

            // WarpClient supports startGame() only in TurnedBasedRooms
            //Tools.wClient.startGame();
            String s = "START";
            Tools.wClient.sendUpdatePeers(s.getBytes());

            Tools.game.setContentView(R.layout.game_field);
            Tools.game.gameBoard = (ViewGroup)Tools.game.getLayoutInflater().inflate(R.layout.game_field, null);
            Tools.game.startGame();
        }
    }
}