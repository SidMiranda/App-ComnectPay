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
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

public class GetOrderFromPortal extends GetFromPortal{

    public static String getOrderFromPortal(String pedido){
        String retorno = "";
        String url = "https://apiportal.comnectlupa.com.br/getOrderFromPortal.php?serial=" + pedido;
        Log.e("ServicePay", "mounting url -> " + url);
        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpsURLConnection conexao;
            InputStream is;

            conexao = (HttpsURLConnection) apiEnd.openConnection();
            conexao.setDoOutput( true );
            conexao.setInstanceFollowRedirects( false );
            conexao.setRequestMethod( "POST" );
            conexao.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conexao.setRequestProperty( "charset", "utf-8");
            conexao.setUseCaches( false );

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

