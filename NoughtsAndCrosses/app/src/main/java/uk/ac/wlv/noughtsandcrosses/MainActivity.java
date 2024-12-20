package uk.ac.wlv.noughtsandcrosses;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    public static final int NOUGHT = 0;
    public static final int CROSS = 1;
    public static final int EMPTY = 2;
    public static final int MAXGAMES = 10000;
    public static final int MAXTURNS = 9;
    public static final int XPOS = 0;
    public static final int YPOS = 1;


    int gameBoard[][];
    Button squares[][];
    TextView textMessage;
    ComputerPlayer mComputerPlayer;
    int winningGames[][][];
    int losingGames[][][];
    int currentGameMoves[][];
    int currentMove = 0;
    int nextWinningGame = 0;
    int nextLosingGame = 0;
    boolean gameOver = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBoard();
        winningGames = new int[MAXGAMES][MAXTURNS][2];
        for (int i = 0; i < MAXGAMES; i++) {
            for (int j = 0; j <MAXTURNS; j++) {
                winningGames[i][j][XPOS] = 0;
                winningGames[i][j][YPOS] = 0;
            }
        }
        losingGames = new int[MAXGAMES][MAXTURNS][2];
        for (int i = 0; i < MAXGAMES; i++) {
            for (int j = 0; j <MAXTURNS; j++) {
                losingGames[i][j][XPOS] = 0;
                losingGames[i][j][YPOS] = 0;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add("New Game");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        setBoard();
        return true;
    }

    // Set up the game board.
    private void setBoard() {
        mComputerPlayer = new ComputerPlayer();
        squares = new Button[4][4];
        gameBoard = new int[4][4];
        currentGameMoves = new int[MAXTURNS][2];

        textMessage = (TextView) findViewById(R.id.dialogue);

        squares[1][3] = (Button) findViewById(R.id.one);
        squares[1][2] = (Button) findViewById(R.id.two);
        squares[1][1] = (Button) findViewById(R.id.three);
        squares[2][3] = (Button) findViewById(R.id.four);
        squares[2][2] = (Button) findViewById(R.id.five);
        squares[2][1] = (Button) findViewById(R.id.six);
        squares[3][3] = (Button) findViewById(R.id.seven);
        squares[3][2] = (Button) findViewById(R.id.eight);
        squares[3][1] = (Button) findViewById(R.id.nine);

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                gameBoard[i][j] = EMPTY;  //clear the board to empty
                squares[i][j].setText(" ");
            }
        }

        for (int i = 0; i <MAXTURNS; i++) {
            currentGameMoves[i][XPOS] = 0;  // clear current game moves to empty
            currentGameMoves[i][YPOS] = 0;
        }

        textMessage.setText("Click a square to start.");
        currentMove = 0;
        // add the click listeners for each button
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                squares[i][j].setOnClickListener(new MyClickListener(i, j));
                if(!squares[i][j].isEnabled()) {
                    squares[i][j].setText(" ");
                    squares[i][j].setEnabled(true);
                }
            }
        }
        gameOver = false;
    }

    // check the board to see if someone has won
    private boolean checkBoard() {
        //Check for a line of Noughts
        if ((gameBoard[1][1] == NOUGHT && gameBoard[2][2] == NOUGHT && gameBoard[3][3] == NOUGHT)
                || (gameBoard[1][3] == NOUGHT && gameBoard[2][2] == NOUGHT && gameBoard[3][1] == NOUGHT)
                || (gameBoard[1][2] == NOUGHT && gameBoard[2][2] == NOUGHT && gameBoard[3][2] == NOUGHT)
                || (gameBoard[1][3] == NOUGHT && gameBoard[2][3] == NOUGHT && gameBoard[3][3] == NOUGHT)
                || (gameBoard[1][1] == NOUGHT && gameBoard[1][2] == NOUGHT && gameBoard[1][3] == NOUGHT)
                || (gameBoard[2][1] == NOUGHT && gameBoard[2][2] == NOUGHT && gameBoard[2][3] == NOUGHT)
                || (gameBoard[3][1] == NOUGHT && gameBoard[3][2] == NOUGHT && gameBoard[3][3] == NOUGHT)
                || (gameBoard[1][1] == NOUGHT && gameBoard[2][1] == NOUGHT && gameBoard[3][1] == NOUGHT)) {
            textMessage.setText("You win!");
            rememberGameLost();
            gameOver = true;
        }
        // Check for a line of Crosses
        else if ((gameBoard[1][1] == CROSS && gameBoard[2][2] == CROSS && gameBoard[3][3] == CROSS)
                || (gameBoard[1][3] == CROSS && gameBoard[2][2] == CROSS && gameBoard[3][1] == CROSS)
                || (gameBoard[1][2] == CROSS && gameBoard[2][2] == CROSS && gameBoard[3][2] == CROSS)
                || (gameBoard[1][3] == CROSS && gameBoard[2][3] == CROSS && gameBoard[3][3] == CROSS)
                || (gameBoard[1][1] == CROSS && gameBoard[1][2] == CROSS && gameBoard[1][3] == CROSS)
                || (gameBoard[2][1] == CROSS && gameBoard[2][2] == CROSS && gameBoard[2][3] == CROSS)
                || (gameBoard[3][1] == CROSS && gameBoard[3][2] == CROSS && gameBoard[3][3] == CROSS)
                || (gameBoard[1][1] == CROSS && gameBoard[2][1] == CROSS && gameBoard[3][1] == CROSS)) {
            textMessage.setText("You lose!");
            rememberGameWon();
            gameOver = true;
        } else {
            boolean empty = false;
            for(int i=1; i<=3; i++) {  // Check for any empty square
                for(int j=1; j<=3; j++) {
                    if(gameBoard[i][j] == EMPTY) {
                        empty = true;
                        break;
                    }
                }
            }
            if(!empty) {  // No empty square means it is a draw
                gameOver = true;
                textMessage.setText("It's a draw!");
            }
        }
        return gameOver;
    }

    // This looks for a previous stored winning game that has matching moves so far
    private boolean findMatchingWinningGame() {
        boolean matched = false;
        for (int k = 0; k < MAXGAMES && winningGames[k][0][XPOS] > 0; k++) {  // Look for a remembered winning game
            matched = true;
            for (int i = 0; i < MAXTURNS; i++) {  // check if the current game is the same set of moves as the winning game
                if (!(currentGameMoves[i][XPOS] == winningGames[k][i][XPOS] &&
                        currentGameMoves[i][YPOS] == winningGames[k][i][YPOS])) {
                    matched = false;
                    break;
                }
            }
        }
        return matched;
    }

    // This looks for a previous stored lost game that has matching moves so far
    private boolean findMatchingLosingGame() {
        boolean matched = false;
        for (int k = 0; k < MAXGAMES && losingGames[k][0][XPOS] > 0; k++) {    // Look for a remembered lost game
            matched = true;
            for (int i = 0; i < MAXTURNS; i++) { // check if the current game is the same set of moves as the lost game
                if (!(currentGameMoves[i][XPOS] == losingGames[k][i][XPOS] &&
                        currentGameMoves[i][YPOS] == losingGames[k][i][YPOS])) {
                    matched = false;
                    break;
                }
            }
        }
        Log.d("findMatchingLosingGame",""+matched);
        return matched;
    }

    // Remember a game that was won
    private void rememberGameWon() {
        if (!findMatchingWinningGame()) {
            for (int i = 0; i < MAXTURNS; i++) {
                winningGames[nextWinningGame][i][XPOS] = currentGameMoves[i][XPOS];
                winningGames[nextWinningGame][i][YPOS] = currentGameMoves[i][YPOS];
            }
            nextWinningGame++;
            if (nextWinningGame >= MAXGAMES)
                nextWinningGame = 0;
        }
        Log.i("Remembering Game Won",  "Remembered " + Integer.toString(nextWinningGame)
                +" games won and " + Integer.toString(nextLosingGame)+" games lost");
    }

    private void rememberGameLost() {
        if (!findMatchingLosingGame()) {
            for (int i = 0; i < MAXTURNS; i++) {
                losingGames[nextLosingGame][i][XPOS] = currentGameMoves[i][XPOS];
                losingGames[nextLosingGame][i][YPOS] = currentGameMoves[i][YPOS];
            }
            nextLosingGame++;
            if (nextLosingGame >= MAXGAMES)
                nextLosingGame = 0;
            //logGameGrid("rememberingGameLost");
        }
        Log.i("Remembering Game Lost",  "Remembered " + Integer.toString(nextWinningGame)
                +" games won and " + Integer.toString(nextLosingGame)+" games lost");
    }


    class MyClickListener implements View.OnClickListener {
        int x;
        int y;

        public MyClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void onClick(View view) {
            if (gameOver)
                return;
            if (squares[x][y].isEnabled()) {
                squares[x][y].setEnabled(false);
                squares[x][y].setText("O");
                gameBoard[x][y] = 0;
                currentGameMoves[currentMove][XPOS] = x;
                currentGameMoves[currentMove][YPOS] = y;
                textMessage.setText("");
                if (!checkBoard()) {
                    mComputerPlayer.takeTurn();
                }
            }
        }
    }

    private class ComputerPlayer {
        public void takeTurn() {
            if (canComputerWin()){
                return;
            }
            else if(blockPlayerWin() ){
                return;
            }else if(choosePastWinMove() ){
                return;
            } else {
                int x;
                int y;
                // Look for a non-lost move and pick it
                for(x=1; x<=3; x++){
                    for(y=1; y<=3; y++){
                        if(gameBoard[x][y] == EMPTY && !isMoveLosing(x,y)){
                            markSquare(x,y);
                            return;
                        }
                    }
                }
                // Otherwise just pick a random square
                Random rand = new Random();
                x = 1 + rand.nextInt(3);
                y = 1 + rand.nextInt(3);
                Log.d("Random move 1","x="+x+" y="+y);
                while(gameBoard[x][y] != EMPTY || (isMoveLosing(x,y) && gameBoard[x][y] == EMPTY)) {
                    x = 1 + rand.nextInt(3);
                    y = 1 + rand.nextInt(3);
                    Log.d("Random move 2","x="+x+" y="+y);
                }
                markSquare(x,y);
            }
        }

        // Check every square for a possible computer win and go for it
        private boolean canComputerWin() {
            if(gameBoard[1][1]==EMPTY &&
                    ((gameBoard[1][2]==CROSS && gameBoard[1][3]==CROSS) ||
                            (gameBoard[2][2]==CROSS && gameBoard[3][3]==CROSS) ||
                            (gameBoard[2][1]==CROSS && gameBoard[3][1]==CROSS))) {
                markSquare(1,1);
            } else if (gameBoard[1][2]==EMPTY &&
                    ((gameBoard[2][2]==CROSS && gameBoard[3][2]==CROSS) ||
                            (gameBoard[1][1]==CROSS && gameBoard[1][3]==CROSS))) {
                markSquare(1,2);
            } else if(gameBoard[1][3]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[1][2]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[2][3]==CROSS && gameBoard[3][3]==CROSS))) {
                markSquare(1,3);
            } else if(gameBoard[2][1]==EMPTY &&
                    ((gameBoard[2][2]==CROSS && gameBoard[2][3]==CROSS) ||
                            (gameBoard[1][1]==CROSS && gameBoard[3][1]==CROSS))){
                markSquare(2,1);
            } else if(gameBoard[2][2]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[3][3]==CROSS) ||
                            (gameBoard[1][2]==CROSS && gameBoard[3][2]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[1][3]==CROSS) ||
                            (gameBoard[2][1]==CROSS && gameBoard[2][3]==CROSS))) {
                markSquare(2,2);
            } else if(gameBoard[2][3]==EMPTY &&
                    ((gameBoard[2][1]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[1][3]==CROSS && gameBoard[3][3]==CROSS))) {
                markSquare(2,3);
            } else if(gameBoard[3][1]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[2][1]==CROSS) ||
                            (gameBoard[3][2]==CROSS && gameBoard[3][3]==CROSS) ||
                            (gameBoard[2][2]==CROSS && gameBoard[1][3]==CROSS))){
                markSquare(3,1);
            } else if(gameBoard[3][2]==EMPTY &&
                    ((gameBoard[1][2]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[3][3]==CROSS))) {
                markSquare(3,2);
            }else if( gameBoard[3][3]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[1][3]==CROSS && gameBoard[2][3]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[3][2]==CROSS))) {
                markSquare(3,3);
            }  else if(gameBoard[1][1]==EMPTY &&
                    ((gameBoard[1][2]==CROSS && gameBoard[1][3]==CROSS) ||
                            (gameBoard[2][2]==CROSS && gameBoard[3][3]==CROSS) ||
                            (gameBoard[2][1]==CROSS && gameBoard[3][1]==CROSS))) {
                markSquare(1,1);
            } else if (gameBoard[1][2]==EMPTY &&
                    ((gameBoard[2][2]==CROSS && gameBoard[3][2]==CROSS) ||
                            (gameBoard[1][1]==CROSS && gameBoard[1][3]==CROSS))) {
                markSquare(1,2);
            } else if(gameBoard[1][3]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[1][2]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[2][3]==CROSS && gameBoard[3][3]==CROSS))) {
                markSquare(1,3);
            } else if(gameBoard[2][1]==EMPTY &&
                    ((gameBoard[2][2]==CROSS && gameBoard[2][3]==CROSS) ||
                            (gameBoard[1][1]==CROSS && gameBoard[3][1]==CROSS))){
                markSquare(2,1);
            } else if(gameBoard[2][2]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[3][3]==CROSS) ||
                            (gameBoard[1][2]==CROSS && gameBoard[3][2]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[1][3]==CROSS) ||
                            (gameBoard[2][1]==CROSS && gameBoard[2][3]==CROSS))) {
                markSquare(2,2);
            } else if(gameBoard[2][3]==EMPTY &&
                    ((gameBoard[2][1]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[1][3]==CROSS && gameBoard[3][3]==CROSS))) {
                markSquare(2,3);
            } else if(gameBoard[3][1]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[2][1]==CROSS) ||
                            (gameBoard[3][2]==CROSS && gameBoard[3][3]==CROSS) ||
                            (gameBoard[2][2]==CROSS && gameBoard[1][3]==CROSS))){
                markSquare(3,1);
            } else if(gameBoard[3][2]==EMPTY &&
                    ((gameBoard[1][2]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[3][3]==CROSS))) {
                markSquare(3,2);
            }else if( gameBoard[3][3]==EMPTY &&
                    ((gameBoard[1][1]==CROSS && gameBoard[2][2]==CROSS) ||
                            (gameBoard[1][3]==CROSS && gameBoard[2][3]==CROSS) ||
                            (gameBoard[3][1]==CROSS && gameBoard[3][2]==CROSS))) {
                markSquare(3,3);
            } else
                return false;

            return true;
        }


        // Checking every square for a possible human player win and block it
        private boolean blockPlayerWin() {
            if(gameBoard[1][1]==EMPTY &&
                    ((gameBoard[1][2]==NOUGHT && gameBoard[1][3]==NOUGHT) ||
                            (gameBoard[2][2]==NOUGHT && gameBoard[3][3]==NOUGHT) ||
                            (gameBoard[2][1]==NOUGHT && gameBoard[3][1]==NOUGHT))) {
                markSquare(1,1);
            } else if (gameBoard[1][2]==EMPTY &&
                    ((gameBoard[2][2]==NOUGHT && gameBoard[3][2]==NOUGHT) ||
                            (gameBoard[1][1]==NOUGHT && gameBoard[1][3]==NOUGHT))) {
                markSquare(1,2);
            } else if(gameBoard[1][3]==EMPTY &&
                    ((gameBoard[1][1]==NOUGHT && gameBoard[1][2]==NOUGHT) ||
                            (gameBoard[3][1]==NOUGHT && gameBoard[2][2]==NOUGHT) ||
                            (gameBoard[2][3]==NOUGHT && gameBoard[3][3]==NOUGHT))) {
                markSquare(1,3);
            } else if(gameBoard[2][1]==EMPTY &&
                    ((gameBoard[2][2]==NOUGHT && gameBoard[2][3]==NOUGHT) ||
                            (gameBoard[1][1]==NOUGHT && gameBoard[3][1]==NOUGHT))){
                markSquare(2,1);
            } else if(gameBoard[2][2]==EMPTY &&
                    ((gameBoard[1][1]==NOUGHT && gameBoard[3][3]==NOUGHT) ||
                            (gameBoard[1][2]==NOUGHT && gameBoard[3][2]==NOUGHT) ||
                            (gameBoard[3][1]==NOUGHT && gameBoard[1][3]==NOUGHT) ||
                            (gameBoard[2][1]==NOUGHT && gameBoard[2][3]==NOUGHT))) {
                markSquare(2,2);
            } else if(gameBoard[2][3]==EMPTY &&
                    ((gameBoard[2][1]==NOUGHT && gameBoard[2][2]==NOUGHT) ||
                            (gameBoard[1][3]==NOUGHT && gameBoard[3][3]==NOUGHT))) {
                markSquare(2,3);
            } else if(gameBoard[3][1]==EMPTY &&
                    ((gameBoard[1][1]==NOUGHT && gameBoard[2][1]==NOUGHT) ||
                            (gameBoard[3][2]==NOUGHT && gameBoard[3][3]==NOUGHT) ||
                            (gameBoard[2][2]==NOUGHT && gameBoard[1][3]==NOUGHT))){
                markSquare(3,1);
            } else if(gameBoard[3][2]==EMPTY &&
                    ((gameBoard[1][2]==NOUGHT && gameBoard[2][2]==NOUGHT) ||
                            (gameBoard[3][1]==NOUGHT && gameBoard[3][3]==NOUGHT))) {
                markSquare(3,2);
            }else if( gameBoard[3][3]==EMPTY &&
                    ((gameBoard[1][1]==NOUGHT && gameBoard[2][2]==NOUGHT) ||
                            (gameBoard[1][3]==NOUGHT && gameBoard[2][3]==NOUGHT) ||
                            (gameBoard[3][1]==NOUGHT && gameBoard[3][2]==NOUGHT))) {
                markSquare(3,3);
            }
            else
                return false;

            return true;
        }


        // Check that a proposed move is not a losing move from memory
        private boolean isMoveLosing(int x, int y) {
            boolean matched=false;
            for(int k=0; k<MAXGAMES && losingGames[k][0][XPOS]>0; k++) {
                if(x == losingGames[k][currentMove+1][XPOS] &&
                        y == losingGames[k][currentMove+1][YPOS]) {
                    matched = true;
                    for(int i=currentMove; i>=0; i--){
                        if(!(currentGameMoves[i][XPOS] == losingGames[k][i][XPOS] &&
                                currentGameMoves[i][YPOS] == losingGames[k][i][YPOS])) {
                            matched = false;
                            break;
                        }
                    }
                }
                if(matched) {
                    Log.d("isMoveLosing", "Found losing move!");
                    return true;
                }
            }
            return false;
        }


        // Choose a move from a matching winning game from the past
        private boolean choosePastWinMove(){
            boolean matched=false;
            for(int k=0; k<MAXGAMES && winningGames[k][0][XPOS]>0; k++) {
                matched=false;
                if(currentGameMoves[currentMove][XPOS] == winningGames[k][currentMove][XPOS] &&
                        currentGameMoves[currentMove][YPOS] == winningGames[k][currentMove][YPOS]) {
                    matched = true;
                    for(int i=(currentMove-1); i>=0; i--){
                        if(!(currentGameMoves[i][XPOS] == winningGames[k][i][XPOS] &&
                                currentGameMoves[i][YPOS] == winningGames[k][i][YPOS])) {
                            matched = false;
                            break;
                        }
                    }
                }
                if(matched){
                    int x = winningGames[k][currentMove+1][XPOS];
                    int y = winningGames[k][currentMove+1][YPOS];
                    markSquare(x,y);
                    return true;
                }
            }
            return false;
        }


        private void markSquare(int x, int y) {
            squares[x][y].setEnabled(false);
            squares[x][y].setText("X");
            gameBoard[x][y] = CROSS;
            currentMove++;
            currentGameMoves[currentMove][XPOS] = x;
            currentGameMoves[currentMove][YPOS] = y;
            currentMove++;
            checkBoard();
        }
    }
}
