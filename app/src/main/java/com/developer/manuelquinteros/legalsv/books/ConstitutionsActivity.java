package com.developer.manuelquinteros.legalsv.books;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import com.developer.manuelquinteros.legalsv.CustomItemClickListener;
import com.developer.manuelquinteros.legalsv.Model.Menu;
import com.developer.manuelquinteros.legalsv.R;
import com.developer.manuelquinteros.legalsv.ShowBookActivity;
import com.developer.manuelquinteros.legalsv.adapter.ConstitutionsRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConstitutionsActivity extends AppCompatActivity {

    private List<Menu> listConstitution;
    private RecyclerView recyclerView;
    ConstitutionsRecyclerViewAdapter mAdapter;
    private Context mContext;

    public static final String FB_DATABASE_PATH = "constituciones";

    private DatabaseReference mDatabase;

    private ProgressDialog progressDialog;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constitutions);

        view = findViewById(R.id.noInternetConstitutions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConstitution);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        recyclerView = (RecyclerView) findViewById(R.id.list_books_constitution);

        progressDialog = new ProgressDialog(this);

        mContext = ConstitutionsActivity.this;

        listConstitution = new ArrayList<>();


        new AsyncConnectTask().execute();

    }

    public class AsyncConnectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Cargando..");
            progressDialog.setMessage("Preparando las Constituciones");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            //Ejecutas tu método para comprobar conexión, el cual regresa un valor boleano.
            return isOnline(ConstitutionsActivity.this);
        }

        @Override
        protected void onPostExecute(Boolean isAvalable) {
            //Se recibe el valor boleano del método doInBackground().
            // Se puede abrir el Dialogo en el Thread principal.

            if (!isAvalable) {
                view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                //....hago otra cosa
                ConstitutionsMenu();
                initCollapsingToolbar();
            }
        }
    }


    private void ConstitutionsMenu(){
        mDatabase = FirebaseDatabase.getInstance().getReference(ConstitutionsActivity.FB_DATABASE_PATH);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Menu port =snapshot.getValue(Menu.class);
                    listConstitution.add(port);

                }
                mAdapter = new ConstitutionsRecyclerViewAdapter(mContext, listConstitution, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(mContext, ShowBookActivity.class);
                        intent.putExtra("title", listConstitution.get(position).getTitle());
                        startActivity(intent);
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ConstitutionsActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(10), true ));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }



    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
