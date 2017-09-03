package lingaraj.hourglass.in.tictactoe.Libraries;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lingaraj.hourglass.in.tictactoe.Constants;
import lingaraj.hourglass.in.tictactoe.Models.MachineMoveChoice;
import lingaraj.hourglass.in.tictactoe.TicTacToeInterface;

/**
 * Created by lingaraj on 9/1/17.
 */

public class TicTacToe  {

    private final String TAG = "TICTACTOELIB";

    //3x3 tic tac toe gameboard
    int[][] game_board = new int[3][3];
    private int player_score;
    private int computer_score;
    private int tie_score;
    private int last_move_player_row;
    private int last_move_player_column;
    private boolean isCenterMatrixIndexOcuppied;
    private Map<String,List<String>> horizontal_map = new HashMap<>();
    private Map<String,List<String>> vertical_map = new HashMap<>();
    private Map<String,List<String>> diagonal_map = new HashMap<>();
    private List<String> scoreKeyList = new ArrayList<String>();
    final String VERTICAL_MODE = "vertical";
    final String HORIZONTAL_MODE = "horizontal";
    final String DIAGONAL_MODE = "diagonal";
    final String NOT_NECESSARY = "notnecessary";
    final String USE_VERTICAL_MOVE_CHOICE = "USEVERTICALMOVECHOICE";
    final String USE_HORIZONTAL_MOVE_CHOICE = "USEHORIZONTALMOVECHOICE";
    final String USE_DIAGONAL_MOVE_CHOICE = "USEDIAGONALMOVECHOICE";
    final String NO_RESULT = "NORESULT";
    private Context mcontext;
    private TicTacToeInterface activity_interface;
    public TicTacToe(Context context){
        //Initializing Empty Constructor
        this.mcontext = context;
        this.activity_interface = (TicTacToeInterface) mcontext;
        setAxisMap();
        resetGameBoard();
    }

    private void setAxisMap() {
        player_score = 0;
        computer_score = 0;
        tie_score = 0;
        setHorizontalMap();
        setVerticalMap();
        setDiagonalMap();
    }


    public void resetGameBoard() {
        //method can be used to reset gameboard and player score
        this.isCenterMatrixIndexOcuppied = false;
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
        activity_interface.nextPlayerMove(player);
    }

