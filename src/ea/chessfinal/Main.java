package ea.chessfinal;

import ea.chessfinal.controller.GameController;
import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.model.Piece;
import ea.chessfinal.view.GameGUI;
import ea.chessfinal.model.Player;

/**
 * The main class that will start the threaded
 * game loop
 * @author Elliot Anderson
 */
public class Main {

    public static void main(String[] args) {

        String gameIdOnServer = null;
        String gamePassword = null;

        GameController game = new GameController();

        // assign players

        Player playerWhite = new Player("testid", "password");
        Player playerBlack = playerWhite;

        game.setPlayer(Piece.WHITE_COLOR, playerWhite);
        game.setPlayer(Piece.BLACK_COLOR, playerBlack);


    }

}
