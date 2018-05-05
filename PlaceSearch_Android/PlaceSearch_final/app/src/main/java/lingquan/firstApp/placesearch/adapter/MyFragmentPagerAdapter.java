package lingquan.firstApp.placesearch.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ListView;

import java.util.List;



public  class MyFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter{
    private List<T> list;
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, List<T> list, Context context) {
        super(fm);
        this.list = list;
        this.context = context;
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
