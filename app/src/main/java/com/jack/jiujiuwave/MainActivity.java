package com.jack.jiujiuwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jack.jiujiuwave.view.WaveCircleView;
import com.jack.jiujiuwave.view.WaveLayout;

public class MainActivity extends AppCompatActivity {
    private WaveLayout wl_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wl_content = (WaveLayout) findViewById(R.id.wl_content);
        wl_content.postDelayed(waveRunable,1000);
    }

    private Runnable waveRunable = new Runnable() {
        @Override
        public void run() {
            WaveCircleView waveCircleView = new WaveCircleView(MainActivity.this);
            wl_content.addCircleView(waveCircleView);
            if (wl_content.getChildCount() < 10) {
                wl_content.postDelayed(this, 1000);
            }
        }
    };

}
