package lingquan.firstApp.placesearch.places_id_detail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lingquan.firstApp.placesearch.jsonscn.json2bean.Review;

public class Result implements Serializable{

    @SerializedName("address_components")
    private List<AddressComponents> addressComponents;
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;
    private Geometry geometry;
    private String icon;
    private String id;
    @SerializedName("international_phone_number")
    private String internationalPhoneNumber;
    private String name;
    @SerializedName("place_id")
    private String placeId;
    private String scope;
    @SerializedName("price_level")
    private String priceLevel;
    @SerializedName("alt_ids")
    private List<AltIds> altIds;
    private double rating=0;
    private String reference;
    private List<Review> reviews;
    private List<String> types;
    private String url;
    private String vicinity;
    private String website;
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

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
         this.formattedPhoneNumber = formattedPhoneNumber;
     }
     public String getFormattedPhoneNumber() {
         return formattedPhoneNumber;
     }

    public void setGeometry(Geometry geometry) {
         this.geometry = geometry;
     }
     public Geometry getGeometry() {
         return geometry;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
         this.internationalPhoneNumber = internationalPhoneNumber;
     }
     public String getInternationalPhoneNumber() {
         return internationalPhoneNumber;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

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

    public void setAltIds(List<AltIds> altIds) {
         this.altIds = altIds;
     }
     public List<AltIds> getAltIds() {
         return altIds;
     }

    public void setRating(double rating) {
         this.rating = rating;
     }
     public double getRating() {
         return rating;
     }

    public void setReference(String reference) {
         this.reference = reference;
     }
     public String getReference() {
         return reference;
     }

    public void setReviews(List<Review> reviews) {
         this.reviews = reviews;
     }
     public List<Review> getReviews() {
         return reviews;
     }

    public void setTypes(List<String> types) {
         this.types = types;
     }
     public List<String> getTypes() {
         return types;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setVicinity(String vicinity) {
         this.vicinity = vicinity;
     }
     public String getVicinity() {
         return vicinity;
     }

    public void setWebsite(String website) {
         this.website = website;
     }
     public String getWebsite() {
         return website;
     }

}