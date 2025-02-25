package uz.gita.lesson3;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import uz.gita.otabek.puzzle15game.R;

public class InfoActivity extends AppCompatActivity {
    public MediaPlayer clickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        clickMedia = MediaPlayer.create(this, R.raw.click_voice);

        findViewById(R.id.btBackToHome).setOnClickListener(v -> {
            clickMedia.start();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}