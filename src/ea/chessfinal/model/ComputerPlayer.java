package ea.chessfinal.model;

import java.util.List;
import java.util.ArrayList;

import ea.chessfinal.controller.MoveValidator;
import ea.chessfinal.interfaces.PlayerInterface;
import ea.chessfinal.controller.GameController;

/**
 * Created by elliotanderson on 5/7/16.
 * This is the player that a GUI player would play against
 * Implements basic AI to see what possible moves the computer can make and then will move
 * @todo highlight the moves made so you can see
 * @todo add a more complex AI algorithm so there can be different game modes (easy, medium, hard)
 */
public class ComputerPlayer implements PlayerInterface {

    /**
     * @var instance of the game
     */
    private GameController game;

    /**
     * @var instance of the move validator class
     */
    private MoveValidator moveValidator;

    /**
     * @var number of moves to look into the future.  (It is AI after all)
     */
    public int futureMoves = 2;

    /**
     * set the instance variables
     * @param game instance of Game
     */
    public ComputerPlayer(GameController game) {
        this.game = game;
        this.moveValidator = this.game.getMoveValidator();
    }

    @Override
    public Move getMove() {
        return this.getBestMove();
    }

    /**
     * get the best move given the current layout of the board
     * and pieces
     * @return a VALID move object
     */
    private Move getBestMove() {
        System.out.println("Getting best move...");


        List<Move> validMoves = this.generateMoves();

        int bestResult = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : validMoves) {
            executeMove(move);

            int evaluationResult = -1 * this.negaMaxAlgorithm(this.futureMoves);
            undoMove(move);
            if (evaluationResult > bestResult) {
                bestResult = evaluationResult;
                bestMove = move;
            }
        }

        return bestMove;

    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        // same instance of the GameController, so no need to do anything extra here
    }


    /**
     * Generates all the possible moves on the board and adds them to a list
     * Not the best in terms of processing speed but it gets the job done
     * @return the List of valid moves
     */
    private List<Move> generateMoves() {

        List<Piece> pieces = this.game.getPieces();
        List<Move> validMoves = new ArrayList<Move>();
        Move testMove = new Move(0,0,0,0);

        int pieceColor = (this.game.getGameState()==GameController.GAME_STATE_WHITE ? Piece.WHITE_COLOR : Piece.BLACK_COLOR);

        // iterate over all the non-captured pieces
        for (Piece piece : pieces) {

            // only check the pieces of the current players color
            if (pieceColor == piece.getColor()) {
                testMove.fromRow = piece.getRow();
                testMove.fromColumn = piece.getColumn();

                for (int toRow = Board.ROW_1; toRow <= Board.ROW_8; toRow++) {
                    for (int toColumn = Board.COLUMN_A; toColumn <= Board.COLUMN_H; toColumn++) {
                        testMove.toRow = toRow;
                        testMove.toColumn = toColumn;

                        // check to see if this move is valid
                        if (this.moveValidator.isMoveValid(testMove)) {
                            validMoves.add(testMove.clone());
                        } else {
                            // skip, move isn't valid
                        }
                    }
                }
            }
        }

        return validMoves;
    }

    /**
     * Executes the move action and then changes the game state afterwards
     * @todo possibly separate changing the game state for better AI and decoupling
     * @param move the move object to move
     */
    private void executeMove(Move move) {
        this.game.movePiece(move);
        this.game.changeGameState();
    }

    /**
     * undo a move that was already made (calls it from the GameController)
     * @param move - executed move object
     */
    private void undoMove(Move move) {
        this.game.undoMove(move);
    }

    private int negaMaxAlgorithm(int depth) {
        if (depth <= 0
                || this.game.getGameState() == GameController.GAME_STATE_END_WHITE_WON
                || this.game.getGameState() == GameController.GAME_STATE_END_BLACK_WON) {
            return evaluateState();
        }

        List<Move> moves = this.generateMoves();
        int currentMax = Integer.MIN_VALUE;

        for (Move currentMove: moves) {
            executeMove(currentMove);

            int score = -1 * negaMaxAlgorithm(depth-1);
            undoMove(currentMove);

            if (score > currentMax) {
                currentMax = score;
            }
        }

        return currentMax;
    }

    /**
     * check out the curent game state from the POV of
     * the current player object.  The higher the number, the better the situation
     * is for the player.
     * @return int of current game state
     */
    private int evaluateState() {

        int scoreWhite = 0;
        int scoreBlack = 0;
        for (Piece piece : this.game.getPieces()) {
            if (piece.getColor() == Piece.BLACK_COLOR) {
                scoreBlack += getScoreForPieceType(piece.getType());
                scoreBlack += getScoreForPiecePosition(piece.getRow(), piece.getColumn());
            } else if (piece.getColor() == Piece.WHITE_COLOR) {
                scoreWhite += getScoreForPieceType(piece.getType());
                scoreWhite += getScoreForPiecePosition(piece.getRow(), piece.getColumn());
            } else {
                throw new IllegalStateException("invalid piece color");
            }
        }

        int gameState = this.game.getGameState();

        if (gameState == GameController.GAME_STATE_BLACK) {
            return scoreBlack - scoreWhite;
        } else if (gameState == GameController.GAME_STATE_WHITE) {
            return scoreWhite - scoreBlack;
        } else if (gameState == GameController.GAME_STATE_END_WHITE_WON
                            || gameState == GameController.GAME_STATE_END_BLACK_WON) {
            return Integer.MIN_VALUE + 1;
        } else {
            throw new IllegalStateException("invalid game state");
        }
    }

    /**
     * get the evaluation score dependent on the type of piece
     * @param type -- of Piece.TYPE_
     * @return integer score
     */
    private int getScoreForPieceType(int type) {
        switch (type) {
            case Piece.PIECE_BISHOP: return 30;
            case Piece.PIECE_KING: return 999999;
            case Piece.PIECE_KNIGHT: return 30;
            case Piece.PIECE_PAWN: return 10;
            case Piece.PIECE_QUEEN: return 90;
            case Piece.PIECE_ROOK: return 50;
            default: throw new IllegalArgumentException("Unknown piece type");
        }
    }

    /**
     * get the evaluation bonus (from the negaMax algorithm) for the specified
     * position
     * @param row - int of the Board.ROW_
     * @param column - int of the Board.COLUMN_
     * @return integer of the score
     */
    private int getScoreForPiecePosition(int row, int column) {
        byte[][] positionWeight =
                { {1,1,1,1,1,1,1,1}
                , {2,2,2,2,2,2,2,2}
                , {2,2,3,3,3,3,2,2}
                , {2,2,3,4,4,3,2,2}
                , {2,2,3,4,4,3,2,2}
                , {2,2,3,3,3,3,2,2}
                , {2,2,2,2,2,2,2,2}
                , {1,1,1,1,1,1,1,1}
                };
        return positionWeight[row][column];
    }




}
