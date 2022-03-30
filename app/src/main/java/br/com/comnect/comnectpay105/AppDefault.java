package br.com.comnect.comnectpay105;

import android.content.Intent;
import android.util.Log;

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

    public static Intent goToScope(String valor, String fp, String atr){
        Intent i = new Intent();
        String mth = "br.com.oki.scope." + fp;
        i.setAction(mth);
        i.putExtra("VALOR", valor);
        i.putExtra("QTD_MAX_PARCELA", "3");
        i.putExtra("CONSULTA_PLANOS", "0");
        i.putExtra("APP_TEMA", "APP_TEMA_AZUL");

        if(atr != "") {
            Log.e("ServicePay", "setando ATRIB_APLICACAO TO " + setAtr());
            i.putExtra("ATRIB_APLICACAO", setAtr());
        }

        return i;
    }

    public static String setAtr(){
        String atr;

        //FORMATO
        //nnc1tt1xx..x1c2tt2xx..x2cnttnxx..xn

        /* REFERENCIA
        nn = quantidade de atributos (tamanho 2)
        c1 = código do atributo 1 (tamanho 2)
        tt1 = tamanho do atributo 1 (tamanho 3)
        xx..x1 = atributo 1 (tamanho tt1)
        ...
        c2 = código do atributo 2 (tamanho 2)
        tt2 = tamanho do atributo 2 (tamanho 3)
        xx..x2 = atributo 2 (tamanho tt2)
        ...
        cn = código do atributo n (tamanho 2)
        ttn = tamanho do atributo n (tamanho 3)
        xx..xn = atributo n (tamanho ttn)
        */

        atr = "0101003666";

        return atr;
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
