package com.zica.minesweeper.game;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    private final int nRows;
    private final int nColumns;
    private final int nMines;
    private int unarmedClosedCellsCounter;
    private final TreeMap<Position, Cell> cellTree;

    public Board (int nRows, int nColumns, int nMines) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nMines = nMines;
        this.cellTree = populateBoard(getRandomizedMinePositions(nRows, nColumns, nMines));
        this.unarmedClosedCellsCounter = ( nRows * nColumns ) - nMines;
    }

    /**
     * Creates a Board with mines at fixed places (i.e., not random)
     * what enables the creation of precise unit tests.
     * This constructor isn't public because it is not meant to be used for
     * the creating of real games.
     */
    Board (int nRows, int nColumns, Set<Position> minePositions) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nMines = minePositions.size();
        this.cellTree = populateBoard(minePositions);
        this.unarmedClosedCellsCounter = ( nRows * nColumns ) - nMines;
    }

    @PersistenceConstructor
    protected Board (int nRows, int nColumns, int nMines, int unarmedClosedCellsCounter, TreeMap<Position, Cell> cellTree){
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nMines = nMines;
        this.unarmedClosedCellsCounter = unarmedClosedCellsCounter;
        this.cellTree = cellTree;
    }

    public int getnRows() {
        return nRows;
    }

    public int getnColumns() {
        return nColumns;
    }

    public int getnMines() {
        return nMines;
    }

    /**
     * @return The Cells in this Board in proper order.
     */
    public Cell[][] getCells2D() {
        Cell[][] cellsArr = new Cell[nRows][nColumns];
        for (int row = 0; row < nRows; row++) {
            for (int column = 0; column < nColumns; column++) {
                cellsArr[row][column] = cellTree.get(new Position(row, column));
            }
        }
        return cellsArr;
    }

    public Collection<Cell> getCellsFlat(){
        return cellTree.values();
    }

    public ToggleFlagResult toggleFlagAt(Position position, Cell.Flags flag) {
        Cell cell = cellTree.get(position);
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
     * convenience method that creates a Position instance and invokes openCellAt(Position)
     */
    public OpenCellResult openCellAt(int row, int column) {
        return this.openCellAt(new Position(row, column));
    }

    /**
     * Opens the Cell at the given Position, performs extra logic that can open other cells,
     * then returns a OPEN_CELL_RESULT to indicate what happened.
     *
     * Valid ranges for Position coordinates are from 0 to nRows-1 and 0 to nColumns-1
     *
     * @param position of the cell that should be opened
     * @return OPEN_CELL_RESULT
     */
    public OpenCellResult openCellAt(Position position) {
        Cell cell = cellTree.get(position);
        if (cell == null){
            return OpenCellResult.INVALID_POSITION;
        }
        if (!cell.isClosed()) {
            return OpenCellResult.NO_CHANGE;
        } else if (cell.isMine()) {
            openAllCells();
            return OpenCellResult.IS_A_MINE;
        } else {
            if (cell.getAdjacentMines() == 0) {
                openCellsWithNoAdjacentMines(cell);
            } else {
                cell.setClosed(false);
                unarmedClosedCellsCounter--;
            }
            if (unarmedClosedCellsCounter == 0){
                openAllCells();
                return OpenCellResult.BOARD_COMPLETE;
            }
            return  OpenCellResult.OPENED_OK;
        }
    }

    /**
     * @return Counter of Cells in this Board that are closed and "unarmed", i.e., don't hold a mine.
     * The counter is updated during each openCellAt() invocation.
     */
    public int getUnarmedClosedCellsCounter(){
        return unarmedClosedCellsCounter;
    }

    private void openCellsWithNoAdjacentMines(Cell startCell){
        TreeSet<Position> stack = new TreeSet<>();
        stack.add(startCell.getPosition());
        while (!stack.isEmpty()){
            Cell cell = this.cellTree.get(stack.pollFirst());
            if (cell.isClosed()){
                cell.open();
                unarmedClosedCellsCounter--;
                if (cell.getAdjacentMines()==0){
                    for (Cell adjCell: getAdjacentClosedUnarmedCells(cell.getPosition())){
                        stack.add(adjCell.getPosition());
                    }
                }
            } else {
                throw new RuntimeException("Bug! Cell at position "+ cell.getPosition() + " should be closed");
            }
        }
    }

    private List<Cell> getAdjacentClosedUnarmedCells(Position position){
        List<Cell> adjacentClosedCells = new LinkedList<>();
        for (Position adjPosition: getAdjacentPositions(position)){
            Cell adjCell = cellTree.get(adjPosition);
            if (adjCell.isClosed() && !adjCell.isMine()){
                adjacentClosedCells.add(adjCell);
            }
        }
        return adjacentClosedCells;
    }

    private void openAllCells() {
        for (Cell cell: cellTree.values()){
            cell.open();
        }
    }

    private TreeMap<Position, Cell> populateBoard(Set<Position> minePositions) {
        TreeMap<Position, Cell> treeMap = new TreeMap<>();

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

        for (Cell cell: cellTree.values()){
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
        List<Position> adjacentPositions = new LinkedList<>();

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
