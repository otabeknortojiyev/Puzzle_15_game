package uz.gita.lesson3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import uz.gita.otabek.puzzle15game.R;

public class LeaderBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);

        String history = preferences.getString("history", "");

        if (!history.isEmpty()) {
            String[] scores = history.split("\\.");
            for (int i = 0; i < scores.length - 1; i++) {
                for (int j = i + 1; j < scores.length; j++) {
                    if (Integer.parseInt(scores[i].substring(0, scores[i].indexOf("-"))) > Integer.parseInt(scores[j].substring(0, scores[j].indexOf("-")))) {
                        String temp = scores[i];
                        scores[i] = scores[j];
                        scores[j] = temp;
                    }
                }
            }
            if (scores.length > 0 && scores.length < 21) {
                for (int i = 0; i < scores.length; i++) {
                    LinearLayout linearLayout = findViewById(R.id.ll_main_leader_board);
                    CardView cardView = (CardView) linearLayout.getChildAt(i);
                    cardView.setVisibility(View.VISIBLE);

                    FrameLayout frameLayout = (FrameLayout) cardView.getChildAt(0);
                    TextView textViewScore = (TextView) frameLayout.getChildAt(2);
                    textViewScore.setText(scores[i].substring(0, scores[i].indexOf("-")));
                    TextView textViewTime = (TextView) frameLayout.getChildAt(1);
                    textViewTime.setText(scores[i].substring(scores[i].indexOf("-") + 1));
                }
            }
        }
    }
}