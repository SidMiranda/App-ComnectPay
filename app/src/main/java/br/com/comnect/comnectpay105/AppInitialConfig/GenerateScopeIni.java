package br.com.comnect.comnectpay105.AppInitialConfig;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;

public class GenerateScopeIni {

    public static boolean generate(String empresa, String filial, String pdv, String ip) {
        File logText = new File(Environment.getExternalStorageDirectory()+"/SCOPE/scope.ini");

        Log.e("ServicePay", "path -> " + logText.toString());

        try (OutputStream os = new FileOutputStream(logText)) {
            Writer wr = new OutputStreamWriter(os);
            BufferedWriter br = new BufferedWriter(wr);

            br.write("["+empresa+filial+"]");
            br.newLine();
            br.write("Name="+ip);
            br.newLine();
            br.write("Port=2046");
            br.newLine();
            br.close();
        }catch (Exception e){
            Log.e("ServicePay", e.getMessage());
            return false;
        }


        return true;
    }

    public LinkedList<String> readLine() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory()+"/SCOPE/scope.ini"));

        String linha;
        LinkedList<String> linhas = new LinkedList<String>();

        while((linha = br.readLine()) != null){
            linhas.add(linha);
            Log.e("ServicePay", "Reading line: " + linha);
        }

        return linhas;
    }

}
