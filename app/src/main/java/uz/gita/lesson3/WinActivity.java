package uz.gita.lesson3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uz.gita.otabek.puzzle15game.R;

public class WinActivity extends AppCompatActivity {
    private String score;
    private String time;
    private SharedPreferences preferences;
    public MediaPlayer clickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        clickMedia = MediaPlayer.create(this, R.raw.click_voice);

        init();
        loadViews();
    }

    private void init() {
        preferences = this.getSharedPreferences("app", MODE_PRIVATE);
    }

    private void loadViews() {
        score = getIntent().getStringExtra("score");
        time = getIntent().getStringExtra("time");

        TextView textView = findViewById(R.id.text_score_win);
        textView.setText(score);
        TextView textView1 = findViewById(R.id.text_time_win);
        textView1.setText(time);

        FrameLayout frameLayout1 = findViewById(R.id.bt_restart);
        frameLayout1.setOnClickListener(v -> {
            clickMedia.start();
            preferences.edit().putString("numbers", "empty").apply();
            startActivity(new Intent(WinActivity.this, MainActivity.class));
            finish();
        });

        FrameLayout frameLayout2 = findViewById(R.id.bt_home);
        frameLayout2.setOnClickListener(v -> {
            clickMedia.start();
            preferences.edit().putString("numbers", "empty").apply();
            startActivity(new Intent(WinActivity.this, HomeActivity.class));
            finish();
        });

        RatingBar ratingBar = findViewById(R.id.ratingBar_id);
        ratingBar.setOnClickListener(v -> clickMedia.start());
    }
}