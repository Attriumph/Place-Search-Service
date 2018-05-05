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
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.Utils;
import lingquan.firstApp.placesearch.activity.PlacesSearchItemActivity;
import lingquan.firstApp.placesearch.adapter.PlacesSearchItemPhotoAdapter;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;
import lingquan.firstApp.placesearch.task.PhotoTask;



public class PlacesSearchItemPhotoFragment extends Fragment{

    private PlacesSearchResultObj.PlacesSearchResultItemObj obj;
    private RecyclerView listView;
    private PlacesSearchItemPhotoAdapter adapter;
    private List<PhotoTask.AttributedPhoto> data = new ArrayList<>();
    private Context context;
    private TextView txt;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        obj = (PlacesSearchResultObj.PlacesSearchResultItemObj) arguments.getSerializable("data");
        adapter = new PlacesSearchItemPhotoAdapter(context,data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.places_search_item_photo_fragment,container,false);
        listView = view.findViewById(R.id.listview);
        listView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        txt = view.findViewById(R.id.phone_no_txt);
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);
        int width = Utils.getWidth(context)-2*30;
        int height = (int) (width*0.6);
        new PhotoTask(context,width,height){
            @Override
            protected void onPostExecute(List<AttributedPhoto> attributedPhotos) {
                super.onPostExecute(attributedPhotos);
                if (attributedPhotos == null||attributedPhotos.size() == 0){
                    listView.setVisibility(View.GONE);
                    txt.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.GONE);
                    adapter.refreshList(attributedPhotos);

                }
                if (context instanceof PlacesSearchItemActivity) {
                    ((PlacesSearchItemActivity) context).requsetNumberAdd();
                }
            }
        }.execute(obj.getPlace_id());
        return view;
    }
}
