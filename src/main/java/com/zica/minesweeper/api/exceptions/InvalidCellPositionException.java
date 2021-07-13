package com.zica.minesweeper.api.exceptions;

/**
 * Exception to indicate that a method tried to update a Cell in an invalid position
 */
public class InvalidCellPositionException extends Exception{
    public InvalidCellPositionException(int row, int column) {
        super("Cell in row="+row+", column="+column+" does not exist");
    }
}
