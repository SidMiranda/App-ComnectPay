package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class CardTypeActivity extends AppCompatActivity {
    Button btn_credito, btn_debito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_type);

        btn_credito = findViewById(R.id.btn_credito);
        btn_debito = findViewById(R.id.btn_debito);

        Intent i = getIntent();
        String valorPagamento = i.getStringExtra("VALOR");

        btn_credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("br.com.oki.scope.COMPRA_CREDITO");
                intent.putExtra("VALOR", valorPagamento);
                intent.putExtra("QTD_MAX_PARCELA", "1");
                intent.putExtra("APP_TEMA", "APP_TEMA_AZUL");
                startActivityForResult(intent, 100);
            }
        });

        btn_debito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("br.com.oki.scope.COMPRA_DEBITO");
                intent.putExtra("VALOR", valorPagamento);
                intent.putExtra("QTD_MAX_PARCELA", "1");
                intent.putExtra("APP_TEMA", "APP_TEMA_AZUL");
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (data != null) {
                HashMap<String, Object> map = (HashMap) data.getExtras().get("DADOS_TRANSACAO");

                if (Integer.parseInt(map.get("VALOR_TRANSACAO").toString()) > 0) {
                    Toast.makeText(this, "Transação aprovada! " + map.get("CODIGO_CONTROLE"), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Erro " + resultCode, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}