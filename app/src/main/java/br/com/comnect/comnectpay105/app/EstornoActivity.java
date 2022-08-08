package br.com.comnect.comnectpay105.app;

import static br.com.comnect.comnectpay105.app.AppDefault.getJSONFromAPI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.comnect.comnectpay105.R;

public class EstornoActivity extends AppCompatActivity {
    ListView lv_pedidos;
    Button btn_refresh, btn_voltar, btn_buscar;
    ProgressDialog load;
    String valorPagamento, numPedido;
    String cardNumber = "0";

    String fromList[] = {"Numero", "Forma de Pagamento", "Valor"};
    int toList[] = {R.id.pedidos_pendentes_Numero,R.id.pedidos_pendentes_pagamento, R.id.pedidos_pendentes_valor};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estorno);

        Intent i = getIntent();
        cardNumber = i.getStringExtra("NUMERO_CARTAO");
        Log.e("ServicePay", "NUMERO DO CARTAO -> " + cardNumber);

        lv_pedidos = findViewById(R.id.lv_pedidos);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_voltar = findViewById(R.id.btn_voltar);
        btn_buscar = findViewById(R.id.btn_buscar);

        refreshList();

        btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                refreshList();
            }
        });

        btn_voltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EstornoActivity.this, CardNumberActivity.class);
                startActivity(intent);
            }
        });

    }


    public void refreshList(){
        GetJson list = new GetJson();
        list.execute();
    }
    private class GetJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(EstornoActivity.this,
                    "Por favor Aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected String doInBackground(Void... params) {
            if(cardNumber == null) {
                Log.e("ServicePay", "Loading ALL 6 last payments");
                return getJSONFromAPI(routes.postTransaction);
            }else{
                Log.e("ServicePay", "Loading card " + cardNumber + " payments");
                return getJSONFromAPI(routes.postTransaction + "?cardNumber=" + cardNumber);
            }
        }

        @Override
        protected void onPostExecute(String list){
            parseList(list);
            load.dismiss();
        }
    }
    private void parseList(String list){

        try {
            ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
            JSONObject obj = new JSONObject(list);
            JSONArray pedidos = obj.getJSONArray("result");
            ArrayList<Object> listData = new ArrayList<Object>();

            if(pedidos != null){
                for(int i=0; i < pedidos.length(); i++){
                    listData.add(pedidos.get(i));
                }
            }

            for(int i=0; i < pedidos.length(); i++){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Numero", "Data -> " + pedidos.getJSONObject(i).getString("Numero"));
                hashMap.put("Forma de Pagamento", "PDV -> " + pedidos.getJSONObject(i).getString("Forma de Pagamento"));
                hashMap.put("Valor", "R$ " + pedidos.getJSONObject(i).getString("Valor").replace(".", ","));
                hashMap.put("Controle", pedidos.getJSONObject(i).getString("Controle"));
                arrayList.add(hashMap);

                lv_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {

                            String controle = pedidos.getJSONObject(i).getString("Controle");
                            valorPagamento = pedidos.getJSONObject(i).getString("Valor");
                            numPedido = pedidos.getJSONObject(i).getString("Numero");

                            Toast.makeText(EstornoActivity.this, controle + " | " + valorPagamento, Toast.LENGTH_SHORT).show();

                            callScope("ESTORNO", controle);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            SimpleAdapter adaptador = new SimpleAdapter(EstornoActivity.this, arrayList, R.layout.ultimos_pagamentos, fromList, toList);
            lv_pedidos.setAdapter(adaptador);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callScope(String fp, String controle){
        Intent i = new Intent(EstornoActivity.this, CallScopePay.class);
        i.putExtra("VALOR", valorPagamento);
        i.putExtra("PEDIDO", numPedido);
        i.putExtra("ACTION", fp);
        i.putExtra("CODIGO_CONTROLE", controle);

        startActivity(i);
    }

}