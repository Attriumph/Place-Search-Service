package lingquan.firstApp.placesearch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.SharePreferenceUtils;
import lingquan.firstApp.placesearch.adapter.PlacesSearchResultAdapter;
import lingquan.firstApp.placesearch.obj.MyFavoriteChangedObj;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;



public class PlacesFravoriteFragment extends Fragment implements PlacesSearchResultAdapter.OnClickFavoriteListener {
    private RecyclerView listView;
    private PlacesSearchResultAdapter adapter;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyFavoriteChangedObj obj){
        adapter.refreshList(SharePreferenceUtils.getInstance(context).getMyFavorite());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.places_favorite_fragment,container,false);
        listView = view.findViewById(R.id.listview);
        adapter = new PlacesSearchResultAdapter(context, SharePreferenceUtils.getInstance(context).getMyFavorite());
        listView.setAdapter(adapter);
        adapter.setListener(this);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onClick(PlacesSearchResultObj.PlacesSearchResultItemObj item) {
        SharePreferenceUtils.getInstance(context).removeMyFavorite(item);
        EventBus.getDefault().post(new MyFavoriteChangedObj());
        Toast.makeText(context,item.getName()+"was removed from favorites",Toast.LENGTH_LONG).show();
    }
}
