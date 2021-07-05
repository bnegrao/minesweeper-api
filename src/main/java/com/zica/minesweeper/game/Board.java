package com.zica.minesweeper.game;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    private int nRows;
    private int nColumns;
    private int nMines;
    private int unarmedClosedCellsCounter;
    private TreeMap<Position, Cell> cells;

    public Board (int nRows, int nColumns, int nMines) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nMines = nMines;
        this.cells = populateBoard();
        this.unarmedClosedCellsCounter = ( nRows * nColumns ) - nMines;
    }

    /**
     * @return The Cells in this Board in proper order.
     */
    public Cell[][] getCells () {
        // TODO
        throw new RuntimeException("not implemented");
    }

    public ToggleFlagResult toggleFlagAt(Position position, Cell.Flags flag) {
        Cell cell = cells.get(position);
        if (cell == null){
            return ToggleFlagResult.INVALID_POSITION;
        }
        if (!cell.isClosed()) {
            return ToggleFlagResult.NO_CHANGE;
        }
        if (cell.getFlag() == flag) {
            cell.setFlag(null);
            return ToggleFlagResult.UNSET;
        } else {
            cell.setFlag(flag);
            return ToggleFlagResult.SET;
        }
    }


    /**
     * Opens the Cell at the given Position, performs extra logic that can open other cells,
     * then returns a OPEN_CELL_RESULT to indicate what happened.
     *
     * @param position of the cell that should be opened
     * @return OPEN_CELL_RESULT
     */
    public OpenCellResult openCellAt(Position position) {
        Cell cell = cells.get(position);
        if (cell == null){
            return OpenCellResult.INVALID_POSITION;
        }
        if (!cell.isClosed()) {
            return OpenCellResult.NO_CHANGE;
        } else if (cell.isMine()) {
            openAllCells();
            return OpenCellResult.IS_A_MINE;
        } else {
            cell.setClosed(false);
            unarmedClosedCellsCounter--;
            if (cell.getAdjacentMines() == 0) {
                openUnarmedAdjacentCells(cell.getPosition());
            }
            if (unarmedClosedCellsCounter == 0){
                return OpenCellResult.BOARD_COMPLETE;
            }
            System.out.println(unarmedClosedCellsCounter);
            return  OpenCellResult.OPENED_OK;
        }
    }

    private void openUnarmedAdjacentCells(Position position) {
        List<Position> positionsOfCellWithNoSurroundingMines = new LinkedList<>();
        for (Cell adjCell: getAdjacentClosedCells(position)){
            if (!adjCell.isClosed()){
                throw new RuntimeException("BUG! Cell at position " + adjCell.getPosition() + " should be closed!");
            }
            adjCell.setClosed(false);
            unarmedClosedCellsCounter--;
            if (adjCell.getAdjacentMines()==0){
                positionsOfCellWithNoSurroundingMines.add(adjCell.getPosition());
            }
        }
        for (Position pos: positionsOfCellWithNoSurroundingMines){
            openUnarmedAdjacentCells(pos);
        }
    }

    private List<Cell> getAdjacentClosedCells(Position position){
        List<Cell> adjacentClosedCells = new LinkedList<>();
        for (Position adjPosition: getAdjacentPositions(position)){
            Cell adjCell = cells.get(adjPosition);
            if (adjCell.isClosed()){
                adjacentClosedCells.add(adjCell);
            }
        }
        return adjacentClosedCells;
    }

    private void openAllCells() {
        for (Cell cell: cells.values()){
            cell.setClosed(false);
        }
    }

    private TreeMap<Position, Cell> populateBoard() {
        TreeMap<Position, Cell> treeMap = new TreeMap<>();

        Set<Position> minePositions = getRandomizedMinePositions(nRows, nColumns, nMines);

        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                Position position = new Position(row, column);
                boolean isMine = minePositions.contains(position);
                int adjacentMinesCounter = countAdjacentMines(position, minePositions);
                Cell cell = new Cell(row, column, isMine, adjacentMinesCounter);
                treeMap.put(position, cell);
            }
        }

        return treeMap;
    }

    public String toAsciiArt() {
        StringBuilder str = new StringBuilder();

        for (Cell cell: cells.values()){
            String art;
            if (!cell.isClosed()) {
                if (!cell.isMine()) {
                    art = cell.getAdjacentMines() == 0 ? "." : Integer.toString(cell.getAdjacentMines());
                } else {
                    art = "@"; // this is a bomb
                }
            } else {
                if (cell.getFlag() == null) {
                    art = "X";
                } else {
                    if (cell.getFlag() == Cell.Flags.MINE) {
                        art = "@";
                    } else {
                        art = "?";
                    }
                }
            }
            str.append(art);
            str.append(" ");
            if (cell.getPosition().getColumn() == nColumns - 1) {
                str.append("\n");
            }
        }
        return str.toString();
    }

    private List<Position> getAdjacentPositions(Position position) {
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

        Position[] possibleAdjacentPositions = new Position[]{topLeft, topCenter, topRight, left, right, bottomLeft, bottomCenter, bottomRight};
        List<Position> adjacentPositions = new ArrayList<>();

        for (Position possiblePosition: possibleAdjacentPositions) {
            int adjRow = possiblePosition.getRow();
            int adjCol = possiblePosition.getColumn();
            if ( adjRow >= 0 && adjRow < this.nRows && adjCol >= 0 && adjCol < this.nColumns ) {
                adjacentPositions.add(new Position(adjRow, adjCol));
            }
        }
        return adjacentPositions;
    }


    private int countAdjacentMines(Position position, Set<Position> minePositions) {
        int counter = 0;
        for (Position adjacentPosition: getAdjacentPositions(position)) {
            if (minePositions.contains(adjacentPosition)) counter++;
        }
        return counter;
    }

    private static Set<Position> getRandomizedMinePositions(int nRows, int nColumns, int nMines) {
        Set<Position> randomPositions = new HashSet<>();
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
