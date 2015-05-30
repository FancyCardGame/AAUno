package at.fancycardgame.aauno.listeners;

import android.widget.Toast;

import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;

import at.fancycardgame.aauno.tasks.AuthenticateToGameCloudTask;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 27.05.2015.
 */
public class ConnectionRequestListener implements com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener {

    @Override
    public void onConnectDone(ConnectEvent connectEvent) {
        Tools.showToast("Successfully connected to the cloud.", Toast.LENGTH_SHORT);
        AuthenticateToGameCloudTask.isConnectedToCloud = true;
    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onInitUDPDone(byte b) {

    }
}
