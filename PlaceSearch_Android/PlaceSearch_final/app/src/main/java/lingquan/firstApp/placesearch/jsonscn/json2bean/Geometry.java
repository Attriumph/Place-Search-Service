package lingquan.firstApp.placesearch.jsonscn.json2bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {

    private Bounds bounds;
    private Location location;
    @SerializedName("location_type")
    private String locationType;
    private Viewport viewport;
    public void setBounds(Bounds bounds) {
         this.bounds = bounds;
     }
     public Bounds getBounds() {
         return bounds;
     }

    public void setLocation(Location location) {
         this.location = location;
     }
     public Location getLocation() {
         return location;
     }

    public void setLocationType(String locationType) {
         this.locationType = locationType;
     }
     public String getLocationType() {
         return locationType;
     }

    public void setViewport(Viewport viewport) {
         this.viewport = viewport;
     }
     public Viewport getViewport() {
         return viewport;
     }

}