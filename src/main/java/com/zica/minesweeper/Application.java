package com.zica.minesweeper;

import com.zica.minesweeper.game.Board;
import com.zica.minesweeper.game.Game;
import com.zica.minesweeper.game.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "bnegrao@gmail.com";
        int nRows = 10;
        int nColumns = 10;
        int nMines = 10;

        Game game = new Game(email, nRows, nColumns, nMines);

        Scanner scanner = new Scanner(System.in);

        String validCommands = "[open <position>|quit]";

        while (true){
            String board = game.getBoard().toAsciiArt();
            System.out.println(board);

            System.out.print("Enter command " + validCommands + ": ");
            String command = scanner.nextLine();
            if (command.equals("quit")){
                System.exit(0);
            } else if (command.startsWith("open ")) {
            int row = Integer.parseInt(command.split(" ")[1].split(",")[0]);
            int column = Integer.parseInt(command.split(" ")[1].split(",")[1]);
            Board.OPEN_CELL_RESULT result = game.getBoard().openCell(new Position(row, column));
            if (result == Board.OPEN_CELL_RESULT.NO_CHANGE){
                System.out.println("That cell was opened already. nothing changed.");
            }
            } else {
                System.out.println("Command " + command + " is invalid. Valid commands are: " + validCommands);
            }
        }
    }
}
