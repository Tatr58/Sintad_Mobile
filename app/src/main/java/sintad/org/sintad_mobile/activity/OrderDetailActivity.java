package sintad.org.sintad_mobile.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import sintad.org.sintad_mobile.R;
import sintad.org.sintad_mobile.fragment.DetailFragment;
import sintad.org.sintad_mobile.fragment.RoutesFragment;
import sintad.org.sintad_mobile.fragment.TruckLoadFragment;

public class OrderDetailActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    RoutesFragment routes = new RoutesFragment();
                    return routes;
                case 1:
                    DetailFragment detail = new DetailFragment();
                    return detail;
                case 2:
                    TruckLoadFragment truckload = new TruckLoadFragment();
                    return truckload;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ruta";
                case 1:
                    return "Ordenes";
                case 2:
                    return "Carga";
            }
            return null;
        }
    }
}
