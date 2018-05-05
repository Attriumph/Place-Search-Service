package lingquan.firstApp.placesearch.places_id_detail;

import java.io.Serializable;

/**
 * Auto-generated: 2018-04-21 16:1:52
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Geometry implements Serializable{

    private Location location;
    private Viewport viewport;
    public void setLocation(Location location) {
         this.location = location;
     }
     public Location getLocation() {
         return location;
     }

    public void setViewport(Viewport viewport) {
         this.viewport = viewport;
     }
     public Viewport getViewport() {
         return viewport;
     }

}