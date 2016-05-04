package ea.chessfinal.model;

/**
 * Created by elliotanderson on 5/3/16.
 * This class represents the chess board that the game will be played on
 * for now, only holds constants of the different spots
 * @note this is to be kept separate from the ea.chessfinal.view logic
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



}
