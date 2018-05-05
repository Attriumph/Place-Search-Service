package lingquan.firstApp.placesearch.jsonscn.json2bean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class AddressComponents implements Serializable {

    @SerializedName("long_name")
    private String longName;
    @SerializedName("short_name")
    private String shortName;
    private List<String> types;
    public void setLongName(String longName) {
         this.longName = longName;
     }
     public String getLongName() {
         return longName;
     }

    public void setShortName(String shortName) {
         this.shortName = shortName;
     }
     public String getShortName() {
         return shortName;
     }

    public void setTypes(List<String> types) {
         this.types = types;
     }
     public List<String> getTypes() {
         return types;
     }

}