import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Deck implements Serializable {
    private static final long serialVersionUID = 11111L;
    ArrayList<Card> cards;
    HashMap<String, Integer> rankMap;
    public  Deck(){
        cards = new ArrayList<>();
        rankMap = new HashMap<>();
        // array of all the suits
        String[] suits = {"Heart", "Diamond", "Club", "Spade"};
        // array of all the ranks
        String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
        // loop to create a deck that contains every rank in every suit
        for (String suit : suits) {
            for (int i = 1; i < 14; i++) {
                // create a new Card.
                // the Card object will hold a cards rank and suit
                Card card = new Card(ranks[i-1], suit);
                // add the Cards to deck, which is an array of Card objects
                cards.add(card);
                // this hash map is used to map a rank (which is in String form) to an integer value
                // example: "Ace" -> 1, "Two" -> 2, . . . , "Jack" -> 11, "Queen" -> 12, etc.
                // this will be used for rank comparison
                rankMap.put(ranks[i-1], i);
            }
        }
        Collections.shuffle(cards);
    }


    public ArrayList<Card> dealCards(){
        ArrayList<Card> cardsDealt = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            cardsDealt.add(cards.get(0));
            cards.remove(0);
        }
        return cardsDealt;
    }
}
