package ea.chessfinal.view; /**
 * Created by elliotanderson on 5/3/16.
 * The VIEW of the chess piece
 * This is the UI class version as opposed to the ea.chessfinal.model.Piece class, which
 * handles the business/game logic
 */

import ea.chessfinal.model.Piece;
import ea.chessfinal.view.GameGUI;

import java.awt.Image;

public class PieceView {

    /**
     * @var  image
     */
    private Image img;

    /**
     *@var x position
     */
    private int x;

    /**
     * @var y position
     */
    private int y;

    /**
     * @var piece to connect the ea.chessfinal.view item to the ea.chessfinal.controller logic
     */
    private Piece piece;

    public PieceView(Image img, Piece piece) {
        this.img = img;
        this.piece = piece;

        this.resetToOriginalPosition();
    }

    /**
     * move the piece back to the coordinates that
     * correspond with the board's set up
     */
    public void resetToOriginalPosition() {
        this.x = GameGUI.convertColumnToX(piece.getColumn());
        this.y = GameGUI.convertRowToY(piece.getRow());
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * get the width of the piece based on the image
     * @return int of width
     */
    public int getWidth() {
        return this.img.getWidth(null);
    }

    /**
     * get the height of the piece based on the image
     * @return int of height
     */
    public int getHeight() {
        return this.img.getHeight(null);
    }

    public Image getImage() {
        return this.img;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean isCaptured() {
        return this.piece.isCaptured();
    }

    public int getColor() {
        return this.piece.getColor();
    }
}
