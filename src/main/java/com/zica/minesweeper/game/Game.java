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
    private boolean gameIsOver;

    public Game (String playerEmail, int nRows, int nColumns, int nMines) {
        this.playerEmail = playerEmail;
        this.board = new Board(nRows, nColumns, nMines);
        this.startDate = new Date();
    }

    @PersistenceConstructor
    Game (String id, Board board, String playerEmail, Date startDate, boolean gameIsOver){
        this.id = id;
        this.board = board;
        this.playerEmail = playerEmail;
        this.startDate = startDate;
        this.gameIsOver = gameIsOver;
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

    public ToggleFlagResult toggleFlagAt(int row, int column, Cell.Flags flag) throws GameIsOverException {
        if (gameIsOver){
            throw new GameIsOverException();
        }
        return board.toggleFlagAt(new Position(row, column), flag);
    }

    public String getBoardAsAsciiArt(){
        return board.toAsciiArt();
    }

    public String getId(){
        return id;
    }
}
