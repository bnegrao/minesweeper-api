package main

import (
	"errors"
	"fmt"

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
			var uiStartGameResponse *ui.StartGameUIResponse
			uiStartGameResponse, err = ui.StartGame()
			if err != nil {
				continue
			}
			gameDTO, err = startGame(uiStartGameResponse)
		case ui.StartMenuOptions.RESUME_GAME:
			var playerEmail string
			playerEmail, err = ui.ResumeGame()
			if err != nil {
				continue
			}
			gameDTO, err = resumeLastGame(playerEmail)
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

			switch option {
			case ui.OpenCellMenuOptions.OPEN_CELL:
				var uiGetCellPositionResponde *ui.GetCellPositionResponse
				uiGetCellPositionResponde, err = ui.GetCellPosition(len(gameDTO.Cells), len(gameDTO.Cells[0]))
				if err != nil {
					gameDTO, err = openCell(uiGetCellPositionResponde)
				}
			case ui.OpenCellMenuOptions.SET_QUESTION_MARK:
				panic("Not Implemented")

			case ui.OpenCellMenuOptions.SET_MINE_MARK:
				panic("Not Implemented")

			case ui.OpenCellMenuOptions.SAVE_AND_QUIT:
				panic("Not Implemented")

			case ui.OpenCellMenuOptions.QUIT_WHITHOUT_SAVING:
				panic("Not Implemented")
			}

			if err != nil {
				fmt.Printf("Error: %s\n", err)
				continue
			}
		}
	}
}

func openCell(options *ui.GetCellPositionResponse) (*dtos.GameDTO, error) {
	return nil, errors.New("Not implemented")
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
