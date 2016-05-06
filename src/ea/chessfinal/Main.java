package ea.chessfinal;

import ea.chessfinal.controller.GameController;
import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.model.Piece;
import ea.chessfinal.view.GameGUI;
import ea.chessfinal.model.Player;

import javax.swing.*;

/**
 * The main class that will start the threaded
 * game loop
 * @author Elliot Anderson
 */
public class Main extends JPanel {

    public static void main(String[] args) {

        String gameIdOnServer = null;
        String gamePassword = null;

        GameController game = new GameController();

        // assign players

        Player playerWhite = new Player("1", "password");
        Player playerBlack = playerWhite;

        game.setPlayer(Piece.WHITE_COLOR, playerWhite);
        game.setPlayer(Piece.BLACK_COLOR, playerBlack);



        new Thread(game).start();
    }

}
