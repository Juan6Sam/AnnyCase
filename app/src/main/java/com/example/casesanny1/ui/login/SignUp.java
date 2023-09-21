package com.example.casesanny1.ui.login;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.casesanny1.MainActivity;
import com.example.casesanny1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText  usernameet, nameet, lastnameet, emailEditText, passwordEditText, telefonoet, dniet,fenet;
    ImageView backImageView;
    Spinner generosp, paissp;
    Button signUpButton;
    String name, lastName, username, pais, email, password, telefono, genero, dni;
    String url = "http://192.168.239.247:3000/api/signup";
    int pos, pos1;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameet = findViewById(R.id.nameet);
        usernameet = findViewById(R.id.usernameet);
        lastnameet = findViewById(R.id.lastnameet);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        telefonoet = findViewById(R.id.telefonoet);
        dniet = findViewById(R.id.dniet);
        fenet = findViewById(R.id.fenet);
        backImageView = findViewById(R.id.backImageView);
        paissp = findViewById(R.id.paissp);
        generosp = findViewById(R.id.generosp);

        signUpButton = findViewById(R.id.signUpButton);

        requestQueue = Volley.newRequestQueue(SignUp.this);
        progressDialog = new ProgressDialog(SignUp.this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginFragment.class);
                startActivity(intent);
            }
        });

    }

    private void signUp(){
    name = nameet.getText().toString();
    lastName = lastnameet.getText().toString();
    username = usernameet.getText().toString();
    email = emailEditText.getText().toString();
    password = passwordEditText.getText().toString();
    pais = paissp.getItemAtPosition(pos).toString();
    telefono = telefonoet.getText().toString();
    genero = generosp.getItemAtPosition(pos1).toString();
    dni = dniet.getText().toString();

if( name.isEmpty()|| lastName.isEmpty() || email.isEmpty() || password.isEmpty() || username.isEmpty()){
    Toast.makeText(getApplicationContext(), "Debes de ingresar datos", Toast.LENGTH_SHORT).show();
} else {
    progressDialog.setMessage("Guardando datos");
    progressDialog.show();

    //Creacion de la cadena a ejecutar en el web service mediante Volley
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String serverResponse) {
                    //quitar progress dialog

                    try {
                        JSONObject obj = new JSONObject(serverResponse);
                        Boolean err = obj.getBoolean("err");
                        String message = obj.getString("req");
                        
                        if(err == true){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), LoginFragment.class);
                            startActivity(intent);

                        }
                    } catch (JSONException err){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registrado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginFragment.class);
                        startActivity(intent);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

                }
            }){
                protected Map<String, String> getParams(){
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("name", name);
                    parametros.put("lastName",lastName);
                    parametros.put("username", username);
                    parametros.put("email", email);
                    parametros.put("password", password);
                    parametros.put("telefono",telefono);
                    parametros.put("dni",dni);
                    return parametros;
                }
           };
                requestQueue.add(stringRequest);
        }
    }

}