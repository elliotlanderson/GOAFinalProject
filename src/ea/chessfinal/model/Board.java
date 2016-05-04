package ea.chessfinal.model;

/**
 * Created by elliotanderson on 5/3/16.
 * This class represents the chess board that the game will be played on
 * for now, only holds constants of the different spots
 * @note this is to be kept separate from the ea.chessfinal.view logic
 * @todo abstract a "Spot" or "Tile" model to add a layer of abstraction
 */
public class Board {
    // board is 8x8 (rows numbered and columns lettered)

    /*
        row definitions, using indexing at 0
        to keep with programming standards, despite
        how weird it looks
     */
    public static final int ROW_1 = 0;
    public static final int ROW_2 = 1;
    public static final int ROW_3 = 2;
    public static final int ROW_4 = 3;
    public static final int ROW_5 = 4;
    public static final int ROW_6 = 5;
    public static final int ROW_7 = 6;
    public static final int ROW_8 = 7;

    /**
     * column definitions
     * again, indexing at 0
     */
    public static final int COLUMN_A = 0;
    public static final int COLUMN_B = 1;
    public static final int COLUMN_C = 2;
    public static final int COLUMN_D = 3;
    public static final int COLUMN_E = 4;
    public static final int COLUMN_F = 5;
    public static final int COLUMN_G = 6;
    public static final int COLUMN_H = 7;

    /**
     * toString methods, but don't override because we need parameters
     * due to the constants and this not actually being instantiated
     */

    public static String getRowString(int row) {

        String strRow = "invalid";

        switch (row) {
            case ROW_1:
                strRow = "1";
                break;
            case ROW_2:
                strRow = "2";
                break;
            case ROW_3:
                strRow = "3";
                break;
            case ROW_4:
                strRow = "4";
                break;
            case ROW_5:
                strRow = "5";
                break;
            case ROW_6:
                strRow = "6";
                break;
            case ROW_7:
                strRow = "7";
                break;
            case ROW_8:
                strRow = "8";
                break;
        }

        return strRow;
    }

    public static String getColumnString(int column) {
        String strCol = "invalid";

        switch(column) {
            case COLUMN_A: strCol = "A"; break;
            case COLUMN_B: strCol = "B"; break;
            case COLUMN_C: strCol = "C"; break;
            case COLUMN_D: strCol = "D"; break;
            case COLUMN_E: strCol = "E"; break;
            case COLUMN_F: strCol = "F"; break;
            case COLUMN_G: strCol = "G"; break;
            case COLUMN_H: strCol = "H"; break;
        }

        return strCol;
    }

}
