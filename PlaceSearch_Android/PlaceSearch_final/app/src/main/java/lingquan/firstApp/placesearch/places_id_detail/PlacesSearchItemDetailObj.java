package lingquan.firstApp.placesearch.places_id_detail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class PlacesSearchItemDetailObj implements Serializable{

    @SerializedName("html_attributions")
    private List<String> htmlAttributions;
    private Result result;
    private String status;
    public void setHtmlAttributions(List<String> htmlAttributions) {
         this.htmlAttributions = htmlAttributions;
     }
     public List<String> getHtmlAttributions() {
         return htmlAttributions;
     }

    public void setResult(Result result) {
         this.result = result;
     }
     public Result getResult() {
         return result;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

}