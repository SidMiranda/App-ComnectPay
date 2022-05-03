package br.com.comnect.comnectpay105;

import static java.security.AccessController.getContext;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AppDefault {
    static int statusPedido;
    static String numPedido;
    static String codControle;
    private static GertecPrinter gertecPrinter;
    private static ConfigPrint configPrint = new ConfigPrint();

    public static String getJSONFromAPI(String url){
        String retorno = "";
        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);

            is.close();
            conexao.disconnect();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    public static String putJSONFromAPI(String url, int statusPedido, String numPedido, String controle){
        String retorno = "";
        String st = "";

        if(statusPedido == 1){
            st = "pendente";
        }else if(statusPedido == 2){
            st = "processando";
        }else if(statusPedido == 3){
            st = "concluido";
        }

        String urlParams = "numero=" + numPedido + "&status=" + st + "&controle=" + controle;

        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setDoOutput(true);
            conexao.setDoInput(true);
            conexao.setRequestMethod("PUT");

            byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

            DataOutputStream out = new DataOutputStream(conexao.getOutputStream());
            out.write((postData));

            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);

            is.close();
            conexao.disconnect();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    private static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }

    public static void setStatus(int status, String pedido, String controle){
        statusPedido = status;
        numPedido = pedido;
        codControle = controle;
        updateList();
    }

    public static void updateList(){
        UpdateStatus update = new UpdateStatus();
        update.execute();
    }

    private static class UpdateStatus extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.e("ServicePay", "Sending request to API pedido:" + numPedido +
                    " status:" + statusPedido + " controle:" + codControle );
            return putJSONFromAPI(routes.updateStatus, statusPedido, numPedido, codControle);
        }

        @Override
        protected void onPostExecute(String list) {
            startPaymentService.aux = 0;
            Log.e("ServicePay", "starting service");
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void imprime(Context c, String txt) throws Exception {

        Toast.makeText(c, txt, Toast.LENGTH_SHORT).show();

        //if(MainActivity.Model.equals(MainActivity.G700)){
            gertecPrinter = new GertecPrinter(c);
        //}
        configPrint.setAlinhamento("CENTER");
        gertecPrinter.setConfigImpressao(configPrint);

        String sStatus = gertecPrinter.getStatusImpressora();
        if(gertecPrinter.isImpressoraOK()) {
            gertecPrinter.imprimeTexto(txt);
            gertecPrinter.avancaLinha(150);
            gertecPrinter.ImpressoraOutput();
        }

    }
}
