package lingquan.firstApp.placesearch.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.BaseUrl;
import lingquan.firstApp.placesearch.Utils.SharePreferenceUtils;
import lingquan.firstApp.placesearch.adapter.PlacesSearchResultAdapter;
import lingquan.firstApp.placesearch.fragment.PlacesSearchNoneResultFragment;
import lingquan.firstApp.placesearch.obj.MyFavoriteChangedObj;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;



public class PlacesSearchResultActivtity extends FragmentActivity implements View.OnClickListener, PlacesSearchResultAdapter.OnClickFavoriteListener {
    private PlacesSearchResultObj obj;
    private View mBack;
    private RecyclerView mList;
    private Button mPrevious;
    private Button mNext;
    private PlacesSearchResultAdapter adapter;
    private Map<Integer, PlacesSearchResultObj> map;
    private int currentIndex;
    private String searchUrl;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_search_result_activity);
        EventBus.getDefault().register(this);
        mBack = findViewById(R.id.back);
        mList = findViewById(R.id.listview);
        mPrevious = findViewById(R.id.previous);
        mNext = findViewById(R.id.next);
        map = new HashMap<>();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching Next page");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mBack.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);
        obj = (PlacesSearchResultObj) getIntent().getSerializableExtra("data");
        searchUrl = getIntent().getStringExtra("url");
        if (obj.getResults() == null || obj.getResults().size() == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.search_layout, new PlacesSearchNoneResultFragment()).commit();
            findViewById(R.id.btn_layout).setVisibility(View.GONE);
        }
        adapter = new PlacesSearchResultAdapter(this, obj.getResults());
        adapter.setListener(this);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.setAdapter(adapter);
        map.put(currentIndex, obj);
        if (currentIndex == 0) mPrevious.setEnabled(false);
        if (TextUtils.isEmpty(map.get(currentIndex).getNext_page_token())){
            mNext.setEnabled(false);
        }else {
            mNext.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyFavoriteChangedObj item){
       adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            finish();
        } else if (v == mNext) {
            if (map.containsKey(currentIndex + 1)) {
                currentIndex = currentIndex + 1;
                adapter.refreshList(map.get(currentIndex).getResults());
                if (TextUtils.isEmpty(map.get(currentIndex).getNext_page_token())) mNext.setEnabled(false);
                else mNext.setEnabled(true);
                if (currentIndex != 0){
                    mPrevious.setEnabled(true);
                }
            } else {
                String nextpageToken = map.get(currentIndex).getNext_page_token();
                if (TextUtils.isEmpty(nextpageToken)) return;
                //search next
                dialog.show();
                RequestQueue mQueue = Volley.newRequestQueue(this);
//                http://placesearch-env.us-west-1.elasticbeanstalk.com/search/next?token=CpQCAgEAAFxg8o-eU7_uKn7Yqjana-HQIx1hr5BrT4zBaEko29ANsXtp9mrqN0yrKWhf-y2PUpHRLQb1GT-mtxNcXou8TwkXhi1Jbk-ReY7oulyuvKSQrw1lgJElggGlo0d6indiH1U-tDwquw4tU_UXoQ_sj8OBo8XBUuWjuuFShqmLMP-0W59Vr6CaXdLrF8M3wFR4dUUhSf5UC4QCLaOMVP92lyh0OdtF_m_9Dt7lz-Wniod9zDrHeDsz_by570K3jL1VuDKTl_U1cJ0mzz_zDHGfOUf7VU1kVIs1WnM9SGvnm8YZURLTtMLMWx8-doGUE56Af_VfKjGDYW361OOIj9GmkyCFtaoCmTMIr5kgyeUSnB-IEhDlzujVrV6O9Mt7N4DagR6RGhT3g1viYLS4kO5YindU6dm3GIof1Q
                String url = BaseUrl.NEXT_PAGE_URL + "/next?token=" + nextpageToken;
                Log.v("next page url",url);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        PlacesSearchResultObj obj = new Gson().fromJson(response, PlacesSearchResultObj.class);
                        if (obj.getResults() != null && obj.getResults().size() > 0) {
                            currentIndex++;
                            map.put(currentIndex, obj);
                            if (TextUtils.isEmpty(map.get(currentIndex).getNext_page_token())) mNext.setEnabled(false);
                            else mNext.setEnabled(true);
                            adapter.refreshList(obj.getResults());
                        }
                        if (currentIndex !=0) mPrevious.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                });
                mQueue.add(request);
            }
        } else if (v == mPrevious) {
            if (map.containsKey(currentIndex - 1)) {
                currentIndex--;
                if (currentIndex == 0) mPrevious.setEnabled(false);
                else mPrevious.setEnabled(true);
                adapter.refreshList(map.get(currentIndex).getResults());
                if (TextUtils.isEmpty(map.get(currentIndex).getNext_page_token())){
                    mNext.setEnabled(false);
                } else {
                    mNext.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void onClick(PlacesSearchResultObj.PlacesSearchResultItemObj item) {
        List<PlacesSearchResultObj.PlacesSearchResultItemObj> myFavorite = SharePreferenceUtils.getInstance(this).getMyFavorite();
        int index = 0;
        for (index = 0;index<myFavorite.size();index++){
            String id = myFavorite.get(index).getId();
            if (id.equals(item.getId())) break;
        }
        if (index<myFavorite.size()) {
            Toast.makeText(this,item.getName()+"was removed from favorites",Toast.LENGTH_LONG).show();
            SharePreferenceUtils.getInstance(this).removeMyFavorite(item);
        }
        else {
            Toast.makeText(this,item.getName()+"was added to favorites",Toast.LENGTH_LONG).show();
            SharePreferenceUtils.getInstance(this).addMyFavorite(item);
        }
        adapter.notifyDataSetChanged();
        EventBus.getDefault().post(new MyFavoriteChangedObj());
    }
}
