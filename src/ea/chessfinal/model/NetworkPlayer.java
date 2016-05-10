package ea.chessfinal.model;

import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.services.MoveTranslator;
import ea.chessfinal.services.NetworkService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by elliotanderson on 5/5/16.
 * This is the actual player object.
 * It will connect with the network bridge to create the
 * interaction of two people playing online
 * @todo implement a string formatter
 * @todo SOCKETS!!!!!
 * @todo Create a network bridge/service to abstractify some of the redundant code and slim this model down
 * @author Elliot Anderson
 */

public class NetworkPlayer implements PlayerInterface {

    /**
     * @var gameID is the unique gameID per server
     */
    private String gameID;

    /**
     * @var password -- add some password for a basic security aspect
     */
    private String gamePassword = null;

    /**
     * @var the string version of the last move received from the network
     */
    private String lastMoveStrReceivedFromNetwork = "###";

    /**
     * @var the last move that this player sent to the network
     */
    private String lastMoveStrSentToNetwork = "###";

    /**
     * @var instance of the connection to the server
     */
    private HttpURLConnection server;

    /**
     * @var instance of the network service, which will help us make calls to the network
     * without having this class bloated with repeated methods that make no sense to the reader
     * YAY Object Oriented Programming & Design Patterns!!!
     */
    private NetworkService networkService;

    /**
     * Player initializer.  Either creates a game on the network or joins an existing one
     * @param gameID the unique game id on the server
     * @param gamePassword the password for that unique game id
     */
    public NetworkPlayer(String gameID, String gamePassword) {

        // create the instance of the network service
        this.networkService = new NetworkService();

        // see if we need to create a new game or join an existing one

        if (gameID == null) {
            // create a new game
            System.out.println("Creating a new game");

            this.gameID = createGame(gamePassword);
            this.gamePassword = gamePassword;
        } else {
            // they are joining an existing game
            System.out.println("Joining an existing game...");

            if (isGameValid(gameID, gamePassword)) {
                this.gameID = gameID;
                this.gamePassword = gamePassword;
            }

        }
    }

    /**
     * This method will create a new instance of a game
     * on the server.
     * @param gamePassword the password for the game
     * @return String of id for the new game
     */
    private String createGame(String gamePassword) {
        System.out.println("Sending request to server...");

        this.networkService.addParam("password", gamePassword);

        this.networkService.postRequest("createGame");

        return this.networkService.getResult();
    }

    /**
     * This method will check to see if the game requested is a valid ID/password combo
     * @param gameID the id of the game on the server
     * @param gamePassword the password for the game of specified ID
     * @return true if the game exists on the server
     */
    private boolean isGameValid(String gameID, String gamePassword)  {
        System.out.println("Checking if game is valid...");

        this.networkService.addParam("gameID", gameID);
        this.networkService.addParam("gamePassword", gamePassword);
        this.networkService.postRequest("isGameValid");

        return Boolean.parseBoolean(this.networkService.getResult());
    }

    /**
     * Retrieves the last move from the server
     *
     * @param gameID the username for the online game
     * @return the last move as a coded string
     */
    private String getLastMove(String gameID)  {
        System.out.println("Getting the last move from the server...");

        this.networkService.addParam("gameID", gameID);
        this.networkService.postRequest("getLastMove");

        String result = this.networkService.getResult();

        System.out.println(result);

        if (result != null && result.trim().length() == 0) {
            result = null; // return null instead of an empty string so the program doesn't get confused
        }


        return result;
    }

    /**
     * this method will send a valid move to the server as a string
     *
     * @param gameID - the server's game id
     * @param gamePassword - the password for the game
     * @param move - the move object formatted as a string (see README.md) for more details
     * @return an id that the server returned to refer to that move
     */
    private String sendMove(String gameID, String gamePassword, String move)  {
        System.out.println("Sending Move to server...");

        // not the cleanest but just to show that you can daisy chain with this class
        String result = this.networkService
                .addParam("gameID", gameID)
                .addParam("gamePassword", gamePassword)
                .addParam("move", move)
                .postRequest("sendMove").getResult();

        return result;
    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        System.out.println("Move successfully executed");

        String moveStr = MoveTranslator.convertMoveToString(move);

        if (!moveStr.equals(this.lastMoveStrReceivedFromNetwork)) {
            // send move to the server
            try {
                this.sendMove(this.gameID, this.gamePassword, moveStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.lastMoveStrSentToNetwork = moveStr;
        } else {
            // the executed move is the one we got from the network, so
            // we don't need to send it again/TRUST ME
        }
    }

    @Override
    public Move getMove() {
        Move receivedMove = null;
        String lastMoveFromServerStr = null;

        // keep a continuous loop until we get a new move

        /*
            instead of having this method be called by the game logic
            every 0.1 seconds (which would increase traffic and my server
            would probably go nuts), I am going to have this method hang for 3 seconds
            while waiting for a move.  You'd be surprised at how efficient this is at keeping
            the game relatively fast paced

            OF cosurse, I REALLY want to do sockets instead of polling, so I added a to-do at the top of this class
         */

        while (receivedMove == null) {
            // see if there are any new moves

            try {
                lastMoveFromServerStr = this.getLastMove(this.gameID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if nothing is returned, return null
            if (lastMoveFromServerStr == null || lastMoveFromServerStr.trim().length() == 0) {
                System.out.println("no moves");
            }

            // if we receive the move we just sent, ignore it
            else if (lastMoveStrSentToNetwork != null && lastMoveStrSentToNetwork.equals(lastMoveFromServerStr)) {
                // this is the move we just sent
            } else {
                receivedMove = MoveTranslator.convertStringToMove(lastMoveFromServerStr);
            }

            try { Thread.sleep(3000);} catch (InterruptedException e) { e.printStackTrace(); };

        }
        // set the last received move
        this.lastMoveStrReceivedFromNetwork = lastMoveFromServerStr;
        return receivedMove;
    }

    /**
     * simple getter method for the game id
     * @return game id stored on the server
     */
    public String getGameID() {
        return this.gameID;
    }
}
