package com.example.patacon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import Modelo.Generador;

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
    private LinearLayout laprincipal;
    private LinearLayout laprincipal2;
    private ProgressBar iniciado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.laprincipal = findViewById(R.id.laprincipal);
        this.laprincipal2 = findViewById(R.id.laprincipal2);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        emailT = (EditText) findViewById(R.id.contraseña);
        passT = (EditText) findViewById(R.id.pass);
        barra = (ProgressBar) findViewById(R.id.my_progressBar);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        tvError = (TextView) findViewById(R.id.tvError);
        myTextClick = findViewById(R.id.text_click);

        if(currentUser!=null){
            laprincipal2.removeAllViews();
            iniciado = (ProgressBar) findViewById(R.id.progressBar3);
            iniciado.setVisibility(View.VISIBLE);
            laprincipal.requestLayout();
        }
        else {
            barra.setVisibility(View.INVISIBLE);
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

            emailT.setText("generador@gmail.com");
            passT.setText("123456");

            myTextClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "Remplazar por tu codigo", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(), RegisterActivity.class);

                    //i.putExtra("MyClass", (Serializable) usuario);
                    startActivity(i);
                    barra.setVisibility(View.INVISIBLE);
                    btnIngresar.setEnabled(true);
                }
            });
        }


        if (currentUser!=null){
            abrirPerfil();
        }

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
                            abrirPerfil();
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


    public void abrirPerfil(){
        Log.d("tag", "signInWithEmail:success");
        FirebaseUser user = mAuth.getCurrentUser();
        tvError.setText("");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("generador").document(user.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("tag", "getInstanceId failed", task.getException());
                                            return;
                                        }

                                        Generador g = (Generador) document.toObject(Generador.class);
                                        System.out.println(g.toString());
                                        SocketSingleton.getInstance().setGenerador(g);
                                        // Get new Instance ID token
                                        String token = task.getResult().getToken();

                                        String antigua = document.get("keyNot").toString();
                                        System.out.println("token antiguo : " + antigua);

                                        //Token cambió
                                        if (token.compareTo(antigua)!=0){
                                            docRef.update("keyNot", token)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("actualizado", "DocumentSnapshot successfully updated!");
                                                            Toast.makeText(MainActivity.this, "key user updated successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MainActivity.this, "key user updated error", Toast.LENGTH_LONG).show();
                                                            Log.w("no actualizado", "Error updating document", e);
                                                        }
                                                    });
                                        }

                                        /*Intent i = new Intent(getBaseContext(), ActivityPrincipal.class);
                                        i.putExtra("return", 1);
                                        startActivity(i);*/
                                        Intent i = new Intent(getBaseContext(), PerfilComerciante.class);
                                        startActivity(i);
                                        barra.setVisibility(View.INVISIBLE);
                                        btnIngresar.setEnabled(true);
                                    }
                                });
                    } else {
                        System.out.println("error1");

                    }
                } else {
                    System.out.println("error2");
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
