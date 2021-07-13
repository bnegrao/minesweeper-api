package com.zica.minesweeper.api.exceptions;

/**
 * Exception to indicate that the searched gameId does not exist on the database
 */
public class GameIdNotFoundException extends Exception {
    public GameIdNotFoundException(String gameId) {
        super("Game with id '"+gameId+"' does not exist");
    }
}
