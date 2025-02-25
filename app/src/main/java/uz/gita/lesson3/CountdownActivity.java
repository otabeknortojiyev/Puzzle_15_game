package uz.gita.lesson3;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import uz.gita.otabek.puzzle15game.R;

public class CountdownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        playGif();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void playGif() {
        new CountDownTimer(3700, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                findViewById(R.id.giv_gif_1).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                findViewById(R.id.giv_gif_1).setVisibility(View.GONE);
                startActivity(new Intent(CountdownActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}