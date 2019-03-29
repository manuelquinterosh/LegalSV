package com.developer.manuelquinteros.legalsv;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.developer.manuelquinteros.legalsv.Model.Menu;
import com.developer.manuelquinteros.legalsv.adapter.MenuRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    public static final String FB_DATABASE_PATH = "menu";

    private DatabaseReference mDatabase;

    private List<Menu> imgList;
    private RecyclerView recyclerView;
    private MenuRecyclerViewAdapter adapter;
    private ProgressDialog progressDialog;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        imgList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerMenu);

        view = findViewById(R.id.noInternet);

        progressDialog = new ProgressDialog(this);


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                imgList.clear();
                swipeRefreshLayout.setRefreshing(false);
                new AsyncConnectTask().execute();
            }
        });

        new AsyncConnectTask().execute();


    }


    public class AsyncConnectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Cargando..");
            progressDialog.setMessage("Preparando menu de opciones");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            //Ejecutas tu método para comprobar conexión, el cual regresa un valor boleano.
            return isOnline(MenuActivity.this);

        }

        @Override
        protected void onPostExecute(Boolean isAvalable) {

            //Se recibe el valor boleano del método doInBackground().
            // Se puede abrir el Dialogo en el Thread principal.

            if (!isAvalable) {
                view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No existe conexión a Internet, intente mas tarde...", Toast.LENGTH_SHORT).show();
                Log.d("No hay Internet", "No HAY");
            } else {
                view.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                //....hago otra cosa
                menuFirebase();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


    private void menuFirebase() {

        mDatabase = FirebaseDatabase.getInstance().getReference(MenuActivity.FB_DATABASE_PATH);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                showLoadingIndicator(true);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu port = snapshot.getValue(Menu.class);
                    imgList.add(port);
                }
                adapter = new MenuRecyclerViewAdapter(imgList, MenuActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                recyclerView.setAdapter(adapter);

                showLoadingIndicator(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


   private void showLoadingIndicator(final boolean show){
       final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
       refreshLayout.post(new Runnable() {
           @Override
           public void run() {
               refreshLayout.setRefreshing(show);
           }
       });
   }
}
