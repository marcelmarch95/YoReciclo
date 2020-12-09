package com.example.patacon;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Modelo.Generador;
import Modelo.Recicladora;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnRegistrarme;
    private EditText enombre;
    private EditText eapellidos;
    private EditText etelefono;
    private EditText ecorreo;
    private EditText econtraseña;
    private Generador g;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registro");

        db = FirebaseFirestore.getInstance();

        this.enombre = (EditText) findViewById(R.id.nombre);
        this.eapellidos = (EditText) findViewById(R.id.apellidos);
        this.etelefono = (EditText) findViewById(R.id.telefono);
        this.ecorreo = (EditText) findViewById(R.id.correo);
        this.econtraseña = (EditText) findViewById(R.id.contraseña);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.progressBar.setIndeterminate(true);


        btnRegistrarme = (Button) findViewById(R.id.btnRegistrarme);
        btnRegistrarme.setOnClickListener(this);
        btnRegistrarme.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("volver");
                //onBackPressed();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean validar(){

        boolean error = false;

        if (this.enombre.getText().toString().isEmpty()){
            this.enombre.requestFocus();
            this.enombre.setError("Ingrese nombre");
            error = true;
        }
        if (this.eapellidos.getText().toString().isEmpty()){
            this.eapellidos.requestFocus();
            this.eapellidos.setError("Ingrese apellidos");
            error = true;
        }
        if (this.etelefono.getText().toString().isEmpty()){
            this.etelefono.requestFocus();
            this.etelefono.setError("Ingrese teléfono");
            error = true;
        }
        if (this.ecorreo.getText().toString().isEmpty()){
            this.ecorreo.requestFocus();
            this.ecorreo.setError("Ingrese correo");
            error = true;
        }
        if (this.econtraseña.getText().toString().isEmpty()){
            this.econtraseña.requestFocus();
            this.econtraseña.setError("Ingrese contraseña");
            error = true;
        }
        return error;
    }


    @Override
    public void onClick(View view) {

        if (view == this.btnRegistrarme){
            if (validar() == false) {
                this.progressBar.setVisibility(View.VISIBLE);
                btnRegistrarme.setEnabled(false);
                this.g = new Generador();
                g.setNombre(enombre.getText().toString());
                g.setApellido(eapellidos.getText().toString());
                g.setNumeroTelefono(etelefono.getText().toString());
                g.setCorreo(ecorreo.getText().toString());
                g.setContraseña(econtraseña.getText().toString());

                mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(g.getCorreo(), g.getContraseña())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    mAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Email de verificacion enviado correctamente", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                    g.setUid(task.getResult().getUser().getUid());
                                    g.setKeyNot("");

                                    db.collection("generador").document(task.getResult().getUser().
                                            getUid()).set(g).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("Usuario creado correctamente");
                                            Intent i = new Intent(getBaseContext(), Informacion.class);
                                            i.putExtra("result", true);
                                            i.putExtra("mensaje", "Usuario creado correctamente");
                                            startActivity(i);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            System.out.println("Error al agregar empresa");
                                            Intent i = new Intent(getBaseContext(), Informacion.class);
                                            i.putExtra("result", false);
                                            i.putExtra("mensaje", "Error al crear usuario");
                                            startActivity(i);
                                        }
                                    });
                                } else {
                                    Intent i = new Intent(getBaseContext(), Informacion.class);
                                    i.putExtra("result", false);
                                    i.putExtra("mensaje", "Error al crear usuario");
                                    startActivity(i);
                                }
                            }
                        });
            }


        }

    }


}
