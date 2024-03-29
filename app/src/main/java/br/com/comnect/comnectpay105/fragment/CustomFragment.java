package br.com.comnect.comnectpay105.fragment;

import static br.com.comnect.comnectpay105.app.AppDefault.getJSONFromAPI;
import static br.com.comnect.comnectpay105.app.AppDefault.setStatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.comnect.comnectpay105.app.CallScopePay;
import br.com.comnect.comnectpay105.app.EstornoActivity;
import br.com.comnect.comnectpay105.R;
import br.com.comnect.comnectpay105.app.ImpressaoActivity;
import br.com.comnect.comnectpay105.app.ValueKeyboardActivity;
import br.com.comnect.comnectpay105.app.routes;
import br.com.comnect.comnectpay105.databinding.FragmentHomeBinding;

public class CustomFragment extends Fragment{
    Button btn_pagar, btn_recarga, btn_cupom, btn_pix, btn_extorno, btn_btc;
    ProgressDialog load;
    String numPedido, fp, valor;
    String pdv = "3";

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        btn_pagar = rootView.findViewById(R.id.btn_pagar);
        btn_recarga = rootView.findViewById(R.id.btn_recarga);
        btn_cupom = rootView.findViewById(R.id.btn_cupom);
        btn_pix = rootView.findViewById(R.id.btn_pix);
        btn_extorno = rootView.findViewById(R.id.btn_extorno);
        btn_btc = rootView.findViewById(R.id.btn_btc);

        btn_pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {pagar();}
        });

        btn_recarga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {recarga();}
        });

        btn_pix.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){goToKeyboard("pix");}
        });

        btn_cupom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                impressaoCupom();
            }
        });

        btn_extorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EstornoActivity.class);
                Log.e("ServicePay", "Abrindo estorno!");
                startActivity(i);
            }
        });

        btn_btc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){goToKeyboard("bitcoin");}
        });

        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void pagar(){
        checkPdv check = new checkPdv();
        check.execute();

    }
    private class checkPdv extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getActivity(),
                    "Por favor Aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected String doInBackground(Void... params) {
            return getJSONFromAPI(routes.getPendentes + "?pdv=" + pdv);
        }

        @Override
        protected void onPostExecute(String list){
            parseList(list);
            load.dismiss();
        }
    }
    private void parseList(String list){
        String jsonS = list;
        JSONObject obj = null;

        Log.e("ServicePay", "Verificando pagamentos pendentes");
        try {
            obj = new JSONObject(jsonS);
            JSONArray pedidos = obj.getJSONArray("result");

            if(pedidos != null && pedidos.length() > 0){
                valor = pedidos.getJSONObject(0).getString("Valor");
                fp = pedidos.getJSONObject(0).getString("Forma de Pagamento");
                numPedido = pedidos.getJSONObject(0).getString("Numero");

                Log.e("ServicePay", "Pendent payment found!");

                setStatus(2, numPedido, "0");
                callScope();
            }else{
                goToKeyboard("pagar");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callScope(){
        Intent i = new Intent(getContext(), CallScopePay.class);
        i.putExtra("VALOR", valor);
        i.putExtra("PEDIDO", numPedido);
        i.putExtra("ACTION", fp);
        i.putExtra("ATRIB_APLICACAO", "");
        i.putExtra("QTD_MAX_PARCELA", "1");

        startActivity(i);
    }

    public void goToKeyboard(String from){
        Log.e("ServicePay", "Redirecting to ValueKeyboardActivity -> " + from);
        Intent i = new Intent(getContext(), ValueKeyboardActivity.class);
        i.putExtra("FROM", from);
        startActivity(i);
    }

    public void recarga(){
        Toast.makeText(getActivity(), "Não disponivel no momento", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent();
        //i.setAction("br.com.oki.scope.RECARGA_CELULAR");
        //startActivityForResult(i, 102);
    }
    public void impressaoCupom(){
        Toast.makeText(getActivity(), "Imprimindo Cupom...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), ImpressaoActivity.class);
        //i.setAction("br.com.oki.scope.REIMPRESSAO_CUPOM");
        //i.putExtra("CODIGO_CONTROLE", "03900731022");
        startActivity(i);
    }

}