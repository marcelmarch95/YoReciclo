package com.example.pataconf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

import Modelo.Comerciante;
import Modelo.Recicladora;

public class Register2Activity extends AppCompatActivity {

    private Button btnRegistrarme;
    private EditText enombre;
    private EditText enumero;
    private EditText ecorreo;
    private EditText econtraseña;
    private boolean errorcorreo = false;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String uid;
    private String nombreEncargado;
    private String numeroTelefono;
    private String correo;
    private String contraseña;
    private Recicladora c;
    private ProgressBar progressBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Intent i = getIntent();
        Recicladora c = (Recicladora) i.getSerializableExtra("recicladora");
        this.c = c;
        System.out.println("En regiter 2 la recicladora: " + c.toString());

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registro de Recicladora");


        this.enombre = (EditText) findViewById(R.id.nombre);
        this.enumero =  (EditText) findViewById(R.id.numero);
        this.ecorreo = (EditText) findViewById(R.id.correo);
        this.econtraseña = (EditText) findViewById(R.id.contraseña);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);

        btnRegistrarme = (Button) findViewById(R.id.btnRegistrarme);
        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);

                if (validar()==false){
                    db.collection("comerciante")
                    .whereEqualTo("correo", ecorreo.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean resultado=false;
                            if (task.isSuccessful()) {
                                if (task.getResult().size()!=0) {
                                    ecorreo.setError("Error, correo ya registrado");
                                    System.out.println("Encontré un correo igualito");
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    btnRegistrarme.setEnabled(false);
                                    c.setNombreEncargado(enombre.getText().toString());
                                    c.setNumeroTelefono(enumero.getText().toString());
                                    c.setCorreo(ecorreo.getText().toString());
                                    c.setContraseña(econtraseña.getText().toString());

                                    Intent i = new Intent(getBaseContext(), SelectorDireccionMapa.class);
                                    i.putExtra("recicladora",c);
                                    startActivity(i);
                                }
                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });



                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnRegistrarme.requestFocus();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                System.out.println("volver");
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean validar(){

        boolean error = false;

        if (this.enombre.getText().toString().isEmpty()){
            this.enombre.requestFocus();
            this.enombre.setError("Ingrese nombre encargado");
            error = true;
        }
        if (this.enumero.getText().toString().isEmpty()){
            this.enumero.requestFocus();
            this.enumero.setError("Ingrese número de teléfono");
            error = true;
        }
        if (this.ecorreo.getText().toString().isEmpty()){
            this.ecorreo.requestFocus();
            this.ecorreo.setError("Ingrese correo");
            error = true;
        }
        if (this.econtraseña.getText().toString().isEmpty()){
            this.econtraseña.requestFocus();
            this.econtraseña.setError("Ingrese una contraseña");
            error = true;
        }

        if (this.enumero.getText().toString().length()!=9){
            this.enumero.requestFocus();
            this.enumero.setError("Ingrese número de teléfono de 9 dígitos");
            error = true;
        }

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(this.ecorreo.getText().toString()).matches()){
            this.ecorreo.setError("Ingrese un correo válido");
            error = true;
        }


        return error;
    }


}
