package br.com.comnect.comnectpay105;

import static br.com.comnect.comnectpay105.AppDefault.setStatus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class CallScopePay extends AppCompatActivity {
    String  numPedido,
            valorPagamento,
            parcela,
            action,
            atrAplicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ServicePay", "opening CallScopePay...");

        Intent i = getIntent();
        valorPagamento = i.getStringExtra("VALOR");
        parcela = i.getStringExtra("QTD_MAX_PARCELA");
        numPedido = i.getStringExtra("PEDIDO");
        action = i.getStringExtra("ACTION");
        atrAplicacao = i.getStringExtra("ATRIB_APLICACAO");

        startPaymentService.aux = 1;
        Log.e("ServicePay", "stopping service CONSULTA_API");

        goToScope(valorPagamento, action, parcela, atrAplicacao);

    }

    public void goToScope(String valor, String action, String qtd_parcela, String atr){
        Intent i = new Intent();

        valor = valor.replace(".", "");
        valor = valor.replace(",", "");

        String mth = "br.com.oki.scope." + action;
        Log.e("ServicePay", "setting intent -> " + mth);
        i.setAction(mth);

        i.putExtra("VALOR", valor);
        i.putExtra("QTD_MAX_PARCELA", qtd_parcela);
        i.putExtra("ATRIB_APLICACAO", "05010042022"); // exemplo 02 01 014 (11) 6097-1234 02 006 123456
        i.putExtra("APP_TEMA", "APP_TEMA_AZUL");

        Log.e("ServicePay", "waiting for result..");

        startActivityForResult(i, 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("ServicePay", "request code -> " + requestCode);
        Log.e("ServicePay", "result code -> " + resultCode);
        Log.e("ServicePay", "data -> " + data);

        if (resultCode == 0) {
            if (data != null) {
                HashMap<String, Object> map = (HashMap) data.getExtras().get("DADOS_TRANSACAO");
                Log.e("ServicePay", "result with data ok");
                if (Integer.parseInt(map.get("VALOR_TRANSACAO").toString()) > 0) {
                    setStatus(3, numPedido);
                    Toast.makeText(this, "Pagamento aprovado!", Toast.LENGTH_SHORT).show();
                    Log.e("ServicePay", "transação aprovada..");
                    finish();
                } else {
                    setStatus(1, numPedido);
                    Toast.makeText(this, "Erro ao processar o pagamento!", Toast.LENGTH_SHORT).show();
                    Log.e("ServicePay", "erro ao efetuar a transação..");
                    finish();
                }
            }
        } else {
            setStatus(1, numPedido);
            Toast.makeText(this, "Erro ao processar o pagamento!", Toast.LENGTH_SHORT).show();
            Log.e("ServicePay", "result erro out..");
            finish();
        }
    }
}

