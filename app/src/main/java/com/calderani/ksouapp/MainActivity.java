package com.calderani.ksouapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    String METHOD_NAME = "Soma";
    String SOAP_ACTION = "";

    final String NAMESPACE = "http://calderani.com.br/";
    final String SOAP_URL = "http://10.3.1.20:8080/Calculadora/Calculadora";

    SoapObject request;
    ProgressDialog progressBar;
    private EditText etNumero1;
    private EditText etNumero2;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etNumero1 = (EditText) findViewById(R.id.etNumero1);
        etNumero2 = (EditText) findViewById(R.id.etNumero2);
        tvResultado = (TextView) findViewById(R.id.tvResultado);
    }

    public void somar(View view) {
        METHOD_NAME = "Soma";
        CalcularAsync calcularAsync = new CalcularAsync();
        calcularAsync.execute(
                etNumero1.getText().toString(),
                etNumero2.getText().toString()
        );
    }

    public void multiplicar(View view) {
        METHOD_NAME = "Multiplicacao";
        CalcularAsync calcularAsync = new CalcularAsync();
        calcularAsync.execute(
                etNumero1.getText().toString(),
                etNumero2.getText().toString()
        );
    }

    public void dividir(View view) {
        METHOD_NAME = "Divisao";
        CalcularAsync calcularAsync = new CalcularAsync();
        calcularAsync.execute(
                etNumero1.getText().toString(),
                etNumero2.getText().toString()
        );
    }


    private class CalcularAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            SoapPrimitive calcular = null;

            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Numero1", params[0]);
            request.addProperty("Numero2", params[1]);
            //request.addProperty("operator", "+");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            try {
                httpTransport.call(SOAP_ACTION, envelope);
                calcular = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.getMessage();
            }
            return calcular.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.dismiss();
            Toast.makeText(getApplicationContext(), "Resultado: " + result, Toast.LENGTH_SHORT).show();
            tvResultado.setText(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setMessage("Calculando...");
            progressBar.show();
        }
    }
}
