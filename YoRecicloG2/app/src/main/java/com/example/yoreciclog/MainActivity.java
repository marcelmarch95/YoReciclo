package com.example.yoreciclog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailT;
    private EditText passT;
    private Button btnIngresar;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private TextView tvError;
    private boolean resultado;
    private ProgressBar barra;
    private TextView myTextClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        barra = (ProgressBar) findViewById(R.id.my_progressBar);
        barra.setVisibility(View.INVISIBLE);

        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                barra.setVisibility(View.VISIBLE);
                btnIngresar.setEnabled(false);
                validarCampos();

            }
        });
        btnIngresar.requestFocus();

        if (currentUser!=null){
            /*Log.d("tag", "signInWithEmail:success");
            Intent i = new Intent(getBaseContext(), PerfilComerciante.class);
            startActivity(i);
            barra.setVisibility(View.INVISIBLE);
            btnIngresar.setEnabled(true);*/
        }

        emailT = (EditText) findViewById(R.id.contraseña);
        passT = (EditText) findViewById(R.id.pass);

        emailT.setText("jucarmarch70@gmail.com");
        passT.setText("123456");

        tvError = (TextView) findViewById(R.id.tvError);



        myTextClick = findViewById(R.id.text_click);

        myTextClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Remplazar por tu codigo", Toast.LENGTH_LONG).show();
                /*Intent i = new Intent(getBaseContext(), RegisterActivity.class);

                //i.putExtra("MyClass", (Serializable) usuario);
                startActivity(i);
                barra.setVisibility(View.INVISIBLE);
                btnIngresar.setEnabled(true);*/
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void validarCampos() {
        String em = emailT.getText().toString();
        String pa = passT.getText().toString();

        if (em.isEmpty() || pa.isEmpty()){
            tvError.setText("Ingrese todos los datos");
            barra.setVisibility(View.INVISIBLE);
            btnIngresar.setEnabled(true);
        }

        else if (!isValidEmail(emailT.getText().toString())){
            tvError.setText("Ingrese un correo electrónico válido");
            barra.setVisibility(View.INVISIBLE);
            btnIngresar.setEnabled(true);
        }

        else {
            verificarCredenciales(emailT.getText().toString(), passT.getText().toString());
        }


    }

    private void verificarCredenciales(String email, String pass) {
        Log.d("Verf", "email: " + email + " pass: " + pass);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("tag", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            tvError.setText("");
                            /*Intent i = new Intent(getBaseContext(), ActivityPrincipal.class);
                            i.putExtra("return", 1);
                            startActivity(i);*/
                            /*Intent i = new Intent(getBaseContext(), PerfilComerciante.class);
                            startActivity(i);
                            barra.setVisibility(View.INVISIBLE);
                            btnIngresar.setEnabled(true);*/

                        } else {
                            Log.w("tag", "signInWithEmail:failure", task.getException());
                            tvError.setText("Error, correo o contraseña inválidos");
                            passT.setText("");
                            passT.setFocusable(true);
                            barra.setVisibility(View.INVISIBLE);
                            btnIngresar.setEnabled(true);
                        }
                    }
                });
    }



    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onBackPressed() {

    }
}
