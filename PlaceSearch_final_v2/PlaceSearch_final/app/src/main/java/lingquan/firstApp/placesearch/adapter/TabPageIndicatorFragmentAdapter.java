package lingquan.firstApp.placesearch.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ListView;

import java.util.List;

import lingquan.firstApp.placesearch.viewpagerindicator.IconPagerAdapter;



public  class TabPageIndicatorFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter implements IconPagerAdapter{
    private List<T> list;
    private List<String> titles;
    private List<Integer> drawables;
    private Context context;

    public TabPageIndicatorFragmentAdapter(FragmentManager fm, List<T> list, Context context,List<String> titles,List<Integer> drawable) {
        super(fm);
        this.list = list;
        this.context = context;
        this.titles = titles;
        this.drawables = drawable;
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getIconResId(int index) {
        return drawables.get(index);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
