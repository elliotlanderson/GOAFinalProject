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
     * @var x position
     */
    private int x;

    /**
     * @var y position
     */
    private int y;

    /**
     * @var color
     */
    private int color;

    /**
     * @var type
     */
    private int type;

    /**
     * Constructor for creating a piece
     * @param img
     * @param x
     * @param y
     */
    public Piece(Image img, int x, int y, int color, int type) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.color = color;
        this.type = type;
    }

    public Image getImage() {
        return this.img;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * gets the integer value width of the object
     * @return int of width
     */
    public int getWidth() {
        return img.getWidth(null);
    }

    public int getHeight() {
        return img.getHeight(null);
    }


}
