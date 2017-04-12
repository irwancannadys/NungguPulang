package com.domikado.nunggupulang;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splashscreen extends AppCompatActivity {

    private  final int DISPLAY_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                Splashscreen.this.startActivity(intent);
                Splashscreen.this.finish();
            }
        }, DISPLAY_TIME);
    }
}
