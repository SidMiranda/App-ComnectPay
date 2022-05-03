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
            atrAplicacao,
            codControle;

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
        codControle = i.getStringExtra("CODIGO_CONTROLE");

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

        if(qtd_parcela != null)
            i.putExtra("QTD_MAX_PARCELA", qtd_parcela);

        // exemplo 02 01 014 (11) 6097-1234 02 006 123456
        atr = "01010042022";

        if(atr != null)
            i.putExtra("ATRIB_APLICACAO", atr);

        if(codControle != null)
            i.putExtra("CODIGO_CONTROLE", codControle);

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

                    Intent i = new Intent(this, ImpressaoActivity.class);
                    i.putExtra("CODIGO_CONTROLE", map.get("CODIGO_CONTROLE").toString());
                    i.putExtra("CNPJ", map.get("DADOS_CNPJ_REDE_SAT").toString());
                    i.putExtra("CODIGO_AUTH", map.get("CODIGO_AUTORIZACAO").toString());
                    i.putExtra("VALOR", map.get("VALOR_TRANSACAO").toString());
                    i.putExtra("BANDEIRA", map.get("NOME_BANDEIRA").toString());
                    i.putExtra("CARTAO", map.get("NUMERO_CARTAO").toString());
                    i.putExtra("DATA", map.get("DATA_LOCAL_TRANSACAO").toString() + " " + map.get("HORA_LOCAL_TRANSACAO").toString());
                    startActivity(i);

                    Log.e("ServicePay", "CODIGO DE CONTROLE -> " + map.get("CODIGO_CONTROLE").toString());
                    setStatus(3, numPedido, map.get("CODIGO_CONTROLE").toString());
                    Toast.makeText(this,  action + " OK!", Toast.LENGTH_SHORT).show();
                    Log.e("ServicePay", "transação aprovada..");
                    finish();
                } else {
                    setStatus(1, numPedido, "0");
                    Toast.makeText(this, "Erro ao processar o pagamento!", Toast.LENGTH_SHORT).show();
                    Log.e("ServicePay", "erro ao efetuar a transação..");
                    finish();
                }
            }
        } else {
            setStatus(1, numPedido, "0");
            Toast.makeText(this, "Erro ao processar o pagamento!", Toast.LENGTH_SHORT).show();
            Log.e("ServicePay", "result erro out..");
            finish();
        }
    }
}

