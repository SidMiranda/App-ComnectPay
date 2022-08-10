package br.com.comnect.comnectpay105.AppInitialConfig;

import static android.view.View.VISIBLE;
import static br.com.comnect.comnectpay105.AppInitialConfig.GetOrderFromPortal.getOrderFromPortal;
import static br.com.comnect.comnectpay105.AppInitialConfig.GetScopeIniFromPortal.getScopeIniFromPortal;
import static br.com.comnect.comnectpay105.MainActivity.Serial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import br.com.comnect.comnectpay105.R;
import br.com.comnect.comnectpay105.app.routes;
import br.com.comnect.comnectpay105.fragment.HomeActivity;

public class InitialConfig extends AppCompatActivity {

    ProgressDialog load;
    TextView msg, msg2, txt_serial;
    Button btn_init;

    String orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_config);

        msg = findViewById(R.id.txt_msg);
        msg2 = findViewById(R.id.txt_msg2);
        txt_serial = findViewById(R.id.txt_serial);
        btn_init = findViewById(R.id.btn_init);

        GetJsonOrder order = new GetJsonOrder();
        order.execute();

        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetJsonOrder order = new GetJsonOrder();
                order.execute();
            }
        });
    }

    private void requestOrder(String list){
        try {
            load.dismiss();
            JSONObject obj = new JSONObject(list);

            if(list == "" || list == null){
                msg.setText("Terminal não configurado");
                msg2.setText("Acesse portal.comnect.com.br e configure um novo terminal com o numero de série:");
                txt_serial.setVisibility(VISIBLE);
                txt_serial.setText(Serial);

                return;
            }

            String order = obj.getString("u");

            Log.e("ServicePay", "order-> " + order);

            orderNumber = order;

            GetScopeIni ini = new GetScopeIni();
            ini.execute();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServicePay", e.getMessage());
        }
    }

    private void requestIni(String list){
        try {
            load.dismiss();
            Log.e("ServicePay", list);

            if(list == "" || list == null){
                msg.setText("Terminal não configurado");
                msg2.setText("Acesse portal.comnect.com.br e configure um novo terminal com o numero de série:");
                txt_serial.setVisibility(VISIBLE);
                txt_serial.setText(Serial);

                return;
            }

            JSONObject obj = new JSONObject(list);
            JSONObject settings = obj.getJSONObject("settings");
            JSONObject message = settings.getJSONObject("message");

            String empresa = message.getString("cod_empresa");
            String filial = message.getString("cod_filial");
            String pdv = message.getString("cod_pdv");
            String ip = message.getString("tls_srv_ip");

            if(empresa != null){
                if(filial != null){
                    if(pdv !=null){
                        if(ip != ""){
                            Intent i = new Intent(InitialConfig.this, HomeActivity.class);
                            Log.e("ServicePay", "Loading HomeActivity");
                            startActivity(i);

                            //msg.setText("Terminal configurado com sucesso \nEmpresa: " + empresa + "\nFilial: " + filial + "\nPDV: " + pdv + "\nIP: " + ip);
                        }else{
                            msg.setText("Ip do servidor inválido");
                        }
                    }else{
                        msg.setText("PDV Inválido");
                    }
                }else{
                    msg.setText("Filial Inválida");
                }
            }else{
                msg.setText("Empresa Inválida");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServicePay", e.getMessage());
        }
    }

    private class GetJsonOrder extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(InitialConfig.this,
                    "Aguarde", "Verificando config");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getOrderFromPortal(Serial);
        }

        @Override
        protected void onPostExecute(String list){
            requestOrder(list);
        }
    }

    private class GetScopeIni extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(InitialConfig.this,
                    "Aguarde", "Verificando config");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getScopeIniFromPortal(routes.getScopeIniPortal, orderNumber);
        }

        @Override
        protected void onPostExecute(String list){
            requestIni(list);
        }
    }
}