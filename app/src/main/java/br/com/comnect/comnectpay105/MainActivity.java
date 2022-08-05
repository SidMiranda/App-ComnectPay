package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import br.com.comnect.comnectpay105.AppInitialConfig.InitialConfig;
import br.com.comnect.comnectpay105.fragment.HomeActivity;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    public static final String G700 = "GPOS700";
    private static final String VERSION = "v1.0.0";
    public static String Model = Build.MODEL;
    public static String Serial = Build.SERIAL;
    //public static String Info = getINFO(); -> metodo da lib Gedi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Log.e("ServicePay", "loading main class");
                if(false) {
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    Log.e("ServicePay", "Loading HomeActivity");
                    startActivity(i);
                }else{
                    Intent i = new Intent(MainActivity.this, InitialConfig.class);
                    Log.e("ServicePay", "Loading InitialConfig");
                    startActivity(i);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}