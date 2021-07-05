package com.zica.minesweeper.game;

public enum ToggleFlagResult {
    /**
     * The position is invalid. The board was not modified by this operation.
     */
    INVALID_POSITION,

    /**
     * The Cell was opened previously, the board was not modified by this operation.
     */
    NO_CHANGE,

    /**
     * The flag was set successfully
     */
    SET,

    /**
     * The flag was unset successfully
     */
    UNSET,
}
