import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Board from './board.js';

const BOARD_SIZE = 8,
    PLAYER_ONE = 1,
    PLAYER_TWO = 2,
    PLAYERS = {
        [PLAYER_ONE]: {
            name: "Grey",
            class: "player-grey"
        },
        [PLAYER_TWO]: {
            name: "Red",
            class: "player-red"
        }
    };

const URL = 'ws://127.0.0.1:3030'

class App extends Component {
    constructor() {
        super();
        this.state = { board: new Board(BOARD_SIZE, PLAYER_ONE, PLAYER_TWO),
            turn: PLAYER_ONE,
            selectedSquare: null, winner: null };
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.turn !== this.state.turn) {
            let board = this.state.board;
            if (!board.hasMoves(this.state.turn)) {
                console.log("no more available moves!");
                this.setState({winner: this.nextPlayer()});
            }
        }
    }

    ws = new WebSocket(URL)
    componentDidMount() {
        this.ws.onopen = () => {
        // on connecting, do nothing but log it to the console
        console.log('connected')
        }

        this.ws.onmessage = evt => {
        // on receiving a message, add it to the list of messages
        const message = JSON.parse(evt.data)
        this.addMessage(message)
        }

        this.ws.onclose = () => {
        console.log('disconnected')
        // automatically try to reconnect on connection loss
        this.setState({
            ws: new WebSocket(URL),
        })
        }
    }

    addMessage = message =>{
        //this.setState(state => ({ messages: [message, ...state.messages] }))
        this.handleSelectSquare(message.row, message.col)
    }
        

    submitMessage = (row, column) => {
        // on submitting the ChatInput form, send the message, add it to the list and reset the input
        const message =  {row: row, col: column} //{ name: this.state.name, message: messageString }
        this.ws.send(JSON.stringify(message))
    }

    handleSelectSquare = (row, column) => {
        console.log("in select square for row " + row + " column " + column);

        let selected = this.state.selectedSquare;

        if (this.canSelectSquare(row, column)) {
            this.setSquare(row, column);
        } else if (selected != null) {
            this.handleMove(row, column);
        }
    }

    selectSquare(row, column) {
        this.handleSelectSquare(row,column);
        this.submitMessage(row, column);
    }

    handleMove(row, col) {
        console.log("handling the selected move...");
        let board = this.state.board,
            selected = this.state.selectedSquare,
            start = board.board[selected.row][selected.column];

        if (!board.canMoveChecker(start, row, col)) {
            console.log("This is an illegal move");
            return;
        }

        let isJump = board.isJumpMove(start, row, col),
            becameKing = false;

        board.moveChecker(start, row, col);

        if (!board.isKing(start) && (board.getPlayer(start) === PLAYER_ONE && row === 0) || (board.getPlayer(start) === PLAYER_TWO && row === ((board.board.length)-1))) {
            console.log("making you a King....");
            becameKing = true;
            board.makeKing(start);
        }

        if (!becameKing && isJump && board.canKeepJumping(start)) {
            this.setState({board: board, selectedSquare: {row: row, column: col}});
        } else {
            this.setState({board: board, turn: this.nextPlayer(), selectedSquare: null});
        }
    }

    //allows the player to select a legal square
    canSelectSquare(row, column) {
        let square = this.state.board.board[row][column];
        if (!square) {
            return false;
        }
        let player = this.state.board.checkers[square].player;
        return player === this.state.turn;
    }

    setSquare(row, column) {
        this.setState({selectedSquare: {row: row, column: column}});
    }

    //keeps track of the player turn
    nextPlayer() {
        return (this.state.turn === PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE)
    }

    //restarts the board/game
    restart() {
        this.setState({ board: new Board(BOARD_SIZE, PLAYER_ONE, PLAYER_TWO),
            turn: PLAYER_ONE, selectedSquare: null, winner: null });
    }

    render() {
        return (
            <div className="App">
                {this.state.winner &&
                    <Winner player={this.state.winner} restart={this.restart.bind(this)} />
                }

                <h3>Current turn: {PLAYERS[this.state.turn].name}<span className={ PLAYERS[this.state.turn].class }></span></h3>

                <button className="restart-btn" onClick={this.restart.bind(this)}>Restart the Game</button>

                <GameBoard board={this.state.board}
                           selectedSquare={this.state.selectedSquare}
                           selectSquare={this.selectSquare.bind(this)}
                />

            </div>
        );
    }
}

//Displays once a winner has been determined. Allows the user to play again.
function Winner(props) {
    let player = PLAYERS[props.player].name;
    return (
        <div id="winner">
            <div>
                <p>{player} has won the game!</p>
                <button onClick={props.restart}>Play again?</button>
            </div>
        </div>
    );
}

//Render the game board to the screen
class GameBoard extends Component {
    render() {
        let selectedRow = this.props.selectedSquare ? this.props.selectedSquare.row : null,
            rows = this.props.board.board.map((row, i) => {
            return <Row key={i}
                        row={row}
                        selectedSquare={i === selectedRow ? this.props.selectedSquare : null}
                        rowNum={i}
                        checkers={this.props.board.checkers}
                        selectSquare={this.props.selectSquare} />;
            });

        return (
            <div className="board">
                {rows}
            </div>
        )
    }
}

class Row extends Component {
    render() {
        let selectedCol = this.props.selectedSquare ? this.props.selectedSquare.column : null,
            squares = this.props.row.map((square, i) => {
                return <Square key={i}
                               val={square != null ? this.props.checkers[square] : null}
                               row={this.props.rowNum}
                               column={i}
                               selected={i === selectedCol}
                               selectSquare={this.props.selectSquare} />
            });

        return (
            <div className="row">
                {squares}
            </div>
        )
    }
}

class Square extends Component {
    render() {
        let color = (this.props.row + this.props.column) % 2 === 0 ? "red" : "white",
            selection = this.props.selected ? " selected" : "",
            classes = "square " + color + selection;

        return (
            <div className={classes} onClick={() => {this.props.selectSquare(this.props.row, this.props.column)}}>
                {this.props.val != null &&
                <Piece checker={this.props.val} />
                }
            </div>
        )
    }
}

function Piece(props) {
    console.log(props.checker);

    let classes = "";

    if (props.checker) {
        classes += PLAYERS[props.checker.player].class;
        if (props.checker.isKing) {
            classes += " is a king";
        }
    }

    return (
        <div className={classes}></div>
    )
}

export default App;
