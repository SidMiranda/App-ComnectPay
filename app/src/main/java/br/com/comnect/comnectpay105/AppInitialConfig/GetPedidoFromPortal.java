package br.com.comnect.comnectpay105.AppInitialConfig;

import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import br.com.comnect.comnectpay105.app.routes;

public class GetPedidoFromPortal extends GetFromPortal {

    //private static final String CA_KEYSTORE_TYPE = KeyStore.getDefaultType(); //"JKS";
    //private static final String CA_KEYSTORE_PATH = "./cacert.jks";
    //private static final String CA_KEYSTORE_PASS = "Telecom01";

    //private static final String CLIENT_KEYSTORE_TYPE = "PKCS12";
    //private static final String CLIENT_KEYSTORE_PATH = "./clientp12.pfx";
    //private static final String CLIENT_KEYSTORE_PASS = "Telecom01";

    public static String getPedidoFromPortal(String serial) {
        String retorno = "";

        try {
            URL apiEnd = new URL(routes.getPedidoPortal);
            int codigoResposta;
            HttpsURLConnection conexao;
            InputStream is;

            String data = URLEncoder.encode("data", "UTF-8") + "=" +
                    URLEncoder.encode("{\"m\":\"get_psk\",\"sn\":\"55464654\",\"c_id\":\"8060565088009857\"}", "UTF-8");

            Log.e("ServicePay", "sending data -> " + data);

            byte[] postData = data.getBytes(StandardCharsets.UTF_8);

            String key = "Telecom01";

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(Environment.getExternalStorageDirectory()+"/client.crt"), key.toCharArray());

            KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
            ts.load(new FileInputStream(Environment.getExternalStorageDirectory()+"/ca.crt"), key.toCharArray());

//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
//            kmf.init(ks, key.toCharArray());
//            KeyManager[] km = kmf.getKeyManagers();
//
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
//            tmf.init(ts);
//
//            SSLContext sslContext = SSLContext.getInstance("TLSv1");
//            sslContext.init(km, tmf.getTrustManagers(), new SecureRandom());

//
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            SSLSocketFactory sslsf = null;
            try {
                sslsf = createSslSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }

            conexao = (HttpsURLConnection) apiEnd.openConnection();

            conexao.setSSLSocketFactory(sslsf);
            //conexao.setSSLSocketFactory(sslContext.getSocketFactory());
            conexao.setDoOutput(true);
            conexao.setInstanceFollowRedirects(false);
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conexao.setRequestProperty("charset", "utf-8");
            conexao.setUseCaches(false);

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
        } catch (Exception e) {
            Log.e("ServicePay", e.getMessage());
            e.printStackTrace();
        }

        return retorno;
    }
}
