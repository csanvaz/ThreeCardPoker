import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ClientHandler {
    Deck deck = new Deck();
    ArrayList<Card> playerCards;
    ArrayList<Card> dealerCards;

    public ClientHandler() {
        playerCards = deck.dealCards();
        dealerCards = deck.dealCards();
    }

    public void setCards(ArrayList<Card> playerCards, ArrayList<Card> dealerCards) {
        this.playerCards = playerCards;
        this.dealerCards = dealerCards;
    }

    public int analyzeCards(ArrayList<Card> cards) {
        if (isStraightFlush(cards)) {
            return 5;
        } else if (isThreeOfKind(cards)) {
            return 4;
        } else if (isStraight(cards)) {
            return 3;
        } else if (isFlush(cards)) {
            return 2;
        } else if (isPair(cards) != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getWinType(GameInfo game, int playerWinType, int dealerWinType) {
        if (playerWinType > dealerWinType) {
            if (playerWinType == 5) {
                game.bank += ((game.anteWager * 41) + (game.playWager * 41));
                return "STRAIGHTFLUSH";
            } else if (playerWinType == 4) {
                game.bank += ((game.anteWager * 31) + (game.playWager * 31));
                return "THREEOFAKIND";
            } else if (playerWinType == 3) {
                game.bank += ((game.anteWager * 7) + (game.playWager * 7));
                return "STRAIGHT";
            } else if (playerWinType == 2) {
                game.bank += ((game.anteWager * 4) + (game.playWager * 4));
                return "FLUSH";
            } else if (playerWinType == 1) {
                game.bank += ((game.anteWager * 2) + (game.playWager * 2) + (game.pairPlus * 2));
                return "PAIR";
            }
            return "ERROR"; // should not reach this point ever
        } else if (playerWinType < dealerWinType) {
            if (dealerWinType == 5) {
                return "DEALERSTRAIGHTFLUSH";
            } else if (dealerWinType == 4) {
                return "DEALERTHREEOFAKIND";
            } else if (dealerWinType == 3) {
                return "DEALERSTRAIGHT";
            } else if (dealerWinType == 2) {
                return "DEALERFLUSH";
            } else if (dealerWinType == 1) {
                return "DEALERPAIR";
            }
            return "ERROR"; // should not reach this point ever
        } else {
            if (playerWinType == 5) {
                if (deck.rankMap.get(getHighCard(playerCards).rank) < deck.rankMap.get(getHighCard(dealerCards).rank)) {
                    return "DEALERSTRAIGHTFLUSH";
                }
                return "STRAIGHTFLUSH";
            } else if (playerWinType == 4) {
                if (isThreeOfKind(dealerCards)) {
                    if (deck.rankMap.get(playerCards.get(0).rank) < deck.rankMap.get(dealerCards.get(0).rank)) {
                        return "DEALERTHREEOFAKIND";
                    }
                }
                return "THREEOFAKIND";
            } else if (playerWinType == 3) {
                if (isStraight(dealerCards)) {
                    if (deck.rankMap.get(getHighCard(playerCards).rank) < deck.rankMap.get(getHighCard(dealerCards).rank)) {
                        return "DEALERSTRAIGHT";
                    }
                }
                return "STRAIGHT";
            } else if (playerWinType == 2) {
                if (isFlush(dealerCards)) {
                    if (deck.rankMap.get(getHighCard(playerCards).rank) < deck.rankMap.get(getHighCard(dealerCards).rank)) {
                        return "DEALERFLUSH";
                    }
                }
                return "FLUSH";
            } else if (playerWinType == 1) {
                if (isPair(dealerCards) != null) {
                    if (deck.rankMap.get(getHighCard(playerCards).rank) < deck.rankMap.get(getHighCard(dealerCards).rank)) {
                        return "DEALERPAIR";
                    }
                }
                return "PAIR";
            } else if (playerWinType == 0) {
                if (deck.rankMap.get(getHighCard(playerCards).rank) < deck.rankMap.get(getHighCard(dealerCards).rank)) {
                    return "DEALERHIGHCARD";
                }
                return "HIGHCARD";
            } else {
                return "ERROR";
            }
        }
    }

    public Card getHighCard(ArrayList<Card> cards) {
        Card highCard = cards.get(0);
        int highRank = deck.rankMap.get(cards.get(0).rank);
        for (Card c : cards) {
            if (highRank < deck.rankMap.get(c.rank)) {
                highRank = deck.rankMap.get(c.rank);
                highCard = c;
            }
        }
        return highCard;
    }

    public boolean isStraightFlush(ArrayList<Card> cards) {
        if (Objects.equals(cards.get(0).suit, cards.get(1).suit) && Objects.equals(cards.get(1).suit, cards.get(2).suit)) {
            int rank1 = deck.rankMap.get(cards.get(0).rank);
            int rank2 = deck.rankMap.get(cards.get(1).rank);
            int rank3 = deck.rankMap.get(cards.get(2).rank);
            // organize the cards ranks from least to greatest
            if (rank1 > rank2) {
                int temp = rank1;
                rank1 = rank2;
                rank2 = temp;
            }

            if (rank2 > rank3) {
                int temp = rank2;
                rank2 = rank3;
                rank3 = temp;
            }

            if (rank1 > rank2) {
                int temp = rank1;
                rank1 = rank2;
                rank2 = temp;
            }
            if (rank3 == rank2 + 1) {
                if (rank2 == rank1 + 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isThreeOfKind(ArrayList<Card> cards) {
        if (Objects.equals(cards.get(0).rank, cards.get(1).rank) && Objects.equals(cards.get(1).rank, cards.get(2).rank)) {
            return true;
        }
        return false;
    }

    public boolean isStraight(ArrayList<Card> cards) {
        int rank1 = deck.rankMap.get(cards.get(0).rank);
        int rank2 = deck.rankMap.get(cards.get(1).rank);
        int rank3 = deck.rankMap.get(cards.get(2).rank);
        int smallest;
        // organize the cards ranks from least to greatest
        if (rank1 > rank2) {
            int temp = rank1;
            rank1 = rank2;
            rank2 = temp;
        }

        if (rank2 > rank3) {
            int temp = rank2;
            rank2 = rank3;
            rank3 = temp;
        }

        if (rank1 > rank2) {
            int temp = rank1;
            rank1 = rank2;
            rank2 = temp;
        }
        if (rank3 == rank2 + 1) {
            if (rank2 == rank1 + 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isFlush(ArrayList<Card> cards) {
        if (Objects.equals(cards.get(0).suit, cards.get(1).suit) && Objects.equals(cards.get(1).suit, cards.get(2).suit)) {
            return true;
        }
        return false;
    }

    // isPair() will be passed in an arraylist of 3 cards and will evaluate whether it contains a pair
    // if a pair is found, it will return the card that is a pair
    public Card isPair(ArrayList<Card> cards) {
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        Card card3 = cards.get(2);

        if (Objects.equals(card1.rank, card2.rank)) {
            return card1;
        } else if (Objects.equals(card1.rank, card3.rank)) {
            return card1;
        } else if (Objects.equals(card2.rank, card3.rank)) {
            return card2;
        }
        return null;
    }

    public void start() {
    }

    public String getClientSocket() {
        return "";
    }

    public void stop() {
    }

    public String getName() {
        return "";
    }


}
