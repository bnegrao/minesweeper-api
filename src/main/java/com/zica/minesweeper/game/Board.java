package com.zica.minesweeper.game;

import java.util.Set;
import java.util.TreeMap;

public class Board {
    private TreeMap<Position, Cell> cells;

    public Board (int nRows, int nColumns, int nMines) {
        cells = populateBoard(nRows, nColumns, nMines);
    }

    /**
     * Opens the Cell at the given Position, perform extra logic that can open other cells,
     * then returns a Cell[][]
     *
     * @param position
     * @return
     */
    public Cell[][] openCell(Position position) {
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
