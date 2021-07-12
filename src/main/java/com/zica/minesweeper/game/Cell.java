package com.zica.minesweeper.game;

import org.springframework.data.annotation.PersistenceConstructor;

/**
 * A Cell in a Board of the Minesweeper game.
 */
public class Cell {
    /**
     * Flags that can be used by a game player to identify a closed cell as one that contains a mine or unknown content.
     * They can only be applied to closed cells.
     */
    public enum Flags {
        /**
         * Flag to indicate that a closed cell contains a mine
         */
        MINE,

        /**
         * Flag to indicate that a closed cell may, or may not, contain a mine.
         */
        QUESTION
    }

    private Position position;
    private Flags flag;
    private boolean isClosed;
    private boolean isMine;
    private int adjacentMines;

    public Cell(int row, int column, boolean isMine, int adjacentMines) {
        this.position = new Position(row, column);
        this.flag = null;
        this.isClosed = true;
        this.isMine = isMine;
        this.adjacentMines = adjacentMines;
    }

    @PersistenceConstructor
    protected Cell(Position position, Flags flag, boolean isClosed, boolean isMine, int adjacentMines) {
        this.position = position;
        this.flag = flag;
        this.isClosed = isClosed;
        this.isMine = isMine;
        this.adjacentMines = adjacentMines;
    }

    public Position getPosition() {
        return position;
    }

    public Flags getFlag() {
        return flag;
    }

    public void setFlag(Flags flag) {
        this.flag = flag;
    }

    public boolean isClosed() {
        return isClosed;
    }
    public boolean isOpened() { return !isClosed; }

    public void setClosed(boolean closed) { isClosed = closed; }

    /**
     * Opens a cell. The same as setClosed(false) but more idiomatic
     */
    public void open() {
        this.isClosed = false;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
