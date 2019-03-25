package com.study.study1;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

//        Log.i(MyLog.TAG, "onCreate: "+Util.getRefreshTime(1));
//        Log.i(MyLog.TAG, "onCreate: "+Util.getRefreshTime(2));
//        Log.i(MyLog.TAG, "onCreate: "+Util.getRefreshTime(3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.caidan1:
                Toast.makeText(this,"caidan1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.caidan2:
                Toast.makeText(this,"caidan2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.caidan3:
                Toast.makeText(this,"caidan3",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter{
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return new ZhiHuFragment();
                case 1:
                    return new GanHuoFragment();
                case 2:
                    return new HaoQiXinFragment();
            }
            throw new IllegalStateException("no fragment at position " + i);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "知乎";
                case 1:
                    return "干货";
                case 2:
                    return "好奇心";
            }
            throw new IllegalStateException("wrong positon" + position);
        }
    }
}
