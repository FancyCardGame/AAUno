package at.fancycardgame.aauno.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.fancycardgame.aauno.R;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 30.05.2015.
 */
public class JoinedPlayersAdapter extends BaseAdapter {

    ArrayList<String> players;
    LayoutInflater inflater;

    public JoinedPlayersAdapter(Context context, ArrayList<String> players) {
        super();

        this.players = players;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String player = this.players.get(position);

        View vi = convertView;
        if(convertView == null)
            vi = this.inflater.inflate(R.layout.listview_item_row_player, null);

        TextView joinedPlayer = (TextView)vi.findViewById(R.id.txtPlayername);
        joinedPlayer.setText(player);

        vi.setEnabled(false);
        vi.setOnClickListener(null);

        return vi;
    }
}
