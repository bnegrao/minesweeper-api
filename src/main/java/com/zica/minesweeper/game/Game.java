package com.zica.minesweeper.game;

import java.util.Date;
import java.util.Scanner;

public class Game {
    private Board board;
    private String playerEmail;
    private Date startDate;
    private boolean gameIsOver;

    public Game (String playerEmail, int nRows, int nColumns, int nMines) {
        this.playerEmail = playerEmail;
        this.board = new Board(nRows, nColumns, nMines);
        this.startDate = new Date();
    }

    public OpenCellResult openCellAt(int row, int column) throws GameIsOverException {
        if (gameIsOver){
            throw new GameIsOverException();
        }
        OpenCellResult result = board.openCellAt(new Position(row, column));
        if (result == OpenCellResult.BOARD_COMPLETE || result == OpenCellResult.IS_A_MINE){
            gameIsOver = true;
        }
        return result;
    }

    public ToggleFlagResult toggleFlagAt(int row, int column, Cell.Flags flag) {
        return board.toggleFlagAt(new Position(row, column), flag);
    }

    public String getBoardAsAsciiArt(){
        return board.toAsciiArt();
    }
}
