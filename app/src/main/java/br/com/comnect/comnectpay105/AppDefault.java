package br.com.comnect.comnectpay105;

import android.content.Intent;

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

    public static Intent goToScope(String valor, String fp){
        Intent i = new Intent();
        String mth = "br.com.oki.scope." + fp;
        i.setAction(mth);
        i.putExtra("VALOR", valor);
        i.putExtra("QTD_MAX_PARCELA", "3");
        i.putExtra("CONSULTA_PLANOS", "0");
        i.putExtra("APP_TEMA", "APP_TEMA_AZUL");

        return i;
    }

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

    public static String putJSONFromAPI(String url, int statusPedido, String numPedido){
        String retorno = "";
        String st = "";

        if(statusPedido == 1){
            st = "pendente";
        }else if(statusPedido == 2){
            st = "processando";
        }else if(statusPedido == 3){
            st = "concluido";
        }

        String urlParams = "numero=" + numPedido + "&status=" + st;

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

}
