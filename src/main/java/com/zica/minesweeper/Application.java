package com.zica.minesweeper;

import com.zica.minesweeper.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Console;

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

        Console console = System.console();

        while (true){

            String board = game.getBoard().toAsciiArt();
            console.printf("%s\n", board);

            String command = console.readLine("command: [open <position>|quit]");

        }
    }
}
