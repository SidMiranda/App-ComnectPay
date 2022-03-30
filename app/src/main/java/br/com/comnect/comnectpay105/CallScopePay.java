package br.com.comnect.comnectpay105;

import static br.com.comnect.comnectpay105.AppDefault.putJSONFromAPI;

import android.content.Intent;
import android.os.AsyncTask;
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

    int statusPedido;

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

        goToScope(valorPagamento, action, parcela, atrAplicacao);

    }

    public void goToScope(String valor, String action, String qtd_parcela, String atr){
        Intent i = new Intent();

        String mth = "br.com.oki.scope." + action;
        Log.e("ServicePay", "setting intent -> " + mth);
        i.setAction(mth);

        i.putExtra("VALOR", valor.replace(",", ""));
        i.putExtra("QTD_MAX_PARCELA", qtd_parcela);
        i.putExtra("ATRIB_APLICACAO", atr);
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
                    setStatus(3);
                    Toast.makeText(this, "Pagamento aprovado!", Toast.LENGTH_SHORT).show();
                    Log.e("ServicePay", "transação aprovada..");
                    finish();
                } else {
                    setStatus(1);
                    Toast.makeText(this, "Erro ao processar o pagamento!", Toast.LENGTH_SHORT).show();
                    Log.e("ServicePay", "erro ao efetuar a transação..");
                    finish();
                }
            }
        } else {
            setStatus(1);
            Toast.makeText(this, "Erro ao processar o pagamento!", Toast.LENGTH_SHORT).show();
            Log.e("ServicePay", "result erro out..");
            finish();
        }
    }

    private void setStatus(int status){
        statusPedido = status;
        updateList();
    }
    public void updateList(){
        UpdateStatus update = new UpdateStatus();
        update.execute();
    }
    private class UpdateStatus extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(Void... params) {
            return putJSONFromAPI("http://192.168.20.152/API/update-status.php", statusPedido, numPedido);
        }

        @Override
        protected void onPostExecute(String list){
            startPaymentService.aux = 0;
            Log.e("ServicePay", "setting aux = " + startPaymentService.aux);
            Log.e("ServicePay", list);

        }
    }

}
