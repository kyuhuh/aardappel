package nl.windesheim.capturetheclue;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PlayActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Button b = (Button) findViewById(R.id.button_random);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("DEBUG", "CLICKED " + v.getId());
    }
}
