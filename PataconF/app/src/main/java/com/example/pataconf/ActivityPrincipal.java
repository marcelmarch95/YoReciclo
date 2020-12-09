package com.example.pataconf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Collections;

import Modelo.Notificacion;
import Modelo.Recicladora;
import Modelo.Usuario;
import Modelo.Viaje;


public class ActivityPrincipal extends AppCompatActivity implements View.OnClickListener {

    private TextView nombreUsuario;
    private TextView tipoUsuario;
    private FirebaseAuth mAuth;
    private Button logout;
    private Button perfil;
    private ActivityNotificaciones fragmentNotificaciones;
    private ActivityEnCurso fragmentCurso;
    private ActivityReporte fragmentReporte;
    private ActivityCargando fragmentCargando;
    private ActivityPerfil fragmentPerfil;
    private Recicladora usuario;
    private FragmentManager fragmentManager;
    private RelativeLayout layoutPrincipal;
    private Toolbar toolbar;
    private Toolbar appbar;
    private BottomNavigationView botom;
    private FrameLayout frameLayout;
    private ActivityNoData fragmentNoData;
    private TabLayout tlayout;
    TabLayout.Tab tab1;
    TabLayout.Tab tab2;
    TabLayout.Tab tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //this.layoutPrincipal = findViewById(R.id.activity_main);
        this.frameLayout = findViewById(R.id.main_container);
        this.botom = findViewById(R.id.bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        fragmentCargando = new ActivityCargando();
        fragmentNotificaciones = new ActivityNotificaciones();
        fragmentCurso = new ActivityEnCurso();
        fragmentCurso.setFragmentManager(fragmentManager);
        fragmentCurso.setPapa(this);
        fragmentReporte = new ActivityReporte();
        fragmentReporte.setFragmentManager(fragmentManager);
        fragmentNoData = new ActivityNoData();


        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragmentCargando).commit();

        this.toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        this.toolbar.setVisibility(View.INVISIBLE);
        this.botom.setVisibility(View.INVISIBLE);

        nombreUsuario = (TextView) findViewById(R.id.nombreUsuario);
        tipoUsuario = (TextView) findViewById(R.id.tipoUsuario);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        System.out.println("sout: "+ user.toString());


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_noti:
                        setearPadding(90);
                        actualizarFragmentNotificaciones();
                        break;

                    case R.id.action_curso:
                        setearPadding(120);
                        actualizarFragmentCurso();
                        tab1.select();
                        break;

