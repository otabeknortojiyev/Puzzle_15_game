package uz.gita.lesson3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

import uz.gita.otabek.puzzle15game.R;

public class MainActivity extends AppCompatActivity {
    private static final int CELL_COUNT = 4;
    private TextView textScore;
    private Chronometer textTime;
    private TextView[][] items;
    private ArrayList<Integer> numbers;
    private CoordinateData emptySpace;
    private int score;
    private MediaPlayer mediaWin;
    private boolean isPlaying;
    private SharedPreferences preferences;
    private Vibrator vibrator;
    public MediaPlayer music;
    public MediaPlayer clickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        music = MediaPlayer.create(this, R.raw.music);
        clickMedia = MediaPlayer.create(this, R.raw.click_voice);

        preferences = getSharedPreferences("app", MODE_PRIVATE);
        if (preferences.getString("numbers", "empty").equals("empty")) {
            initData();
            loadViews();
            loadDataToView();
        } else {
            initData();
            loadViews();
            loadDataToView();
            String[] numbers = preferences.getString("numbers", "").split("\\.");
            for (int i = 0; i < numbers.length; i++) {
                int y = i / CELL_COUNT;
                int x = i % CELL_COUNT;
                if (numbers[i].isEmpty()) {
                    items[y][x].setText(numbers[i]);
                    emptySpace.setX(x);
                    emptySpace.setY(y);
                    items[y][x].setBackgroundResource(R.drawable.db_rounded_background_grey);
                } else {
                    items[y][x].setText(numbers[i]);
                    items[y][x].setBackgroundResource(R.drawable.db_rounded_background_white);
                }
            }
            score = Integer.parseInt(preferences.getString("score", "0"));
            textScore.setText(String.valueOf(score));
            textTime.setBase(Long.parseLong(preferences.getString("time", "0")));
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadDataToView() {
        Collections.shuffle(numbers);
        items[emptySpace.getY()][emptySpace.getX()].setText("");

        for (int i = 0; i < numbers.size(); i++) {
            int y = i / CELL_COUNT;
            int x = i % CELL_COUNT;
            items[y][x].setText(numbers.get(i).toString());
        }

        items[CELL_COUNT - 1][CELL_COUNT - 1].setText("");

        emptySpace = new CoordinateData(CELL_COUNT - 1, CELL_COUNT - 1);
        score = 0;
        textScore.setText("0");
        textTime.setBase(SystemClock.elapsedRealtime());
        textTime.start();
        for (int i = 0; i < CELL_COUNT; i++) {
            for (int j = 0; j < CELL_COUNT; j++) {
                if (!items[i][j].getText().equals("")) {
                    items[i][j].setBackgroundResource(R.drawable.db_rounded_background_white);
                } else items[i][j].setBackgroundResource(R.drawable.db_rounded_background_grey);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        music.stop();
        outState.putLong("time", textTime.getBase());
        outState.putInt("score", score);
        ArrayList<String> arrayList = new ArrayList<>(16);
        for (int i = 0; i < CELL_COUNT; i++) {
            for (int j = 0; j < CELL_COUNT; j++) {
                arrayList.add(items[i][j].getText().toString());
            }
        }
        outState.putStringArrayList("items", arrayList);
        outState.putInt("emptySpaceX", emptySpace.getX());
        outState.putInt("emptySpaceY", emptySpace.getY());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textTime.setBase(savedInstanceState.getLong("time"));
        score = savedInstanceState.getInt("score");
        textScore.setText(String.valueOf(score));

        music.start();
        ArrayList<String> arrayList = savedInstanceState.getStringArrayList("items");
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                int y = i / CELL_COUNT;
                int x = i % CELL_COUNT;
                items[y][x].setText(arrayList.get(i));
                if (items[y][x].getText().equals("")) {
                    emptySpace.setX(savedInstanceState.getInt("emptySpaceX"));
                    emptySpace.setY(savedInstanceState.getInt("emptySpaceY"));
                    items[y][x].setBackgroundResource(R.drawable.db_rounded_background_grey);
                } else items[y][x].setBackgroundResource(R.drawable.db_rounded_background_white);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        music.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveResult();
    }

    private void initData() {
        items = new TextView[CELL_COUNT][CELL_COUNT];
        numbers = new ArrayList<>();
        for (int i = 1; i < CELL_COUNT * CELL_COUNT; i++) {
            numbers.add(i);
        }
        emptySpace = new CoordinateData(CELL_COUNT - 1, CELL_COUNT - 1);
        score = 0;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        textScore = findViewById(R.id.text_score_main);
        textTime = findViewById(R.id.text_time_main);
        mediaWin = MediaPlayer.create(this, R.raw.win_voice);
        music.start();
        music.setLooping(true);
        isPlaying = true;
    }

    private void loadViews() {
        ViewGroup group = findViewById(R.id.relativeLayout);
        for (int i = 0; i < group.getChildCount(); i++) {
            TextView button = (TextView) group.getChildAt(i);
            int y = i / CELL_COUNT;
            int x = i % CELL_COUNT;
            button.setTag(new CoordinateData(x, y));
            items[y][x] = button;
            button.setOnClickListener(this::itemClick);
        }

        findViewById(R.id.btBackToHome).setOnClickListener(v -> {
            music.stop();
            clickMedia.start();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        });

        findViewById(R.id.btRestart).setOnClickListener(v -> {
            clickMedia.start();
            restartGame();
        });

        ImageView imageView = findViewById(R.id.bt_play_song);
        imageView.setImageResource(R.drawable.db_play);
        imageView.setOnClickListener(v -> {
            if (isPlaying) {
                imageView.setImageResource(R.drawable.db_pause);
                music.pause();
                isPlaying = false;
            } else {
                imageView.setImageResource(R.drawable.db_play);
                music.start();
                isPlaying = true;
            }
        });
    }

    private void restartGame() {
        preferences.edit().putString("numbers", "empty").apply();
        loadDataToView();
    }

    private void itemClick(View view) {
        TextView button = (TextView) view;
        CoordinateData c = (CoordinateData) button.getTag();
        int diffX = Math.abs(emptySpace.getX() - c.getX());
        int diffY = Math.abs(emptySpace.getY() - c.getY());
        if (diffY + diffX == 1) {
            TextView emptyButton = items[emptySpace.getY()][emptySpace.getX()];
            emptyButton.setText(button.getText());
            emptyButton.setBackgroundResource(R.drawable.db_rounded_background_white);
            button.setBackgroundResource(R.drawable.db_rounded_background_grey);
            button.setText("");
            emptySpace = c;
            textScore.setText(String.valueOf(++score));
        }
        saveResult();
        if (isStuck()) {
            vibrator.vibrate(200);
            Toast.makeText(this, "There is no way to win in this situation.\nTry again!", Toast.LENGTH_SHORT).show();
        }
        if (isWin()) {
            String history = preferences.getString("history", "");
            StringBuilder builder = new StringBuilder();
            if (history.isEmpty()) {
                builder.append(score);
                builder.append('-');
                builder.append(textTime.getText().toString());
            } else {
                builder.append(history);
                builder.append('.');
                builder.append(score);
                builder.append('-');
                builder.append(textTime.getText().toString());
            }
            preferences.edit().putString("history", builder.toString()).apply();
            preferences.edit().putString("numbers", "empty").apply();

            findViewById(R.id.fl_main_layout).setClickable(false);
            findViewById(R.id.ll_main_layout).setClickable(false);
            findViewById(R.id.fl_top_layout).setClickable(false);
            findViewById(R.id.bt_play_song).setClickable(false);
            findViewById(R.id.btBackToHome).setClickable(false);
            findViewById(R.id.tv_title).setClickable(false);
            findViewById(R.id.fl_bottom_layout).setClickable(false);
            findViewById(R.id.bt_step).setClickable(false);
            findViewById(R.id.text_step).setClickable(false);
            findViewById(R.id.text_score_main).setClickable(false);
            findViewById(R.id.btRestart).setClickable(false);
            findViewById(R.id.reload).setClickable(false);
            findViewById(R.id.fl_game_layout).setClickable(false);
            findViewById(R.id.relativeLayout).setClickable(false);
            findViewById(R.id.btn1).setClickable(false);
            findViewById(R.id.btn2).setClickable(false);
            findViewById(R.id.btn3).setClickable(false);
            findViewById(R.id.btn4).setClickable(false);
            findViewById(R.id.btn5).setClickable(false);
            findViewById(R.id.btn6).setClickable(false);
            findViewById(R.id.btn7).setClickable(false);
            findViewById(R.id.btn8).setClickable(false);
            findViewById(R.id.btn9).setClickable(false);
            findViewById(R.id.btn10).setClickable(false);
            findViewById(R.id.btn11).setClickable(false);
            findViewById(R.id.btn12).setClickable(false);
            findViewById(R.id.btn13).setClickable(false);
            findViewById(R.id.btn14).setClickable(false);
            findViewById(R.id.btn15).setClickable(false);
            findViewById(R.id.btn16).setClickable(false);
            findViewById(R.id.btTimer).setClickable(false);
            findViewById(R.id.text_time_main).setClickable(false);
            findViewById(R.id.iv_win_bg).setClickable(false);
            findViewById(R.id.giv_gif).setClickable(false);

            music.stop();
            mediaWin.start();
            textTime.stop();
            findViewById(R.id.giv_gif).setVisibility(View.VISIBLE);
            @SuppressLint("ResourceType") Animation shake = AnimationUtils.loadAnimation(this, R.drawable.db_shake);
            shake.setRepeatCount(100);
            findViewById(R.id.iv_win_bg).setVisibility(View.VISIBLE);

            Intent intent = new Intent(this, WinActivity.class);
            intent.putExtra("score", String.valueOf(score));
            intent.putExtra("time", textTime.getText().toString());
            new CountDownTimer(3500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            items[i][j].startAnimation(shake);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
        clickMedia.start();
    }

    private boolean isWin() {
        if (emptySpace.getX() != CELL_COUNT - 1 || emptySpace.getY() != CELL_COUNT - 1)
            return false;
        for (int i = 0; i < CELL_COUNT * CELL_COUNT - 1; i++) {
            int y = i / CELL_COUNT;
            int x = i % CELL_COUNT;
            TextView textView = items[y][x];
            if (!textView.getText().equals(String.valueOf(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private boolean isStuck() {
        return items[0 / CELL_COUNT][0 % CELL_COUNT].getText().equals("1") && items[1 / CELL_COUNT][1 % CELL_COUNT].getText().equals("2") && items[2 / CELL_COUNT][2 % CELL_COUNT].getText().equals("3") && items[3 / CELL_COUNT][3 % CELL_COUNT].getText().equals("4") && items[4 / CELL_COUNT][4 % CELL_COUNT].getText().equals("5") && items[5 / CELL_COUNT][5 % CELL_COUNT].getText().equals("6") && items[6 / CELL_COUNT][6 % CELL_COUNT].getText().equals("7") && items[7 / CELL_COUNT][7 % CELL_COUNT].getText().equals("8") && items[8 / CELL_COUNT][8 % CELL_COUNT].getText().equals("9") && items[9 / CELL_COUNT][9 % CELL_COUNT].getText().equals("10") && items[10 / CELL_COUNT][10 % CELL_COUNT].getText().equals("11") && items[11 / CELL_COUNT][11 % CELL_COUNT].getText().equals("12") && items[12 / CELL_COUNT][12 % CELL_COUNT].getText().equals("13") && items[13 / CELL_COUNT][13 % CELL_COUNT].getText().equals("15") && items[14 / CELL_COUNT][14 % CELL_COUNT].getText().equals("14");
    }

    private void saveResult() {
        if (score != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < CELL_COUNT; i++) {
                for (int j = 0; j < CELL_COUNT; j++) {
                    stringBuilder.append(items[i][j].getText());
                    stringBuilder.append('.');
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            preferences.edit().putString("numbers", stringBuilder.toString()).apply();
            preferences.edit().putString("score", String.valueOf(score)).apply();
            preferences.edit().putString("time", String.valueOf(textTime.getBase())).apply();
        }
    }
}
