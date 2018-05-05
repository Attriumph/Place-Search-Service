package lingquan.firstApp.placesearch.jsonscn.json2bean;

import java.io.Serializable;

public class Bounds implements Serializable {

    private Northeast northeast;
    private Southwest southwest;
    public void setNortheast(Northeast northeast) {
         this.northeast = northeast;
     }
     public Northeast getNortheast() {
         return northeast;
     }

    public void setSouthwest(Southwest southwest) {
         this.southwest = southwest;
     }
     public Southwest getSouthwest() {
         return southwest;
     }

}