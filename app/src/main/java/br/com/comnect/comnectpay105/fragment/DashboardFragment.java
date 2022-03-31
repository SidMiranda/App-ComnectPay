package br.com.comnect.comnectpay105.fragment;

import static br.com.comnect.comnectpay105.AppDefault.getJSONFromAPI;
import static br.com.comnect.comnectpay105.AppDefault.putJSONFromAPI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.comnect.comnectpay105.CallScopePay;
import br.com.comnect.comnectpay105.CardTypeActivity;
import br.com.comnect.comnectpay105.R;

public class DashboardFragment extends Fragment {
    ListView lv_pedidos;
    Button btn_refresh;
    ProgressDialog load;
    String numPedido, valorPagamento;
    int statusPedido;

    String fromList[] = {"Numero", "Forma de Pagamento", "Valor"};
    int toList[] = {R.id.pedidos_pendentes_Numero,R.id.pedidos_pendentes_pagamento, R.id.pedidos_pendentes_valor};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        lv_pedidos = rootView.findViewById(R.id.lv_pedidos);
        btn_refresh = rootView.findViewById(R.id.btn_refresh);

        refreshList();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList();
            }
        });

        return rootView;
    }

    public void refreshList(){
        GetJson list = new GetJson();
        list.execute();
    }
    private class GetJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getActivity(),
                    "Por favor Aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getJSONFromAPI("http://192.168.20.152/API/get-pendentes.php");
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
                hashMap.put("Numero", "Numero -> " + pedidos.getJSONObject(i).getString("Numero"));
                hashMap.put("Forma de Pagamento", pedidos.getJSONObject(i).getString("Forma de Pagamento"));
                hashMap.put("Valor", "R$ " + pedidos.getJSONObject(i).getString("Valor"));
                arrayList.add(hashMap);

                lv_pedidos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {

                            numPedido = pedidos.getJSONObject(i).getString("Numero");
                            valorPagamento = pedidos.getJSONObject(i).getString("Valor");
                            String fp = pedidos.getJSONObject(i).getString("Forma de Pagamento");
                            String status = pedidos.getJSONObject(i).getString("Status");

                            if(status.equals("pendente")){
                                callScope(fp);
                            }else{
                                Toast.makeText(getActivity(), "Pedido não esta mais disponivel! | " + status, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }

            SimpleAdapter adaptador = new SimpleAdapter(getActivity(), arrayList, R.layout.pedidos_pendentes, fromList, toList);
            lv_pedidos.setAdapter(adaptador);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callScope(String fp){
        Intent i = new Intent(getContext(), CallScopePay.class);
        i.putExtra("VALOR", valorPagamento);
        i.putExtra("PEDIDO", "");
        i.putExtra("ACTION", fp);
        i.putExtra("ATRIB_APLICACAO", "");
        i.putExtra("QTD_MAX_PARCELA", "1");

        startActivity(i);
    }
}