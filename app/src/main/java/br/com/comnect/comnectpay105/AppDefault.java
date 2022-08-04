package br.com.comnect.comnectpay105;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLHandshakeException;

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

    public static String postJSONFromPortal(String url){
        String retorno = "";
        //String data = "{'m':'get_settings','u':'14013'}";

        Log.e("ServicePay", "init postJSONFromPortal...");

        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpsURLConnection conexao;
            InputStream is;

            String data = URLEncoder.encode("data", "UTF-8") + "=" +
                    URLEncoder.encode("{\"m\":\"get_settings\",\"u\":\"14013\"}", "UTF-8");

            //String data2 = "data=%7B%22m%22%3A%22get_settings%22%2C%22u%22%3A%2214013%22%7D";

            Log.e("ServicePay", "sending parans -> " + data);

            byte[] postData = data.getBytes(StandardCharsets.UTF_8);

            SSLSocketFactory sslsf = null;
            try {
                sslsf = createSslSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }

            conexao = (HttpsURLConnection) apiEnd.openConnection();
            conexao.setDoOutput( true );
            conexao.setInstanceFollowRedirects( false );
            conexao.setRequestMethod( "POST" );
            conexao.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conexao.setRequestProperty( "charset", "utf-8");
            conexao.setUseCaches( false );

            conexao.setSSLSocketFactory(sslsf);

            try (

                    DataOutputStream out = new DataOutputStream(conexao.getOutputStream())) {
                    out.write((postData));
            }catch (SSLHandshakeException e){
                Log.e("ServicePay", e.getMessage());
            }

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

    private static SSLSocketFactory createSslSocketFactory() throws Exception {
        TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        } };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
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
