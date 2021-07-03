package com.zica.minesweeper.game;

/**
 * Position of a Cell in a Board.
 * 'row' is the vertical axis which grows from top to bottom,
 * 'column' is the horizontal axis which grows from left two right,
 * In a Board of N rows and N columns, (row=0, column=0) is the position
 * of the top-left Cell while (row=N, column=N) is the bottom-right Cell
 */
public class Position implements Comparable {

    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int compareTo(Object other) {
        return this.toString().compareTo(other.toString());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return row +", " + column;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Position)){
            return false;
        }
        return this.toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
