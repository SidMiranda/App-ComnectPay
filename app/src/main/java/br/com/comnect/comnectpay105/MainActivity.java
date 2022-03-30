package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import br.com.comnect.comnectpay105.fragment.HomeActivity;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                Log.e("ServicePay", "Loading HomeActivity");
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}