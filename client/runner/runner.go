package main

import (
	"fmt"

	"github.com/bnegrao/minesweeper-api/client/minesweeper"
	"github.com/bnegrao/minesweeper-api/client/minesweeper/dtos"
)

func main() {

	client := minesweeper.New("http://localhost:8080/game")

	gameDTO, err := client.StartGame(dtos.StartGameDTO{
		PlayerEmail: "bnegrao@gmail.com",
		Rows:        5,
		Columns:     5,
		Mines:       4,
	})

	if err != nil {
		panic(err)
	}

	fmt.Printf("%+v", gameDTO)
}
