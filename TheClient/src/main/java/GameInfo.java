import java.io.Serializable;
import java.util.ArrayList;

/*
   This class will hold Three Card Poker's game data. This means it will take in the cards that are in the game and analyze winnings
   or losings.
*/

public class GameInfo implements Serializable {
    private static final long serialVersionUID = 11111L;
    String ipAddress;

    int port;
    int anteWager;
    int pairPlus;
    int playWager;
    int bank;

    String code = "";
    String winType ="";
    ArrayList<Card> dealerCards = new ArrayList<>(3);
    ArrayList<Card> playerCards = new ArrayList<>(3);

    ArrayList<String> playerUpdates = new ArrayList<>();


}
