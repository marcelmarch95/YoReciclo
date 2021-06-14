package com.example.pataconf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.InputStream;
import java.net.URL;

import Modelo.Recicladora;

public class PerfilComerciante extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    private TextView encargado;
    private TextView comercio;
    private ImageView logoComercio;
    private Recicladora usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_comerciante);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_reports, R.id.nav_slideshow, R.id.nav_calendario, R.id.nav_cobertura,
                R.id.nav_micomercio)
                .setDrawerLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerLayout = navigationView.getHeaderView(0);

        this.encargado = (TextView) headerLayout.findViewById(R.id.encargado);
        this.comercio = (TextView) headerLayout.findViewById(R.id.comercio);
        this.logoComercio = (ImageView) headerLayout.findViewById(R.id.logoComercio);

        System.out.println(this.encargado.toString());
        System.out.println(this.comercio.toString());

        getSupportActionBar().setTitle("Inicio");



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("recicladora").document(user.getUid().toString());
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
                                                            Toast.makeText(PerfilComerciante.this, "key user updated successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(PerfilComerciante.this, "key user updated error", Toast.LENGTH_LONG).show();
                                                            Log.w("no actualizado", "Error updating document", e);
                                                        }
                                                    });
                                        }
                                    }
                                });
                        cargarVista(document);
                    } else {
                        System.out.println("error1");

                    }
                } else {
                    System.out.println("error2");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil_comerciante, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        System.out.println("pille el back");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void cargarVista(DocumentSnapshot document) {
        System.out.println("cargarvista metodo");
        this.usuario = (Recicladora) document.toObject(Recicladora.class);

        System.out.println("usuario comerciante: " + this.usuario.toString());

        try {
            new PerfilComerciante.DownloadImageTask(this.logoComercio).execute(this.usuario.getFoto());
        }
        catch (Exception e){
            this.logoComercio.setImageResource(R.drawable.recicon);
            System.out.println("Error al cargar foto: " + this.usuario.getFoto());
        }

        System.out.println(this.usuario.getNombreEncargado());
        System.out.println();

        encargado.setText(this.usuario.getNombreEncargado());
        comercio.setText(this.usuario.getNombreEmpresa());

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result==null){
                bmImage.setImageResource(R.drawable.market);
            }
            else {
                bmImage.setImageBitmap(result);

            }
        }
    }
}
