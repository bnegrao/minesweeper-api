package ui

import (
	"bufio"
	"errors"
	"fmt"
	"os"
	"runtime"
	"strconv"
	"strings"

	"github.com/bnegrao/minesweeper-api/client/minesweeper/dtos"
	"github.com/bnegrao/minesweeper-api/client/runner/messages"
)

func PrintGameTitle() {
	fmt.Println(messages.GameTitle)
}

type GetCellPositionResponse struct {
	Row    int
	Column int
}

func GetCellPosition(nRows int, nColumns int) (*GetCellPositionResponse, error) {
	return nil, errors.New("Not implemented")
}

type StartGameUIResponse struct {
	PlayerEmail string
	NRows       int
	NCols       int
	NMines      int
}

type OrderStatusType string

var OrderStatus = struct {
	APPROVED         OrderStatusType
	APPROVAL_PENDING OrderStatusType
	REJECTED         OrderStatusType
	REVISION_PENDING OrderStatusType
}{
	APPROVED:         "approved",
	APPROVAL_PENDING: "approval pending",
	REJECTED:         "rejected",
	REVISION_PENDING: "revision pending",
}

type StartMenuOption string

var StartMenuOptions = struct {
	START_NEW_GAME StartMenuOption
	RESUME_GAME    StartMenuOption
}{
	START_NEW_GAME: "1",
	RESUME_GAME:    "2",
}

// returns one of the values in the var ui.StartMenuOptions
func StartMenu() StartMenuOption {
	for {
		option, err := ReadInput(messages.StartMenu, string(StartMenuOptions.START_NEW_GAME))
		if err != nil {
			fmt.Println(err)
			fmt.Println("Please try again.")
			continue
		}
		switch StartMenuOption(option) {
		case StartMenuOptions.START_NEW_GAME, StartMenuOptions.RESUME_GAME:
			return StartMenuOption(option)
		default:
			fmt.Printf("Option '%s' is invalid, please try again", option)
		}
	}
}

type OpenCellMenuOption string

var OpenCellMenuOptions = struct {
	OPEN_CELL            OpenCellMenuOption
	SET_QUESTION_MARK    OpenCellMenuOption
	SET_MINE_MARK        OpenCellMenuOption
	SAVE_AND_QUIT        OpenCellMenuOption
	QUIT_WHITHOUT_SAVING OpenCellMenuOption
}{
	OPEN_CELL:            "1",
	SET_QUESTION_MARK:    "2",
	SET_MINE_MARK:        "3",
	SAVE_AND_QUIT:        "4",
	QUIT_WHITHOUT_SAVING: "5",
}

// returns one of the values in the var ui.OpenCellMenuOptions
func OpenCellMenu() OpenCellMenuOption {
	for {
		option, err := ReadInput(messages.OpenCellMenu, string(OpenCellMenuOptions.OPEN_CELL))
		if err != nil {
			fmt.Println(err)
			fmt.Println("Please try again.")
			continue
		}
		switch OpenCellMenuOption(option) {
		case OpenCellMenuOptions.OPEN_CELL,
			OpenCellMenuOptions.SAVE_AND_QUIT, OpenCellMenuOptions.QUIT_WHITHOUT_SAVING,
			OpenCellMenuOptions.SET_MINE_MARK, OpenCellMenuOptions.SET_QUESTION_MARK:
			return OpenCellMenuOption(option)
		default:
			fmt.Printf("Option '%s' is invalid, please try again", option)
		}
	}
}

func StartGame() (*StartGameUIResponse, error) {
	playerEmail, err := ReadInput(messages.EnterEmail, "asdf@asdf.com")
	if err != nil {
		return nil, err
	}
	nRows, err := ReadInput(messages.EnterNColumns, "9")
	if err != nil {
		return nil, err
	}
	nColumns, err := ReadInput(messages.EnterNColumns, "9")
	if err != nil {
		return nil, err
	}
	nMines, err := ReadInput(messages.EnterNMines, "10")
	if err != nil {
		return nil, err
	}
	return &StartGameUIResponse{
		PlayerEmail: playerEmail,
		NRows:       parseInt(nRows),
		NCols:       parseInt(nColumns),
		NMines:      parseInt(nMines),
	}, nil
}

func PrintBoard(cells [][]dtos.CellDTO) {
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

func ResumeGame() (playerEmail string, err error) {
	return "", errors.New("Not implemented")
}

func ReadInput(message string, defaultValue string) (string, error) {
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
