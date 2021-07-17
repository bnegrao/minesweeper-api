package minesweeper

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"github.com/bnegrao/minesweeper-api/client/minesweeper/dtos"
)

var contentType = "application/json"

type Client struct {
	ServerUrl  string
	httpClient http.Client
}

func New(serverUrl string) *Client {
	client := new(Client)
	client.ServerUrl = serverUrl
	client.httpClient = http.Client{
		Timeout: 5 * time.Second,
	}
	return client
}

func (client *Client) StartGame(startGameDTO dtos.StartGameDTO) (*dtos.GameDTO, error) {
	dtoAsJson, err := json.Marshal(startGameDTO)
	if err != nil {
		return nil, err
	}

	response, err := client.httpClient.Post(client.ServerUrl, contentType, bytes.NewBuffer(dtoAsJson))
	if err != nil {
		return nil, err
	}

	responseData, err := io.ReadAll(response.Body)
	if err != nil {
		return nil, err
	}

	if response.StatusCode != 201 {
		return nil, fmt.Errorf("server returned status %s and body %s", response.Status, responseData)
	}

	gameDTO := new(dtos.GameDTO)
	err = json.Unmarshal(responseData, gameDTO)
	if err != nil {
		return nil, err
	}

	return gameDTO, nil
}
