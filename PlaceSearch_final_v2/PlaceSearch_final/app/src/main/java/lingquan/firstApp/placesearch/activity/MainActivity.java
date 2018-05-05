package lingquan.firstApp.placesearch.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.Utils;
import lingquan.firstApp.placesearch.adapter.MyFragmentPagerAdapter;
import lingquan.firstApp.placesearch.fragment.PlaceFavoritesFragment;
import lingquan.firstApp.placesearch.fragment.PlaceSearchFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private MyFragmentPagerAdapter mAdapter;
    private PlaceSearchFragment mPlaceSearchFragment;
    private PlaceFavoritesFragment mPlaceFavoritesFragment;
    private View mLine;
    private View mPlacesSearchLayout;
    private View mPlacesFarivoritesLayout;
    private int width;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        mPlacesSearchLayout = findViewById(R.id.places_search_layout);
        mPlacesFarivoritesLayout = findViewById(R.id.places_favorite_layout);

        mLine = findViewById(R.id.line);

        viewPager.setOffscreenPageLimit(2);


        mPlaceSearchFragment = new PlaceSearchFragment();
        mPlaceFavoritesFragment = new PlaceFavoritesFragment();

        width = Utils.getWidth(this);
        ViewGroup.LayoutParams lp = mLine.getLayoutParams();
        lp.width = width / 2;

        initView();

        mPlacesSearchLayout.setOnClickListener(this);
        mPlacesFarivoritesLayout.setOnClickListener(this);
    }

    private void initView() {

        List<Fragment> list = new ArrayList<>();
        list.add(mPlaceSearchFragment);
        list.add(mPlaceFavoritesFragment);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list, this);
        viewPager.setAdapter(mAdapter);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v== mPlacesSearchLayout){
            viewPager.setCurrentItem(0,true);
        } else if (v== mPlacesFarivoritesLayout){
            viewPager.setCurrentItem(1,true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mLine,"x",mLine.getX(),width/2*position);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
