<?php

namespace App\Http\Controllers;

/**
 * Since the server isn't the main focus/scope of this project, the server will be kind of thrown
 * together without all the design patterns I focused on so heavily for the java app
 * @author Elliot Anderson <eanderson17@germantownfriends.org>
 */


use Illuminate\Http\Request;

use App\Models\Game;
use App\Models\Move;


class MainController extends Controller {

	/**
	 * The controller method that gets called whenever the java app
	 * makes a request (POST) to the server.
	 * Rememeber, the parameter "action" is the one that tells us which method
	 * to call and how to return the data
	 */
	public function handleServer(Request $request) {

		$action = $request->input('action');

		$response = null;

		switch ($action) {
			case 'createGame':
				$response = $this->createGame($request->input('password'));
				break;
			case 'isGameValid':
				$response = $this->isGameValid($request->input('gameID'), $request->input('gamePassword'));
				break;
			case 'sendMove':
				$response = $this->sendMove($request->input('gameID'), $request->input('gamePassword'), $request->input('move'));
				break;
			case 'getLastMove':
				$response = $this->getLastMove($request->input('gameID'));
				break;
			default:
				$response = "error";
		}

		return $response;
	}

	/**
	 * gets the last move from a specified game
	 * @note does not require authentication
	 * @return " " if there was an error (no game or no last move)
	 * or @return the string of the move
	 */
	public function getLastMove($gameID) {
		// first, validate the game

		$game = Game::find($gameID);

		if (is_null($game)) {
			return " ";
		} else {
			if (is_null($game->moves()->first())) {
				return " ";
			} else {
				return $game->moves()->first()->moveStr;

			}
		}

	}

	/**
	 * creates an instance of a move in the DB
	 * first validates it
	 * then saves the moves string and associates the relationship
	 */
	public function sendMove($gameID, $gamePassword, $move) {

		if (! $this->isGameValid($gameID, $gamePassword)) {
			return "error";
		} else {
			$move = new Move;
			$move->moveStr = $move;
			$move->game_id = $gameID;
			$move->save();
		}

		return strval($move->id);
	}

	/**
	 * Create an instance of the game object
	 * Forget about security because this project is due today
	 * @return string of the game id
	 */
	public function createGame($gamePassword) {

		$game = new Game;
		$game->password = $gamePassword;
		$game->save();

		return strval($game->id);
	}

	/**
	 * checks to see if the game is valid by using the most basic
	 * of authentication systems
	 * @return boolean true/false in string form
	 */
	public function isGameValid($gameID, $gamePassword) {

		$game = Game::find($gameID);

		if (is_null($game)) {
			return "false";
		} else {
			if ($game->password != $gamePassword) {
				return "false";
			}
		}

		return "true";
	}
}