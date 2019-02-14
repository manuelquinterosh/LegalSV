package com.developer.manuelquinteros.legalsv;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        imgList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerMenu);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading lis potrait...");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference(MenuActivity.FB_DATABASE_PATH);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Menu port =snapshot.getValue(Menu.class);
                    imgList.add(port);
                    Toast.makeText(MenuActivity.this, "Lista"+port.getTitle(), Toast.LENGTH_SHORT).show();
                    Log.d("Lista", port.getTitle());
                }
                adapter = new MenuRecyclerViewAdapter(imgList, MenuActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
