package br.com.comnect.comnectpay105.app;

import static br.com.comnect.comnectpay105.app.AppDefault.getJSONFromAPI;
import static br.com.comnect.comnectpay105.app.routes.getPendentes;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class startPaymentService extends Service {
    String pdv = "3";
    String valor, fp, pedido;
    public static int aux = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.e("ServicePay", "new thread created");
                        while (true) {
                            if(aux == 0){
                                verificaPagamento();
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void verificaPagamento(){
        checkPdv check = new checkPdv();
        check.execute();
    }
    private class checkPdv extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(Void... params) {
            return getJSONFromAPI(getPendentes + "?pdv=" + pdv);
        }

        @Override
        protected void onPostExecute(String list){
            parseList(list);
        }
    }
    private void parseList(String list){
        String jsonS = list;
        JSONObject obj = null;

        try {
            obj = new JSONObject(jsonS);
            JSONArray pedidos = obj.getJSONArray("result");
            Log.e("ServicePay", "verify pendent payment");
            if(pedidos != null && pedidos.length() > 0){

                valor = pedidos.getJSONObject(0).getString("Valor");
                fp = pedidos.getJSONObject(0).getString("Forma de Pagamento");
                pedido = pedidos.getJSONObject(0).getString("Numero");

                callScope();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callScope(){
        Intent i = new Intent(startPaymentService.this, CallScopePay.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("VALOR", valor);
        i.putExtra("PEDIDO", pedido);
        i.putExtra("ACTION", fp);
        i.putExtra("ATRIB_APLICACAO", "01010042022");
        i.putExtra("QTD_MAX_PARCELA", "1");

        startActivity(i);
    }
}

