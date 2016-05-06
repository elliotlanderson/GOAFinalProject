package ea.chessfinal.model;

/**
 * Created by elliotanderson on 5/4/16.
 * This class aggregates and objectifies the idea of
 * a "Move".
 * @author Elliot Anderson
 */
public class Move {

    /**
     * @var fromRow
     */
    public int fromRow;

    /**
     * @var fromColumn
     */
    public int fromColumn;

    /**
     * @var toRow
     */
    public int toRow;

    /**
     * @var toColumn
     */
    public int toColumn;

    /**
     * @var to hold the captured piece, for use of the server
     */
    public Piece capturedPiece;

    /**
     * The init (constructor) method to set all the variables
     * @param fromRow the row on the Board the original piece is located at
     * @param fromColumn the column on the Board the original piece is located at
     * @param toRow the row on the Board the piece is trying to be moved to
     * @param toColumn the column on the Board the piece is trying to be moved to
     */
    public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
    }

    /**
     * clone the exact same move
     * @return new Move object
     */
    public Move clone() {
        return new Move(fromRow, fromColumn, toRow, toColumn);
    }

    /**
     * When the move object gets sent as a string, send it in the format of the col/row -> col/row
     * @return String
     */
    @Override
    public String toString() {
        return Board.getColumnString(fromColumn)+"/"+Board.getRowString(fromRow) + " -> " + Board.getColumnString(toColumn) + "/" + Board.getRowString(toRow);
    }

}
