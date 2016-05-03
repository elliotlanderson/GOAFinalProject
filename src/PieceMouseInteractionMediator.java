/**
 * Created by elliotanderson on 5/3/16.
 * This will act as the mediator controller for interactions
 * between the mouse and the Piece objects
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class PieceMouseInteractionMediator implements MouseListener, MouseMotionListener {

    /**
     * @var list of all pieces
     */
    private List<Piece> pieces;

    /**
     * @var instance of the Game GUI class
     */
    private GameGUI gameGUI;

    /**
     * @var piece that will be dragged
     */
    private Piece dragPiece;

    /**
     * @var drag offset X position
     */
    private int dragOffsetX;

    /**
     * @var drag offset Y position
     */
    private int dragOffsetY;

    /**
     * constructor to instantiate variables
     * @param pieces
     * @param gameGUI
     */
    public PieceMouseInteractionMediator(List<Piece> pieces, GameGUI gameGUI) {
        this.pieces = pieces;
        this.gameGUI = gameGUI;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x  = e.getPoint().x;
        int y = e.getPoint().y;

        // figure out which piece to move,
        // check list from top to bottom (reverse order)

        for (int i = this.pieces.size() - 1; i >= 0; i--) {
            Piece piece = this.pieces.get(i);

            if (mouseOverPiece(piece,x,y)) {
                // calculate the offset so that the corner does not
                // go to where the mouse is
                this.dragOffsetX = x - piece.getX();
                this.dragOffsetY = y - piece.getY();
                this.dragPiece = piece;
                break;
            }
        }

        // move drag piece to the top of the list
        if (this.dragPiece != null) {
            this.pieces.remove(this.dragPiece);
            this.pieces.add(this.dragPiece);
        }
    }

    /**
     * check whether the mouse is currently over this piece
     * @param piece --> the playing piece
     * @param x --> x coordinate of mouse
     * @param y --> y coordinate of mouse
     * @return true if mouse is over the piece
     */
    private boolean mouseOverPiece(Piece piece, int x, int y) {
        return piece.getX() <= x && piece.getX() + piece.getWidth() >= x && piece.getY() <= y && piece.getY() + piece.getHeight() >= y;
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        this.dragPiece = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.dragPiece != null) {
            this.dragPiece.setX(e.getPoint().x - this.dragOffsetX);
            this.dragPiece.setY(e.getPoint().y - this.dragOffsetY);
            this.gameGUI.repaint();
        }
    }

    /** rest of the implementation requirements **/

    @Override
    public void mouseClicked(MouseEvent arg0) {}

    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mouseMoved(MouseEvent arg0) {}

}
