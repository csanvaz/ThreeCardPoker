import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

// Client class used to create threads and manage input and output streams between the server and the client
public class Client extends Thread {
    private Socket socketClient;
    private GameInfo gameData;
    private ObjectOutputStream out; // creates stream to write information
    private ObjectInputStream in; // creates stream to read information

    private Consumer<Serializable> callback;  // consumer is an interface that lets us use accept()

    //Constructor that take object of class consumer
    Client(GameInfo gameData, Consumer<Serializable> call) { //pass through ip address and port number
        this.gameData = gameData;
        callback = call;
    }

    public void run() {

        try {
            socketClient = new Socket(gameData.ipAddress, gameData.port); // socket that takes in local host and port information
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
            GameInfo data = new GameInfo();
            data.code = "connected"; // informing server of connection
            out.writeObject(data);

            while (true) { // infinite listening loop
                try {
                    data = (GameInfo) in.readObject();
                    callback.accept(data);  // once server has made a connection a socket is returned
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void send(GameInfo data) {

        try {
            out.writeObject(data); // outputting data to server
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
