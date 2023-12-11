import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 11111L;
    public String rank;
    public String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

}

