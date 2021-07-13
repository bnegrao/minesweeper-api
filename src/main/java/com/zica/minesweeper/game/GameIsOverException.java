package com.zica.minesweeper.game;

/**
 * This exception indicates that a method is trying to modify a Game that is not longer in RUNNING status
 */
public class GameIsOverException extends Exception{
    public GameIsOverException(Game.GameStatus gameStatus){
        super("A Game in status '"+gameStatus+"' cannot be modified");
    }
}