    private void checkPlayerWinSituation(int row, int column, int player) {
        // Splitted as a three step check because, if check got succeed in vertical mode, horizontal and diagonal execution will be excluded

        //1.check for score in vertical mode if returned a false
        if (player ==Constants.HUMAN){
            last_move_player_row = row;
            last_move_player_column = column;
        }

        if (game_board[1][1]!=-1 && !isCenterMatrixIndexOcuppied) {
            isCenterMatrixIndexOcuppied = true;
        }

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



    private void  updateScoreMap(String score_key, int player) {
        //make sure the current score key is already verified and given score.
        if (!this.scoreKeyList.contains(score_key)){
            this.scoreKeyList.add(score_key);
            if (player==Constants.COMPUTER){
                increementComputerScore();
                boolean is_tie = false;
                this.activity_interface.endGame(is_tie,score_key,player);

                //todo communicated with UI display Animation for success and change score
            }
            else {
                increementPlayerScore();
                boolean is_tie = false;
                this.activity_interface.endGame(is_tie,score_key,player);
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

    public int getTie_score() {
        return tie_score;
    }


    private void increementPlayerScore(){
        this.player_score++;
        Log.d(TAG,"Player score:"+computer_score);

    }

    private void increementComputerScore(){
        this.computer_score++;
        Log.d(TAG,"Computer score:"+computer_score);
    }

    public void increementTieScore(){
        this.tie_score++;
        Log.d(TAG,"Tie score increemented");
    }

    public String generateMachineMove(){
        //starts with the defence move ocuppying the center index(11) will block
        if (!isCenterMatrixIndexOcuppied){
            return "11";
        }
        else {
            String chosen_key = generateComputerNextMove();
            Log.d(TAG,"Computer Generated Next Move:"+chosen_key);
        /*    if (!chosen_key.equals(NO_RESULT)){
                int row = Integer.parseInt(String.valueOf(chosen_key.charAt(0)));
                int column = Integer.parseInt(String.valueOf(chosen_key.charAt(1)));
                addPlayerMove(row,column,Constants.COMPUTER); */
                return  chosen_key;

        }


    }

    private String generateComputerNextMove() {
        String human_move_key = String.valueOf(last_move_player_row)+String.valueOf(last_move_player_column);
        MachineMoveChoice vertical_move_choice = null;
        MachineMoveChoice horizontal_move_choice = null;
        MachineMoveChoice diagonal_move_choice = null;
        if (vertical_map.containsKey(human_move_key)){
            vertical_move_choice = isBlockStrategyNecessaryVertical(human_move_key,VERTICAL_MODE);
        }
        else {
            vertical_move_choice = new MachineMoveChoice(true);
        }
        if (horizontal_map.containsKey(human_move_key)){
            horizontal_move_choice = isBlockStrategyNecessaryVertical(human_move_key,HORIZONTAL_MODE);
        }
        else {
            horizontal_move_choice = new MachineMoveChoice(true);
        }
        if (diagonal_map.containsKey(human_move_key)){
            diagonal_move_choice = isBlockStrategyNecessaryDiagonal(human_move_key);
        }
        else {
            diagonal_move_choice = new MachineMoveChoice(true);
        }


         if (!vertical_move_choice.isNull() && vertical_move_choice.isRedAlert()){
             return vertical_move_choice.getBoardKey();
         }
         else if (!horizontal_move_choice.isNull() && horizontal_move_choice.isRedAlert()){
             return horizontal_move_choice.getBoardKey();
         }
         else if (!diagonal_move_choice.isNull() && diagonal_move_choice.isRedAlert()){
             return diagonal_move_choice.getBoardKey();
         }
         else if (!vertical_move_choice.isNull() && vertical_move_choice.isScore()){
             return vertical_move_choice.getBoardKey();
         }
         else if (!horizontal_move_choice.isNull() && horizontal_move_choice.isScore()){
             return horizontal_move_choice.getBoardKey();
         }
         else if (!diagonal_move_choice.isNull() && diagonal_move_choice.isScore()){
             return diagonal_move_choice.getBoardKey();
         }
         else if (!vertical_move_choice.isNull() && !diagonal_move_choice.isRedAlert()){
             return vertical_move_choice.getBoardKey();
         }
         else if (!horizontal_move_choice.isNull() && !horizontal_move_choice.isRedAlert()){
             return horizontal_move_choice.getBoardKey();
         }
         else if (!diagonal_move_choice.isNull() && !diagonal_move_choice.isRedAlert()){
             return diagonal_move_choice.getBoardKey();
         }
         else {

             return generateRandomMove();
         }

    }

    private String generateRandomMove() {
        List<String> un_occupied_place = new ArrayList<String>();
        for (int row = 0; row <3 ; row++) {
            for (int column=0;column<3;column++){
            if (game_board[row][column]==Constants.INDEX_UNOCCUPIED){
                String key = String.valueOf(row)+String.valueOf(column);
                un_occupied_place.add(key);
            }
            }
        }
            Collections.shuffle(un_occupied_place);
            return un_occupied_place.get(0);
     }

    private MachineMoveChoice isBlockStrategyNecessaryDiagonal(String human_move_key) {

        if (isCenterMatrixIndexOcuppied && game_board[1][1]==Constants.HUMAN){
            return isDiagonalBlockStrategy(human_move_key);
        }
        else if (isCenterMatrixIndexOcuppied && game_board[1][1]==Constants.COMPUTER){
            return isDiagonalBlockStrategy(human_move_key);
        }
        else {
            return getMachineOccupiablePosition(this.diagonal_map.get(human_move_key));
        }

    }

    private MachineMoveChoice isDiagonalBlockStrategy(String human_move_key) {
        if (human_move_key.equals("11")){
            //diagonal can be on both side have to check
            List<String> matrixIndexes = new ArrayList<String>();
            matrixIndexes = this.diagonal_map.get("11");
            List<String> diagonal_one = matrixIndexes.subList(0,3);
            List<String> diagonal_two = matrixIndexes.subList(3,6);
            MachineMoveChoice machine_diagonal_one = verifyDiagonalMatch(diagonal_one);
            MachineMoveChoice machine_daigonal_two = verifyDiagonalMatch(diagonal_two);
            if (machine_diagonal_one.isRedAlert()){
                return machine_diagonal_one;
            }
            else if (machine_daigonal_two.isRedAlert()){
                return machine_daigonal_two;
            }
            else {
                return machine_diagonal_one;
            }
        }
        else {
            List<String> matrixIndexes = this.diagonal_map.get(human_move_key);
            return verifyDiagonalMatch(matrixIndexes);
        }
    }

    private MachineMoveChoice verifyDiagonalMatch(List<String> matrixIndexes) {
        int tic = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(1)))];
        int tac = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(1)))];
        int toe = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(1)))];
        if (tic == Constants.HUMAN && tac == Constants.HUMAN && toe == Constants.INDEX_UNOCCUPIED){
            Log.d(TAG,"Requires Diagonal Block At:"+matrixIndexes.get(0));
            return new MachineMoveChoice(true,matrixIndexes.get(2),false);
        }
       else if (tic == Constants.COMPUTER && tac == Constants.COMPUTER && toe == Constants.INDEX_UNOCCUPIED){
            Log.d(TAG,"Requires Diagonal Block At:"+matrixIndexes.get(0));
            MachineMoveChoice record =  new MachineMoveChoice(false,matrixIndexes.get(2),false);
            record.setScore(true);
            return record;
        }

        else if (tic==Constants.HUMAN && tac==Constants.INDEX_UNOCCUPIED && toe==Constants.HUMAN){
            Log.d(TAG,"Requires Diagonal Block At:"+matrixIndexes.get(1));
            return new MachineMoveChoice(true,matrixIndexes.get(1),false);
        }
        else if (tic==Constants.COMPUTER && tac==Constants.INDEX_UNOCCUPIED && toe==Constants.COMPUTER){
            Log.d(TAG,"Requires Diagonal Block At:"+matrixIndexes.get(1));
            MachineMoveChoice record =  new MachineMoveChoice(false,matrixIndexes.get(1),false);
            record.setScore(true);
            return record;
        }

        else if (tic==Constants.INDEX_UNOCCUPIED && tac==Constants.HUMAN && toe==Constants.HUMAN){
            Log.d(TAG,"Requires Diagonal Block At:"+matrixIndexes.get(0));
            return new MachineMoveChoice(true,matrixIndexes.get(0),false);
        }
        else if (tic==Constants.INDEX_UNOCCUPIED && tac==Constants.COMPUTER && toe==Constants.COMPUTER){
            Log.d(TAG,"Requires Diagonal Block At:"+matrixIndexes.get(0));
            MachineMoveChoice record = new MachineMoveChoice(false,matrixIndexes.get(0),false);
            record.setScore(true);
            return record;

        }
        else {
            return getMachineOccupiablePosition(matrixIndexes);
        }
    }

    private MachineMoveChoice isBlockStrategyNecessaryVertical(String human_move_key,String mode) {
        List<String> matrixIndexes = new ArrayList<String>();
        if (mode.equals(VERTICAL_MODE)){
            matrixIndexes =  this.vertical_map.get(human_move_key);
        }
        else {
            matrixIndexes = this.horizontal_map.get(human_move_key);
        }
        int tic = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(0).charAt(1)))];
        int tac = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(1).charAt(1)))];
        int toe = game_board[Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(0)))][Integer.parseInt(String.valueOf(matrixIndexes.get(2).charAt(1)))];
        if (tic == Constants.INDEX_UNOCCUPIED  && tac == Constants.HUMAN && toe == Constants.HUMAN){
             Log.d(TAG,"Requires Vertical Block At:"+matrixIndexes.get(0));
            return new MachineMoveChoice(true,matrixIndexes.get(0),false);
        }
        else if (tic == Constants.INDEX_UNOCCUPIED  && tac == Constants.COMPUTER && toe == Constants.COMPUTER){
            Log.d(TAG,"Score Vertical Block:"+matrixIndexes.get(0));
            MachineMoveChoice  record = new MachineMoveChoice(false,matrixIndexes.get(0),false);
            record.setScore(true);
            return record;
        }
        else if(tic == Constants.HUMAN  && tac == Constants.INDEX_UNOCCUPIED && toe == Constants.HUMAN)  {
            Log.d(TAG,"Requires Vertical Block:"+matrixIndexes.get(1));
            return new MachineMoveChoice(true,matrixIndexes.get(1),false);
        }
        else if(tic == Constants.COMPUTER  && tac == Constants.INDEX_UNOCCUPIED && toe == Constants.COMPUTER) {
            Log.d(TAG,"Requires Vertical Score:"+matrixIndexes.get(1));
            MachineMoveChoice record = new MachineMoveChoice(false,matrixIndexes.get(1),false);
            record.setScore(true);
            return record;
        }
        else if (tic == Constants.HUMAN  && tac == Constants.HUMAN && toe == Constants.INDEX_UNOCCUPIED){
            Log.d(TAG,"Requires Vertical Block:"+matrixIndexes.get(2));
            return  new MachineMoveChoice(true,matrixIndexes.get(2),false);
        }
        else if (tic == Constants.COMPUTER  && tac == Constants.COMPUTER && toe == Constants.INDEX_UNOCCUPIED) {
            Log.d(TAG, "Requires Vertical Score:" + matrixIndexes.get(2));
            MachineMoveChoice record = new MachineMoveChoice(false,matrixIndexes.get(2),false);
            record.setScore(true);
            return record;
        }

        else if (tic == Constants.HUMAN && tac == Constants.INDEX_UNOCCUPIED && toe==Constants.INDEX_UNOCCUPIED) {
            String random_key = chooseRandomInTwoNumbers(new ArrayList<String>(Arrays.asList(matrixIndexes.get(1),matrixIndexes.get(2))));
            Log.d(TAG,"Doesn't Require Vertical Block:"+random_key);
            return new MachineMoveChoice(false,random_key,false);

        }
        else if (tic == Constants.INDEX_UNOCCUPIED && tac==Constants.HUMAN && toe==Constants.INDEX_UNOCCUPIED){
            String random_key = chooseRandomInTwoNumbers(new ArrayList<String>(Arrays.asList(matrixIndexes.get(0),matrixIndexes.get(2))));
            Log.d(TAG,"Doesn't Require Vertical Block:"+random_key);
            return new MachineMoveChoice(false,random_key,false);

        }
        else if (tic == Constants.INDEX_UNOCCUPIED && tac==Constants.INDEX_UNOCCUPIED && toe==Constants.HUMAN){
            String random_key = chooseRandomInTwoNumbers(new ArrayList<String>(Arrays.asList(matrixIndexes.get(0),matrixIndexes.get(1))));
            Log.d(TAG,"Doesn't Require Vertical Block:"+random_key);
            return new MachineMoveChoice(false,random_key,false);

        }
        else {
            Log.d(TAG,"Getting Occupiable position from matrixIndexes:"+matrixIndexes);
            return getMachineOccupiablePosition(matrixIndexes);
        }
    }

    private MachineMoveChoice getMachineOccupiablePosition(List<String> matrixIndexes) {
        // set the field isNull to false;
        MachineMoveChoice machine_move = new MachineMoveChoice(true);
        List<String> unoccupied_indexes = new ArrayList<String>();
        for (String key:matrixIndexes) {
            int row = Integer.parseInt(String.valueOf(key.charAt(0)));
            int column = Integer.parseInt(String.valueOf(key.charAt(1)));
            if (game_board[row][column]==Constants.INDEX_UNOCCUPIED){
               // machine_move =  new MachineMoveChoice(false,key,false);
                unoccupied_indexes.add(key);
            }

        }
        if (unoccupied_indexes.size()>0){
            Collections.shuffle(unoccupied_indexes);
            String shuffled_unoccupied_key = unoccupied_indexes.get(0);
            machine_move.setBoardKey(shuffled_unoccupied_key);
            machine_move.setScore(false);
            machine_move.setNull(false);
            machine_move.setRedAlert(false);

        }

        return machine_move;
    }

    private String chooseRandomInTwoNumbers(ArrayList<String> integers) {
        Collections.shuffle(integers);
        return integers.get(0);
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
        vertical_map.put("12",new ArrayList<String>(Arrays.asList("02","12","22")));
        vertical_map.put("22",new ArrayList<String>(Arrays.asList("02","12","22")));
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
