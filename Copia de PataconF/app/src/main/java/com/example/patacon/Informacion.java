package com.example.patacon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Informacion extends AppCompatActivity implements View.OnClickListener {

    //private Timer myTimer;
    //private int segundos;
    private String mensaje;
    private Button finalizar;
    private TextView text;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        this.finalizar = findViewById(R.id.finalizar);
        this.finalizar.setOnClickListener(this);

        this.text = findViewById(R.id.text1);
        this.imageView = findViewById(R.id.imageView);

        Intent i = getIntent();
        boolean result = i.getExtras().getBoolean("result");
        this.mensaje = i.getExtras().getString("mensaje");

        this.text.setText(this.mensaje);

        if (!result){
            imageView.setImageResource(R.drawable.error);
        }

        /*myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                segundos++;
                progressBar.setProgress(segundos);
                if (segundos==3){
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }
            }

        }, 0, 1000);*/
    }


    @Override
    public void onClick(View view) {
        if (view == this.finalizar){
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }
    }
}
