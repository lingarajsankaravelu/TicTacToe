package lingaraj.hourglass.in.tictactoe.activities;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lingaraj.hourglass.in.tictactoe.Constants;
import lingaraj.hourglass.in.tictactoe.Libraries.TicTacToe;
import lingaraj.hourglass.in.tictactoe.R;
import lingaraj.hourglass.in.tictactoe.TicTacToeInterface;
import lingaraj.hourglass.in.tictactoe.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements TicTacToeInterface {

    ActivityMainBinding activity_binding;
    final String TAG = "ACTMAIN";
    Map<String,ImageView> tiles_map = new HashMap<>();
    TicTacToe ticTacToe;
    int total_tiles = 9;
    int current_index = 0;
    private int player_mode = Constants.HUMAN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        ticTacToe = new TicTacToe(MainActivity.this);
        setInitalData();

    }

    private void setInitalData() {
        setScoreBoard();
        showGameBoard();
        enableGameBoardTiles();

    }

    public void gameBoardTilesClick(View view){
        switch (view.getId()){
            case R.id.zero_zero:
                setTilesImage("00",activity_binding.tiles.zeroZero);
                ticTacToe.addPlayerMove(0,0,player_mode);
                break;
            case R.id.zero_one:
                setTilesImage("01",activity_binding.tiles.zeroOne);
                ticTacToe.addPlayerMove(0,1,player_mode);
                break;
            case R.id.zero_two:
                setTilesImage("02",activity_binding.tiles.zeroTwo);
                ticTacToe.addPlayerMove(0,2,player_mode);
                break;
            case R.id.one_zero:
                setTilesImage("10",activity_binding.tiles.oneZero);
                ticTacToe.addPlayerMove(1,0,player_mode);
                break;
            case R.id.one_one:
                setTilesImage("11",activity_binding.tiles.oneOne);
                ticTacToe.addPlayerMove(1,1,player_mode);
                break;
            case R.id.one_two:
                setTilesImage("12",activity_binding.tiles.oneTwo);
                ticTacToe.addPlayerMove(1,2,player_mode);
                break;
            case R.id.two_zero:
                setTilesImage("20",activity_binding.tiles.twoZero);
                ticTacToe.addPlayerMove(2,0,player_mode);
                break;
            case R.id.two_one:
                setTilesImage("21",activity_binding.tiles.twoOne);
                ticTacToe.addPlayerMove(2,1,player_mode);
                break;
            case R.id.two_two:
                setTilesImage("22",activity_binding.tiles.twoTwo);
                ticTacToe.addPlayerMove(2,2,player_mode);
                break;

        }
    }

    private void setTilesImage(String key,ImageView image_view) {
        image_view.setClickable(false);
        if (player_mode == Constants.HUMAN){
            image_view.setImageResource(R.mipmap.ic_human_marker);
            activity_binding.tiles.gameBoardLayout.setEnabled(false);
        }
        else {
            image_view.setImageResource(R.mipmap.ic_computer_marker);
            activity_binding.tiles.gameBoardLayout.setEnabled(true);

        }
        this.tiles_map.put(key,image_view);

    }

    public void tryAgain(View view){
        resetTilesMap();
        ticTacToe.resetGameBoard();
        setScoreBoard();
        //reset index to start with zero
        this.current_index = 0;
        //always starts with human as first player
        this.player_mode = Constants.HUMAN;
        showGameBoard();
        Log.d(TAG,"try again clicked");
    }

    private void resetTilesMap() {
        List<String> keyset = new ArrayList<String>(this.tiles_map.keySet());
        for (String key:keyset) {
            this.tiles_map.get(key).setClickable(true);
            this.tiles_map.get(key).setImageResource(R.mipmap.ic_default_marker);
            this.tiles_map.remove(key);
            Log.d(TAG,"Removed Key from Tiles Map:"+key);
        }
    }

    public void showGameBoard(){
        activity_binding.screen.getRoot().setVisibility(View.GONE);
        activity_binding.tiles.getRoot().setVisibility(View.VISIBLE);
        Log.d(TAG,"Game Board Visible");
    }

    public void showRetry(){
        activity_binding.tiles.getRoot().setVisibility(View.GONE);
        activity_binding.screen.getRoot().setVisibility(View.VISIBLE);
        Log.d(TAG,"Game Board Visible");

    }

    @Override
    public void endGame(boolean is_tie, String score_key, int player) {
        Log.d(TAG,"End Game Called show");
        if (is_tie){
            ticTacToe.increementTieScore();
            setScoreBoard();
            showToast(getString(R.string.tie_toast));

        }
        else {
            if (player==Constants.COMPUTER){
                setScoreBoard();
                showToast(getString(R.string.player_lost));
            }
            else {
                setScoreBoard();
                showToast(getString(R.string.player_won));

            }

        }
        disableGameBoardTiles();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showRetry();

            }
        },2000);
    }

    private void disableGameBoardTiles() {
        activity_binding.tiles.gameBoardLayout.setEnabled(false);
    }

    private void enableGameBoardTiles(){
        activity_binding.tiles.gameBoardLayout.setEnabled(true);
    }
    private void setScoreBoard() {
        activity_binding.tiles.computerScoreCount.setText(String.valueOf(ticTacToe.getComputer_score()));
        activity_binding.tiles.playerScoreView.setText(String.valueOf(ticTacToe.getPlayer_score()));
        activity_binding.tiles.tieCountView.setText(String.valueOf(ticTacToe.getTie_score()));
        Log.d(TAG,"Scored Board Updated");
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Showing End game toast message");
    }

    @Override
    public void nextPlayerMove(int previously_played_player) {
        navigateNext(previously_played_player);
    }

    private void navigateNext(int previously_played_player) {
        if (current_index+1<total_tiles){
                changePlayerTurn();

            current_index = current_index + 1;
                if (player_mode==Constants.HUMAN){
                    setPlayerInfo(getString(R.string.human_turn));
                }
                else {
                    setPlayerInfo(getString(R.string.computer_turn));
                    String computer_choosen_key = ticTacToe.generateMachineMove();
                    if (computer_choosen_key.equals("NORESULT")){
                        setPlayerInfo(getString(R.string.generate_machine_move_error));
                    }else {
                        computerMove(computer_choosen_key);
                    }
                }
        }
        else {
            boolean is_tie = true;
            endGame(is_tie, "isTie",-1);
        }

    }

    private void changePlayerTurn() {
        if (this.player_mode == Constants.HUMAN){
            this.player_mode = Constants.COMPUTER;
        }
        else {
            this.player_mode = Constants.HUMAN;
        }
        Log.d(TAG,"Player Turn Switched");
    }

    private void computerMove(String computer_choosen_key) {
        switch (computer_choosen_key) {
            case "00":
                gameBoardTilesClick(activity_binding.tiles.zeroZero);
                break;
            case "01":
                gameBoardTilesClick(activity_binding.tiles.zeroOne);
                break;
            case "02":
                gameBoardTilesClick(activity_binding.tiles.zeroTwo);
                break;
            case "10":
                gameBoardTilesClick(activity_binding.tiles.oneZero);
                break;
            case "11":
                gameBoardTilesClick(activity_binding.tiles.oneOne);
                break;
            case "12":
                gameBoardTilesClick(activity_binding.tiles.oneTwo);
                break;
            case "20":
                gameBoardTilesClick(activity_binding.tiles.twoZero);
                break;
            case "21":
                gameBoardTilesClick(activity_binding.tiles.twoOne);
                break;
            case "22":
                gameBoardTilesClick(activity_binding.tiles.twoTwo);
                break;
            default:
                return;
        }





    }
    private void setPlayerInfo(String player_info) {
        activity_binding.tiles.gameStatus.setText(player_info);

    }



}
