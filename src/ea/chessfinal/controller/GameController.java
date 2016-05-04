package ea.chessfinal.controller;

/**
 * Created by elliotanderson on 5/3/16.
 * The ea.chessfinal.controller for the chess game
 * This class is the GAME/BUSINESS logic for the chess game
 * This should be completely separated from the view, or at least as
 * abstracted as possible and within reason to the scope of this project.
 * @todo clean up the code (as per usual)
 * @author Elliot Anderson
 */

import ea.chessfinal.model.Board;
import ea.chessfinal.model.Piece;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GameController {

    /**
     * define the gameState constants
     */
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END = 2;

    private int gameState = GAME_STATE_WHITE; // default white starts

    // index 0 = bottom, size-1 = top
    private List<Piece> pieces = new ArrayList<Piece>();

    /**
     * @var instance of the move validator
     */
    private MoveValidator moveValidator;


    /**
     * initialize the game and set class instance variables
     */
    public GameController() {

        // instantiate the move validator
        this.moveValidator = new MoveValidator(this);

        // create and place white pieces on board

        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_ROOK, Board.ROW_1, Board.COLUMN_A);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_KNIGHT, Board.ROW_1, Board.COLUMN_B);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_BISHOP, Board.ROW_1, Board.COLUMN_C);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_KING, Board.ROW_1, Board.COLUMN_D);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_QUEEN, Board.ROW_1, Board.COLUMN_E);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_BISHOP, Board.ROW_1, Board.COLUMN_F);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_KNIGHT, Board.ROW_1, Board.COLUMN_G);
        createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_ROOK, Board.ROW_1, Board.COLUMN_H);

        // add white pawns
        int currentColumn = Board.COLUMN_A;
        for (int i = 0; i < 8; i++ ) {
            createAndAddPiece(Piece.WHITE_COLOR, Piece.PIECE_PAWN, Board.ROW_2, currentColumn);
            currentColumn++;
        }

        // create and place black pieces on board
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_ROOK, Board.ROW_8, Board.COLUMN_A);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_KNIGHT, Board.ROW_8, Board.COLUMN_B);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_BISHOP, Board.ROW_8, Board.COLUMN_C);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_KING, Board.ROW_8, Board.COLUMN_D);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_QUEEN, Board.ROW_8, Board.COLUMN_E);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_BISHOP, Board.ROW_8, Board.COLUMN_F);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_KNIGHT, Board.ROW_8, Board.COLUMN_G);
        createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_ROOK, Board.ROW_8 , Board.COLUMN_H);

        // add black pawns
        currentColumn = Board.COLUMN_A;
        for (int i = 0; i < 8; i++ ) {
            createAndAddPiece(Piece.BLACK_COLOR, Piece.PIECE_PAWN, Board.ROW_7, currentColumn);
            currentColumn++;
        }

    }

    /**
     *
     * @param color - color constant defined above
     * @param type - type constant defined above
     * @param row - ea.chessfinal.model.Board.ROW constant
     * @param column - ea.chessfinal.model.Board.COLUMN constant
     */
    private void createAndAddPiece(int color, int type, int row, int column) {
        Piece piece = new Piece(color, type, row, column);
        this.pieces.add(piece);
    }

    /**
     * Move the piece to the specified location.  If the target already has a piece in it
     * then that piece is marked as "captured".  If the move cannot be successfully done because
     * of constraints or piece rules, return false
     *
     * @param fromRow the row the piece is being moved from
     * @param fromCol the col the piece is being moved from
     * @param toRow the row the piece is being moved to
     * @param toCol the column the piece is being moved to
     * @return true if the piece moved successfully, false if it wasn't
     */
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {

        if (! this.moveValidator.isMoveValid(fromRow, fromCol, toRow, toCol)) {
            // move is invalid
            System.out.println("move invalid");
            return false;
        }

        Piece piece = getNonCapturedPieceAtLocation(fromRow, fromCol);

        // check to see if the move is capturing an opponent's piece
        int opponentColor = (piece.getColor() == Piece.BLACK_COLOR ? Piece.BLACK_COLOR : Piece.WHITE_COLOR);

        if (isNonCapturedPieceAtLocation(opponentColor, toRow, toCol)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(toRow, toCol);
            opponentPiece.isCaptured = true;
        }

        piece.setRow(toRow);
        piece.setColumn(toCol);

        if (isGameEndConditionReached()) {
            this.gameState = GAME_STATE_END;
        } else {
            this.changeGameState();
        }

        return true;
    }

    /**
     * Check if the condition for the game ending has been met: one king has been captured
     * YES, i understand this is an oversimplification
     * @todo Add functionality for a "checkmate" so the user doesn't actually have to capture the king
     * @return true if the conditions show the game has ended (king captured)
     */
    private boolean isGameEndConditionReached() {
        for (Piece piece : this.pieces) {
            if (piece.getType() == Piece.PIECE_KING && piece.isCaptured()) {
                return true;
            }
        }

        return false;
    }

    /**
     * checks if there is a piece at the specified location that is not
     * set to "captured" and has the specified color
     * @param color of piece
     * @param row the row to check for occupation
     * @param col the col to check for occupation
     * @return boolean as to whether the space is occupied with specified color
     */
    public boolean isNonCapturedPieceAtLocation(int color, int row, int col) {
        for (Piece piece: this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == col && !piece.isCaptured  && piece.getColor() == color ) {
                return true;
            }
        }

        return false;
    }

    /**
     * same method as above without color param
     * @param row
     * @param col
     * @return
     */
    public boolean isNonCapturedPieceAtLocation(int row, int col) {
        for (Piece piece: this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == col && !piece.isCaptured  ) {
                return true;
            }
        }

        return false;
    }

    /**
     * returns the piece at the specified location that is not
     * set to "captured"
     * @param row of Board
     * @param col of Board
     * @return the  non captured piece at the specified location
     */
    public Piece getNonCapturedPieceAtLocation(int row, int col) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == col && !piece.isCaptured) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Change game state method
     * this method will change the game state
     * As of now, it simply acts as a toggle between black and white
     * @todo make it so that it will have other game states (@see above)
     * @note the reason I use a switch here (as opposted to if-else) is for scalability
     */
    public void changeGameState() {

        // check to see if the game has ended (based on the game end condition)
        if (this.isGameEndConditionReached()) {
            if (this.gameState == GameController.GAME_STATE_BLACK) {
                System.out.println("Game Over!  Black Won!");
            } else {
                System.out.println("Game Over! White Won!");
            }

            this.gameState = GameController.GAME_STATE_END;
        }

        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END:
                //don't change anymore
                break;
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

    /**
     * @return the list of pieces
     */
    public List<Piece> getPieces() {
        return this.pieces;
    }
}
