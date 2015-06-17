package at.fancycardgame.aauno;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Christian on 11.06.2015.
 */
public class HelpActivity extends Activity {

    TextView infoText;
    TextView url;

    private static final String menu_font = "Comic Book Bold.ttf";

    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.help_activity_rules);

       infoText = (TextView)findViewById(R.id.infoTextID);
       infoText.setMovementMethod(new ScrollingMovementMethod());
       setOptionsMenuTypeface();
       String info =
                "<p><u>Information:</u></p> \n" +
                "\n" +
                "<p><i>To play this game, you have to create a user.\n" +
                "You can find this option under options/usermanagement/create user. \n" +
                "Later you are able to change the password under ../change password.\n" +
                "In order to play, you are able to create or join an existing room. " +
                "If you create a room, you can set the name of the room and the " +
                "number of players. Other players will be able to join your session, that " +
                "is listed as an entry name in the join game option. " +
                "The player, who created the game makes the first turn and mixes the " +
                "card deck by shaking the device.</i></p>" +
                "<u>Rules:</u>" +
                "\n" +
                "<p><i>Each player is dealt 7 cards with the remaining ones placed face down to form a DRAW pile. " +
                "The top card of the DRAW pile is turned over to begin a DISCARD pile. " +
                "The first player has to match the card in the DISCARD pile either by number, color or word. " +
                "For example, if the card is a red 7, player must throw down a red card or any color 7. " +
                "Or the player can throw down a Wild Card. " +
                "If the player doesn't have anything to match, he must pick a card from the DRAW pile. " +
                "If he can play what is drawn, great. Otherwise play moves to the next person. " +
                "When you have one card left, you must yell UNO (meaning one)." +
                "Failure to do this results in you having to pick two cards from the DRAW pile. That is," +
                "of course if you get caught by the other players. " +
                "Once a player has no cards left, the hand is over. " +
                "Points are scored (see scoring section) and you start over again.</i></p>"
                + "\n" +
                "<p>For more information follow this link:<p>"
                +"http://www.wonkavator.com/uno/unorules.html";
        infoText.setText(Html.fromHtml(info));
        Linkify.addLinks(infoText, Linkify.WEB_URLS);
    }
    private void setOptionsMenuTypeface() {
        Typeface menu_userm = Typeface.createFromAsset(getAssets(), menu_font);
        setStringTypeface(R.id.infoTextID);
    }
    private void setStringTypeface(int textview) {
        Typeface options_menu = Typeface.createFromAsset(getAssets(), menu_font);
        ((TextView) findViewById(textview)).setTypeface(options_menu);
    }
}
