package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CardNumberActivity extends AppCompatActivity {
    EditText edt_6n, edt_4n;
    Button btn_buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_number);

        Log.e("ServicePay", "Creating Card Number View");

        edt_6n = findViewById(R.id.edt_6n);
        edt_4n = findViewById(R.id.edt_4n);
        btn_buscar = findViewById(R.id.btn_buscar);

        btn_buscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String cardNumber = edt_4n.getText().toString();
                Intent i = new Intent(CardNumberActivity.this, EstornoActivity.class);
                i.putExtra("NUMERO_CARTAO", cardNumber);
                startActivity(i);
            }
        });
    }
}