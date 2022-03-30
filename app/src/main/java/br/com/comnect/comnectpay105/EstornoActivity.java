package br.com.comnect.comnectpay105;

import static java.lang.String.valueOf;
import static br.com.comnect.comnectpay105.AppDefault.getJSONFromAPI;
import static br.com.comnect.comnectpay105.AppDefault.putJSONFromAPI;
import static br.com.comnect.comnectpay105.AppDefault.goToScope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class EstornoActivity extends AppCompatActivity {
    ListView lv_pedidos;
    Button btn_refresh, btn_voltar;
    ProgressDialog load;
    String numPedido;
    int statusPedido;

    String fromList[] = {"Numero", "Forma de Pagamento", "Valor"};
    int toList[] = {R.id.pedidos_pendentes_Numero,R.id.pedidos_pendentes_pagamento, R.id.pedidos_pendentes_valor};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estorno);

        lv_pedidos = findViewById(R.id.lv_pedidos);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_voltar = findViewById(R.id.btn_voltar);

        refreshList();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
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
            return getJSONFromAPI("http://192.168.20.152/SCOPE/post-transaction.php");
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
                            String valor = pedidos.getJSONObject(i).getString("Valor");

                            Toast.makeText(EstornoActivity.this, controle + " | " + valor, Toast.LENGTH_SHORT).show();

                            estorno(controle, valor.replace(".", ""));

                            numPedido = pedidos.getJSONObject(i).getString("Numero");
                            /*String valor = pedidos.getJSONObject(i).getString("Valor").replace(",", "");
                            String fp = pedidos.getJSONObject(i).getString("Forma de Pagamento");

                            if(status.equals("pendente")){
                                setStatus(2);
                                startActivityForResult(goToScope(valor, fp), 100);
                           */

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

    public void estorno(String controle, String valor){
        Intent iEstorno = new Intent();
        iEstorno.setAction("br.com.oki.scope.ESTORNO");
        iEstorno.putExtra("VALOR", valor.replace(",", ""));
        iEstorno.putExtra("CODIGO_CONTROLE", controle);

        startActivityForResult(iEstorno, 100);
    }


    public void updateList(){
        UpdateStatus update = new UpdateStatus();
        update.execute();
    }
    private void setStatus(int status){
        statusPedido = status;
        updateList();
        load.dismiss();
    }
    private class UpdateStatus extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(EstornoActivity.this,
                    "Por favor Aguarde ...", "Atualizando Status...");
        }

        @Override
        protected String doInBackground(Void... params) {
            return putJSONFromAPI("http://192.168.20.152/API/update-status.php", statusPedido, numPedido);
        }

        @Override
        protected void onPostExecute(String list){
            load.dismiss();
        }
    }

    private class GetPedido extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(EstornoActivity.this,
                    "Por favor Aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getJSONFromAPI("http://192.168.20.152/API/get.php?numero=" + "num");
        }

        @Override
        protected void onPostExecute(String list){
            parsePedido(list);
            load.dismiss();
        }
    }
    private void parsePedido(String list){
        String jsonS = list;
        JSONObject obj = null;

        try {
            obj = new JSONObject(jsonS);
            JSONArray pedidos = obj.getJSONArray("result");

            String valor = pedidos.getJSONObject(0).getString("Valor");
            String fp = pedidos.getJSONObject(0).getString("Forma de Pagamento");

            startActivityForResult(goToScope(valor.replace(",", ""), fp), 100);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (data != null) {
                HashMap<String, Object> map = (HashMap) data.getExtras().get("DADOS_TRANSACAO");

                if (Integer.parseInt(map.get("VALOR_TRANSACAO").toString()) > 0) {
                    setStatus(3);
                    refreshList();
                    Toast.makeText(EstornoActivity.this, "Transação aprovada! " + map.get("CODIGO_CONTROLE"), Toast.LENGTH_SHORT).show();
                } else {
                    setStatus(1);
                    Toast.makeText(EstornoActivity.this, map.get("VALOR_TRANSACAO").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            setStatus(1);
            Toast.makeText(EstornoActivity.this, "Erro " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

}