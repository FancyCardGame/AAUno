package at.fancycardgame.aauno;

import java.util.ArrayList;

/**
 * Created by Philip on 25.05.2015.
 */
public class UnoGame {

    private String[] players;
    private ArrayList<ArrayList<UnoCard>> playerCards;
    private UnoCardDeck cardDeck;
    private ArrayList<UnoCard> playedCards;
    private int nextPlayer;
    private String chosenColor;
    private int cardsToDraw;


    public UnoGame(){
    }

}
