package ea.chessfinal;

import ea.chessfinal.controller.GameController;
import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.model.ComputerPlayer;
import ea.chessfinal.model.GUIPlayer;
import ea.chessfinal.model.Piece;

import ea.chessfinal.view.MainMenu;

import javax.swing.*;

/**
 * The main class that will start the threaded
 * game loop
 * @author Elliot Anderson
 */
public class Main {

    public final static String baseDirectory = System.getProperty("user.dir");

    public static void main(String[] args) {

        MainMenu mainMenu = new MainMenu(500,500, new Main());
        mainMenu.showJPanel();

    }

    public void startGame() {
        GameController game = new GameController();

        // assign players

        PlayerInterface playerWhite = new GUIPlayer(game);
        PlayerInterface playerBlack = new ComputerPlayer(game);

        game.setPlayer(Piece.WHITE_COLOR, playerWhite);
        game.setPlayer(Piece.BLACK_COLOR, playerBlack);


        new Thread(game).start();
    }

}
