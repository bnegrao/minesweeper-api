package main

import (
	"fmt"
	"os"

	"github.com/bnegrao/minesweeper-api/client/minesweeper"
	"github.com/bnegrao/minesweeper-api/client/minesweeper/dtos"
	"github.com/bnegrao/minesweeper-api/client/runner/ui"
)

var awsServer = "http://minesweeperapi-env.eba-gpc4cmgm.us-west-2.elasticbeanstalk.com/game"

//var localServer = "http://localhost:8080/game"
var msClient = minesweeper.New(awsServer)

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
			gameDTO, err = msClient.ResumeLastSession(playerEmail)
			if err != nil {
				break
			}
			ui.PrintStartDate(gameDTO.StartDate)
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
			switch option {
			case ui.OpenCellMenuOptions.OPEN_CELL:
				ui.PrintBoard(gameDTO.Cells)
				var position *ui.GetCellPositionResponse
				position, err = ui.GetCellPosition(len(gameDTO.Cells), len(gameDTO.Cells[0]))
				if err != nil {
					break
				}
				gameDTO, err = msClient.OpenCellAt(gameDTO.Id, position.Row-1, position.Column-1)
			case ui.OpenCellMenuOptions.TOGGLE_QUESTION_MARK, ui.OpenCellMenuOptions.TOGGLE_MINE_MARK:
				ui.PrintBoard(gameDTO.Cells)
				var position *ui.GetCellPositionResponse
				position, err = ui.GetCellPosition(len(gameDTO.Cells), len(gameDTO.Cells[0]))
				if err != nil {
					break
				}
				var flag string
				if option == ui.OpenCellMenuOptions.TOGGLE_MINE_MARK {
					flag = "MINE"
				} else {
					flag = "QUESTION"
				}
				gameDTO, err = msClient.ToggleFlagAt(gameDTO.Id, position.Row-1, position.Column-1, flag)
			case ui.OpenCellMenuOptions.SAVE_AND_QUIT:
				os.Exit(0)
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
