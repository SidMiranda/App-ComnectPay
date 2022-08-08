package br.com.comnect.comnectpay105.AppInitialConfig;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

public class GetScopeIniFromPortal extends GetFromPortal {

    public static String getScopeIniFromPortal(String url, String pedido){
        String retorno = "";
        //String data = "{'m':'get_settings','u':'14013'}";

        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpsURLConnection conexao;
            InputStream is;

            String data = URLEncoder.encode("data", "UTF-8") + "=" +
                    URLEncoder.encode("{\"m\":\"get_settings\",\"u\":\""+pedido+"\"}", "UTF-8");

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
}
