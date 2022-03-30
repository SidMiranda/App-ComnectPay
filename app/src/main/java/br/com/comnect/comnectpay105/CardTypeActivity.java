package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class CardTypeActivity extends AppCompatActivity {
    Button btn_credito, btn_debito;
    String valorPagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_type);

        btn_credito = findViewById(R.id.btn_credito);
        btn_debito = findViewById(R.id.btn_debito);

        Intent i = getIntent();
        valorPagamento = i.getStringExtra("VALOR");

        btn_credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {callScope("COMPRA_CREDITO");}
        });

        btn_debito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {callScope("COMPRA_DEBITO");}
        });
    }

    private void callScope(String fp){
        Intent i = new Intent(CardTypeActivity.this, CallScopePay.class);
        i.putExtra("VALOR", valorPagamento);
        i.putExtra("PEDIDO", "");
        i.putExtra("ACTION", fp);
        i.putExtra("ATRIB_APLICACAO", "");
        i.putExtra("QTD_MAX_PARCELA", "1");

        startActivityForResult(i, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}