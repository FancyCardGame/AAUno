package at.fancycardgame.aauno;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

import at.fancycardgame.aauno.listeners.ShakeEventListener;
import at.fancycardgame.aauno.toolbox.Tools;

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

        // create drawables for  special cards & backside
        Drawable color_change = this.appContext.getResources().getDrawable(R.drawable.color_change);
        Drawable color_change_plus4 = this.appContext.getResources().getDrawable(R.drawable.color_change_plus4);
        Drawable card_back = this.appContext.getResources().getDrawable(R.drawable.card_back);


        // blue
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_0, card_back, "Blue 0", "", "0", "Blue"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_1, card_back, "Blue 1", "", "1", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_2, card_back, "Blue 2", "", "2", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_3, card_back, "Blue 3", "", "3", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_4, card_back, "Blue 4", "", "4", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_5, card_back, "Blue 5", "", "5", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_6, card_back, "Blue 6", "", "6", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_7, card_back, "Blue 7", "", "7", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_8, card_back, "Blue 8", "", "8", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_9, card_back, "Blue 9", "", "9", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_skip, card_back, "Blue Skip", "", "SKIP", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_turn, card_back, "Blue Turn", "", "TURN", "Blue"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), blue_plus2, card_back, "Blue Plus 2", "", "PLUS 2", "Blue"));
        }

        // red
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_0, card_back, "red 0", "", "0", "Red"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_1, card_back, "Red 1", "", "1", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_2, card_back, "Red 2", "", "2", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_3, card_back, "Red 3", "", "3", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_4, card_back, "Red 4", "", "4", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_5, card_back, "Red 5", "", "5", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_6, card_back, "Red 6", "", "6", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_7, card_back, "Red 7", "", "7", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_8, card_back, "Red 8", "", "8", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_9, card_back, "Red 9", "", "9", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_skip, card_back, "Red Skip", "", "SKIP", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_turn, card_back, "Red Turn", "", "TURN", "Red"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), red_plus2, card_back, "Red Plus 2", "", "PLUS 2", "Red"));
        }

        // green
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_0, card_back, "Green 0", "", "0", "Green"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_1, card_back, "Green 1", "", "1", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_2, card_back, "Green 2", "", "2", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_3, card_back, "Green 3", "", "3", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_4, card_back, "Green 4", "", "4", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_5, card_back, "Green 5", "", "5", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_6, card_back, "Green 6", "", "6", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_7, card_back, "Green 7", "", "7", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_8, card_back, "Green 8", "", "8", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_9, card_back, "Green 9", "", "9", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_skip, card_back, "Green Skip", "", "SKIP", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_turn, card_back, "Green Turn", "", "TURN", "Green"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), green_plus2, card_back, "Green Plus 2", "", "PLUS 2", "Green"));
        }

        // yellow
        // 0 only once
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_0, card_back, "Yellow 0", "", "0", "Yellow"));
        for (int twice = 0; twice < 2; twice++) {
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_1, card_back, "Yellow 1", "", "1", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_2, card_back, "Yellow 2", "", "2", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_3, card_back, "Yellow 3", "", "3", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_4, card_back, "Yellow 4", "", "4", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_5, card_back, "Yellow 5", "", "5", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_6, card_back, "Yellow 6", "", "6", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_7, card_back, "Yellow 7", "", "7", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_8, card_back, "Yellow 8", "", "8", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_9, card_back, "Yellow 9", "", "9", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_skip, card_back, "Yellow Skip", "", "SKIP", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_turn, card_back, "Yellow Turn", "", "TURN", "Yellow"));
            this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), yellow_plus2, card_back, "Yellow Plus 2", "", "PLUS 2", "Yellow"));
        }

        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, card_back, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, card_back, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, card_back, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change, card_back, "Color Change", "", "COLOR CHANGE", "COLOR CHANGE"));

        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, card_back, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, card_back, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, card_back, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));
        this.cards.add(new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, card_back, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4"));

        // LOGGING ONLY
        Toast.makeText(this.appContext, "CardDeck created. Size=" + this.cards.size(), Toast.LENGTH_LONG).show();
        this.mixDeck();
        // mix it !
        Tools.showToast("CardDeck mixed.", Toast.LENGTH_SHORT);
    }

    private void mixDeck() {
       // Toast.makeText(Tools.game.appContext, "CardDeck mixed.", Toast.LENGTH_LONG).show();

        // to generate random numbers between 0 and this.cards.size()
        Random rnd = new Random();
        ArrayList<UnoCard> mixed = new ArrayList<UnoCard>();

        // mix it!
        for(int i = cards.size()-1; i>0;i--) {
            int index = rnd.nextInt(i+1);
            UnoCard helper = cards.get(index);
            cards.remove(index);
            mixed.add(helper);
        }

        // delete current arrayList
        cards = null;
        // set mixed cards to original carddeck
        cards = mixed;

        // paint cards again because order has changed
        for(UnoCard c : cards) {
            c.setContainer(deckPos);
        }

    }

    public ArrayList<UnoCard> getCards() {
        return cards;
    }

    // Return a random card from the card deck
    public UnoCard getCard(){
        Random rnd = new Random();
        int rnd_number = rnd.nextInt(((this.cards.size()-1)-0) + 1) + 0;
        return this.cards.get(rnd_number);

    }

    public String getCardByView(View cardView){
        String card = "";
        for (UnoCard c : this.cards){
            if (c.getImageView() == cardView){
                card = c.getValue();
            }
        }
        return card;
    }

    public UnoCard getCardByName(String cardName){
        Drawable color_change_plus4 = this.appContext.getResources().getDrawable(R.drawable.color_change_plus4);
        Drawable card_back = this.appContext.getResources().getDrawable(R.drawable.card_back);
        UnoCard card = new UnoCard(this.appContext, deckPos, new Point(20, 20), color_change_plus4, card_back, "Color Change Plus 4", "", "COLOR CHANGE PLUS 4", "COLOR CHANGE PLUS 4");

        /*for(int i=0;i<this.cards.size();i++){
            if (this.cards.get(i).getName().equals(cardName)){
                card = this.cards.get(i);
                break;
            }
        }*/

        for (UnoCard c : this.cards){
            Log.d("c.getName()", "" + c.getName());
            Log.d("cardName", "" + cardName);
            if (c.getName().equals(cardName)){

                card = c;
                break;

            }
        }

        return card;
    }

    public void removeCard(UnoCard cardToRemove){
        // remove a given card from the the card deck
        // e.g. because it has been drawn by a player
        // TODO: re-enable removal after testing
        for (UnoCard c : this.cards){
            if (c.getName() == cardToRemove.getName()){
                Log.d("Removed Card:", c.getName());
                //this.cards.remove(c);
                break;
            }
        }
    }

    public int getSize(){
        return this.cards.size();
    }
}
