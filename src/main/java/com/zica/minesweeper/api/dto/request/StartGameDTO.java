package com.zica.minesweeper.api.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

public class StartGameDTO {

    @Email
    private String playerEmail;

    @Min(1)
    private int rows;

    @Min(1)
    private int columns;

    @Min(0)
    private int mines;

    public StartGameDTO(String playerEmail, int rows, int columns, int mines) {
        this.playerEmail = playerEmail;
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
    }

    public String getPlayerEmail() {
        return playerEmail;
    }

    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }
}
