package indi.seven.android.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import indi.seven.android.utils.json.DanceJsonParser;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void press(View view){
        int id = view.getId();
        switch (id){
            case R.id.read_btn:
                DanceJsonParser parser = new DanceJsonParser();
                parser.open(getApplicationContext());
                parser.parse();
                break;
        }
    }
}
