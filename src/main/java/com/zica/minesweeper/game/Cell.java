package com.zica.minesweeper.game;

/**
 * A Cell in a Board of the Minesweeper game.
 */
public class Cell {
    private Position position;
    private FLAGS flag;
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

    public Position getPosition() {
        return position;
    }

    public FLAGS getFlag() {
        return flag;
    }

    public void setFlag(FLAGS flag) {
        this.flag = flag;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
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
