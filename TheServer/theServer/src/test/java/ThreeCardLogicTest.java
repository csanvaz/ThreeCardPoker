import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ThreeCardLogicTest {
    @Test
    // card 1 and card 2 are matching
    public void isPairTest(){
        // create three cards where cards 1 and 2 have a matching rank
        Card card1 = new Card("Ace", "Club");
        Card card2 = new Card("Ace", "Heart");
        Card card3 = new Card("One", "Diamond");
        // dealerCards is only here so that we can create an object of ThreeCardLogic
        ArrayList<Card> dealerCards = new ArrayList<>();
        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);
        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 1 and 2 ranks match
        assertEquals(card1, hand.isPair(playerCards));
    }

    @Test
    // card 1 and card 3 are matching
    public void isPairTest2(){
        // create three cards where cards 1 and 3 have a matching rank
        Card card1 = new Card("Ace", "Club");
        Card card2 = new Card("Two", "Heart");
        Card card3 = new Card("Ace", "Diamond");
        // dealerCards is only here so that we can create an object of ThreeCardLogic
        ArrayList<Card> dealerCards = new ArrayList<>();
        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);
        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 1 and 3 match
        assertEquals(card1, hand.isPair(playerCards));
    }

    @Test
    // card 2 and card 3 are matching
    public void isPairTest3(){
        // create three cards where cards 2 and 3 have a matching rank
        Card card1 = new Card("Ace", "Club");
        Card card2 = new Card("Two", "Heart");
        Card card3 = new Card("Two", "Diamond");
        // dealerCards is only here so that we can create an object of ThreeCardLogic
        ArrayList<Card> dealerCards = new ArrayList<>();
        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);
        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals(card2, hand.isPair(playerCards));
    }

    @Test
    // no cards are matching
    public void isPairTest4(){
        // create three cards where cards 2 and 3 have a matching rank
        Card card1 = new Card("Ace", "Club");
        Card card2 = new Card("Two", "Heart");
        Card card3 = new Card("Three", "Diamond");
        // dealerCards is only here so that we can create an object of ThreeCardLogic
        ArrayList<Card> dealerCards = new ArrayList<>();
        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);
        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals(null, hand.isPair(playerCards));
    }

    @Test
    // this will test for when the player has a flush
    public void isFlushTest(){
        // create a flush for playerCards
        Card card1 = new Card("Three", "Club");
        Card card2 = new Card("Ten", "Club");
        Card card3 = new Card("Ace", "Club");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Ace", "Spade");
        Card card5 = new Card("Seven", "Heart");
        Card card6 = new Card("Three", "Diamond");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("FLUSH", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when the dealer has a flush
    public void isFlushTest2(){
        // create a flush for playerCards
        Card card1 = new Card("Three", "Club");
        Card card2 = new Card("Ten", "Spade");
        Card card3 = new Card("Ace", "Heart");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Ace", "Spade");
        Card card5 = new Card("Seven", "Spade");
        Card card6 = new Card("Three", "Spade");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("DEALERFLUSH", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when both the player and dealer have a flush but player has high
    public void isFlushTest3() {
        // create a flush for playerCards
        Card card1 = new Card("Three", "Club");
        Card card2 = new Card("Ten", "Club");
        Card card3 = new Card("Ace", "Club");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create a flush for dealerCards
        Card card4 = new Card("King", "Spade");
        Card card5 = new Card("Seven", "Spade");
        Card card6 = new Card("Three", "Spade");

        // dealerCards is only here so that we can create an object of ThreeCardLogic
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("FLUSH", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when both the player and dealer have a flush but dealer has high
    public void isFlushTest4() {
        // create a flush for playerCards
        Card card1 = new Card("Three", "Club");
        Card card2 = new Card("Ten", "Club");
        Card card3 = new Card("King", "Club");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create a flush for dealerCards
        Card card4 = new Card("Ace", "Spade");
        Card card5 = new Card("Seven", "Spade");
        Card card6 = new Card("Three", "Spade");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("DEALERFLUSH", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when the player has a straight
    public void isStraightTest() {
        // create a straight for playerCards
        Card card1 = new Card("Eight", "Club");
        Card card2 = new Card("Seven", "Spade");
        Card card3 = new Card("Six", "Diamond");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Ace", "Spade");
        Card card5 = new Card("Seven", "Diamond");
        Card card6 = new Card("Three", "Club");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("STRAIGHT", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when the dealer has a straight
    public void isStraightTest2() {
        // create a straight for playerCards
        Card card1 = new Card("Eight", "Club");
        Card card2 = new Card("Queen", "Spade");
        Card card3 = new Card("Jack", "Diamond");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Ace", "Spade");
        Card card5 = new Card("King", "Diamond");
        Card card6 = new Card("Queen", "Club");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("DEALERSTRAIGHT", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when both the player and dealer has a straight but player has high card
    public void isStraightTest3() {
        // create a straight for playerCards
        Card card1 = new Card("Four", "Club");
        Card card2 = new Card("Six", "Spade");
        Card card3 = new Card("Five", "Diamond");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Three", "Club");
        Card card5 = new Card("Four", "Diamond");
        Card card6 = new Card("Five", "Heart");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("STRAIGHT", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when both the player and dealer has a straight but dealer has high card
    public void isStraightTest4() {
        // create a straight for playerCards
        Card card1 = new Card("Four", "Club");
        Card card2 = new Card("Five", "Spade");
        Card card3 = new Card("Six", "Diamond");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Seven", "Club");
        Card card5 = new Card("Six", "Diamond");
        Card card6 = new Card("Five", "Heart");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("DEALERSTRAIGHT", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when the player has a three of a kind
    public void isThreeOfAKindTest() {
        // create a three of a kind for playerCards
        Card card1 = new Card("Four", "Club");
        Card card2 = new Card("Four", "Spade");
        Card card3 = new Card("Four", "Diamond");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create random cards for dealerCards
        Card card4 = new Card("Jack", "Club");
        Card card5 = new Card("Nine", "Diamond");
        Card card6 = new Card("Six", "Heart");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("THREEOFAKIND", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }

    @Test
    // this will test for when the dealer has a three of a kind
    public void isThreeOfAKindTest2() {
        Card card1 = new Card("Four", "Club");
        Card card2 = new Card("Eight", "Spade");
        Card card3 = new Card("Four", "Diamond");

        // add cards to playerCards
        ArrayList<Card> playerCards = new ArrayList<>();
        playerCards.add(card1);
        playerCards.add(card2);
        playerCards.add(card3);

        // create three of a kind  for dealerCards
        Card card4 = new Card("Jack", "Club");
        Card card5 = new Card("Jack", "Diamond");
        Card card6 = new Card("Jack", "Heart");

        // add cards to dealerCards
        ArrayList<Card> dealerCards = new ArrayList<>();
        dealerCards.add(card4);
        dealerCards.add(card5);
        dealerCards.add(card6);

        GameInfo game = new GameInfo();

        // hand is an object that contains the methods that analyze our cards to check if we have a winning hand
        ClientHandler hand = new ClientHandler();
        hand.setCards(playerCards, dealerCards);
        // should return 1 if cards 2 and 3 match
        assertEquals("DEALERTHREEOFAKIND", hand.getWinType(game, hand.analyzeCards(playerCards), hand.analyzeCards(dealerCards)));
    }
}