package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.gertec.gedi.exceptions.GediException;

public class TesteImpressora extends AppCompatActivity {
    Button btnTeste;

    private GertecPrinter gertecPrinter;
    private ConfigPrint configPrint = new ConfigPrint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_impressora);

        btnTeste = findViewById(R.id.btnTeste);

        if(MainActivity.Model.equals(MainActivity.G700)){
            gertecPrinter = new GertecPrinter(this.getApplicationContext());
        }

        btnTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    teste();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void teste() throws Exception {
        Toast.makeText(this, "imprimindo...", Toast.LENGTH_SHORT).show();
        imprime();
    }

    private void imprime() throws Exception {
        configPrint.setAlinhamento("CENTER");
        gertecPrinter.setConfigImpressao(configPrint);

        // Faz a impressão
        String sStatus = gertecPrinter.getStatusImpressora();
        if(gertecPrinter.isImpressoraOK()) {
            gertecPrinter.imprimeTexto("COMNECT TESTE");
            // Usado apenas no exemplo, esse pratica não deve
            // ser repetida na impressão em produção
            gertecPrinter.avancaLinha(150);
            gertecPrinter.ImpressoraOutput();
        }else{
            Toast.makeText(this, "falha", Toast.LENGTH_SHORT).show();
        }
    }
}