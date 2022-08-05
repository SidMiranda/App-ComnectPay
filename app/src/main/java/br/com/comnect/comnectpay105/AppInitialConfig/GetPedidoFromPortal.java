package br.com.comnect.comnectpay105.AppInitialConfig;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import br.com.comnect.comnectpay105.routes;

public class GetPedidoFromPortal extends GetFromPortal {

    public static String getPedidoFromPortal(String serial) {
        String retorno = "";
        //String data = "{'m':'get_psk','u':'8971D0A','c_id':'8060565088009857'}";

        try {
            URL apiEnd = new URL(routes.getPedidoPortal);
            int codigoResposta;
            HttpsURLConnection conexao;
            InputStream is;

            String data = URLEncoder.encode("data", "UTF-8") + "=" +
                    URLEncoder.encode("{\"m\":\"get_psk\",\"sn\":\"55464654\",\"c_id\":\"8060565088009857\"}", "UTF-8");

            Log.e("ServicePay", "sending data -> " + data);

            byte[] postData = data.getBytes(StandardCharsets.UTF_8);

            SSLSocketFactory sslsf = null;
            try {
                sslsf = createSslSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }

            conexao = (HttpsURLConnection) apiEnd.openConnection();
            conexao.setDoOutput(true);
            conexao.setInstanceFollowRedirects(false);
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conexao.setRequestProperty("charset", "utf-8");
            conexao.setUseCaches(false);

            conexao.setSSLSocketFactory(sslsf);

            try (
                    DataOutputStream out = new DataOutputStream(conexao.getOutputStream())) {
                out.write((postData));
            } catch (Exception e) {
                Log.e("ServicePay", e.getMessage());
            }

            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);

            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conexao.getInputStream();
            } else {
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);

            Log.e("ServicePay", "retorno -> " + retorno);

            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;

    }
}
