//package com.ratna.foosip;
//
//import android.app.Activity;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import Adapter.PagerAdapter;
//import StepperIndicator.StepperIndicator;
//
//
//public class MainActivity extends FragmentActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        final StepperIndicator indicator = (StepperIndicator) findViewById(R.id.stepperindicator);
//        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
//        final PagerAdapter adapter = new PagerAdapter
//                (getSupportFragmentManager(), 3);
//        pager.setAdapter(adapter);
//        indicator.setViewPager(pager);
//        indicator.setViewPager(pager, pager.getAdapter().getCount() - 1);
//        indicator.setStepCount(3);
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position==3)
//                {
//                    indicator.setCurrentStep(3);
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
