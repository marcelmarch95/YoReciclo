package com.example.patacon.ui.reportepunto2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patacon.Informacion;
import com.example.patacon.PerfilComerciante;
import com.example.patacon.R;
import com.example.patacon.ui.cargando.CargandoFragment;
import com.example.patacon.ui.informacion.InformacionFragment;
import com.example.patacon.ui.optionproducts.OptionsPuntosListViewModel;
import com.example.patacon.ui.puntosmapa.PuntosMapaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Modelo.ModeloVistaPunto;
import Modelo.Punto;
import Modelo.Reporte;

import static android.app.Activity.RESULT_OK;

public class ReportePunto2Fragment extends Fragment implements View.OnClickListener {

    private OptionsPuntosListViewModel homeViewModel;
    private TextView title;
    private TextView tvdireccion;
    private ArrayList<ModeloVistaPunto> data = new ArrayList<>();
    private ImageView imgp;
    private ImageView imgl;
    private ImageView imgv;
    private String userid;
    private TextView motivo;
    private ArrayList<Punto> data2 = new ArrayList<>();
    private String fecha;
    private String hora;
    private EditText comentarios;

    private Button volver;
    private boolean stateMap;
    private Button selectfoto;
    private Button guardar;
    private Button eliminarfoto;
    private Button continuar;
    private Button exit;
    private ImageView foto;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    private Uri imageUri;
    private View root;
    private static final int PICK_IMAGE = 100;
    private ModeloVistaPunto punto;
    private String motivoreporte;

    private Reporte reporte;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        homeViewModel =
                ViewModelProviders.of(this).get(OptionsPuntosListViewModel.class);
        root = inflater.inflate(R.layout.fragment_reportepunto2, container, false);

        this.punto = (ModeloVistaPunto) getArguments().getSerializable("punto");
        this.motivoreporte = getArguments().getString("motivo");
        title = (TextView) root.findViewById(R.id.title);

        mAuth = FirebaseAuth.getInstance();

        this.motivo = root.findViewById(R.id.motivo);
        this.motivo.setText(this.motivoreporte);

        this.comentarios = root.findViewById(R.id.comentarios);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        this.tvdireccion = root.findViewById(R.id.tvdireccion);

        System.out.println("Direccion en ver punto: " + punto.getDireccion());
        this.tvdireccion.setText(punto.getDireccion());

        this.volver = root.findViewById(R.id.volver);

        this.volver.setOnClickListener(this);

        this.fecha = String.valueOf(Calendar.getInstance().getTime().getDate()) + "/"+
                String.valueOf((Calendar.getInstance().getTime().getMonth()+1)) + "/" +
                String.valueOf(Calendar.getInstance().getTime().getYear()+1900);

        this.hora = String.valueOf(Calendar.getInstance().getTime().getHours()) + ":" +
                String.valueOf(Calendar.getInstance().getTime().getMinutes()) + ":" +
                String.valueOf(Calendar.getInstance().getTime().getSeconds());


        System.out.println("Finalmente fecha y hora: " + fecha + " " + hora);

        System.out.println("Observ: " + punto.getObservacion());
        System.out.println("id: "  +punto.getId());
        this.foto = (ImageView) root.findViewById(R.id.imageView4);
        this.selectfoto = (Button) root.findViewById(R.id.selectfoto);
        this.selectfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        db = FirebaseFirestore.getInstance();

        this.reporte = new Reporte();

        if (this.motivoreporte.compareTo("otro")==0) {

            this.motivo.setText("OTRO MOTIVO");
            this.foto.setImageResource(R.drawable.otro);
        }
        else if (this.motivoreporte.compareTo("inexistente")==0) {
            this.motivo.setText("PUNTO INEXISTENTE");
            this.foto.setImageResource(R.drawable.inexistente);
        }
        else if (this.motivoreporte.compareTo("lleno")==0) {
            this.motivo.setText("PUNTO LLENO");
            this.foto.setImageResource(R.drawable.full);
        }
        else if (this.motivoreporte.compareTo("deteriorado")==0){
            this.motivo.setText("PUNTO DETERIORADO");
            this.foto.setImageResource(R.drawable.roto);
        }

        this.eliminarfoto = (Button) root.findViewById(R.id.eliminarfoto);
        this.eliminarfoto.setOnClickListener(this);

        this.imgp = root.findViewById(R.id.imgp);
        this.imgl = root.findViewById(R.id.imgl);
        this.imgv = root.findViewById(R.id.imgv);

        if (punto.isIsplastico()==false) {
            imgp.setVisibility(View.INVISIBLE);
            imgp.getLayoutParams().height = 0;
            imgp.getLayoutParams().width = 0;
            imgp.requestLayout();
        }
        if (punto.isIslatas()==false){
            imgl.setVisibility(View.INVISIBLE);
            imgl.getLayoutParams().height = 0;
            imgl.getLayoutParams().width = 0;
            imgl.requestLayout();
        }
        if (punto.isIsvidrio()==false){
            imgv.setVisibility(View.INVISIBLE);
            imgv.getLayoutParams().height = 0;
            imgv.getLayoutParams().width = 0;
            imgv.requestLayout();
        }


        this.continuar = root.findViewById(R.id.continuar);
        this.continuar.setOnClickListener(this);


