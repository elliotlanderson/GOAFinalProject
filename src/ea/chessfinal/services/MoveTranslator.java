package ea.chessfinal.services;

import ea.chessfinal.model.Piece;
import ea.chessfinal.model.Move;
import ea.chessfinal.model.Board;

/**
 * Created by elliotanderson on 5/5/16.
 * This class will translate the move objects into strings and vice-versa
 * loosely based on the Translator design pattern, though very simplified
 * @author Elliot Anderson
 */
public class MoveTranslator {

    /**
     * converts a move object to a string for the server to interpret
     * @param move Move object
     * @return String
     */
    public static String convertMoveToString(Move move) {
        String moveStr = Board.getColumnString(move.fromColumn)
                + Board.getRowString(move.fromRow)
                + "-" + Board.getColumnString(move.toColumn)
                + Board.getRowString(move.toRow);
        return moveStr;
    }

    /**
     * converts a string that would be returned from the server into
     * a move object to be used by the program
     * @param input string of move
     * @return
     */
    public static Move convertStringToMove(String input) {
        if (input == null || input.length() != 5) {
            return null;
        }

        String strFromColumn = input.substring(0,1);
        String strFromRow = input.substring(1,2);
        String strToColumn = input.substring(3,4);
        String strToRow = input.substring(4,5);

        int fromColumn = 0;
        int fromRow = 0;
        int toColumn = 0;
        int toRow = 0;

        fromColumn = convertColumnStrToColumnInt(strFromColumn);
        fromRow = convertRowStrToRowInt(strFromRow);
        toColumn = convertColumnStrToColumnInt(strToColumn);
        toRow = convertRowStrToRowInt(strToRow);

        return new Move(fromRow, fromColumn, toRow, toColumn);
    }

    /**
     * Converts a column string (e.g. 'a') into its internal representation.
     *
     * @param strColumn a valid column string (e.g. 'a')
     * @return internal integer representation of the column
     */
    private static int convertColumnStrToColumnInt(String strColumn) {
        if (strColumn.equalsIgnoreCase("a")) {
            return Board.COLUMN_A;
        } else if (strColumn.equalsIgnoreCase("b")) {
            return Board.COLUMN_B;
        } else if (strColumn.equalsIgnoreCase("c")) {
            return Board.COLUMN_C;
        } else if (strColumn.equalsIgnoreCase("d")) {
            return Board.COLUMN_D;
        } else if (strColumn.equalsIgnoreCase("e")) {
            return Board.COLUMN_E;
        } else if (strColumn.equalsIgnoreCase("f")) {
            return Board.COLUMN_F;
        } else if (strColumn.equalsIgnoreCase("g")) {
            return Board.COLUMN_G;
        } else if (strColumn.equalsIgnoreCase("h")) {
            return Board.COLUMN_H;
        } else
            throw new IllegalArgumentException("invalid column: " + strColumn);
    }

    /**
     * Converts a row string (e.g. '1') into its internal representation.
     *
     * @param strRow a valid row string (e.g. '1')
     * @return internal integer representation of the row
     */
    private static int convertRowStrToRowInt(String strRow) {
        if (strRow.equalsIgnoreCase("1")) {
            return Board.ROW_1;
        } else if (strRow.equalsIgnoreCase("2")) {
            return Board.ROW_2;
        } else if (strRow.equalsIgnoreCase("3")) {
            return Board.ROW_3;
        } else if (strRow.equalsIgnoreCase("4")) {
            return Board.ROW_4;
        } else if (strRow.equalsIgnoreCase("5")) {
            return Board.ROW_5;
        } else if (strRow.equalsIgnoreCase("6")) {
            return Board.ROW_6;
        } else if (strRow.equalsIgnoreCase("7")) {
            return Board.ROW_7;
        } else if (strRow.equalsIgnoreCase("8")) {
            return Board.ROW_8;
        } else
            throw new IllegalArgumentException("invalid column: " + strRow);
    }
}
