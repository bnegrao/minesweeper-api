package com.zica.minesweeper.game;

public enum OpenCellResult {

    /**
     * The position is invalid. The board was not modified by this operation.
     */
    INVALID_POSITION,

    /**
     * The Cell opened at the given position has a mine (game lost).
     * As consequence all the remaining closed cells are opened.
     */
    IS_A_MINE,

    /**
     * The Cell was opened and it was unarmed.
     * Surrounding unarmed cells could have been opened as consequence.
     */
    OPENED_OK,

    /**
     * The Cell was opened previously, the board was not modified by this operation.
     */
    NO_CHANGE,

    /**
     * The Cell opened was unarmed and there are no unarmed
     * cells left to be opened (game won)
     */
    BOARD_COMPLETE,
}
