package ea.chessfinal.controller;

/**
 * Created by elliotanderson on 5/3/16.
 * Validates the moves for the chess pieces based on their type
 * Class (and rules) inspired by https://github.com/zachallaun/CSC-281-Chess/blob/master/src/Piece.java
 * @author Elliot Anderson
 * @todo clean the code up a little if you get a chance
 * @todo add some of the more advanced functionality for a larger scope if you get a change (i.e. stalemate, checkmate, etc)
 */

import ea.chessfinal.model.Piece;
import ea.chessfinal.model.Board;
import ea.chessfinal.model.Move;

public class MoveValidator {

    /**
     * instance of the game object
     */
    private GameController game;

    /**
     * instance of the source piece
     */
    private Piece sourcePiece;

    /**
     * instance o the target piece
     */
    private Piece targetPiece;

    /**
     * init
     */
    public MoveValidator(GameController game) {
        this.game = game;
    }


    /**
     * the main method that is called to check if a move is valid
     * for now, checks to make sure that it is the correct player's turn
     * based on the game state, as well as to make sure that the move is in bounds.
     * from there, it uses each piece's defined rules for available moves
     *
     * @param move Move object
     * @return true if the move is valid
     */
    public boolean isMoveValid(Move move) {

        int fromRow = move.fromRow;
        int fromColumn = move.fromColumn;
        int toRow = move.toRow;
        int toColumn = move.toColumn;

        this.sourcePiece = this.game.getNonCapturedPieceAtLocation(fromRow, fromColumn);
        this.targetPiece = this.game.getNonCapturedPieceAtLocation(toRow, toColumn);

        // check if the source piece does not exist
        if (this.sourcePiece == null) {
            System.out.println("no Source piece");
            return false;
        }

        // check to make sure the source piece has the right color
        if (this.sourcePiece.getColor() == Piece.WHITE_COLOR && this.game.getGameState() == GameController.GAME_STATE_WHITE) {
            // all good
        } else if ( this.sourcePiece.getColor() == Piece.BLACK_COLOR && this.game.getGameState() == GameController.GAME_STATE_BLACK) {
            // again, all good
        } else {
            System.out.println("Not your turn");
            return false;
        }

        // check if the target location is within the boundaries of the board
        if (toRow < Board.ROW_1 || toRow > Board.ROW_8 || toColumn < Board.COLUMN_A || toColumn > Board.COLUMN_H) {
            System.out.println("out of bounds error");
            return false;
        }

        // validate piece movement by rules
        boolean validMove = false;

        switch (this.sourcePiece.getType()) {
            case Piece.PIECE_BISHOP:
                validMove = isValidBishopMove(fromRow, fromColumn, toRow, toColumn);
                break;
            case Piece.PIECE_KING:
                validMove = isValidKingMove(fromRow, fromColumn, toRow, toColumn);
                break;
            case Piece.PIECE_KNIGHT:
                validMove = isValidKnightMove(fromRow, fromColumn, toRow, toColumn);
                break;
            case Piece.PIECE_PAWN:
                validMove = isValidPawnMove(fromRow, fromColumn, toRow, toColumn);
                break;
            case Piece.PIECE_QUEEN:
                validMove = isValidQueenMove(fromRow, fromColumn, toRow, toColumn);
                break;
            case Piece.PIECE_ROOK:
                validMove = isValidRookMove(fromRow, fromColumn, toRow, toColumn);
                break;
            default:
                break;
        }

        if ( !validMove ) {
            return false;
        } else {
            // everything is ok
        }

        // TODO: add checkmate functionality (and/or stalemate)

        return true;

    }

