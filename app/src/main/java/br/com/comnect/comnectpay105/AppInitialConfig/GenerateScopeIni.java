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
    private static final String FILE_NAME = "scope.ini";
    private static final File PATH = Environment.getExternalStorageDirectory();

    public static boolean generate(String empresa, String filial, String pdv, String ip) {
        Log.e("ServicePay", "Status Storage ->" + Environment.getExternalStorageState());
        Log.e("ServicePay", "PATH ->" + PATH + FILE_NAME);
        Log.e("ServicePay", "PATH ABSOLUTE ->" + PATH.getAbsolutePath());

        File iniFile = new File(PATH + "/" +FILE_NAME);

        try (OutputStream os = new FileOutputStream(iniFile)){
            Writer wr = new OutputStreamWriter(os);
            BufferedWriter br = new BufferedWriter(wr);

            br.write("["+empresa+filial+"]");
            br.newLine();
            br.write("Name="+ip);
            br.newLine();
            br.write("Port=2046");
            br.newLine();
            br.write("ArqTracePath=./");
            br.newLine();
            br.newLine();
            br.write("[PINPAD]");
            br.newLine();
            br.write("TamMinDados=4");
            br.close();

            Log.e("ServicePay", "SAVED");
        }catch (Exception e){
            Log.e("ServicePay", "ERR: " + e.getMessage());
            return false;
        }

        try {
            readScope();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static String readScope() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory()+"/scope.ini"));

        String linha;
        LinkedList<String> linhas = new LinkedList<String>();

        while((linha = br.readLine()) != null){
            linhas.add(linha);
            Log.e("ServicePay", "Reading line: " + linha);
        }

        return "finished...";
    }
}
