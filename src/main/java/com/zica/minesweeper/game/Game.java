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
    private GameStatus gameStatus;

    public Game (String playerEmail, int nRows, int nColumns, int nMines) {
        this.playerEmail = playerEmail;
        this.board = new Board(nRows, nColumns, nMines);
        this.startDate = new Date();
        this.gameStatus = GameStatus.RUNNING;
    }

    @PersistenceConstructor
    protected Game (String id, Board board, String playerEmail, Date startDate, Game.GameStatus gameStatus){
        this.id = id;
        this.board = board;
        this.playerEmail = playerEmail;
        this.startDate = startDate;
        this.gameStatus = GameStatus.RUNNING;
    }

    public String getBoardAsAsciiArt(){
        return board.toAsciiArt();
    }

    public Cell[][] getCells() {
        return board.getCells2D();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getId(){
        return id;
    }

    public String getPlayerEmail() {
        return this.playerEmail;
    }

    public Date getStartDate(){
        return this.startDate;
    }

    /**
     * Opens the Cell at the given position, returns the OpenCellResult and potentially
     * updates the value of 'gameStatus' property depending on the OpenCellResult returned.
     * @throws GameIsOverException if the game is not in GameStatus.RUNNING
     */
    public OpenCellResult openCellAt(int row, int column) throws GameIsOverException {
        if (gameStatus != GameStatus.RUNNING){
            throw new GameIsOverException();
        }
        OpenCellResult result = board.openCellAt(new Position(row, column));
        if (result == OpenCellResult.BOARD_COMPLETE) {
            gameStatus = GameStatus.GAME_WON;
        } else if (result == OpenCellResult.IS_A_MINE){
            gameStatus = GameStatus.GAME_LOST;
        }
        return result;
    }

    public ToggleFlagResult toggleFlagAt(int row, int column, Cell.Flags flag) throws GameIsOverException {
        if (gameStatus != GameStatus.RUNNING){
            throw new GameIsOverException();
        }
        return board.toggleFlagAt(new Position(row, column), flag);
    }

    public enum GameStatus {
        RUNNING, GAME_WON, GAME_LOST
    }
}
