package ea.chessfinal.interfaces;

/**
 * Created by elliotanderson on 5/4/16.
 * This interface will be the definition of what a Player object can do
 * As of the creation of this interface, I am not sure what exactly I want
 * to make the player object do, so I am making this abstract on purpose
 *
 * @author Elliot Anderson
 */

import ea.chessfinal.model.Move;

public interface PlayerInterface {

    /**
     * this method will grab the player's attepmt at a move
     * @return move object
     */
    Move getMove();

    /**
     * This method will successfully execute the player's move, if it is valid
     * it will then notify all the other listening objects (other players/server/etc)
     * that the move was successfully executed
     * @param move the valid move object
     */
    void moveSuccessfullyExecuted(Move move);
}
