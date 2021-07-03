package com.zica.minesweeper.game;

import java.util.Date;

public class Game {
    private Board board;
    private String playerEmail;
    private Date startDate;

    public Game (String playerEmail, int nRows, int nColumns, int nMines) {
        this.playerEmail = playerEmail;
        this.board = new Board(nRows, nColumns, nMines);
        this.startDate = new Date();
    }

    public Board getBoard(){
        return this.board;
    }

}
