package minesweeper

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strconv"
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
		Timeout: 5000 * time.Second,
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

	return getGameDTOFromResponse(response, 201)
}

func (client *Client) OpenCellAt(gameId string, row int, column int) (*dtos.GameDTO, error) {

	url := client.ServerUrl + "/" + gameId + "/cell/" + intToString(row) + "-" + intToString(column)

	httpRequest, err := http.NewRequest("PUT", url, nil)
	if err != nil {
		return nil, err
	}

	response, err := client.httpClient.Do(httpRequest)
	if err != nil {
		return nil, err
	}

	return getGameDTOFromResponse(response, 200)
}

func (client *Client) ToggleFlagAt(gameId string, row int, column int, flag string) (*dtos.GameDTO, error) {
	if !(flag == "MINE" || flag == "QUESTION") {
		return nil, fmt.Errorf("Flag '%s' is invalid. Valid flags are '%s' and '%s'", flag, "MINE", "QUESTION")
	}

	url := client.ServerUrl + "/" + gameId + "/cell/" + intToString(row) + "-" + intToString(column) + "/flag/" + flag

	httpRequest, err := http.NewRequest("PUT", url, nil)
	if err != nil {
		return nil, err
	}

	response, err := client.httpClient.Do(httpRequest)
	if err != nil {
		return nil, err
	}

	return getGameDTOFromResponse(response, 200)
}

func (client *Client) ResumeLastSession(playerEmail string) (*dtos.GameDTO, error) {

	url := client.ServerUrl + "?playerEmail=" + playerEmail

	httpRequest, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}

	response, err := client.httpClient.Do(httpRequest)
	if err != nil {
		return nil, err
	}

	return getGameDTOFromResponse(response, 200)
}

func getGameDTOFromResponse(response *http.Response, successHttpStatusCode int) (*dtos.GameDTO, error) {
	responseData, err := io.ReadAll(response.Body)
	if err != nil {
		return nil, err
	}

	if response.StatusCode != successHttpStatusCode {
		return nil, fmt.Errorf("server returned status %s and body %s", response.Status, responseData)
	}

	gameDTO := new(dtos.GameDTO)
	err = json.Unmarshal(responseData, gameDTO)
	if err != nil {
		return nil, err
	}

	return gameDTO, nil
}

func intToString(i int) string {
	return strconv.Itoa(i)
}
