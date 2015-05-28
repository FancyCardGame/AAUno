package at.fancycardgame.aauno.listeners;

import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

import java.util.ArrayList;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 27.05.2015.
 */
public class RoomRequestListener implements com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener {

    @Override
    public void onSubscribeRoomDone(RoomEvent roomEvent) {
        if(roomEvent.getData()!=null) {
            Tools.showToast("You subscribed to room " + roomEvent.getData().getName(), Toast.LENGTH_SHORT);
        }
        else
            Tools.showToast("roomEvent onSubscribeRoomDone was null!", Toast.LENGTH_SHORT);
    }

    @Override
    public void onUnSubscribeRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onJoinRoomDone(RoomEvent roomEvent) {
        if(roomEvent.getData()!=null)
            Tools.showToast("You joined the room " + roomEvent.getData().getName(), Toast.LENGTH_SHORT);
    }

    @Override
    public void onLeaveRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {
        // should be only executed when it is 0 or something, because it will also fill the arraylist when only the players are needed
        // idea: check current view --> should only do if currentview = join game view
        Tools.allRoomNamesList.add(liveRoomInfoEvent.getData().getName() + " - ID:" + liveRoomInfoEvent.getData().getId());

        Tools.playersInRoom = liveRoomInfoEvent.getJoinedUsers();
    }

    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onLockPropertiesDone(byte b) {

    }

    @Override
    public void onUnlockPropertiesDone(byte b) {

    }
}