                    case R.id.action_report:
                        setearPadding(90);
                        final FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                        transaction3.replace(R.id.main_container, fragmentReporte).commit();
                        break;
                }


                return true;
            }
        });

        this.perfil = (Button) findViewById(R.id.btnPerfil);
        this.logout = (Button) findViewById(R.id.btnLogout);

        this.perfil.setOnClickListener(this);
        this.logout.setOnClickListener(this);

        System.out.println("aca");
        System.out.println("user id: " + user.getUid());


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
                                                    Toast.makeText(ActivityPrincipal.this, "key user updated successfully", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ActivityPrincipal.this, "key user updated error", Toast.LENGTH_LONG).show();
                                                    Log.w("no actualizado", "Error updating document", e);
                                                }
                                            });
                                }
                            }
                        });
                        cargarVista(document);
                    } else {
                        System.out.println("error1");
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentCargando).commit();
                    }
                } else {
                    System.out.println("error2");
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, fragmentCargando).commit();
                }
            }
        });

        tlayout= (TabLayout) findViewById(R.id.tlayout);
        tlayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().compareTo("En Curso")==0) {
                    System.out.println("En curso seleccionado");
                    setearPadding(120);
                    actualizarFragmentCurso();
                }
                else if (tab.getText().toString().compareTo("Planificados")==0){
                    System.out.println("Planificados seleccionados");
                    setearPadding(120);
                    actualizarFragmentPlanificado();
                }
                else {
                    System.out.println("Finalizados seleccionados");
                    setearPadding(120);
                    actualizarFragmentFinalizado();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getText().toString().compareTo("En Curso")==0) {
                    System.out.println("En curso seleccionado");
                    setearPadding(120);
                    actualizarFragmentCurso();
                }
                else if (tab.getText().toString().compareTo("Planificados")==0){
                    System.out.println("Planificados seleccionados");
                    setearPadding(120);
                    actualizarFragmentPlanificado();
                }
                else {
                    System.out.println("Finalizados seleccionados");
                    setearPadding(120);
                    actualizarFragmentFinalizado();
                }
            }
        });

        tab1 = tlayout.getTabAt(0);
        tab2 = tlayout.getTabAt(1);
        tab3 = tlayout.getTabAt(2);

    }



    private void setearPadding(int i) {
        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.height = dp1;
        toolbar.setLayoutParams(layoutParams);
        frameLayout.setPadding(0,dp1,0,0);
    }

    private void actualizarFragmentNotificaciones() {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragmentCargando).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference viajesRef = db.collection("notificacion");
        Query query = viajesRef.whereEqualTo("idProductor", user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Notificacion> nots = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Notificacion n = new Notificacion("1",R.drawable.patacon, doc.get("titulo").toString(), doc.get("cuerpo").toString(), doc.get("fecha").toString(), doc.get("hora").toString(),true);
                        n.setearFecha();
                        nots.add(n);
                    }
                    if (nots.size()>0){
                        Collections.sort(nots);
                        Bundle args = new Bundle();
                        args.putSerializable("nots", nots);
                        fragmentNotificaciones.setArguments(args);
                        final FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                        transaction2.replace(R.id.main_container, fragmentNotificaciones).commit();
                    }
                    else {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentNoData).commit();
                    }

                } else {
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, fragmentCargando).commit();
                }
            }
        });
    }

    public void actualizarFragmentPlanificado() {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragmentCargando).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference viajesRef = db.collection("viaje");
        Query query = viajesRef.whereEqualTo("idProductor", user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Viaje> viajes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        System.out.println("salida" + doc.toString());
                        Viaje v = new Viaje(doc.getId(),doc.get("estado").toString(),
                                doc.get("idChofer").toString(), doc.get("idProductor").toString(), doc.get("origen").toString(),
                                doc.get("destino").toString(), doc.get("origenTexto").toString(), doc.get("destinoTexto").toString(),
                                doc.get("ubicacionActual").toString(), doc.get("patente").toString(), doc.get("fechaInicio").toString(), doc.get("horaInicio").toString(), doc.get("carro").toString(), doc.get("envase").toString(), doc.get("kg").toString());
                        v.setearFecha();
                        System.out.println("viaje: " + v.toString());
                        if (v.getEstado().compareTo("Pendiente")==0){
                            viajes.add(v);
                        }
                    }
                    if (viajes.size()>0){
                        Collections.sort(viajes);
                        ArrayList<String> fechas = new ArrayList<>();

                        for (Viaje via: viajes){
                            if (fechas.size()<2){
                                if (fechas.contains(via.getFecha())==false)
                                    fechas.add(via.getFecha());
                            }
                        }

                        ArrayList<Viaje> proximos = new ArrayList<>();

                        for (Viaje v: viajes){
                            for (String d: fechas){
                                if (v.getFecha().compareTo(d)==0){
                                    proximos.add(v);
                                    break;
                                }
                            }
                        }

                        Bundle args = new Bundle();
                        args.putSerializable("viajes", proximos);
                        args.putSerializable("planned", 1);
                        args.putSerializable("origen", "planificado");
                        fragmentCurso.setArguments(args);
                        final FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                        transaction2.replace(R.id.main_container, fragmentCurso).commit();
                    }
                    else {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentNoData).commit();
                    }
                } else {
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, fragmentCargando).commit();
                }
            }
        });
    }

    public void actualizarFragmentCurso() {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragmentCargando).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference viajesRef = db.collection("viaje");
        Query query = viajesRef.whereEqualTo("idProductor", user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Viaje> viajes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        System.out.println("salida" + doc.toString());
                        Viaje v = new Viaje(doc.getId(),doc.get("estado").toString(),
                                doc.get("idChofer").toString(), doc.get("idProductor").toString(), doc.get("origen").toString(),
                                doc.get("destino").toString(), doc.get("origenTexto").toString(), doc.get("destinoTexto").toString(),
                                doc.get("ubicacionActual").toString(), doc.get("patente").toString(), doc.get("fechaInicio").toString(), doc.get("horaInicio").toString(), doc.get("carro").toString(), doc.get("envase").toString(), doc.get("kg").toString());
                        System.out.println("viaje: " + v.toString());
                        if (v.getEstado().compareTo("Asignado")==0)
                            viajes.add(v);
                    }
                    if (viajes.size()>0){
                        Bundle args = new Bundle();
                        args.putSerializable("viajes", viajes);
                        args.putSerializable("planned", 0);
                        args.putSerializable("origen", "curso");
                        fragmentCurso.setArguments(args);
                        final FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                        transaction2.replace(R.id.main_container, fragmentCurso).commit();
                    }
                    else {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentNoData).commit();
                    }
                } else {
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, fragmentCargando).commit();
                }
            }
        });
    }

    public void actualizarFragmentFinalizado() {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragmentCargando).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference viajesRef = db.collection("viaje");
        Query query = viajesRef.whereEqualTo("idProductor", user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Viaje> viajes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        System.out.println("salida" + doc.toString());
                        Viaje v = new Viaje(doc.getId(),doc.get("estado").toString(),
                                doc.get("idChofer").toString(), doc.get("idProductor").toString(), doc.get("origen").toString(),
                                doc.get("destino").toString(), doc.get("origenTexto").toString(), doc.get("destinoTexto").toString(),
                                doc.get("ubicacionActual").toString(), doc.get("patente").toString(), doc.get("fechaInicio").toString(), doc.get("horaInicio").toString(), doc.get("carro").toString(), doc.get("envase").toString(), doc.get("kg").toString());
                        System.out.println("viaje: " + v.toString());
                        if (v.getEstado().compareTo("Finalizado")==0)
                            viajes.add(v);
                    }
                    if (viajes.size()>0){
                        System.out.println("viajes finalziados: " + viajes);
                        Bundle args = new Bundle();
                        args.putSerializable("viajes", viajes);
                        args.putSerializable("planned", 1);
                        args.putSerializable("origen", "finalizado");
                        fragmentCurso.setArguments(args);
                        final FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                        transaction2.replace(R.id.main_container, fragmentCurso).commit();
                    }
                    else {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentNoData).commit();
                    }
                } else {
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, fragmentCargando).commit();
                }
            }
        });
    }



    private void cargarVista(DocumentSnapshot document) {
        System.out.println("cargarvista metodo");
        this.usuario = (Recicladora) document.toObject(Recicladora.class);

        System.out.println("usuario comerciante: " + this.usuario.toString());

        this.nombreUsuario.setText(usuario.getNombreEmpresa());
        this.tipoUsuario.setText("Comerciante");


        setearPadding(120);
        toolbar.setVisibility(View.VISIBLE);
        botom.setVisibility(View.VISIBLE);

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference viajesRef = db.collection("viaje");
        Query query = viajesRef.whereEqualTo("idProductor", user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Viaje> viajes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        System.out.println("salida" + doc.toString());
                        Viaje v = new Viaje(doc.getId(),doc.get("estado").toString(),
                                doc.get("idChofer").toString(), doc.get("idProductor").toString(), doc.get("origen").toString(),
                                doc.get("destino").toString(), doc.get("origenTexto").toString(), doc.get("destinoTexto").toString(),
                                doc.get("ubicacionActual").toString(), doc.get("patente").toString(), doc.get("fechaInicio").toString(), doc.get("horaInicio").toString(), doc.get("carro").toString(), doc.get("envase").toString(), doc.get("kg").toString());
                        System.out.println("viaje: " + v.toString());
                        if (v.getEstado().compareTo("Pendiente")!=0 && v.getEstado().compareTo("Finalizado")!=0)
                            viajes.add(v);
                    }
                    toolbar.setVisibility(View.VISIBLE);
                    botom.setVisibility(View.VISIBLE);

                    if (viajes.size()>0){
                        Bundle args = new Bundle();
                        args.putSerializable("viajes", viajes);
                        args.putSerializable("planned", 0);
                        args.putSerializable("origen", "curso");
                        fragmentCurso.setArguments(args);
                        final FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                        transaction2.replace(R.id.main_container, fragmentCurso).commit();
                    }
                    else {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentNoData).commit();
                    }
                } else {
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_container, fragmentCargando).commit();
                }
            }
        });*/
        tab1.select();
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnLogout){
            mAuth.signOut();
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }
        if (v.getId()==R.id.btnPerfil){
            setearPadding(90);

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragmentCargando).commit();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference docRef = db.collection("usuario").document(user.getUid().toString());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        final DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String nombre = document.get("nombre").toString();
                            String correo = document.get("correo").toString();
                            String key = document.get("keyNot").toString();
                            String perfil = document.get("perfil").toString();
                            String telefono = document.get("telefono").toString();
                            Usuario usu = new Usuario(user.getUid().toString(),nombre,correo,telefono,perfil,key);

                            Bundle args = new Bundle();
                            args.putSerializable("usuario", usu);

                            fragmentPerfil = new ActivityPerfil();
                            fragmentPerfil.setArguments(args);
                            fragmentPerfil.setFragmentManager(fragmentManager);
                            final FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                            transaction4.replace(R.id.main_container, fragmentPerfil).commit();
                        } else {
                            final FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.main_container, fragmentCargando).commit();
                        }
                    } else {
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragmentCargando).commit();
                    }
                }
            });



        }

    }

    @Override
    public void onBackPressed() {

    }
}
