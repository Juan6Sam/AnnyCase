package com.example.casesanny1.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.casesanny1.MainActivity;
import com.example.casesanny1.databinding.AppBarMenuPrincipalBinding;
import com.example.casesanny1.databinding.FragmentLoginBinding;

import com.example.casesanny1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

   private AppBarConfiguration login;
    TextView signUpTextView;
    EditText emailet, passwordEditText;
    Button signInAppCompatButton;
    String  email, password;
    String url = "http://192.168.239.247:3000/api/signin";

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    private FragmentLoginBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        signUpTextView = view.findViewById(R.id.signUpTextView);
        emailet = view.findViewById(R.id.usernameet);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        signInAppCompatButton = view.findViewById(R.id.signInAppCompatButton);


        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        signInAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        return view;
    }
    public void signin(){
        email = emailet.getText().toString();
        password = passwordEditText.getText().toString();

        if( email.isEmpty() || password.isEmpty()){
            Toast.makeText(getActivity(), "Debes de ingresar datos", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getActivity(), LoginFragment.class);
                                    startActivity(intent);

                                }
                            } catch (JSONException err){
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Logueado exitosamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                protected Map<String, String> getParams(){
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("email", email);
                    parametros.put("password", password);
                    return parametros;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
    public void signup(){
        Intent intent = new Intent(getActivity(), SignUp.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}