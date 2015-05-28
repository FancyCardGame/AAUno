package at.fancycardgame.aauno.listeners;


import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 27.05.2015.
 */



public class ZoneRequestListener implements com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener {

    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {
        Tools.allRoomIDs = allRoomsEvent.getRoomIds();
        if(Tools.allRoomIDs!=null)
            for(String id : Tools.allRoomIDs) {
                if(id!=null)
                    Tools.wClient.getLiveRoomInfo(id);
            }
        else
            Tools.showToast("allRoomIDs arraylist was null!", Toast.LENGTH_SHORT);
    }

    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {
        Tools.showToast("Room created.", Toast.LENGTH_SHORT);
        Tools.currentRoom = roomEvent.getData().getId();
    }

    @Override
    public void onGetOnlineUsersDone(AllUsersEvent allUsersEvent) {

    }

    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent) {

    }

}