    /**
     * checks to see if the space is available to "capture" or to move to
     * @return true/false
     */
    private boolean isTargetLocationCaptureable() {
        if (this.targetPiece == null) {
            return false;
        } else if (this.targetPiece.getColor() != this.sourcePiece.getColor()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * simply checks to see if the target location is empty
     * if it is return true
     * @return boolean
     */
    private boolean isTargetLocationFree() {
        return this.targetPiece == null;
    }

    /**
     * checks all the necessities for a valid chess move
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @return true if the move is valid
     */
    private boolean isValidBishopMove(int fromRow, int fromColumn, int toRow, int toColumn) {
        if ( this.isTargetLocationCaptureable() || this.isTargetLocationFree()) {
            //everything okay
        } else {
            return false;
        }

        boolean isValid = false;

        // check to see if the path is diagonal
        int diffRow = toRow - fromRow;
        int diffCol = toColumn - fromColumn;

        if (diffRow == diffCol && diffCol > 0) {
            // moving diagonally up-right
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, +1, +1);

        } else if (diffRow == -diffCol && diffCol > 0) {
            // moving diagonally down-right
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, -1, +1);
        } else if (diffRow == diffCol && diffCol < 0) {
            // moving diagonally down-left
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, -1, -1);
        } else if (diffRow == -diffCol && diffCol < 0) {
            // moving diagonally up-left
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, +1, -1);
        } else {
            // they are not moving diagonally
            isValid = false;
        }

        return isValid;


    }

    /**
     * checks to see if the move for the rook is valid
     * the rook can only move in a straight direction (no diagonal)
     * and cannot jump over pieces
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @return
     */
    private boolean isValidRookMove(int fromRow, int fromColumn, int toRow, int toColumn) {
        // rook can move accross any row or column, but cannot move diagonally or jump pieces
        if (this.isTargetLocationFree() || this.isTargetLocationCaptureable()) {
            //everything is okay
        } else {
            return false;
        }

        boolean isValid = false;

        // check to make sure the path is straight
        int diffRow = toRow - fromRow;
        int diffCol = toColumn - fromColumn;

        if (diffRow == 0 && diffCol > 0) {
            // moving accross right
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, 0, +1);
        } else if (diffRow == 0 && diffCol < 0) {
            // moving across left
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, 0, -1);
        } else if (diffRow > 0 && diffCol == 0) {
            // moving up
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, +1, 0);
        } else if (diffRow < 0 && diffCol == 0) {
            // moving down
            isValid = !arePiecesBetweenSourceAndTarget(fromRow, fromColumn, toRow, toColumn, -1, 0);
        } else {
            // not a straight move
            isValid = false;
        }

        return isValid;
    }

    /**
     * checks to see if the queen's move is valid
     * the queen is really just a combination of the rook and bishop in terms of rules of allowed movement
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @return true if move is valid
     */
    private boolean isValidQueenMove(int fromRow, int fromColumn, int toRow, int toColumn) {
        // queen is simply the move validation of a rook and bishop

        boolean result = isValidBishopMove(fromRow, fromColumn, toRow, toColumn);
        result |= isValidRookMove(fromRow, fromColumn, toRow, toColumn); // |= operator is a conditional assignment
        return result;
    }

    /**
     * check to see if the pawns move is valid
     * the pawn can move forward if it is not attacking,
     * and can move diagonal (to the either forward adjacent spot) to capture a piece
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @todo add functionality for if it is the pawns first move to move forward two spaces
     * @return true is move is valid
     */
    private boolean isValidPawnMove(int fromRow, int fromColumn, int toRow, int toColumn) {

        boolean isValid = false;

        // the pawn is able to move forward one occupied square, or two if it is its first move
        if (this.isTargetLocationFree()) {
            if (fromColumn == toColumn) {
                // same column, not trying to capture a piece
                if (this.sourcePiece.getColor() == Piece.WHITE_COLOR) {
                    // white piece moving
                    if (fromRow + 1 == toRow) {
                        // they are moving one up
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                } else {  // black piece moving
                    if (fromRow - 1 == toRow) {
                        //move one down (because of opposite board positions)
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                }
            } else {
                // since they are trying to move diagonal/to the side to not capture a piece,
                // this move is invalid
                isValid = false;
            }
            // ELSE IF they are trying to capture a piece that is diagonal + adjacent to it
        } else if ( isTargetLocationCaptureable()) {
            if (fromColumn + 1 == toColumn || fromColumn - 1 == toColumn) {
                // moving one column to the right or left
                if (this.sourcePiece.getColor() == Piece.WHITE_COLOR) {
                    // white pieces
                    if (fromRow + 1 == toRow) {
                        // move one up
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                } else { // black piece moving, change vertical direction of movement
                    if (fromRow - 1 == toRow) {
                        // moving one down
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                }
            } else { // they are trying to move more than one column to the left/right
                isValid = false;
            }
        } else {
            // piece is not making a valid move

            // TODO: Add functionality for moving forward two squares the first move

            isValid = false;
        }


        return isValid;
    }

    /**
     * Checks to see if the knight move is valid
     * notice there is no need for the arePiecesBetweenSourceandTarget method
     * because the knight can jump pieces
     * the color also does not matter as the piece can move in both directions
     * Must move in L-Shape so we will just do an if-else will all possible combinations
     * @todo Clean this code up a little
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @return true if move is valid
     */
    private boolean isValidKnightMove(int fromRow, int fromColumn, int toRow, int toColumn) {
        // NOTE: knight is the only piece that can jump other pieces

        System.out.println(this.isTargetLocationFree());
        if (this.isTargetLocationFree() || this.isTargetLocationCaptureable()) {
            // everything's good
        } else {
            return false;
        }


        // check all possible L-shaped combinations.  Since this piece can move both forward
        // and backwards, we do not need to distinguish between color for movement

        if (fromRow + 2 == toRow && fromColumn +1 == toColumn) {
            // move up twice and one to the right
            return true;
        } else if (fromRow +1 == toRow && fromColumn +2 == toColumn) {
            // move up once and twice to the right
            return true;
        } else if (fromRow -1 == toRow && fromColumn + 2 == toColumn) {
            // move down once and twice to the right
            return true;
        } else if (fromRow - 2 == toRow && fromColumn +1 == toColumn) {
            // move down twice and once to the right
            return true;
        } else if (fromRow -2 == toRow && fromColumn -1 == toColumn) {
            // move down twice and once to the left
            return true;
        } else if (fromRow -1 == toRow && fromColumn -2 == toColumn) {
            // move down once and twice to the left
            return true;
        } else if (fromRow + 1 == toRow && fromColumn-2 == toColumn) {
            // move up once and twice to the left
            return true;
        } else if (fromRow + 2 == toRow && fromColumn-1 == toColumn) {
            // move twice up and once to the left
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks to see if the king's move is valid
     * the king can move to any open adjacent/captureable spot
     * in any direction, therefore color again doesn't matter
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @todo add castling functionality
     * @return true if move is valid
     */
    private boolean isValidKingMove(int fromRow, int fromColumn, int toRow, int toColumn) {

        if (this.isTargetLocationFree() || this.isTargetLocationCaptureable()) {
            // everything is okay to continue
        } else {
            return false;
        }

        boolean isValid = true;

        if (fromRow + 1 == toRow && fromColumn == toColumn) {
            // up once
            isValid = true;
        } else if (fromRow +1 == toRow && fromColumn+1 == toColumn) {
            // move up one right one
            isValid =  true;
        } else if (fromRow == toRow && fromColumn + 1 == toColumn) {
            // move right one
            isValid =  true;
        } else if (fromRow - 1 == toRow && fromColumn == toColumn) {
            // move down once
            isValid =  true;
        } else if (fromRow - 1 == toRow && fromColumn - 1 == toColumn) {
            // move down and to the left
            isValid =  true;
        } else if (fromRow == toRow && fromColumn - 1 == toColumn) {
            // move to the left once
            isValid = true;
        } else if (fromRow + 1 == toRow && fromColumn - 1 == toColumn) {
            isValid = true;
        } else {
            isValid = false;
        }

        // TODO: add castling functionality

        return isValid;
    }

    /**
     * checks to see if there is a piece in the path from the source piece and target piece
     * @param fromRow
     * @param fromColumn
     * @param toRow
     * @param toColumn
     * @param rowIncrementPerStep
     * @param colIncrementPerStep
     * @return true if there are pieces in between/false if not
     */
    private boolean arePiecesBetweenSourceAndTarget(int fromRow, int fromColumn, int toRow, int toColumn, int rowIncrementPerStep, int colIncrementPerStep) {
        int currentRow = fromRow + rowIncrementPerStep;
        int currentColumn = fromColumn + colIncrementPerStep;

        while (true) {
            if (currentRow == toRow && currentColumn == toColumn) {
                break;
            }
            if (currentRow < Board.ROW_1 || currentRow > Board.ROW_8 || currentColumn < Board.COLUMN_A || currentColumn > Board.COLUMN_H) {
                break;
            }
            if (this.game.isNonCapturedPieceAtLocation(currentRow, currentColumn)) {
                return true;
            }

            currentRow += rowIncrementPerStep;
            currentColumn += colIncrementPerStep;
        }

        return false;
    }




}
