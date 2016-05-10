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

import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.model.Board;
import ea.chessfinal.model.Piece;
import ea.chessfinal.model.Move;

import java.awt.*;
import java.lang.Runnable;
import java.util.List;
import java.util.ArrayList;

public class GameController implements Runnable {

    /**
     * define the gameState constants
     */
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END_BLACK_WON = 2;
    public static final int GAME_STATE_END_WHITE_WON = 3;

    private int gameState = GAME_STATE_WHITE; // default white starts

    // index 0 = bottom, size-1 = top
    private List<Piece> pieces = new ArrayList<Piece>();
    private List<Piece> capturedPieces = new ArrayList<Piece>();

    /**
     * @var instance of the move validator
     */
    private MoveValidator moveValidator;

    /**
     * define the player implementations
     */
    private PlayerInterface blackPlayerInterface;
    private PlayerInterface whitePlayerInterface;
    private PlayerInterface activePlayerInterface;


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
     * Sets the player for the specified piece color in parameter 1
     * @param pieceColor - the color of the player being set
     * @param playerAbstract - the player
     */
    public void setPlayer(int pieceColor, PlayerInterface playerAbstract) {
        switch (pieceColor) {
            case Piece.WHITE_COLOR: this.whitePlayerInterface = playerAbstract; break;
            case Piece.BLACK_COLOR: this.blackPlayerInterface = playerAbstract; break;
            default: throw new IllegalArgumentException("Invalid piece color");
        }
    }

    /**
     * Starts the game
     */
    public void start() {
        // make sure all players are ready
        System.out.println("Waiting for both players"); // this will come in use later when we are doing two online players

        while (this.blackPlayerInterface == null || this.whitePlayerInterface == null) {
            // wait for both players to accept game
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {e.printStackTrace();}
        }

        // set the starting player (white default)
        this.activePlayerInterface = this.whitePlayerInterface;

        // start the flow of the game

        while (!isGameEndConditionReached()) {
            this.waitForMove();
            this.toggleActivePlayer();
        }

        if (this.gameState == GameController.GAME_STATE_END_BLACK_WON) {
            System.out.println("Black won!");
        } else if (this.gameState == GameController.GAME_STATE_END_WHITE_WON) {
            System.out.println("White won!");
        } else {
            throw new IllegalStateException("Not a legal end state");
        }


        // game is over
    }

    /**
     * toggles the "active" player.  The active player is the one whose move it
     * currently is, as well as the one whose move we are waiting on
     */
    private void toggleActivePlayer() {
        if (this.activePlayerInterface == this.whitePlayerInterface) {
            this.activePlayerInterface = this.blackPlayerInterface;
        } else {
            this.activePlayerInterface = this.whitePlayerInterface;
        }

        this.changeGameState();
    }

    private void waitForMove() {
        Move move = null;

        do {
            move = this.activePlayerInterface.getMove();
            try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();};
            if (move != null && this.moveValidator.isMoveValid(move)) {
                break;
            } else if (move != null && !this.moveValidator.isMoveValid(move)) {
                move = null;
                System.exit(0);
            }
        } while (move == null);

        // execute the actual move
        boolean success = this.movePiece(move);

        if (success) {
            this.blackPlayerInterface.moveSuccessfullyExecuted(move);
            this.whitePlayerInterface.moveSuccessfullyExecuted(move);
        } else {
            throw new IllegalStateException("The move was valid, but there was an issue during the execution");
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
     * @param move the move object
     * @return true if the piece moved successfully, false if it wasn't
     */
    public boolean movePiece(Move move) {

        move.capturedPiece = this.getNonCapturedPieceAtLocation(move.toRow, move.toColumn);

        Piece piece = getNonCapturedPieceAtLocation(move.fromRow, move.fromColumn);

        // check to see if the move is capturing an opponent's piece
        int opponentColor = (piece.getColor() == Piece.BLACK_COLOR ? Piece.WHITE_COLOR : Piece.BLACK_COLOR);

        if (isNonCapturedPieceAtLocation(opponentColor, move.toRow, move.toColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(move.toRow, move.toColumn);
            this.pieces.remove(opponentPiece);
            this.capturedPieces.add(opponentPiece);
            opponentPiece.isCaptured(true);
        }

        piece.setRow(move.toRow);
        piece.setColumn(move.toColumn);

        return true;
    }

    /**
     * undo the move specified in the parameter
     * @param move
     */
    public void undoMove(Move move) {
        Piece piece = getNonCapturedPieceAtLocation(move.toRow, move.toColumn);

        piece.setRow(move.fromRow);
        piece.setColumn(move.fromColumn);

        if (move.capturedPiece != null) {
            move.capturedPiece.setRow(move.toRow);
            move.capturedPiece.setColumn(move.toColumn);
            move.capturedPiece.isCaptured(false);
            this.capturedPieces.remove(move.capturedPiece);
            this.pieces.add(move.capturedPiece);
        }

        if (piece.getColor() == Piece.BLACK_COLOR) {
            this.gameState = GameController.GAME_STATE_BLACK;
        } else {
            this.gameState = GameController.GAME_STATE_WHITE;
        }
    }

    /**
     * Check if the condition for the game ending has been met: one king has been captured
     * YES, i understand this is an oversimplification
     * @todo Add functionality for a "checkmate" so the user doesn't actually have to capture the king
     * @return true if the conditions show the game has ended (king captured)
     */
    private boolean isGameEndConditionReached() {
        for (Piece piece : this.capturedPieces) {
            if (piece.getType() == Piece.PIECE_KING) {
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
            if (piece.getRow() == row && piece.getColumn() == col  && piece.getColor() == color ) {
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
            if (piece.getRow() == row && piece.getColumn() == col ) {
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
            if (piece.getRow() == row && piece.getColumn() == col) {
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
                this.gameState = GameController.GAME_STATE_END_BLACK_WON;
            } else if (this.gameState == GameController.GAME_STATE_WHITE) {
                this.gameState = GameController.GAME_STATE_END_WHITE_WON;
            } else {
                // leave the game state as it is
            }

            return;
        }

        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END_BLACK_WON:
                break;
            case GAME_STATE_END_WHITE_WON:
                //don't change anymore
                break;
            default:
                throw new IllegalStateException("Unknown game state");
        }
    }

    /**
     * @return the move validator instance
     */
    public MoveValidator getMoveValidator() {
        return this.moveValidator;
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

    /**
     * constant threaded run loop
     */
    @Override
    public void run() {
        this.start();
    }
}
