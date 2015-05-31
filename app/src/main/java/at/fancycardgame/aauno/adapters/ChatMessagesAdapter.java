package at.fancycardgame.aauno.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.fancycardgame.aauno.R;

/**
 * Created by Thomas on 30.05.2015.
 */
public class ChatMessagesAdapter extends BaseAdapter {

    ArrayList<String> chatMsg;
    LayoutInflater inflater;

    public ChatMessagesAdapter(Context context, ArrayList<String> msgQ) {
        super();

        this.chatMsg = msgQ;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.chatMsg.size();
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

        String player = this.chatMsg.get(position);

        View vi = convertView;
        if(convertView == null)
            vi = this.inflater.inflate(R.layout.listview_item_row_chat, null);

        TextView chatMsg = (TextView)vi.findViewById(R.id.txtChatMsg);
        chatMsg.setText(player);

        vi.setEnabled(false);
        vi.setOnClickListener(null);

        return vi;
    }
}

