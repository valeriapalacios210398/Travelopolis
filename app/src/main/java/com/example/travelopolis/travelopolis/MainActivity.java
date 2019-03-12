package com.example.travelopolis.travelopolis;

import android.content.Intent;
import android.se.omapi.Session;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText txtUsuario;
    EditText txtContraseña;
    Button btnIngresar;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private String USER = "";
    private String PASSWORD = "";

    private String ip = "http://travelopolis.ddns.net/travelopolis/android/login_GETUSER.php?usuario=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarLogin(txtUsuario.getText().toString().toLowerCase(),txtContraseña.getText().toString().toLowerCase());
            }
        });
        }

        public void verificarLogin(String usuario, String contraseña){
            USER = usuario;
            PASSWORD = contraseña;
            Toast.makeText(this,"El usuarios es:"+usuario+"y la contraseña es: "+contraseña,Toast.LENGTH_SHORT).show();
            solicitudJSON(ip+usuario);

        }

        public void solicitudJSON(String URL){
            JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    verificarPassword(datos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"Ocurrio un Error",Toast.LENGTH_SHORT).show();
                }
            });
            VolleyRP.addToQueue(solicitud,mRequest,this,volley);
        }

        public void verificarPassword(JSONObject datos){
            //Controllar el JSON
            Toast.makeText(this,"Los Datos son:"+datos.toString(),Toast.LENGTH_SHORT).show();
            try{
                String estado = datos.getString("resultado");
                if(estado.equals("CC")){
                    //Toast.makeText(this, "Contraseña y Usuario Correcto",Toast.LENGTH_SHORT).show();
                    JSONObject JsonDatos = new JSONObject(datos.getString("datos"));
                    String usuario = JsonDatos.getString("usuario");
                    String contraseña = JsonDatos.getString("contraseña");
                    //Toast.makeText(this, "El usuario es"+usuario+"y la contraseña es:"+contraseña,Toast.LENGTH_SHORT).show();
                    if(usuario.equals(USER) && contraseña.equals(PASSWORD)){
                        Toast.makeText(this, "LOGIN CORRECTO",Toast.LENGTH_SHORT).show();
                        //Mandar a Otro Activity
                        Intent i = new Intent(this,NuevaActividad.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(this, "Contraseña Incorrecta",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, estado,Toast.LENGTH_SHORT).show();
                }

            }catch (JSONException e){}
        }


}
