package com.example.patacon.ui.cargando;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.patacon.R;
import com.example.patacon.ui.optionretiros.OptionsRetirosListFragment;

import java.io.InputStream;

public class VistaFotografiaFragment extends Fragment implements View.OnClickListener {

    public Button volver;
    public ImageView imageView;
    public ProgressBar progressBar;
    public TextView carg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_vistafotografia, container, false);

        imageView = root.findViewById(R.id.imageView);
        progressBar = root.findViewById(R.id.progressBar);
        carg = root.findViewById(R.id.carg);


        String url =  getArguments().getString("url");

        try {
            new VistaFotografiaFragment.DownloadImageTask(this.imageView, this.progressBar, this.carg).execute(url);
        }
        catch (Exception e){
            this.imageView.setImageResource(R.drawable.nodata);
        }

        volver = root.findViewById(R.id.volver);
        volver.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == this.volver){
            FragmentManager fragmentManager = getFragmentManager();

            Fragment lp = new OptionsRetirosListFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, lp);
            fragmentTransaction.commit();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ProgressBar progressBar;
        TextView carg;

        public DownloadImageTask(ImageView bmImage, ProgressBar progressBar, TextView carg) {
            this.bmImage = bmImage;
            this.progressBar = progressBar;
            this.carg = carg;
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

            }
            else {
                bmImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                bmImage.setVisibility(View.VISIBLE);
                bmImage.setImageBitmap(result);
                bmImage.requestLayout();
                progressBar.setVisibility(View.INVISIBLE);
                carg.setText("");
                carg.setVisibility(View.INVISIBLE);
            }
        }
    }
}