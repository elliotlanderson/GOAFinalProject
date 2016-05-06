package ea.chessfinal.model;

/**
 * Created by elliotanderson on 5/3/16.
 * This defines the piece object, which is an abstracted
 * definition of the subsets of pieces
 */

import java.awt.Image;

public class Piece {

    /**
     * @var piece constants
     * these define the different types of pieces on the chess board
     */
    public static final int PIECE_ROOK = 1;
    public static final int PIECE_KNIGHT = 2;
    public static final int PIECE_BISHOP = 3;
    public static final int PIECE_QUEEN = 4;
    public static final int PIECE_KING = 5;
    public static final int PIECE_PAWN = 6;

    /**
     * @var colors
     * defines the two colors the pieces can have
     */
    public static final int WHITE_COLOR = 0;
    public static final int BLACK_COLOR = 1;



    /**
     * @var instance of the image object
     */
    private Image img;

    /**
     * @var row (from ea.chessfinal.model.Board class)
     */
    private int row;

    /**
     * @var column (from ea.chessfinal.model.Board class)
     */
    private int column;

    /**
     * @var color
     */
    private int color;

    /**
     * @var type
     */
    private int type;

    /**
     * @var isCaptured
     * determines whether or not the piece is captured
     */
    public boolean isCaptured = false;

    /**
     * Constructor for creating a piece
     * @param row
     * @param col
     * @param color
     * @param type
     */
    public Piece( int color, int type, int row, int col) {
        this.color = color;
        this.type = type;
        this.row = row;
        this.column = col;
    }


    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int col) {
        this.column = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColor() {
        return this.color;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void isCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    public boolean isCaptured() {
        return this.isCaptured;
    }

    /**
     * Method will return a string associated with the piece type
     * for communication with the server
     * @param type one of the type constants defined above
     * @return string that server can read
     */
    public static String getTypeString(int type) {
        switch (type) {
            case PIECE_BISHOP: return "B";
            case PIECE_KING: return "K";
            case PIECE_KNIGHT: return "N";
            case PIECE_PAWN: return "P";
            case PIECE_QUEEN: return "Q";
            case PIECE_ROOK: return "R";
            default: return "?";
        }
    }

    /**
     * this method will get the color of the piece and return the
     * string associated with that color "B" or "W"
     * @param color
     * @return
     */
    public static String getColorString(int color) {
        switch (color) {
            case BLACK_COLOR: return "B";
            case WHITE_COLOR: return "W";
            default: return "?";
        }
    }

    /**
     * Override the toString() method for the server/console
     * so we can get a simple description of the piece and where it is
     * in relation to the board
     * @return string of the piece
     */
    @Override
    public String toString() {
        String strColor = getColorString(this.color);
        String strType = getTypeString(this.type);
        String strRow = getTypeString(this.row);
        String strColumn = getTypeString(this.column);

        return strColor + " " + strType + " " + strRow + "/" + strColumn;
    }

}