        ((PerfilComerciante) getActivity()).getSupportActionBar().setTitle("Reporte Punto");

        return root;
    }


    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto.setImageURI(imageUri);
            eliminarfoto.setEnabled(true);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == this.eliminarfoto){
            if (this.motivoreporte.compareTo("otro")==0) {
                this.foto.setImageResource(R.drawable.otro);
            }
            else if (this.motivoreporte.compareTo("inexistente")==0) {
                this.foto.setImageResource(R.drawable.inexistente);
            }
            else if (this.motivoreporte.compareTo("lleno")==0) {
                this.foto.setImageResource(R.drawable.full);
            }
            else if (this.motivoreporte.compareTo("deteriorado")==0){
                this.foto.setImageResource(R.drawable.roto);
            }
            this.eliminarfoto.setEnabled(false);
        }

        if (view==this.continuar){
            this.crearReporte();
        }

        if (view == this.volver){
            db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            db.collection("punto")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    data2.add(document.toObject(Punto.class));
                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("puntos", data2);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                Fragment lp = new PuntosMapaFragment();
                                lp.setArguments(bundle);
                                fragmentTransaction.replace(R.id.nav_host_fragment, lp);
                                fragmentTransaction.commit();
                            } else {
                                System.out.println("Error al conectarse ");
                            }
                        }
                    });

        }
    }


    private void crearReporte(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CargandoFragment cf = new CargandoFragment();
        fragmentTransaction.replace(R.id.nav_host_fragment, cf);
        fragmentTransaction.commit();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String dia = String.valueOf(Calendar.getInstance().getTime().getDate());
        String mes = String.valueOf((Calendar.getInstance().getTime().getMonth()+1));
        String año = String.valueOf(Calendar.getInstance().getTime().getYear()+1900);

        String horaa = String.valueOf(Calendar.getInstance().getTime().getHours());
        String minuto = String.valueOf(Calendar.getInstance().getTime().getMinutes());
        String segundo = String.valueOf(Calendar.getInstance().getTime().getSeconds());

        if (Integer.valueOf(dia)<10)
            dia = "0"+dia;
        if (Integer.valueOf(mes)<10)
            mes = "0"+mes;

        if (Integer.valueOf(horaa)<10)
            horaa = "0"+horaa;
        if (Integer.valueOf(minuto)<10)
            minuto = "0" + minuto;
        if (Integer.valueOf(segundo)<10)
            segundo = "0" + segundo;

        this.fecha = dia + "/"+ mes + "/" + año;
        this.hora = horaa + ":" + minuto + ":" + segundo;


        System.out.println("Finalmente fecha y hora: " + fecha + " " + hora);

        reporte.setFecha(this.fecha);
        reporte.setHora(this.hora);
        reporte.setIdPunto(this.punto.getId());
        reporte.setMotivo(this.motivoreporte);
        reporte.setComentarios(this.comentarios.getText().toString());
        reporte.setIdGenerador(this.mAuth.getUid());

        if (imageUri==null){
            reporte.setFoto("null");
            db.collection("reporte")
                    .add(reporte)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            System.out.println("Reporte creado correctamente con ID: " + documentReference.getId());

                            InformacionFragment fi = new InformacionFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Reporte enviado correctamente");
                            bundle.putBoolean("estado", true);
                            fi.setArguments(bundle);

                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                            fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                            fragmentTransaction2.commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            System.out.println("Error al crear el reporte");

                            InformacionFragment fi = new InformacionFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mensaje", "Error al enviar reporte");
                            bundle.putBoolean("estado", false);
                            fi.setArguments(bundle);

                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                            fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                            fragmentTransaction2.commit();
                        }
                    });
        }

        else {
            StorageReference riversRef = storageRef.child("images/"+mAuth.getUid()+"/" + imageUri.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(imageUri);

            //this.progressBar.setVisibility(View.VISIBLE);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("error al subir : " + exception.toString());
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri downloadUrl)
                        {
                            reporte.setFoto(downloadUrl.toString());
                            System.out.println("subida correctamente link: " + reporte.getFoto());

                            db.collection("reporte")
                                    .add(reporte)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            System.out.println("Reporte creado correctamente con ID: " + documentReference.getId());
                                            //Toast.makeText(getContext(),"Reporte creado correctamente con ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();


                                            InformacionFragment fi = new InformacionFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("mensaje", "Reporte enviado correctamente");
                                            bundle.putBoolean("estado", true);
                                            fi.setArguments(bundle);

                                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                                            fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                                            fragmentTransaction2.commit();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            System.out.println("Error al crear el reporte");
                                            //Toast.makeText(getContext(),"Error al crear reporte!",Toast.LENGTH_SHORT).show();


                                            InformacionFragment fi = new InformacionFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("mensaje", "Error al enviar reporte");
                                            bundle.putBoolean("estado", false);
                                            fi.setArguments(bundle);

                                            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

                                            fragmentTransaction2.replace(R.id.nav_host_fragment, fi);
                                            fragmentTransaction2.commit();
                                        }
                                    });

                        }
                    });

                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int intValue = (int) progress;
                    cf.actualizarCarga(intValue);
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            });
        }


    }

}