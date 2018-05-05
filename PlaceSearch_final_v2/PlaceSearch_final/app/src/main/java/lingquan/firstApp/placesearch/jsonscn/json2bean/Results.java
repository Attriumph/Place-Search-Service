package lingquan.firstApp.placesearch.jsonscn.json2bean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class Results implements Serializable {

    @SerializedName("address_components")
    private List<AddressComponents> addressComponents;
    @SerializedName("formatted_address")
    private String formattedAddress;
    private Geometry geometry;
    @SerializedName("place_id")
    private String placeId;
    private List<String> types;
    public void setAddressComponents(List<AddressComponents> addressComponents) {
         this.addressComponents = addressComponents;
     }
     public List<AddressComponents> getAddressComponents() {
         return addressComponents;
     }

    public void setFormattedAddress(String formattedAddress) {
         this.formattedAddress = formattedAddress;
     }
     public String getFormattedAddress() {
         return formattedAddress;
     }

    public void setGeometry(Geometry geometry) {
         this.geometry = geometry;
     }
     public Geometry getGeometry() {
         return geometry;
     }

    public void setPlaceId(String placeId) {
         this.placeId = placeId;
     }
     public String getPlaceId() {
         return placeId;
     }

    public void setTypes(List<String> types) {
         this.types = types;
     }
     public List<String> getTypes() {
         return types;
     }

}