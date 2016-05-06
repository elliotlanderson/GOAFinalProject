<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

final class Game extends Model 
{
	/**
	 * defines the relationship between a game and the moves
	 */
	public function moves() {
		return $this->hasMany('App\Models\Move');
	}
}