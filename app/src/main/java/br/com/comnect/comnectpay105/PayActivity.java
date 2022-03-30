package br.com.comnect.comnectpay105;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class PayActivity extends AppCompatActivity {
    String valor = "";
    Button key1, key2, key3, key4, key5, key6, key7, key8, key9, key0;
    Button btn_pagar, btn_corrige;
    TextView txt_valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        key1 = findViewById(R.id.btn_credito);
        key2 = findViewById(R.id.btn_debito);
        key3 = findViewById(R.id.key3);
        key4 = findViewById(R.id.key4);
        key5 = findViewById(R.id.key5);
        key6 = findViewById(R.id.key6);
        key7 = findViewById(R.id.key7);
        key8 = findViewById(R.id.key8);
        key9 = findViewById(R.id.key9);
        key0 = findViewById(R.id.key0);
        btn_pagar = findViewById(R.id.btn_pagar);
        btn_corrige = findViewById(R.id.btn_corrige);
        txt_valor = findViewById(R.id.txt_valor);


        key1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("1");}
        });

        key2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("2");}
        });

        key3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("3");}
        });
        key4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("4");}
        });
        key5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("5");}
        });
        key6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("6");}
        });
        key3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("3");}
        });
        key7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("7");}
        });
        key8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("8");}
        });
        key9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("9");}
        });
        key0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {addValor("0");}
        });
    }

    public void corrige(View v){
        valor = "";
        txt_valor.setText("R$ 0,00");
    }

    public void pagar(View v){
        String valor = txt_valor.getText().toString();
        valor = valor.substring(3);
        valor = valor.replace(",", "");

        if(Integer.parseInt(valor) > 0){
            Intent i = new Intent(PayActivity.this, CardTypeActivity.class);
            i.putExtra("VALOR", valor);
            startActivityForResult(i, 100);
        }else{
            Toast.makeText(this, "Digite um valor", Toast.LENGTH_SHORT).show();
        }

    }

    public void addValor(String n){
        valor = valor + n;

        switch(valor.length()){
            case 1:
                txt_valor.setText("R$ 0,0" + valor);
                break;
            case 2:
                txt_valor.setText("R$ 0," + valor);
                break;
            case 3:
                txt_valor.setText("R$ " + valor.substring(0, 1) + "," + valor.substring(1));
                break;
            case 4:
                txt_valor.setText("R$ " + valor.substring(0,2) + "," + valor.substring(2));
                break;
            case 5:
                txt_valor.setText("R$ " + valor.substring(0,3) + "," + valor.substring(3));
                break;
            case 6:
                txt_valor.setText("R$ " + valor.substring(0,4) + "," + valor.substring(4));
                break;
            case 7:
                txt_valor.setText("R$ " + valor.substring(0,5) + "," + valor.substring(5));
                break;
            case 8:
                txt_valor.setText("R$ " + valor.substring(0,6) + "," + valor.substring(6));
                break;
        }

        //Toast.makeText(this, txt_valor.getText(), Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

}