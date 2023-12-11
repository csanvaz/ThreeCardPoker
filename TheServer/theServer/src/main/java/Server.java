import com.sun.glass.ui.Menu;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Server {
    ServerSocket serverSocket;
    private final GameInfo gameData;
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<>();
    TheServer server;
    private Consumer<Serializable> callback;


    Server(GameInfo gameData, Consumer<Serializable> call) {
        this.gameData = gameData;
        callback = call;
        server = new TheServer();
        server.start();
    }


public class TheServer extends Thread {

    public void run() {

        try (ServerSocket mysocket = new ServerSocket(gameData.port);) {
            while (true) {
                ClientThread c = new ClientThread(mysocket.accept(), count);
                callback.accept(gameData.code = "connected");
                clients.add(c);
                c.start();
                count++;
            }
        }//end of try
        catch (Exception e) {
            callback.accept(gameData.code = "Server socket did not launch");
            System.out.println(e); // print the exception
        }
    }
}


class ClientThread extends Thread {

        GameInfo game;
    Socket connection;
    int count;
    ObjectInputStream in;
    ObjectOutputStream out;

    ClientThread(Socket s, int count) {
        this.connection = s;
        this.count = count;
    }


    public void run() {
        try {  // opening input and output streams and waiting for client to connect and send socket
            in = new ObjectInputStream(connection.getInputStream());
            out = new ObjectOutputStream(connection.getOutputStream());
            connection.setTcpNoDelay(true);


            Deck deck;

            while (true) {
                try { //where we put protocol
                    game = (GameInfo) in.readObject(); // reading in GameInfo object client sent
                    if (game.code.equals("connected")){
                        game.code = "connected";
                        out.writeObject(game);
                    }
                    else if (game.code.equals("DEAL")){
                        deck = new Deck(); // create the deck for this game
                        game.playerCards = deck.dealCards(); // generate 3 random cards for the player

                        game.bank -= game.anteWager;
                        if(game.pairPlus != 0) {
                            game.bank -= game.pairPlus;
                        }
                        // send back to client
                        out.writeObject(game);
                    }
                    else if (game.code.equals("PLAY")){
                        // update the players bank by subtracting the value of their play wager
                        game.bank -= game.playWager;

                        // creating the dealers cards
                        deck = new Deck(); // create the deck for this game
                        game.dealerCards = deck.dealCards(); // generate 3 random cards for the dealer
                        game.code = "PLAY"; // reset code

                        ClientHandler handAnalysis = new ClientHandler();
                        game.winType = handAnalysis.getWinType(game, handAnalysis.analyzeCards(game.playerCards), handAnalysis.analyzeCards(game.dealerCards));
                        System.out.println(game.winType);
                        if (game.winType.equals("STRAIGHTFLUSH") || game.winType.equals("THREEOFAKIND") || game.winType.equals("STRAIGHT") ||
                                game.winType.equals("FLUSH") || game.winType.equals("PAIR")) {
                            game.playerUpdates.add("Player " + count + " has a " + game.winType);
                        }
                        else{
                            game.playerUpdates.add("Dealer" + " has a " + game.winType);
                        }
                        out.writeObject(game);
                    }else if(game.code.equalsIgnoreCase("PLAY_AGAIN")){
                        game.code = "PLAY_AGAIN";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    GameInfo clientException = new GameInfo();
                    callback.accept(clientException.code = "Client " + count + " disconnected");
                    clients.remove(this); // something went wrong with the connection so delete client from container
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Streams not open");
        }
    }


}//end of client thread
    public void stopServer() throws IOException {
        serverSocket.close();
    }
}
