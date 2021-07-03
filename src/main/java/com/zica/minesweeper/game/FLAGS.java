package com.zica.minesweeper.game;

/**
 * Flags that can be used by a game player to identify a closed cell as one that contains a mine or unknown content.
 */
public enum FLAGS {
    /**
     * Flag to indicate that a closed cell contains a mine
     */
    MINE,

    /**
     * Flag to indicate that a closed cell may, or may not, contain a mine.
     */
    QUESTION
}
