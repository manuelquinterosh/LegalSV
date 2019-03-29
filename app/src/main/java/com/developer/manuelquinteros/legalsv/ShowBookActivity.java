package com.developer.manuelquinteros.legalsv;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowBookActivity extends AppCompatActivity {

    private PDFView pdfView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private TextView textView;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_showBooks);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        textView = (TextView) findViewById(R.id.textDato);

        extras = getIntent().getExtras();

        String title = extras.getString("title");

        DatabaseReference mref = database.getReference("pdf_constitucion").child(title);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                       new RetrivePdfStream().execute(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {

            InputStream inputStream;

            @Override
            protected InputStream doInBackground(String... strings) {

                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                    if (urlConnection.getResponseCode() ==200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    }
                }catch (IOException e){
                    return null;
                }

                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).
                         defaultPage(0).
                         scrollHandle(new DefaultScrollHandle(getApplicationContext())).
                         enableSwipe(true).
                         swipeHorizontal(true).

                         password(null).
                         enableDoubletap(true).
                         enableAnnotationRendering(true).
                         load();
            }
      }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // por defecto si escribes aquí el super el botón hará lo que debía hacer si lo quitas ya no hará lo que debía de hacer y puedes programar otros comportamientos.
//Quita el super y has un finish() a la actividad o bien replanteate bien lo que quieres hacer cuando se presione hacia atrás.

    }

}
