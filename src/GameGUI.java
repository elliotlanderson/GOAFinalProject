/**
 * Created by elliotanderson on 5/3/16.
 * This is the main Game GUI
 * @author Elliot Anderson <eanderson17@germantownfriends.org>
 * @src: Images for icons taken from http://ixian.com/chess/jin-piece-sets/
 */

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class GameGUI extends JPanel {


    /**
     * @var the starting position of the board
     * in the image
     */
    private static final int BOARD_START_X = 301;
    private static final int BOARD_START_Y = 51;

    /**
     * defines the tile offset for each board piece
     */
    private static final int TILE_OFFSET_X = 50;
    private static final int TILE_OFFSET_Y = 50;

    /**
     * define the current gameState constants
     */
    private final static int GAME_STATE_WHITE = 0;
    private final static int GAME_STATE_BLACK = 1;

    /**
     * @var gameState --> determines the state of the game
     * @todo assign codes for white, black, game over, check, checkmate, etc
     */
    private int gameState = GAME_STATE_WHITE;

    private Image imgBackground;

    // index 0 = bottom, size-1 = top
    private List<Piece> pieces = new ArrayList<Piece>();

    public GameGUI() {
        this.setLayout(null);


        URL backgroundImg = getClass().getResource("/images/board.png");
        this.imgBackground = new ImageIcon(backgroundImg).getImage();

        // create and place white pieces on board

        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_ROOK, BOARD_START_X + TILE_OFFSET_X * 0, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_KNIGHT, BOARD_START_X + TILE_OFFSET_X * 1, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_BISHOP, BOARD_START_X + TILE_OFFSET_X * 2, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_KING, BOARD_START_X + TILE_OFFSET_X * 3, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_QUEEN, BOARD_START_X + TILE_OFFSET_X * 4, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_BISHOP, BOARD_START_X + TILE_OFFSET_X * 5, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_KNIGHT, BOARD_START_X + TILE_OFFSET_X * 6, BOARD_START_Y + TILE_OFFSET_Y * 7);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_ROOK, BOARD_START_X + TILE_OFFSET_X * 7, BOARD_START_Y + TILE_OFFSET_Y * 7);

        // add pawns

        for (int i = 0; i < 8; i++ ) {
            createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_PAWN, BOARD_START_X + TILE_OFFSET_X * i, BOARD_START_Y + TILE_OFFSET_Y * 6);
        }

        // create and place black pieces on board
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_ROOK, BOARD_START_X + TILE_OFFSET_X * 0, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_KNIGHT, BOARD_START_X + TILE_OFFSET_X * 1, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_BISHOP, BOARD_START_X + TILE_OFFSET_X * 2, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_KING, BOARD_START_X + TILE_OFFSET_X * 3, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_QUEEN, BOARD_START_X + TILE_OFFSET_X * 4, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_BISHOP, BOARD_START_X + TILE_OFFSET_X * 5, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_KNIGHT, BOARD_START_X + TILE_OFFSET_X * 6, BOARD_START_Y + TILE_OFFSET_Y * 0);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_ROOK, BOARD_START_X + TILE_OFFSET_X * 7, BOARD_START_Y + TILE_OFFSET_Y * 0);

        // add black pawns
        for (int i = 0; i < 8; i++ ) {
            createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_PAWN, BOARD_START_X + TILE_OFFSET_X * i, BOARD_START_Y + TILE_OFFSET_Y * 1);
        }

        // add mouse listeners to enable drag and drop
        PieceMouseInteractionMediator mediator = new PieceMouseInteractionMediator(this.pieces, this);
        this.addMouseListener(mediator);
        this.addMouseMotionListener(mediator);

        // create application frame
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));

    }

    /**
     *
     * @param color - color constant defined above
     * @param type - type constant defined above
     * @param x - x position of upper left corner
     * @param y - y position of upper left corner
     */
    private void createAndAddPiece(int color, int type, int x, int y) {
        Image img = this.getImageForPiece(color, type);
        Piece piece = new Piece(img, x, y, color, type);
        this.pieces.add(piece);
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
     * Change game state method
     * this method will change the game state
     * As of now, it simply acts as a toggle between black and white
     * @todo make it so that it will have other game states (@see above)
     * @note the reason I use a switch here (as opposted to if-else) is for scalability
     */
    public void changeGameState() {
        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
            default:
                throw new IllegalStateException("Unknown game state");
        }
    }

    /**
     * @return current game state
     */
    public int getGameState() {
        return this.gameState;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(this.imgBackground, 0, 0, null);
        for (Piece piece : this.pieces) {
            g.drawImage(piece.getImage(), piece.getX(), piece.getY(), null);
        }
    }

    /**
     * main method to start the game
     */
    public static void main(String[] args) {
        new GameGUI();
    }
}
