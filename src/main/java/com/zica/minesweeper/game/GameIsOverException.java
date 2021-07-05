package com.zica.minesweeper.game;

public class GameIsOverException extends RuntimeException{
    public GameIsOverException() {
    }

    public GameIsOverException(String message) {
        super(message);
    }

    public GameIsOverException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameIsOverException(Throwable cause) {
        super(cause);
    }

    public GameIsOverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
