package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void triggerRequest(View v) throws IOException, InterruptedException {
        String matrikelnummer = getMatrikelnummer(R.id.matrikel_input);
        NetworkRequest request = new NetworkRequest(matrikelnummer);

        request.start();
        request.join();

        updateText(request.getResponse());
    }

    public void triggerCalculation(View v){
        String matrikelnummer = getMatrikelnummer(R.id.matrikel_input);
        String primes = getOnlyPrimes(matrikelnummer);
        updateText(primes);
    }

    private void updateText(String text) {
        TextView output = (TextView) findViewById(R.id.response);
        output.setText(text);
    }

    private String getMatrikelnummer(int inputField){
        TextInputLayout matrikel_field = findViewById(inputField);
        String nummer = String.valueOf(matrikel_field.getEditText().getText());
        return nummer;
    }

    private String getOnlyPrimes(String input){
        String[] notPrimes={"0","1","2","4","8","9"};
        for (String number: notPrimes){
            input = input.replaceAll(number,"");
        }
        return input;
    }
}

class NetworkRequest extends Thread {
    private String matno;
    private String response;

    NetworkRequest(String s) {
        this.matno = s;
    }

    public String getResponse(){
        return this.response;
    }

    public void run() {
        String serverAnswer;

        Socket clientSocket;
        try {
            clientSocket = new Socket("143.205.174.165", 53212);

            DataOutputStream outToServer = null;
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader inFromServer = null;
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToServer.writeBytes(this.matno + '\n');
            this.response = inFromServer.readLine();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}