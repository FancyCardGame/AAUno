package at.fancycardgame.aauno;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas on 15.04.2015.
 *
 *
 * TODO:
 *  - distribute card 1) only to one player , 2) to all registered players
 *  - giveCards(int x) - where cards
 */
public class UnoCardDeck {
    // the list were the cards are stored
    private ArrayList<UnoCard> cards = new ArrayList<UnoCard>();
    // the application context
    private Context appContext;
    // the position of the deck
    private FrameLayout deckPos;

    // constructor
    public UnoCardDeck(Context appcon, FrameLayout deckPos) {
        this.appContext = appcon;
        this.deckPos = deckPos;
        this.createCardDeck(deckPos);
    }

    /**
     * Method that creates UNO card deck.
     * 1) drawables are initialized
     * 2) UnoCards objects are created with the initialized drawables
     * 3) simultaneously they are added to the cardDeck Hashmap
     * <p/>
     * INFO: point is created with -999, -999 (x,y) coordinates to move deck out of screen
     * TODO: backside of card, init drawable and extend constructor (in class UnoCard and on object creation)
     */
    private void createCardDeck(FrameLayout deckPos) {
        // create drawables for  normal cards BLUE
        Drawable blue_0 = this.appContext.getResources().getDrawable(R.drawable.blue_0);
        Drawable blue_1 = this.appContext.getResources().getDrawable(R.drawable.blue_1);
        Drawable blue_2 = this.appContext.getResources().getDrawable(R.drawable.blue_2);
        Drawable blue_3 = this.appContext.getResources().getDrawable(R.drawable.blue_3);
        Drawable blue_4 = this.appContext.getResources().getDrawable(R.drawable.blue_4);
        Drawable blue_5 = this.appContext.getResources().getDrawable(R.drawable.blue_5);
        Drawable blue_6 = this.appContext.getResources().getDrawable(R.drawable.blue_6);
        Drawable blue_7 = this.appContext.getResources().getDrawable(R.drawable.blue_7);
        Drawable blue_8 = this.appContext.getResources().getDrawable(R.drawable.blue_8);
        Drawable blue_9 = this.appContext.getResources().getDrawable(R.drawable.blue_9);
        // create drawables for  special cards BLUE
        Drawable blue_skip = this.appContext.getResources().getDrawable(R.drawable.blue_skip);
        Drawable blue_plus2 = this.appContext.getResources().getDrawable(R.drawable.blue_plus2);
        Drawable blue_turn = this.appContext.getResources().getDrawable(R.drawable.blue_turn);

        // create drawables for  normal cards RED
        Drawable red_0 = this.appContext.getResources().getDrawable(R.drawable.red_0);
        Drawable red_1 = this.appContext.getResources().getDrawable(R.drawable.red_1);
        Drawable red_2 = this.appContext.getResources().getDrawable(R.drawable.red_2);
        Drawable red_3 = this.appContext.getResources().getDrawable(R.drawable.red_3);
        Drawable red_4 = this.appContext.getResources().getDrawable(R.drawable.red_4);
        Drawable red_5 = this.appContext.getResources().getDrawable(R.drawable.red_5);
        Drawable red_6 = this.appContext.getResources().getDrawable(R.drawable.red_6);
        Drawable red_7 = this.appContext.getResources().getDrawable(R.drawable.red_7);
        Drawable red_8 = this.appContext.getResources().getDrawable(R.drawable.red_8);
        Drawable red_9 = this.appContext.getResources().getDrawable(R.drawable.red_9);
        // create drawables for  special cards red
        Drawable red_skip = this.appContext.getResources().getDrawable(R.drawable.red_skip);
        Drawable red_plus2 = this.appContext.getResources().getDrawable(R.drawable.red_plus2);
        Drawable red_turn = this.appContext.getResources().getDrawable(R.drawable.red_turn);

        // create drawables for  normal cards GREEN
        Drawable green_0 = this.appContext.getResources().getDrawable(R.drawable.green_0);
        Drawable green_1 = this.appContext.getResources().getDrawable(R.drawable.green_1);
        Drawable green_2 = this.appContext.getResources().getDrawable(R.drawable.green_2);
        Drawable green_3 = this.appContext.getResources().getDrawable(R.drawable.green_3);
        Drawable green_4 = this.appContext.getResources().getDrawable(R.drawable.green_4);
        Drawable green_5 = this.appContext.getResources().getDrawable(R.drawable.green_5);
        Drawable green_6 = this.appContext.getResources().getDrawable(R.drawable.green_6);
        Drawable green_7 = this.appContext.getResources().getDrawable(R.drawable.green_7);
        Drawable green_8 = this.appContext.getResources().getDrawable(R.drawable.green_8);
        Drawable green_9 = this.appContext.getResources().getDrawable(R.drawable.green_9);
        // create drawables for  special cards GREEN
        Drawable green_skip = this.appContext.getResources().getDrawable(R.drawable.green_skip);
        Drawable green_plus2 = this.appContext.getResources().getDrawable(R.drawable.green_plus2);
        Drawable green_turn = this.appContext.getResources().getDrawable(R.drawable.green_turn);

        // create drawables for  normal cards YELLOW
        Drawable yellow_0 = this.appContext.getResources().getDrawable(R.drawable.yellow_0);
        Drawable yellow_1 = this.appContext.getResources().getDrawable(R.drawable.yellow_1);
        Drawable yellow_2 = this.appContext.getResources().getDrawable(R.drawable.yellow_2);
        Drawable yellow_3 = this.appContext.getResources().getDrawable(R.drawable.yellow_3);
        Drawable yellow_4 = this.appContext.getResources().getDrawable(R.drawable.yellow_4);
        Drawable yellow_5 = this.appContext.getResources().getDrawable(R.drawable.yellow_5);
        Drawable yellow_6 = this.appContext.getResources().getDrawable(R.drawable.yellow_6);
        Drawable yellow_7 = this.appContext.getResources().getDrawable(R.drawable.yellow_7);
        Drawable yellow_8 = this.appContext.getResources().getDrawable(R.drawable.yellow_8);
        Drawable yellow_9 = this.appContext.getResources().getDrawable(R.drawable.yellow_9);
        // create drawables for  special cards YELLOW
        Drawable yellow_skip = this.appContext.getResources().getDrawable(R.drawable.yellow_skip);
        Drawable yellow_plus2 = this.appContext.getResources().getDrawable(R.drawable.yellow_plus2);
        Drawable yellow_turn = this.appContext.getResources().getDrawable(R.drawable.yellow_turn);

        // create drawables for  special cards
        Drawable color_change = this.appContext.getResources().getDrawable(R.drawable.color_change);
        Drawable color_change_plus4 = this.appContext.getResources().getDrawable(R.drawable.color_change_plus4);

        // blue
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_0, "Blue 0", "", "0", "Blue"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_1, "Blue 1", "", "1", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_2, "Blue 2", "", "2", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_3, "Blue 3", "", "3", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_4, "Blue 4", "", "4", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_5, "Blue 5", "", "5", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_6, "Blue 6", "", "6", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_7, "Blue 7", "", "7", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_8, "Blue 8", "", "8", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_9, "Blue 9", "", "9", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_skip, "Blue Skip", "", "SKIP", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_turn, "Blue Turn", "", "TURN", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_plus2, "Blue Plus 2", "", "PLUS 2", "Blue"));
        }

        // red
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_0, "red 0", "", "0", "red"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_1, "Red 1", "", "1", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_2, "Red 2", "", "2", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_3, "Red 3", "", "3", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_4, "Red 4", "", "4", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_5, "Red 5", "", "5", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_6, "Red 6", "", "6", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_7, "Red 7", "", "7", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_8, "Red 8", "", "8", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_9, "Red 9", "", "9", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_skip, "Red Skip", "", "SKIP", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_turn, "Red Turn", "", "TURN", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_plus2, "Red Plus 2", "", "PLUS 2", "Red"));
        }

        // green
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_0, "Green 0", "", "0", "Green"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_1, "Green 1", "", "1", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_2, "Green 2", "", "2", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_3, "Green 3", "", "3", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_4, "Green 4", "", "4", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_5, "Green 5", "", "5", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_6, "Green 6", "", "6", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_7, "Green 7", "", "7", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_8, "Green 8", "", "8", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_9, "Green 9", "", "9", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_skip, "Green Skip", "", "SKIP", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_turn, "Green Turn", "", "TURN", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_plus2, "Green Plus 2", "", "PLUS 2", "Green"));
        }

        // yellow
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_0, "Yellow 0", "", "0", "Yellow"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_1, "Yellow 1", "", "1", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_2, "Yellow 2", "", "2", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_3, "Yellow 3", "", "3", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_4, "Yellow 4", "", "4", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_5, "Yellow 5", "", "5", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_6, "Yellow 6", "", "6", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_7, "Yellow 7", "", "7", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_8, "Yellow 8", "", "8", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_9, "Yellow 9", "", "9", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_skip, "Yellow Skip", "", "SKIP", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_turn, "Yellow Turn", "", "TURN", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_plus2, "Yellow Plus 2", "", "PLUS 2", "Yellow"));
        }

        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));

        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));

        // LOGGING ONLY
        Toast.makeText(this.appContext, "CardDeck created. Size=" + this.cards.size(), Toast.LENGTH_LONG).show();
        this.mixDeck();
        Toast.makeText(this.appContext, "CardDeck mixed.", Toast.LENGTH_LONG).show();
    }

    private void mixDeck() {
        // to generate random numbers between 0 and this.cards.size()
        Random rnd = new Random();
        ArrayList<UnoCard> mixed = new ArrayList<UnoCard>();

        // mix it!
        for(int i = this.cards.size()-1; i>0;i--) {
            int index = rnd.nextInt(i+1);
            UnoCard helper = this.cards.get(index);
            this.cards.remove(index);
            mixed.add(helper);
        }

        // delete current arrayList
        this.cards = null;
        // set mixed cards to original carddeck
        this.cards = mixed;

        // paint cards again because order has changed
        for(UnoCard c : this.cards) {
            c.setContainer(this.deckPos);
        }
    }
}
