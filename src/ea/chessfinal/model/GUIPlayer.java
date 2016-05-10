package ea.chessfinal.model;

import ea.chessfinal.Main;
import ea.chessfinal.controller.GameController;
import ea.chessfinal.controller.MoveValidator;
import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.listener.PieceMouseInteractionMediator;
import ea.chessfinal.view.PieceView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;



/**
 * Created by elliotanderson on 5/6/16.
 * This is the GUI Player object
 * This player will
 */
public class GUIPlayer extends JPanel implements PlayerInterface {

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
     * @var lastMove the last move the user made (model.Move object)
     */
    private Move lastMove;

    /**
     * @var currentMove
     */
    private Move currentMove;

    /**
     * object of the piece currently being moved
     */
    private PieceView dragPiece;

    private boolean draggingGamePiecesEnabled;

    public GUIPlayer(GameController game) {
        this.setLayout(null);

        URL backgroundImg = getClass().getResource("/ea/chessfinal/view/images/board.png");
        this.imgBackground = new ImageIcon(backgroundImg).getImage();

        this.game = game;

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
            case GameController.GAME_STATE_END_BLACK_WON: stateStr = "black won"; break;
            case GameController.GAME_STATE_END_WHITE_WON: stateStr = "white won"; break;
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

        if ( !isUserDraggingPiece() && this.lastMove != null) {
            int highlightFromX = convertColumnToX(this.lastMove.fromColumn);
            int highlightFromY = convertRowToY(this.lastMove.fromRow);
            int highlightToX = convertColumnToX(this.lastMove.toColumn);
            int highlightToY = convertRowToY(this.lastMove.toRow);

            g.setColor(Color.YELLOW);

            g.drawRoundRect(highlightFromX+4, highlightFromY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT-8, 10, 10);
            g.drawRoundRect(highlightFromX+4, highlightFromY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT-8, 10, 10);


        }

        if (isUserDraggingPiece()) {
            MoveValidator moveValidator = this.game.getMoveValidator();


            for (int column = Board.COLUMN_A; column <= Board.COLUMN_H; column++) {
                for (int row = Board.ROW_1; row <= Board.ROW_8; row++) {

                    int fromRow = this.dragPiece.getPiece().getRow();
                    int fromColumn = this.dragPiece.getPiece().getColumn();

                    // check to see if the target location is valid
                    if (moveValidator.isMoveValid(new Move(fromRow
                    , fromColumn, row, column))) {

                        int highlightX = convertColumnToX(column);
                        int highlightY = convertRowToY(row);

                        g.setColor(Color.BLACK);
                        g.drawRoundRect(highlightX + 5, highlightY + 5, SQUARE_WIDTH-8, SQUARE_HEIGHT-8, 10, 10);

                        g.setColor(Color.GREEN);
                        g.drawRoundRect(highlightX + 4, highlightY + 4, SQUARE_WIDTH - 8, SQUARE_HEIGHT - 8, 10, 10);
                    }
                }
            }
        }



        this.gameStateLabel.setText(this.getGameStateAsText());
    }

    /**
     * checks to see if the user is currently dragging a  pice
     * @return true if they are
     */
    private boolean isUserDraggingPiece() {
        return this.dragPiece != null;
    }

    /**
     * change the location of a given piece if the location is valid
     * if the location is not valid, move the piece back to its original spot
     * @param piece
     * @param x
     * @param y
     */
    public void setNewPieceLocation(PieceView piece, int x, int y) {
        int targetRow = GUIPlayer.convertYToRow(y);
        int targetColumn = GUIPlayer.convertXToColumn(x);

        Move move = new Move(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn(), targetRow, targetColumn);

        if (this.game.getMoveValidator().isMoveValid(move)) {
            this.currentMove = move;
        } else {
            dragPiece.resetToOriginalPosition();
        }
    }

    // implement the PlayerInterface required methods

    @Override
    public Move getMove() {
        this.draggingGamePiecesEnabled = true;

        Move moveToExecute = this.currentMove;
        this.currentMove = null;
        return moveToExecute;
    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        PieceView piece = this.getPieceViewAt(move.toRow, move.toColumn);

        if (piece == null) {
            throw new IllegalStateException("No Piece");
        }

        piece.resetToOriginalPosition();

        // store the last move
        this.lastMove = move;

        // disable dragging until it's their turn again
        this.draggingGamePiecesEnabled = false;



        // repaint the new board
        this.repaint();
    }

    public boolean isDraggingGamePiecesEnabled() {
        return this.draggingGamePiecesEnabled;
    }

    /**
     * gets a non-captured GUI piece at the specified Row (from Board) + Column (from Board)
     * @param row from Board.ROW_
     * @param column from Board.COLUMN_
     * @return the PieceView at the specified location, or null
     */
    private PieceView getPieceViewAt(int row, int column) {
        for (PieceView piece : this.pieces) {
            if (piece.getPiece().getRow() == row && piece.getPiece().getColumn() == column && piece.isCaptured() == false) {
                return piece;
            }
        }

        return null;
    }

    /**
     * sets the drag piece
     * @param pieceView
     */
    public void setDragPiece(PieceView pieceView) {
        this.dragPiece = pieceView;
    }

    /**
     * gets the drag piece
     * @return dragpiece
     */
    public PieceView getDragPiece() {
        return this.dragPiece;
    }
}
