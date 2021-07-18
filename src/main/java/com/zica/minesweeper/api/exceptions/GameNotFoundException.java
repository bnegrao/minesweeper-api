package com.zica.minesweeper.api.exceptions;

/**
 * Exception to indicate that the searched gameId does not exist on the database
 */
public class GameNotFoundException extends Exception {
    public GameNotFoundException(String message) {
        super(message);
    }
}
