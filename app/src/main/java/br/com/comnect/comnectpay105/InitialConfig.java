package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.telephony.TelephonyManagerCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InitialConfig extends AppCompatActivity {

    TextView msg, txt_serial;
    Button btn_init;

    //final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_config);

        msg = findViewById(R.id.txt_msg);
        txt_serial = findViewById(R.id.txt_serial);
        btn_init = findViewById(R.id.btn_init);

        msg.setText("POS não configurado!");

        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_init.setText("PRONTO");
                btn_init.setTextSize(30);

                txt_serial.setText(Build.SERIAL);
                txt_serial.setVisibility(view.VISIBLE);

                msg.setTextSize(20);
                msg.setText("Acesse portal.comnect.com.br e configure um novo terminal com o seguinte numero de série:");

                Toast.makeText(InitialConfig.this, "Iniciando configuração...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}