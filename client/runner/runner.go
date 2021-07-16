package main

import (
	"bufio"
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"

	"github.com/bnegrao/minesweeper-api/client/minesweeper"
	"github.com/bnegrao/minesweeper-api/client/minesweeper/dtos"
	"github.com/bnegrao/minesweeper-api/client/runner/messages"
)

var msClient = minesweeper.New("http://localhost:8080/game")

func main() {
	var OPTION_START_NEW_GAME = "1"
	var OPTION_RESUME_LAST_GAME = "2"

	fmt.Println(messages.GameTitle)

	for {
		var gameDTO *dtos.GameDTO = nil

		option, err := readInput(messages.StartMenu, OPTION_START_NEW_GAME)
		if err != nil {
			fmt.Printf("Error: %s\n", err)
			continue
		}

		switch option {
		case OPTION_START_NEW_GAME:
			gameDTO, err = startGameUIHandler()
		case OPTION_RESUME_LAST_GAME:
			gameDTO, err = resumeGameUIHandler()
		default:
			fmt.Println("Option ", option, " is invalid. Please try again.")
		}
		if err != nil {
			fmt.Printf("Error: %s\n", err)
			continue
		}

		for gameDTO.GameStatus == "RUNNING" {
			printBoard(gameDTO.Cells)

			op, err := readInput("asdf", "ddd")
			if err != nil {
				break
			}
			fmt.Println(op)
		}

		if err != nil {
			fmt.Printf("Error: %s\n", err)
			continue
		}

	}
}

func printBoard(cells [][]dtos.CellDTO) {
	// printing the columns header.
	var padding = "      " // space for 5 digits
	fmt.Print(padding)
	for i := 1; i <= len(cells); i++ {
		fmt.Print(i, " ")
	}
	fmt.Println()

	// printing the board
	for row := 0; row < len(cells); row++ {
		for col := 0; col < len(cells[0]); col++ {
			if col == 0 {
				fmt.Printf("%5d ", row+1)
			}
			cellDTO := cells[row][col]
			if cellDTO.Properties == nil {
				fmt.Print("X ")
			} else {
				if cellDTO.Properties.IsMine {
					fmt.Print("@ ")
				} else {
					if cellDTO.Properties.AdjacentMines > 0 {
						fmt.Print(cellDTO.Properties.AdjacentMines)
					} else {
						fmt.Print(". ")
					}
				}
			}
		}
		fmt.Println()
	}
}

func resumeGameUIHandler() (*dtos.GameDTO, error) {
	return nil, nil
}

func startGameUIHandler() (*dtos.GameDTO, error) {
	playerEmail, err := readInput(messages.EnterEmail, "asdf@asdf.com")
	if err != nil {
		return nil, err
	}
	nRows, err := readInput(messages.EnterNColumns, "9")
	if err != nil {
		return nil, err
	}
	nColumns, err := readInput(messages.EnterNColumns, "9")
	if err != nil {
		return nil, err
	}
	nMines, err := readInput(messages.EnterNMines, "10")
	if err != nil {
		return nil, err
	}

	gameDTO, err := msClient.StartGame(dtos.StartGameDTO{
		PlayerEmail: playerEmail,
		Rows:        parseInt(nRows),
		Columns:     parseInt(nColumns),
		Mines:       parseInt(nMines),
	})

	if err != nil {
		return nil, err
	}

	return gameDTO, nil
}

func readInput(message string, defaultValue string) (string, error) {
	fmt.Print(message)
	reader := bufio.NewReader(os.Stdin)
	input, err := reader.ReadString('\n')
	if err != nil {
		return "", nil
	}
	input = strings.Replace(input, lineSeparator(), "", 1)
	if input == "" {
		return defaultValue, nil
	}
	return input, nil
}

func lineSeparator() string {
	if runtime.GOOS == "windows" {
		return "\r\n"
	} else {
		return "\n"
	}
}

func parseInt(n string) int {
	intVar, err := strconv.Atoi(n)
	if err != nil {
		panic(err)
	}
	return intVar
}
