package ea.chessfinal.controller;

/**
 * Created by elliotanderson on 5/3/16.
 * The ea.chessfinal.controller for the chess game
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

    private int gameState = GAME_STATE_WHITE; // default white starts

    // index 0 = bottom, size-1 = top
    private List<Piece> pieces = new ArrayList<Piece>();


    /**
     * initialize the game
     */
    public GameController() {
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
     * then that piece is marked as "captured"
     * @param fromRow the row the piece is being moved from
     * @param fromCol the col the piece is being moved from
     * @param toRow the row the piece is being moved to
     * @param toCol the column the piece is being moved to
     */
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = getNonCapturedPieceAtLocation(fromRow, fromCol);

        // check to see if the move is capturing an opponent's piece
        int opponentColor = (piece.getColor() == Piece.BLACK_COLOR ? Piece.BLACK_COLOR : Piece.WHITE_COLOR);

        if (isNonCapturedPieceAtLocation(opponentColor, toRow, toCol)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(toRow, toCol);
            opponentPiece.isCaptured = true;
        }

        piece.setRow(toRow);
        piece.setColumn(toCol);
    }

    /**
     * checks if there is a piece at the specified location that is not
     * set to "captured" and has the specified color
     * @param color of piece
     * @param row the row to check for occupation
     * @param col the col to check for occupation
     * @return boolean as to whether the space is occupied with specified color
     */
    private boolean isNonCapturedPieceAtLocation(int color, int row, int col) {
        for (Piece piece: this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == col && !piece.isCaptured  && piece.getColor() == color ) {
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
    private Piece getNonCapturedPieceAtLocation(int row, int col) {
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

    /**
     * @return the list of pieces
     */
    public List<Piece> getPieces() {
        return this.pieces;
    }
}
