package lingaraj.hourglass.in.tictactoe.Libraries;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lingaraj.hourglass.in.tictactoe.Constants;

/**
 * Created by lingaraj on 9/1/17.
 */

public class TicTacToe  {

    private final String TAG = "TICTACTOELIB";

    //3x3 tic tac toe gameboard
    int[][] game_board = new int[3][3];
    private int player_score;
    private int computer_score;
    private Map<String,List<String>> horizontal_map = new HashMap<>();
    private Map<String,List<String>> vertical_map = new HashMap<>();
    private Map<String,List<String>> diagonal_map = new HashMap<>();
    private List<String> scoreKeyList = new ArrayList<String>();
    final String VERTICAL_MODE = "vertical";
    final String HORIZONTAL_MODE = "horizontal";
    final String DIAGONAL_MODE = "diagonal";
    public TicTacToe(){
        //Initializing Empty Constructor
        setAxisMap();
        resetGameBoard();
    }

    private void setAxisMap() {
        setHorizontalMap();
        setVerticalMap();
        setDiagonalMap();
    }


    public void resetGameBoard() {
        //method can be used to reset gameboard and player score
        player_score = 0;
        computer_score = 0;
        this.scoreKeyList.clear();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                game_board[row][column] = -1;
                //initializing some values to all the board

            }
        }
    }

    public void addPlayerMove(int row,int column,int player){
        //every time a player/computer makes a move(drawn cross or o on a board the respective value in updated in the game board matrix 3x3)
        if (player== Constants.HUMAN){
            //move made by player updating the same
            game_board[row][column] = Constants.HUMAN;
            Log.d(TAG,"["+row+"]"+"["+column+"]"+Constants.HUMAN);
        }
        else {
            //move made by COMPUTER updating the same
            game_board[row][column] = Constants.COMPUTER;
            Log.d(TAG,"["+row+"]"+"["+column+"]"+Constants.COMPUTER);

        }
        checkPlayerWinSituation(row,column,player);
    }

    private void checkPlayerWinSituation(int row, int column, int player) {
        // Splitted as a three step check because, if check got succeed in vertical mode, horizontal and diagonal execution will be excluded

        //1.check for score in vertical mode if returned a false
        boolean is_match_vertical_mode = verifyVerticalHorizontalMatch(row,column,player,VERTICAL_MODE);
        if (!is_match_vertical_mode){
            //2. vertical verificatin failed, doing horizontal mode
            boolean is_match_horizontal_mode = verifyVerticalHorizontalMatch(row,column,player,HORIZONTAL_MODE);
            if (!is_match_horizontal_mode){
                //3. if vertical failed looking for diagonal
                String key = String.valueOf(row)+String.valueOf(column);
                if (this.diagonal_map.containsKey(key)){
                    // there is possiblity  for diagonal scoring.
                    obtainDiagonalData(key,player);
                }
            }
        }

    }

    private void obtainDiagonalData(String key, int player) {
        List<String> matrixIndexes = this.diagonal_map.get(key);
        if (key.equals("11")){
            //if its a mid index in 3x3 matirx we have to check two diagonals in two ways as it is the mid value
            //this list will contain six indexes instead of three because it represent two diagonals as the mid point is choosen.
            int tic1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(1)))];
            int tac1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(1)))];
            int toe1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(1)))];
            //takes first three indexes as key
            String score_key = generateScoreKey(matrixIndexes.subList(0,3));
            if (player == Constants.HUMAN){
                if (tic1==Constants.HUMAN && tac1 ==Constants.HUMAN && toe1==Constants.HUMAN){
                    if (!scoreKeyList.contains(score_key)){
                        updateScoreMap(score_key,player);
                    }
                } else {
                    processSecondIndexes(matrixIndexes.subList(3,6),player);
                }
            }
            else if (player == Constants.COMPUTER){
                if (tic1==Constants.COMPUTER && tac1 ==Constants.COMPUTER && toe1==Constants.COMPUTER){
                    if (!scoreKeyList.contains(score_key)){
                        updateScoreMap(score_key,player);
                    }
                } else {
                    processSecondIndexes(matrixIndexes.subList(3,6),player);
                }


            }

        }
        else {
            int tic1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(1)))];
            int tac1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(1)))];
            int toe1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(1)))];
            String score_key = generateScoreKey(matrixIndexes);
            checkGameBoard(tic1,tac1,toe1,player,score_key);
        }
    }

    private void processSecondIndexes(List<String> matrixIndexes, int player) {
        int tic1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(1)))];
        int tac1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(1)))];
        int toe1 = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(1)))];
        String score_key = generateScoreKey(matrixIndexes);
        if (!scoreKeyList.contains(score_key)) {
            checkGameBoard(tic1,tac1,toe1,player,score_key);

        }
        Log.d(TAG,"processing second set of list from IndexesList");


    }


    private boolean verifyVerticalHorizontalMatch(int row, int column, int player, String mode) {
        //horizontal and vertical map contains board indexes which can be obtained from row,column  value exampel (3,3) will become 33 and it will have its
        String key = String.valueOf(row)+String.valueOf(column);
        //whenever a current horizontal or vertical marking is identified as correct, am adding the key to scoredkeylist
        List<String> matrixIndexes = new ArrayList<String>();
            if (mode.equals(HORIZONTAL_MODE)) {
                //gets indexes required to verify for score in horizontal line, gameboard
                matrixIndexes = this.horizontal_map.get(key);
            } else {
                //gets indexes required to verify for score in vertical line, gameboard
                matrixIndexes = this.vertical_map.get(key);
            }
            int tic = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(1)))];
            int tac = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(1)))];
            int toe = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(1)))];
            String score_key = generateScoreKey(matrixIndexes);
        if (!scoreKeyList.contains(score_key)) {
            Log.d(TAG, "Score Key:" + score_key);
            return checkGameBoard(tic,tac,toe,player,score_key);
        }
        else {
            return false;
        }
      }

      private boolean checkGameBoard(int tic,int tac ,int toe,int player,String score_key){
          if (player == Constants.HUMAN && tic == Constants.HUMAN && tac == Constants.HUMAN && toe == Constants.HUMAN) {
              updateScoreMap(score_key, player);
              return true;
          } else if (player == Constants.COMPUTER && tic == Constants.COMPUTER && tac == Constants.COMPUTER && toe == Constants.COMPUTER) {
              updateScoreMap(score_key, player);
              return true;
          }
          else {
              return false;
          }

      }



    private void updateScoreMap(String score_key, int player) {
        //make sure the current score key is already verified and given score.
        if (!this.scoreKeyList.contains(score_key)){
            this.scoreKeyList.add(score_key);
            if (player==Constants.COMPUTER){
                increementComputerScore();
                //todo communicated with UI display Animation for success and change score
            }
            else {
                increementPlayerScore();
                //todo communicated with UI display Animation for success and change score
            }
        }

    }

    private String generateScoreKey(List<String> verticalIndexes) {
        Log.d(TAG,"Scored Key List:"+verticalIndexes);
        return verticalIndexes.get(0)+"_"+verticalIndexes.get(1)+"_"+verticalIndexes.get(2);
    }


    public int getPlayer_score() {
        return player_score;
    }

    public int getComputer_score() {
        return computer_score;
    }

    public void increementPlayerScore(){
        this.player_score++;
        Log.d(TAG,"Player score:"+computer_score);

    }

    public void increementComputerScore(){
        this.computer_score++;
        Log.d(TAG,"Computer score:"+computer_score);
    }

    private void setHorizontalMap() {
        horizontal_map.put("00",new ArrayList<String>(Arrays.asList("00","01","02")));
        horizontal_map.put("01",new ArrayList<String>(Arrays.asList("00","01","02")));
        horizontal_map.put("02",new ArrayList<String>(Arrays.asList("00","01","02")));
        horizontal_map.put("10",new ArrayList<String>(Arrays.asList("10","11","12")));
        horizontal_map.put("11",new ArrayList<String>(Arrays.asList("10","11","12")));
        horizontal_map.put("12",new ArrayList<String>(Arrays.asList("10","11","12")));
        horizontal_map.put("20",new ArrayList<String>(Arrays.asList("20","21","22")));
        horizontal_map.put("21",new ArrayList<String>(Arrays.asList("20","21","22")));
        horizontal_map.put("22",new ArrayList<String>(Arrays.asList("20","21","22")));
        Log.d(TAG,"Horizontal Map for each index set");
    }

    private void setVerticalMap(){
        // vertical map for column 0
        vertical_map.put("00",new ArrayList<String>(Arrays.asList("00","10","20")));
        vertical_map.put("10",new ArrayList<String>(Arrays.asList("00","10","20")));
        vertical_map.put("20",new ArrayList<String>(Arrays.asList("00","10","20")));
        //vertical map for column 1
        vertical_map.put("01",new ArrayList<String>(Arrays.asList("01","11","21")));
        vertical_map.put("11",new ArrayList<String>(Arrays.asList("01","11","21")));
        vertical_map.put("21",new ArrayList<String>(Arrays.asList("01","11","21")));
        //vertical map for column 2
        vertical_map.put("02",new ArrayList<String>(Arrays.asList("02","12","22")));
        vertical_map.put("12",new ArrayList<String>(Arrays.asList("01","11","21")));
        vertical_map.put("21",new ArrayList<String>(Arrays.asList("01","11","21")));
        Log.d(TAG,"Vertical Map set");
    }

    private void setDiagonalMap(){
        diagonal_map.put("00",new ArrayList<String>(Arrays.asList("00","11","22")));
        diagonal_map.put("11",new ArrayList<String>(Arrays.asList("00","11","22","02","11","20")));
        diagonal_map.put("22",new ArrayList<String>(Arrays.asList("00","11","22")));
        diagonal_map.put("20",new ArrayList<String>(Arrays.asList("02","11","20")));
        diagonal_map.put("02",new ArrayList<String>(Arrays.asList("02","11","20")));
        Log.d(TAG,"Diagonal Map");

    }


}
