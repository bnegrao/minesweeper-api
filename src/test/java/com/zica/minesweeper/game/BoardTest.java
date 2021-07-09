package com.zica.minesweeper.game;

import org.junit.jupiter.api.Test;

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

        assertTrue(unarmedCell instanceof Cell);
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

        assertTrue(unarmedCell instanceof Cell);
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
    }

    @Test
    void testOpenCellResultNoChange(){
        Board board = new Board(100, 100, 30);
        Position position = null;
        for (Cell cell: board.getCellsFlat()) {
            if (cell.isMine() == false){
                position = cell.getPosition();
                break;
            }
        }

        OpenCellResult result = board.openCellAt(position);
        assertEquals(OPENED_OK, result);
        result = board.openCellAt(position);
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

}
