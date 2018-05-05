package lingquan.firstApp.placesearch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.SharePreferenceUtils;
import lingquan.firstApp.placesearch.obj.MyFavoriteChangedObj;



public class PlaceFavoritesFragment extends Fragment {


    private Context context;
    private PlacesFravoriteNoneFragment ft;
    private PlacesFravoriteFragment ft1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        ft = new PlacesFravoriteNoneFragment();
        ft1 = new PlacesFravoriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharePreferenceUtils.getInstance(context).getMyFavorite().size() == 0) {
            if (ft.isAdded()) {
            } else {
                getChildFragmentManager().beginTransaction().replace(R.id.content, ft).commit();
            }
        } else {
            if (ft1.isAdded()) {
            } else {
                getChildFragmentManager().beginTransaction().replace(R.id.content, ft1).commit();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyFavoriteChangedObj obj) {
        if (SharePreferenceUtils.getInstance(context).getMyFavorite().size() == 0) {
            if (ft.isAdded()) {
            } else {
                getChildFragmentManager().beginTransaction().replace(R.id.content, ft).commit();
            }
        } else {
            if (ft1.isAdded()) {
            } else {
                getChildFragmentManager().beginTransaction().replace(R.id.content, ft1).commit();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_favorite_fragment, container, false);
        if (SharePreferenceUtils.getInstance(context).getMyFavorite().size() == 0) {
            getChildFragmentManager().beginTransaction().replace(R.id.content, ft).commit();
        } else {
            getChildFragmentManager().beginTransaction().replace(R.id.content, ft1).commit();
        }
        return view;
    }
}
