package main

import (
	"errors"
	"fmt"
	"os"

	"github.com/bnegrao/minesweeper-api/client/minesweeper"
	"github.com/bnegrao/minesweeper-api/client/minesweeper/dtos"
	"github.com/bnegrao/minesweeper-api/client/runner/ui"
)

var msClient = minesweeper.New("http://localhost:8080/game")

func main() {
	ui.PrintGameTitle()

	for {
		var gameDTO *dtos.GameDTO = nil
		var err error
		var startMenuOption = ui.StartMenu()

		switch startMenuOption {
		case ui.StartMenuOptions.START_NEW_GAME:
			var uiResponse *ui.StartGameUIResponse
			uiResponse, err = ui.StartGame()
			if err != nil {
				break
			}
			gameDTO, err = startGame(uiResponse)
		case ui.StartMenuOptions.RESUME_GAME:
			var playerEmail string
			playerEmail, err = ui.ResumeGame()
			if err != nil {
				break
			}
			gameDTO, err = resumeLastGame(playerEmail)
		case ui.StartMenuOptions.QUIT:
			os.Exit(0)
		default:
			panic(fmt.Sprintf("I can't handle option '%s'", startMenuOption))
		}

		if err != nil {
			fmt.Printf("Error: %s\n", err)
			continue
		}

		for gameDTO.GameStatus == "RUNNING" {
			ui.PrintBoard(gameDTO.Cells)
			var err error
			option := ui.OpenCellMenu()
			ui.PrintBoard(gameDTO.Cells)
			switch option {
			case ui.OpenCellMenuOptions.OPEN_CELL:
				var uiResponse *ui.GetCellPositionResponse
				uiResponse, err = ui.GetCellPosition(len(gameDTO.Cells), len(gameDTO.Cells[0]))
				if err != nil {
					break
				}
				gameDTO, err = openCell(gameDTO.Id, uiResponse.Row, uiResponse.Column)
			case ui.OpenCellMenuOptions.SET_QUESTION_MARK:
				panic("Not Implemented")

			case ui.OpenCellMenuOptions.SET_MINE_MARK:
				panic("Not Implemented")

			case ui.OpenCellMenuOptions.SAVE_AND_QUIT:
				panic("Not Implemented")

			case ui.OpenCellMenuOptions.QUIT_WITHOUT_SAVING:
				panic("Not Implemented")
			}

			if err != nil {
				fmt.Printf("Error: %s\n", err)
				continue
			}
		}

		if gameDTO.GameStatus == "GAME_WON" {
			ui.GameWon(gameDTO.Cells)
		} else {
			ui.GameLost(gameDTO.Cells)
		}

	}
}

func openCell(gameId string, row int, column int) (*dtos.GameDTO, error) {
	// subtractin 1 from each position coordinate because the
	// backend API handles positions starting from 0.
	return msClient.OpenCellAt(gameId, row-1, column-1)
}

func resumeLastGame(playerEmail string) (*dtos.GameDTO, error) {
	return nil, errors.New("Not implemented")
}

func startGame(options *ui.StartGameUIResponse) (*dtos.GameDTO, error) {

	gameDTO, err := msClient.StartGame(dtos.StartGameDTO{
		PlayerEmail: options.PlayerEmail,
		Rows:        options.NRows,
		Columns:     options.NCols,
		Mines:       options.NMines,
	})

	if err != nil {
		return nil, err
	}

	return gameDTO, nil
}
