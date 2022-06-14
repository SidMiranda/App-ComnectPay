package br.com.comnect.comnectpay105;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ScopeIni {

    public String teste() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(String.valueOf(Environment.getExternalStorageDirectory()+"/SCOPE/scope.ini")));

        String linha;
        LinkedList<String> linhas = new LinkedList<String>();

        while((linha = br.readLine()) != null){
            linhas.add(linha);
            Log.e("ServicePay", "Reading line: " + linha);
        }

        return "finished...";
    }

}
