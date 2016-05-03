/**
 * Created by elliotanderson on 5/3/16.
 * This defines the piece object, which is an abstracted
 * definition of the subsets of pieces
 */

import java.awt.Image;

public class Piece {
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
     * Constructor for creating a piece
     * @param img
     * @param x
     * @param y
     */
    public Piece(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
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
