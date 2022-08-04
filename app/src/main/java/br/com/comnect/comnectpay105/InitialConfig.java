package br.com.comnect.comnectpay105;

import static br.com.comnect.comnectpay105.GetScopeIniFromPortal.getScopeIniFromPortal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class InitialConfig extends AppCompatActivity {

    ProgressDialog load;
    TextView msg, txt_serial;
    Button btn_init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_config);

        msg = findViewById(R.id.txt_msg);
        txt_serial = findViewById(R.id.txt_serial);
        btn_init = findViewById(R.id.btn_init);

        GetJson list = new GetJson();
        list.execute();

        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_init.setText("PRONTO");
                btn_init.setTextSize(30);

                txt_serial.setText(MainActivity.Serial);
                txt_serial.setVisibility(view.VISIBLE);

                msg.setTextSize(20);
                msg.setText("Acesse portal.comnect.com.br e configure um novo terminal com o seguinte numero de série:");

                ScopeIni ini = new ScopeIni();
                try {
                    Log.e("ServicePay", ini.teste());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private class GetJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(InitialConfig.this,
                    "Aguarde", "Verificando config");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getScopeIniFromPortal(routes.getSettingsPortal);
        }

        @Override
        protected void onPostExecute(String list){
            try {

                load.dismiss();
                Toast.makeText(InitialConfig.this, "busca OK", Toast.LENGTH_SHORT).show();
                JSONObject obj = new JSONObject(list);
                JSONObject settings = obj.getJSONObject("settings");
                JSONObject message = settings.getJSONObject("message");

                String empresa = message.getString("cod_empresa");
                String filial = message.getString("cod_filial");
                String pdv = message.getString("cod_pdv");
                String ip = message.getString("tls_srv_ip");

                msg.setText("Empresa: " + empresa + "\nFilial: " + filial + "\nPDV: " + pdv + "\nIP: " + ip);


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ServicePay", e.getMessage());
            }
        }
    }
}