package lingquan.firstApp.placesearch.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;



public class SharePreferenceUtils {
    private String name="places_search";
    private String favorite_name="favorite";

    private SharedPreferences sp;
    private static SharePreferenceUtils instance;
    private SharedPreferences.Editor editor;

    private SharePreferenceUtils(Context context) {
        sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SharePreferenceUtils getInstance(Context context){
        if (instance == null){
            synchronized (SharePreferenceUtils.class){
                if (instance == null){
                    instance = new SharePreferenceUtils(context);
                }
            }
        }
        return instance;
    }

    public List<PlacesSearchResultObj.PlacesSearchResultItemObj> getMyFavorite(){
        String name = sp.getString(favorite_name,"");
        if (TextUtils.isEmpty(name)) return new ArrayList<>();
        return new Gson().fromJson(name,new TypeToken<List<PlacesSearchResultObj.PlacesSearchResultItemObj>>(){}.getType());
    }

    public void addMyFavorite(PlacesSearchResultObj.PlacesSearchResultItemObj obj){
        List<PlacesSearchResultObj.PlacesSearchResultItemObj> myFavorite = getMyFavorite();
        myFavorite.add(obj);
        editor.putString(favorite_name,new Gson().toJson(myFavorite));
        editor.commit();
    }

    public void removeMyFavorite(PlacesSearchResultObj.PlacesSearchResultItemObj obj){
        List<PlacesSearchResultObj.PlacesSearchResultItemObj> myFavorite = getMyFavorite();
        int index = 0;
        for (index = 0;index<myFavorite.size();index++){
            if (myFavorite.get(index).getId().equals(obj.getId())) break;
        }
        if (index<myFavorite.size()) myFavorite.remove(index);
        editor.putString(favorite_name,new Gson().toJson(myFavorite));
        editor.commit();
    }

    public boolean isMyFavorite(PlacesSearchResultObj.PlacesSearchResultItemObj obj) {
        List<PlacesSearchResultObj.PlacesSearchResultItemObj> myFavorite = getMyFavorite();
        for (PlacesSearchResultObj.PlacesSearchResultItemObj itemObj:myFavorite){
            if (itemObj.getId().equals(obj.getId())) return true;
        }
        return false;
    }
}
