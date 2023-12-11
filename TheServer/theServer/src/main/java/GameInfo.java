import java.io.Serializable;
import java.util.ArrayList;

public class GameInfo implements Serializable {
    private static final long serialVersionUID = 11111L;  //Allows computer to know GameData is same in both server and client programs
    String ipAddress;
    String code = "";
    String winType;
    int port;
    int bank;
    int anteWager;
    int pairPlus;
    int playWager;
    ArrayList<Card> dealerCards = new ArrayList<>(3);
    ArrayList<Card> playerCards = new ArrayList<>(3);
    ArrayList<String> playerUpdates = new ArrayList<>();

}
