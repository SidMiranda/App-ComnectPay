package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import br.com.gertec.gedi.exceptions.GediException;

public class ImpressaoActivity extends AppCompatActivity {
    Button btnViaCliente, btnViaLoja, btnReduzido;
    String pagamento, parcela, cartao;
    String codigo_controle, valor, bandeira, cnpj, codigo_auth, data;

    private GertecPrinter gertecPrinter;
    private ConfigPrint configPrint = new ConfigPrint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressao);

        btnViaCliente = findViewById(R.id.btnViaCliente);
        btnViaLoja = findViewById(R.id.btnViaLoja);

        Intent i = getIntent();
        codigo_controle = i.getStringExtra("CODIGO_CONTROLE");
        cnpj = i.getStringExtra("CNPJ");
        codigo_auth = i.getStringExtra("CODIGO_AUTORIZACAO");
        valor = i.getStringExtra("VALOR");
        bandeira = i.getStringExtra("BANDEIRA");
        parcela = i.getStringExtra("PARCELA");
        cartao = i.getStringExtra("CARTAO");
        data = i.getStringExtra("DATA");

        Log.e("ServicePay", "Loading data OK");

        if(MainActivity.Model.equals(MainActivity.G700)){
            gertecPrinter = new GertecPrinter(ImpressaoActivity.this);
        }

        btnViaCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    montaCupom();
                    output();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnViaLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    imprimeImagem();
                    output();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void montaCupom() throws Exception {
        Log.e("ServicePay", "Montando Cupom...");

        imprime("REDE", 40);
        imprime(" ", 15);
        imprime(bandeira, 30);
        imprime(" ", 15);
        imprime("COMPR:" + codigo_controle + " \n " + "VALOR: R$" + valor, 22);
        imprime(" ", 20);
        imprime("CNPJ_ESTAB:" + cnpj, 20);
        imprime("*********************************************", 18);
        imprime("ESTAB:" + codigo_auth + "SCOPE TESTE", 20);
        imprime("DATA PAG: " + data, 20);
        imprime("CARTÃO: " + cartao, 20);
        imprime("AUTORIZAÇÃO: " + codigo_auth, 20);
        imprime("", 20);
        imprime(" RECONHEÇO E PAGAREI A DIVIDA AQUI REPRESENTADA ", 18);
        imprime("", 18);
        imprime("DEMONSTRAÇÃO", 25);
        imprime("Transação sem validade para reembolso", 18);
        imprime("Autorização gerada por simulador", 18);
        imprime("********************************************", 18);
        imprime("", 18);
        imprime("Transação autorizada mediante \n o uso de senha pessoal", 19);
        imprime("********************************************", 18);
        imprime("", 18);
        imprime("SCOPE/NCR By COMNECT PAY", 18);

    }

    private void imprimeImagem() throws Exception {
        Log.e("ServicePay", "iniciando impressão imagem");
        gertecPrinter.getStatusImpressora();
        configPrint.setiWidth(300);
        //configPrint.setiHeight(600);
        configPrint.setAlinhamento("CENTER");
        gertecPrinter.setConfigImpressao(configPrint);

        String sStatus = gertecPrinter.getStatusImpressora();
        if(gertecPrinter.isImpressoraOK()) {
            Log.e("ServicePay", "sending black.JPG...");
            gertecPrinter.imprimeImagem("cupom_logo");
        }else{
            Toast.makeText(this, "falha", Toast.LENGTH_SHORT).show();
        }
    }

    private void imprime(String cupom, int tam) throws Exception {
        configPrint.setAlinhamento("CENTER");
        configPrint.setFonte("MONOSPACE");
        configPrint.setTamanho(tam);
        gertecPrinter.setConfigImpressao(configPrint);

        String sStatus = gertecPrinter.getStatusImpressora();
        if(gertecPrinter.isImpressoraOK()) {
            gertecPrinter.imprimeTexto(cupom);
        }else{
            Toast.makeText(this, "falha", Toast.LENGTH_SHORT).show();
        }
    }

    private void output() throws Exception {

        Log.e("ServicePay", "Sending output");

        gertecPrinter.avancaLinha(200);
        gertecPrinter.ImpressoraOutput();
        gertecPrinter.avancaLinha(100);
    }
}