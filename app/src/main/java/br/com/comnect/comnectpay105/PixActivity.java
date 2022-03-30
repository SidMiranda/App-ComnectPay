package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class PixActivity extends AppCompatActivity {
    Button btn_ok;
    TextView txt_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pix);

        btn_ok = findViewById(R.id.btn_ok);
        txt_value = findViewById(R.id.txt_value);

        Intent i = getIntent();
        String valor = i.getStringExtra("VALUE");
        String valorFormatado = valor.replace(",", "");

        if(Integer.parseInt(valorFormatado) > 0){
            txt_value.setText("R$ " + valor);
        }else{
            txt_value.setText("R$ 0,00");
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}