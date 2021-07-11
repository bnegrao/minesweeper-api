package com.zica.minesweeper;

import com.zica.minesweeper.game.Cell;
import com.zica.minesweeper.game.Game;
import com.zica.minesweeper.game.OpenCellResult;
import com.zica.minesweeper.game.ToggleFlagResult;
import com.zica.minesweeper.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.zica.minesweeper.repository")
public class ConsoleGameApplication implements CommandLineRunner {

    @Autowired
    private GameRepository gameRepository;

    private static Logger LOG = LoggerFactory.getLogger(ConsoleGameApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsoleGameApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String email = "bnegrao@gmail.com";
        int nRows = 10;
        int nColumns = 10;
        int nMines = 10;

        Game game = new Game(email, nRows, nColumns, nMines);

        Scanner scanner = new Scanner(System.in);

        String validCommands = "[open <row,column>|mine <row,column>|question <row,column>|quit|restart|save|resume <gameid>]";

        while (true) {
            System.out.println(game.getBoardAsAsciiArt());

            System.out.print("Enter command " + validCommands + ": ");
            String command = scanner.nextLine();
            if (command.equals("quit")) {
                System.exit(0);
            } else if (command.startsWith("open ")) {
                int row = Integer.parseInt(command.split(" ")[1].split(",")[0]);
                int column = Integer.parseInt(command.split(" ")[1].split(",")[1]);
                OpenCellResult result = game.openCellAt(row, column);
                switch (result) {
                    case IS_A_MINE:
                        System.out.println("I'M SORRY, YOU LOST!");
                        break;
                    case BOARD_COMPLETE:
                        System.out.println("CONGRATULATIONS, YOU WON!!");
                        break;
                    case INVALID_POSITION:
                        System.out.println("Invalid position. Please try again");
                        break;
                    case NO_CHANGE:
                        System.out.println("That cell was opened already, nothing changed, please try to open a different cell");
                        break;
                    case OPENED_OK:
                        System.out.println("Good move.");
                        break;
                }
            } else if (command.startsWith("mine ") || command.startsWith("question ")) {
                Cell.Flags flag = command.startsWith("mine")? Cell.Flags.MINE : Cell.Flags.QUESTION;
                int row = Integer.parseInt(command.split(" ")[1].split(",")[0]);
                int column = Integer.parseInt(command.split(" ")[1].split(",")[1]);
                ToggleFlagResult result = game.toggleFlagAt(row, column, flag);
                switch (result) {
                    case INVALID_POSITION:
                        System.out.println("Invalid position. Please try again");
                        break;
                    case NO_CHANGE:
                        System.out.println("That cell was opened already, nothing changed, please try to open a different cell");
                        break;
                    case SET:
                        System.out.println("Flag set.");
                        break;
                    case UNSET:
                        System.out.println("Flag unset.");
                        break;
                }
            } else if (command.startsWith("restart")) {
                System.out.println("Starting a new game. Good luck!");
                game = new Game(email, nRows, nColumns, nMines);
            } else if (command.startsWith("save")) {
                game = gameRepository.save(game);
                System.out.println("Your game was saved with id " + game.getId() + ". Use that id to resume your game.");
            } else if (command.startsWith("resume")) {
                String gameId = command.split(" ")[1];
                System.out.println("Trying to restore game with id "+gameId+"...");
                Optional<Game> optionalGame = gameRepository.findById(gameId);
                if (optionalGame.isPresent()){
                    System.out.println("Success restoring your game, enjoy!");
                    game = optionalGame.get();
                } else {
                    System.out.println("Game with id " + gameId + " was not found.");
                }
            } else {
                System.out.println("Command " + command + " is invalid. Valid commands are: " + validCommands);
            }
        }
    }
}
