package com.zica.minesweeper.game;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    public enum OPEN_CELL_RESULT {

        /**
         * The position is invalid. The board was not modified by this operation.
         */
        INVALID_POSITION,

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

    /**
     * @return The Cells in this Board in proper order.
     */
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
        Cell cell = cells.get(position);
        if (cell == null){
            return OPEN_CELL_RESULT.INVALID_POSITION;
        }
        if (cell.isClosed() == false) {
            return OPEN_CELL_RESULT.NO_CHANGE;
        } else if (cell.isMine()) {
            openAllCells();
            return OPEN_CELL_RESULT.IS_A_MINE;
        } else if (cell.getAdjacentMines() == 0) {
            openSurroundingCells(cell.getPosition());
        }
        cell.setClosed(false);
        return  OPEN_CELL_RESULT.OPENED_OK;
    }

    private void openSurroundingCells(Position position) {
        // TODO
        throw new RuntimeException ("Not Implemented");
    }

    private void openAllCells() {
        // TODO
        throw new RuntimeException ("Not Implemented");
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

    public String toAsciiArt() {
        StringBuffer str = new StringBuffer();

        for (Cell cell: cells.values()){
            String art;
            if (!cell.isClosed()) {
                if (!cell.isMine()) {
                    art = cell.getAdjacentMines() == 0 ? "." : Integer.toString(cell.getAdjacentMines());
                } else {
                    art = "@"; // this is a bomb
                }

            } else {
                art = "X";
            }
            str.append(art + " ");
            if (cell.getPosition().getColumn() == nColumns - 1) {
                str.append("\n");
            }
        }

//        for (int row = 0; row<nRows; row++){
//            for (int col = 0; col <nColumns; col++){
//                Cell cell = cells.get(new Position(row, col));
//                String art;
//                if (!cell.isClosed()) {
//                    art = Integer.toString(cell.getAdjacentMines());
//                } else {
//                    art = "X";
//                }
//                str.append(art+ " ");
//            }
//            str.append("\n");
//        }
        return str.toString();
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

    private static Set<Position> getRandomizedMinePositions(int nRows, int nColumns, int nMines) {
        Set<Position> randomPositions = new HashSet<Position>();
        for (int i = 0; i<nMines; i++){
            int randomRow = ThreadLocalRandom.current().nextInt(0, nRows);
            int randomCol = ThreadLocalRandom.current().nextInt(0, nColumns);
            Position randomPosition = new Position(randomRow, randomCol);
            if (randomPositions.contains(randomPosition)){
                i--;
            } else {
                randomPositions.add(randomPosition);
            }
        }
        return randomPositions;
    }
}
