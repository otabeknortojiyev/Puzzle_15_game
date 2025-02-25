package uz.gita.lesson3;

import android.app.Application;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class App extends Application {
    public static Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }
}
