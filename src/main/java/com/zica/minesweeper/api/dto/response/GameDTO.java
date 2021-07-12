package com.zica.minesweeper.api.dto.response;

import io.swagger.annotations.ApiModel;

import java.util.Date;

@ApiModel
public class GameDTO {
    private String id;
    private CellDTO[][] cells;
    private String playerEmail;
    private Date startDate;
    private boolean isGameOver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CellDTO[][] getCells() {
        return cells;
    }

    public void setCells(CellDTO[][] cells) {
        this.cells = cells;
    }

    public String getPlayerEmail() {
        return playerEmail;
    }

    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }
}
