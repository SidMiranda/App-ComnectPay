package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import br.com.comnect.comnectpay105.AppInitialConfig.InitialConfig;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    public static final String G700 = "GPOS700";
    public static final String Model = Build.MODEL;
    public static final String Serial = Build.SERIAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Log.e("ServicePay", "Loading initial config ");
                Intent i = new Intent(MainActivity.this, InitialConfig.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}