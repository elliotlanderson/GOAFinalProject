package ea.chessfinal.view;

/**
 * Created by elliotanderson on 5/3/16.
 * This is the main Game View (GUI)
 * @author Elliot Anderson <eanderson17@germantownfriends.org>
 * @src: Images for icons taken from http://ixian.com/chess/jin-piece-sets/
 */

import ea.chessfinal.controller.GameController;
import ea.chessfinal.model.Board;
import ea.chessfinal.model.Piece;
import ea.chessfinal.listener.PieceMouseInteractionMediator;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameGUI extends JPanel {


    /**
     * @var the starting position of the board
     * in the image
     */
    private static final int BOARD_START_X = 301;
    private static final int BOARD_START_Y = 51;

    /**
     * defines the square width & height
     */
    private static final int SQUARE_WIDTH = 50;
    private static final int SQUARE_HEIGHT = 50;

    /**
     * defines the piece width & height
     */
    private static final int PIECE_WIDTH = 48;
    private static final int PIECE_HEIGHT = 48;

    private static final int PIECES_START_X = BOARD_START_X + (int)(SQUARE_WIDTH/2.0 - PIECE_WIDTH/2.0);
    private static final int PIECES_START_Y = BOARD_START_Y + (int)(SQUARE_HEIGHT/2.0 - PIECE_HEIGHT/2.0);


    private static final int SQUARE_START_X = BOARD_START_X - (int)(PIECE_WIDTH/2.0);
    private static final int SQUARE_START_Y = BOARD_START_Y - (int)(PIECE_HEIGHT/2.0);


    /**
     * define the current gameState constants
     */
    private final static int GAME_STATE_WHITE = 0;
    private final static int GAME_STATE_BLACK = 1;


    /**
     * @var imgBackground --> background image
     */
    private Image imgBackground;

    /**
     * @var game instance of controller.GameController (business logic)
     */
    private GameController game;

    /**
     * @var list of the GUI PieceViews that we will connect to the game's
     * business logic pieces
     */
    private List<PieceView> pieces = new ArrayList<PieceView>();

    /**
     * @var JLabel gameStateLabel shows the current game state
     */
    private JLabel gameStateLabel;

    /**
     * initializer
     * set up the GUI for the game
     * instantiate the mediator to bridge the GUI and the Controller
     */
    public GameGUI() {
        this.setLayout(null);


        URL backgroundImg = getClass().getResource("/ea/chessfinal/view/images/board.png");
        this.imgBackground = new ImageIcon(backgroundImg).getImage();

        this.game = new GameController();

        // wrap game pieces into their GUI representations
        for (Piece piece : this.game.getPieces()) {
            createAndAddViewPiece(piece);
        }


        // add mouse listeners to enable drag and drop
        PieceMouseInteractionMediator mediator = new PieceMouseInteractionMediator(this.pieces, this);
        this.addMouseListener(mediator);
        this.addMouseMotionListener(mediator);

        // create a label that dispalys whose turn it is (game state)
        String gameStateLabelText = this.getGameStateAsText();
        this.gameStateLabel = new JLabel(gameStateLabelText);
        gameStateLabel.setBounds(0, 30, 80, 80);
        this.add(gameStateLabel);

        // create application frame
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));

    }

    private String getGameStateAsText() {
        String stateStr = "invalid";

        switch (this.game.getGameState()) {
            case GameController.GAME_STATE_BLACK: stateStr = "black"; break;
            case GameController.GAME_STATE_END: stateStr = "end"; break;
            case GameController.GAME_STATE_WHITE: stateStr = "white"; break;
        }

        return stateStr;
    }

    /**
     * Creates a game piece that associates with a piece model
     * @param piece
     */
    private void createAndAddViewPiece(Piece piece) {
        Image img = this.getImageForPiece(piece.getColor(), piece.getType());
        PieceView pieceView = new PieceView(img, piece);
        this.pieces.add(pieceView);
    }


    private Image getImageForPiece(int color, int type) {
        String filename = "";

        filename += (color == Piece.WHITE_COLOR ? "w" : "b");

        switch (type) {
            case Piece.PIECE_BISHOP:
                filename += "b";
                break;
            case Piece.PIECE_KING:
                filename += "k";
                break;
            case Piece.PIECE_KNIGHT:
                filename += "n";
                break;
            case Piece.PIECE_PAWN:
                filename += "p";
                break;
            case Piece.PIECE_QUEEN:
                filename += "q";
                break;
            case Piece.PIECE_ROOK:
                filename += "r";
                break;
        }

        filename += ".png";

        URL urlPieceImg = this.getClass().getResource("images/" + filename);
        return new ImageIcon(urlPieceImg).getImage();
    }



    /**
     * convert column (ea.chessfinal.controller) into x coordinate (ea.chessfinal.view)
     * @param column
     * @return x coordinate for column
     */
    public static int convertColumnToX(int column) {
        return PIECES_START_X + SQUARE_WIDTH * column;
    }

    /**
     * convert row (ea.chessfinal.controller) into y coordinate (ea.chessfinal.view)
     * @param row
     * @return y coordinate for row
     */
    public static int convertRowToY(int row) {
        return PIECES_START_Y + SQUARE_HEIGHT * (Board.ROW_8 - row);
    }

    /**
     * convert x coordinate into column
     * @param x
     * @return column for x coordinate
     */
    public static int convertXToColumn(int x) {
        return (x - SQUARE_START_X)/SQUARE_WIDTH;
    }

    /**
     * convert y coordinate into row
     * @param y
     * @return row for y coordinate
     */
    public static int convertYToRow(int y) {
        return Board.ROW_8 - (y - SQUARE_START_Y)/SQUARE_HEIGHT;
    }

    /**
     * get game state
     * @return current game state
     */
    public int getGameState() {
        return this.game.getGameState();
    }

    /**
     * change game state
     */
    public void changeGameState() {
        this.game.changeGameState();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(this.imgBackground, 0, 0, null);
        for (PieceView piece : this.pieces) {
            if (!piece.isCaptured()) {
                g.drawImage(piece.getImage(), piece.getX(), piece.getY(), null);
            }
        }

        this.gameStateLabel.setText(this.getGameStateAsText());
    }

    /**
     * change the location of a given piece if the location is valid
     * if the location is not valid, move the piece back to its original spot
     * @param piece
     * @param x
     * @param y
     */
    public void setNewPieceLocation(PieceView piece, int x, int y) {
        int targetRow = GameGUI.convertYToRow(y);
        int targetColumn = GameGUI.convertXToColumn(x);

        if (targetRow < Board.ROW_1 || targetRow > Board.ROW_8 || targetColumn < Board.COLUMN_A || targetRow > Board.COLUMN_H) {
            // reset piece position because the move isn't valid
            piece.resetToOriginalPosition();

        } else {
            // update the model and GUI
            this.game.movePiece(piece.getPiece().getRow(), piece.getPiece().getColumn(), targetRow, targetColumn);
            piece.resetToOriginalPosition();
        }
    }


    /**
     * main method to start the game
     */
    public static void main(String[] args) {
        new GameGUI();
    }
}
