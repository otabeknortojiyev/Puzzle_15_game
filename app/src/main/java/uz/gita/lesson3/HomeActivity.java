package uz.gita.lesson3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import uz.gita.otabek.puzzle15game.R;

public class HomeActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    public MediaPlayer clickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        clickMedia = MediaPlayer.create(this, R.raw.click_voice);

        preferences = this.getSharedPreferences("app", Context.MODE_PRIVATE);

        if ((preferences.getString("numbers", "empty").equals("empty"))) {
            findViewById(R.id.bt_continue).setVisibility(View.INVISIBLE);
        } else findViewById(R.id.bt_continue).setVisibility(View.VISIBLE);

        findViewById(R.id.bt_play).setOnClickListener(v -> {
            clickMedia.start();
            preferences.edit().putInt("score", 0).apply();
            preferences.edit().putString("time", "").apply();
            preferences.edit().putString("numbers", "empty").apply();
            startActivity(new Intent(HomeActivity.this, CountdownActivity.class));
            finish();
        });

        findViewById(R.id.bt_continue).setOnClickListener(v -> {
            clickMedia.start();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });

        findViewById(R.id.bt_leaderBoard).setOnClickListener(v -> {
            clickMedia.start();
            startActivity(new Intent(HomeActivity.this, LeaderBoardActivity.class));
        });

        findViewById(R.id.bt_settings).setOnClickListener(v -> {
            clickMedia.start();
            Toast.makeText(this, "Yaqinda paydo bo'ladi", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.bt_info).setOnClickListener(v -> {
            clickMedia.start();
            startActivity(new Intent(HomeActivity.this, InfoActivity.class));
        });

        findViewById(R.id.bt_quit).setOnClickListener(v -> {
            clickMedia.start();
            finishAffinity();
        });

        @SuppressLint("ResourceType") Animation shake = AnimationUtils.loadAnimation(this, R.drawable.db_shake);
        findViewById(R.id.iv_logo).setAnimation(shake);
        shake.setRepeatCount(15);
    }
}