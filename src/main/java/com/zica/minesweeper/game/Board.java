package com.zica.minesweeper.game;

import java.util.Set;
import java.util.TreeMap;

public class Board {
    public enum OPEN_CELL_RESULT {
        /**
         * The Cell opened at the given position has a mine.
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
    private int nRows;
    private int nColumns;
    private int nMines;
    private TreeMap<Position, Cell> cells;

    public Board (int nRows, int nColumns, int nMines) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nMines = nMines;
        this.cells = populateBoard(nRows, nColumns, nMines);
    }

    public Cell[][] getCells () {
        // TODO
        throw new RuntimeException("not implemented");
    }


    /**
     * Opens the Cell at the given Position, performs extra logic that can open other cells,
     * then returns a OPEN_CELL_RESULT to indicate what happened.
     *
     * @param position
     * @return OPEN_CELL_RESULT
     */
    public OPEN_CELL_RESULT openCell(Position position) {
        // TODO
        throw new RuntimeException("not implemented");
    }

    private static TreeMap<Position, Cell> populateBoard(int nRows, int nColumns, int nMines) {
        TreeMap<Position, Cell> treeMap = new TreeMap<Position, Cell>();

        Set<Position> minePositions = getRandomizedMinePositions(nRows, nColumns, nMines);

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                Position position = new Position(row, column);
                boolean isMine = minePositions.contains(position);
                int adjacentMinesCounter = countAdjacentMines(position, minePositions, nRows, nColumns);
                Cell cell = new Cell(row, column, isMine, adjacentMinesCounter);
                treeMap.put(position, cell);
            }
        }

        return treeMap;
    }

    private static int countAdjacentMines(Position position, Set<Position> minePositions, int nRows, int nColumns) {
        int r = position.getRow();
        int c = position.getColumn();

        Position topLeft = new Position(r - 1, c - 1);
        Position topCenter = new Position(r - 1, c);
        Position topRight = new Position(r - 1, c + 1);
        Position left = new Position(r, c - 1);
        Position right = new Position(r, c + 1);
        Position bottomLeft = new Position(r + 1, c - 1);
        Position bottomCenter = new Position( r + 1, c);
        Position bottomRight = new Position( r + 1, c + 1);

        Position[] adjacentPositions = new Position[]{topLeft, topCenter, topRight, left, right, bottomLeft, bottomCenter, bottomRight};

        int counter = 0;

        for (Position adjacentPosition: adjacentPositions) {
            int adjRow = adjacentPosition.getRow();
            int adjCol = adjacentPosition.getColumn();
            if ( adjRow >= 0 && adjRow < nRows && adjCol >= 0 && adjCol < nColumns ) {
                if (minePositions.contains(adjacentPosition)) counter++;
            }
        }

        return counter;
    }

    private void updateMinesCountersInAllCells(TreeMap<Position, Cell> treeMap, Set<Position> minePositions) {
        // TODO
        throw new RuntimeException("not implemented");
    }

    private static Set<Position> getRandomizedMinePositions(int nRows, int nColumns, int nMines) {
        // TODO
        throw new RuntimeException("not implemented");
    }
}
