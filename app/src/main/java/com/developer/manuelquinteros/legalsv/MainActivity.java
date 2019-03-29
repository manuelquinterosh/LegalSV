package com.developer.manuelquinteros.legalsv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.manuelquinteros.legalsv.pref.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this);
        if (!preferenceManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        layouts = new int[] {
                R.layout.bienvenida,
                R.layout.objetivo,
                R.layout.finalizar};

        addBottomDots(0);

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int current = getItem(+1);
               if (current < layouts.length){
                   viewPager.setCurrentItem(current);
               } else {
                   launchHomeScreen();
               }
            }
        });

        }

        private void addBottomDots(int currentPage){
          dots = new TextView[layouts.length];

          int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
          int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

          dotsLayout.removeAllViews();
          for (int i = 0; i < dots.length; i++) {
              dots[i] = new TextView(this);
              dots[i].setText(Html.fromHtml(""));
              dots[i].setTextSize(35);
              dots[i].setTextColor(colorsInactive[currentPage]);
              dotsLayout.addView(dots[i]);
          }

          if (dots.length > 0) {
              dots[currentPage].setTextColor(colorsActive[currentPage]);
          }
        }

        private int getItem(int i) {
         return viewPager.getCurrentItem() + i;
        }

    private void launchHomeScreen() {

        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
        finish();

    }

    //Viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
           addBottomDots(i);

           if (i == layouts.length - 1) {
               btnNext.setText(getString(R.string.start));
               btnSkip.setVisibility(View.GONE);
           }else{
               btnNext.setText(getString(R.string.next));
               btnSkip.setVisibility(View.VISIBLE);
           }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
