<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddGameAndMovesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('games', function(Blueprint $table) {
            $table->increments('id');
            $table->string('password');
            $table->timestamps();
        }); 

        Schema::create('moves', function(Blueprint $table) {
            $table->increments('id');
            $table->string('moveStr');
            $table->integer('game_id');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
        Schema::drop('games');
        Schema::drop('moves');
    }
}
