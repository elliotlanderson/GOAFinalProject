<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

final class Move extends Model 
{
	public function game() {
		return $this->belongsTo('App\Models\Game');
	}
}