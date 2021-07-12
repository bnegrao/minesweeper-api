package com.zica.minesweeper.game;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "game")
public class Game {
    @Id
    private String id;
    private Board board;
    private String playerEmail;
    private Date startDate;
    private boolean isGameOver;

    public Game (String playerEmail, int nRows, int nColumns, int nMines) {
        this.playerEmail = playerEmail;
        this.board = new Board(nRows, nColumns, nMines);
        this.startDate = new Date();
    }

    @PersistenceConstructor
    protected Game (String id, Board board, String playerEmail, Date startDate, boolean isGameOver){
        this.id = id;
        this.board = board;
        this.playerEmail = playerEmail;
        this.startDate = startDate;
        this.isGameOver = isGameOver;
    }

    public OpenCellResult openCellAt(int row, int column) throws GameIsOverException {
        if (isGameOver){
            throw new GameIsOverException();
        }
        OpenCellResult result = board.openCellAt(new Position(row, column));
        if (result == OpenCellResult.BOARD_COMPLETE || result == OpenCellResult.IS_A_MINE){
            isGameOver = true;
        }
        return result;
    }

    public ToggleFlagResult toggleFlagAt(int row, int column, Cell.Flags flag) throws GameIsOverException {
        if (isGameOver){
            throw new GameIsOverException();
        }
        return board.toggleFlagAt(new Position(row, column), flag);
    }

    public Cell[][] getCells() {
        return board.getCells2D();
    }

    public String getBoardAsAsciiArt(){
        return board.toAsciiArt();
    }

    public String getId(){
        return id;
    }

    public boolean isGameOver(){
        return this.isGameOver;
    }

    public String getPlayerEmail() {
        return this.playerEmail;
    }

    public Date getStartDate(){
        return this.startDate;
    }

}
