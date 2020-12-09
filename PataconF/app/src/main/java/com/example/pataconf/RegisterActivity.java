package com.example.pataconf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import Modelo.Recicladora;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegistrarme;
    private EditText enombrecomercio;
    private Spinner slocalidad;
    private EditText edireccion;


    private String uid;
    private String nombreComercio;
    private String categoria;
    private String localidad;
    private String direccion;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Spinner spinner = (Spinner) findViewById(R.id.localidad);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.localidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registro de Empresa Recicladora");


        this.enombrecomercio = (EditText) findViewById(R.id.nombrecomercio);
        this.slocalidad = (Spinner) findViewById(R.id.localidad);
        this.edireccion = (EditText) findViewById(R.id.direccion);


        btnRegistrarme = (Button) findViewById(R.id.btnRegistrarme);
        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //barra.setVisibility(View.VISIBLE);
                if (validar()==false){
                    btnRegistrarme.setEnabled(false);
                    Recicladora  r = new Recicladora();
                    r.setNombreEmpresa(enombrecomercio.getText().toString());
                    //c.setCategoria(scategoria.getSelectedItem().toString());
                    r.setLocalidad(slocalidad.getSelectedItem().toString());
                    r.setDireccion(edireccion.getText().toString());

                    Intent i = new Intent(getBaseContext(), Register2Activity.class);
                    i.putExtra("recicladora",r);
                    startActivity(i);
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

        if (this.enombrecomercio.getText().toString().isEmpty()){
            this.enombrecomercio.requestFocus();
            this.enombrecomercio.setError("Ingrese nombre comercio");
            error = true;
        }
        if (this.edireccion.getText().toString().isEmpty()){
            this.edireccion.requestFocus();
            this.edireccion.setError("Ingrese dirección");
            error = true;
        }
        return error;
    }



}
