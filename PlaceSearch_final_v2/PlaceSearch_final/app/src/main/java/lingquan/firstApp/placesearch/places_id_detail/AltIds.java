package lingquan.firstApp.placesearch.places_id_detail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AltIds implements Serializable{

    @SerializedName("place_id")
    private String placeId;
    private String scope;
    public void setPlaceId(String placeId) {
         this.placeId = placeId;
     }
     public String getPlaceId() {
         return placeId;
     }

    public void setScope(String scope) {
         this.scope = scope;
     }
     public String getScope() {
         return scope;
     }

}