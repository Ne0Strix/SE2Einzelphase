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
        TextInputLayout matrikel_field = findViewById(R.id.matrikel_input);
        String matrikelnummer = String.valueOf(matrikel_field.getEditText().getText());

        NetworkRequest request = new NetworkRequest(matrikelnummer);

        request.start();

        request.join();

        updateText(request.getResponse());

    }

    private void updateText(String text) {
        TextView output = (TextView) findViewById(R.id.response);
        output.setText(text);
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