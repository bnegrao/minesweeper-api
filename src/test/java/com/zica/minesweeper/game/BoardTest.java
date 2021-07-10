package com.zica.minesweeper.game;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.zica.minesweeper.game.OpenCellResult.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    void testOneCellAndOneMine() {
        Board board = new Board(1, 2, 1);

        Cell[][] cells = board.getCells2D();

        assertEquals(1, cells.length);
        assertEquals(2, cells[0].length);

        Cell unarmedCell = null;
        for (Cell cell: cells[0]){
            if (!cell.isMine()){
                unarmedCell = cell;
                break;
            }
        }

        assertTrue(unarmedCell != null);
        assertEquals(1, unarmedCell.getAdjacentMines());
    }

    @Test
    void testThreeMinesAndOneUnarmedCell(){
        Board board = new Board(2, 2, 3);

        Cell[][] cells = board.getCells2D();

        assertEquals(2, cells.length);
        assertEquals(2, cells[0].length);
        assertEquals(2, cells[1].length);

        Cell unarmedCell = null;
        for (Cell[] cell_row: cells){
            for (Cell cell: cell_row) {
                if (!cell.isMine()){
                    unarmedCell = cell;
                    break;
                }
            }
            if (unarmedCell != null){
                break;
            }
        }

        assertTrue(unarmedCell != null);
        assertEquals(3, unarmedCell.getAdjacentMines());
    }

    @Test
    void testOpenCellResultInvalidPosition() {
        Board board = new Board(3, 3, 1);

        OpenCellResult result = board.openCellAt(new Position(3,3));
        assertEquals(INVALID_POSITION, result);

        result = board.openCellAt(new Position(0,-1));
        assertEquals(INVALID_POSITION, result);
    }

    @Test
    void testOpenCellResultBoardComplete(){
        Board board = new Board(100, 100, 0);
        OpenCellResult result = board.openCellAt(new Position(0,0));
        assertEquals(BOARD_COMPLETE, result);

        board = new Board(100, 100, 0);
        result = board.openCellAt(new Position(99,99));
        assertEquals(BOARD_COMPLETE, result);

        board = new Board(100, 100, 0);
        result = board.openCellAt(new Position(50,50));
        assertEquals(BOARD_COMPLETE, result);

        board = new Board(2, 2, 3);
        Position position = null;
        for (Cell cell: board.getCellsFlat()) {
            if (!cell.isMine()){
                position = cell.getPosition();
                break;
            }
        }
        result = board.openCellAt(position);
        System.out.println(board.toAsciiArt());
        assertEquals(BOARD_COMPLETE, result);
    }

    @Test
    void testOpenCellResultNoChange(){
        Board board = new Board(100, 100, 30);
        Position position = null;
        for (Cell cell: board.getCellsFlat()) {
            if (!cell.isMine()){
                position = cell.getPosition();
                break;
            }
        }

        board.openCellAt(position);
        OpenCellResult result = board.openCellAt(position);
        assertEquals(NO_CHANGE, result);
    }

    @Test
    void testOpenCellResultIsAMine(){
        Board board = new Board(50, 50, 10);
        Position position = null;
        for (Cell cell: board.getCellsFlat()) {
            if (cell.isMine()){
                position = cell.getPosition();
                break;
            }
        }
        OpenCellResult result = board.openCellAt(position);
        assertEquals(IS_A_MINE, result);
    }

    /**
     * Creating a board of 2x3 with fixed mines in the top row:
     * X X
     * X X
     * X X
     *
     * Opening cell 2,1 (bottom-right cell) should result in:
     * @ @ (the cells with mines are opened)
     * 2 2 (the cells in the middle are opened and counting 2 adjacent mines)
     * . . (the cells in the bottom are opened and counting 0 adjacent mines)
     */
    @Test
    void testingBoardWithMinesInFixedPositions1() {
        Set<Position> minePositions = new HashSet<>();
        minePositions.add(new Position(0,0));
        minePositions.add(new Position(0,1));
        Board board = new Board(3, 2, minePositions);

        OpenCellResult result = board.openCellAt(new Position(2,1));
        assertEquals(BOARD_COMPLETE, result);
        Cell[][] cells2D = board.getCells2D();
        assertTrue(cells2D[0][0].isOpened());
        assertTrue(cells2D[0][1].isOpened());
        assertTrue(cells2D[1][0].isOpened());
        assertTrue(cells2D[1][0].getAdjacentMines() == 2);
        assertTrue(cells2D[1][1].isOpened());
        assertTrue(cells2D[1][1].getAdjacentMines() == 2);
        assertTrue(cells2D[2][0].isOpened());
        assertTrue(cells2D[2][0].getAdjacentMines() == 0);
        assertTrue(cells2D[2][1].isOpened());
        assertTrue(cells2D[2][1].getAdjacentMines() == 0);
    }

    /**
     * Creating a board 5x3 like this:
     * 1 @ @
     * 1 3 3
     * . 1 @
     * . 2 2
     * . 1 @
     *
     * Opening the cell 0,0 should result OPENED_OK and no other adjacent cell should be opened
     * 1 X X
     * X X X
     * X X X
     * X X X
     * X X X
     */
    @Test
    void testingBoardWithMinesInFixedPositions2() {
        Set<Position> minePositions = new HashSet<>();
        minePositions.add(new Position(0,1));
        minePositions.add(new Position(0,2));
        minePositions.add(new Position(2,2));
        minePositions.add(new Position(4,2));
        Board board = new Board(5, 3, minePositions);
        assertEquals(11, board.getUnarmedClosedCellsCounter());

        OpenCellResult result = board.openCellAt(new Position(0,0));
        assertEquals(10, board.getUnarmedClosedCellsCounter());

        Cell[][] cells2D = board.getCells2D();

        assertTrue(cells2D[0][0].isOpened());
        assertTrue(cells2D[0][0].getAdjacentMines() == 1);
    }

    /**
     * Creating a board 5x3 like this:
     *
     * 1 @ @
     * 1 3 3
     * . 1 @
     * . 2 2
     * . 1 @
     *
     * Opening the cell 4,0 should result OPENED_OK and other adjacent cells should be opened too,
     * resulting in this scenario:
     *
     * X X X
     * 1 3 X
     * . 1 X
     * . 2 X
     * . 1 X
     */
    @Test
    void testingBoardWithMinesInFixedPositions3() {
        Set<Position> minePositions = new HashSet<>();
        minePositions.add(new Position(0,1));
        minePositions.add(new Position(0,2));
        minePositions.add(new Position(2,2));
        minePositions.add(new Position(4,2));
        Board board = new Board(5, 3, minePositions);
        assertEquals(11, board.getUnarmedClosedCellsCounter());

        OpenCellResult result = board.openCellAt(4,0);
        assertEquals(3, board.getUnarmedClosedCellsCounter());
        Cell[][] cells2D = board.getCells2D();
        // row 0
        assertTrue(cells2D[0][0].isClosed());
        assertTrue(cells2D[0][1].isClosed());
        assertTrue(cells2D[0][2].isClosed());
        // row 1
        assertTrue(cells2D[1][0].isOpened() && cells2D[1][0].getAdjacentMines() == 1);
        assertTrue(cells2D[1][1].isOpened() && cells2D[1][1].getAdjacentMines() == 3);
        assertTrue(cells2D[1][2].isClosed());
        // row 2
        assertTrue(cells2D[2][0].isOpened() && cells2D[2][0].getAdjacentMines() == 0);
        assertTrue(cells2D[2][1].isOpened() && cells2D[2][1].getAdjacentMines() == 1);
        assertTrue(cells2D[2][2].isClosed());
        // row 3
        assertTrue(cells2D[3][0].isOpened() && cells2D[3][0].getAdjacentMines() == 0);
        assertTrue(cells2D[3][1].isOpened() && cells2D[3][1].getAdjacentMines() == 2);
        assertTrue(cells2D[3][2].isClosed());
        // row 4
        assertTrue(cells2D[4][0].isOpened() && cells2D[4][0].getAdjacentMines() == 0);
        assertTrue(cells2D[4][1].isOpened() && cells2D[4][1].getAdjacentMines() == 1);
        assertTrue(cells2D[4][2].isClosed());
    }

    /**
     * Creating a board 5x3 like this:
     *
     * 1 @ @
     * 1 3 3
     * . 1 @
     * . 2 2
     * . 1 @
     *
     * Opening the cells (4,0), then (3,2), then (1,2), then (0,0) will result in BOARD_COMPLETE
     *
     * X X X       X X X       X X X       1 @ @
     * 1 3 X       1 3 X       1 3 3       1 3 3
     * . 1 X  ==>  . 1 X  ==>  . 1 X  ==>  . 1 @
     * . 2 X       . 2 2       . 2 2       . 2 2
     * . 1 X       . 1 X       . 1 X       . 1 @
     */
    @Test
    void testingBoardWithMinesInFixedPositions4() {
        Set<Position> minePositions = new HashSet<>();
        minePositions.add(new Position(0,1));
        minePositions.add(new Position(0,2));
        minePositions.add(new Position(2,2));
        minePositions.add(new Position(4,2));
        Board board = new Board(5, 3, minePositions);
        assertEquals(11, board.getUnarmedClosedCellsCounter());

        assertEquals(OPENED_OK, board.openCellAt(4,0));
        assertEquals(OPENED_OK, board.openCellAt(3,2));
        assertEquals(OPENED_OK, board.openCellAt(1,2));
        assertEquals(BOARD_COMPLETE, board.openCellAt(0,0));
    }

}
