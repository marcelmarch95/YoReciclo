package com.example.pataconf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Modelo.Notificacion;
import Modelo.Usuario;
import Modelo.Viaje;


public class LayoutCampo extends Fragment {


    private FragmentManager fragmentManager;
    private Usuario user;
    private TextView key;
    private EditText value;
    private Button modificar;
    private String ke, va;
    private ProgressBar cargando;
    private TextView textError;
    private LinearLayout pre;
    private Button volver;
    private TextView title;


    public LayoutCampo() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.layout_campo, container, false);
        return inflater.inflate(R.layout.layout_campo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle args = getArguments();
        this.user = (Usuario) args.getSerializable("usuario");
        ke = (String) args.getSerializable("title");
        va = (String) args.getSerializable("value");

        this.key = (TextView) view.findViewById(R.id.keyy);
        this.value = (EditText) view.findViewById(R.id.valuee);
        this.modificar = (Button) view.findViewById(R.id.btnModificar);

        this.textError = (TextView) view.findViewById(R.id.textError);
        this.textError.setVisibility(View.INVISIBLE);

        this.title = (TextView) view.findViewById(R.id.title);


        this.pre = (LinearLayout) view.findViewById(R.id.layoutPre);

        if (ke.compareTo("Número de teléfono")!=0){
            pre.setVisibility(View.GONE);
            this.title.setText("Modificar Contraseña");
            this.key.setText("Contraseña nueva");


            int maxLength = 20;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            this.value.setFilters(FilterArray);
            this.value.setText("");
        }
        else {
            this.title.setText("Modificar Teléfono");
            this.value.setInputType(InputType.TYPE_CLASS_TEXT);
            this.key.setText(ke);
            this.value.setText(va);
        }

        this.volver = view.findViewById(R.id.buttonVolver);

        this.value.requestFocus();

        this.cargando = (ProgressBar) view.findViewById(R.id.progressBar);
        cargando.setVisibility(View.INVISIBLE);

        this.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCampo();
            }

        });

        this.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable("usuario", user);

                ActivityPerfil fragmentPerfil = new ActivityPerfil();
                fragmentPerfil.setArguments(args);
                fragmentPerfil.setFragmentManager(fragmentManager);
                final FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                transaction4.replace(R.id.main_container, fragmentPerfil).commit();
            }

        });

    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void verificarCampo(){

        boolean resultado = false;

        if(ke.compareTo("Número de teléfono")==0){
            //verificar telefono

            if (this.value.getText().length()!=8)
                this.textError.setVisibility(View.VISIBLE);
            else {
                resultado = true;
            }
        }


        else if (ke.compareTo("Contraseña nueva")==0){
            //verificar contra

            if (value.getText().toString().length()<8 || value.getText().toString().length()>20){
                this.textError.setText("Error, la contraseña debe tener entre 8 y 20 caracteres");
                this.textError.setVisibility(View.VISIBLE);
            }
            else {
                resultado = true;
            }

        }

        if (resultado){
            cargando.setVisibility(View.VISIBLE);


            if (ke.compareTo("Contraseña nueva")==0){
                FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("user entro aca" + userr.toString());

                userr.updatePassword(this.value.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Bundle args = new Bundle();
                                    args.putSerializable("usuario", user);

                                    ActivityPerfil fragmentPerfil = new ActivityPerfil();
                                    fragmentPerfil.setArguments(args);
                                    fragmentPerfil.setFragmentManager(fragmentManager);
                                    final FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                                    transaction4.replace(R.id.main_container, fragmentPerfil).commit();
                                }
                            }
                        });
            }
            else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                final DocumentReference docRef = db.collection("usuario").document(user.getUid().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                FirebaseInstanceId.getInstance().getInstanceId() .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("tag", "getInstanceId failed", task.getException());
                                            return;
                                        }
                                        String antigua = document.get("telefono").toString();
                                        user.setTelefono(value.getText().toString());

                                        //Token cambió
                                        if (value.getText().toString().compareTo(antigua)!=0){
                                            docRef.update("telefono", value.getText().toString()) .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Bundle args = new Bundle();
                                                    args.putSerializable("usuario", user);

                                                    ActivityPerfil fragmentPerfil = new ActivityPerfil();
                                                    fragmentPerfil.setArguments(args);
                                                    fragmentPerfil.setFragmentManager(fragmentManager);
                                                    final FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                                                    transaction4.replace(R.id.main_container, fragmentPerfil).commit();
                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                        }
                                        else {
                                            Bundle args = new Bundle();
                                            args.putSerializable("usuario", user);
                                            ActivityPerfil fragmentPerfil = new ActivityPerfil();
                                            fragmentPerfil.setArguments(args);
                                            fragmentPerfil.setFragmentManager(fragmentManager);
                                            final FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                                            transaction4.replace(R.id.main_container, fragmentPerfil).commit();
                                        }
                                    }
                                });
                            } else {
                            }
                        } else {
                        }
                    }
                });
            }

        }

    }


}