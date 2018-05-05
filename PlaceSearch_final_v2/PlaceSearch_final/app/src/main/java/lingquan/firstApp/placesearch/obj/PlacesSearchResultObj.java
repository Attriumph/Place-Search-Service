package lingquan.firstApp.placesearch.obj;

import java.io.Serializable;
import java.util.List;



public class PlacesSearchResultObj implements Serializable{
    private List<String> html_attributions;
    private List<PlacesSearchResultItemObj> results;
    private String status;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    private String next_page_token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public List<PlacesSearchResultItemObj> getResults() {
        return results;
    }

    public void setResults(List<PlacesSearchResultItemObj> results) {
        this.results = results;
    }

    public static class PlacesSearchResultItemObj implements Serializable{
        private PlacesSearchResultGeometryObj geometry;
        private String icon;
        private String id;
        private String name;
        private PlacesSearchResultOpenHoursObj opening_hours;
        private List<PlacesSearchResultPhotosObj>photos;
        private String place_id;
        private float rating;
        private String reference;
        private String scope;
        private List<String> types;
        private String vicinity;


        public PlacesSearchResultGeometryObj getGeometry() {
            return geometry;
        }

        public void setGeometry(PlacesSearchResultGeometryObj geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PlacesSearchResultOpenHoursObj getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(PlacesSearchResultOpenHoursObj opening_hours) {
            this.opening_hours = opening_hours;
        }

        public List<PlacesSearchResultPhotosObj> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PlacesSearchResultPhotosObj> photos) {
            this.photos = photos;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }
    }


    public static class PlacesSearchResultGeometryObj implements Serializable{
        private PlacesSearchResultGeometryLocation location;
        private PlacesSearchResultGeometryItemLocation viewport;

        public PlacesSearchResultGeometryLocation getLocation() {
            return location;
        }

        public void setLocation(PlacesSearchResultGeometryLocation location) {
            this.location = location;
        }

        public PlacesSearchResultGeometryItemLocation getViewport() {
            return viewport;
        }

        public void setViewport(PlacesSearchResultGeometryItemLocation viewport) {
            this.viewport = viewport;
        }
    }

    public static class PlacesSearchResultGeometryItemLocation implements Serializable{
        private PlacesSearchResultGeometryLocation northeast;
        private PlacesSearchResultGeometryLocation southwest;

        public PlacesSearchResultGeometryLocation getNortheast() {
            return northeast;
        }

        public void setNortheast(PlacesSearchResultGeometryLocation northeast) {
            this.northeast = northeast;
        }

        public PlacesSearchResultGeometryLocation getSouthwest() {
            return southwest;
        }

        public void setSouthwest(PlacesSearchResultGeometryLocation southwest) {
            this.southwest = southwest;
        }
    }

    public static class PlacesSearchResultOpenHoursObj implements Serializable{
        private boolean open_now;
        private List<String>weekday_text;

        public boolean isOpen_now() {
            return open_now;
        }

        public void setOpen_now(boolean open_now) {
            this.open_now = open_now;
        }

        public List<String> getWeekday_text() {
            return weekday_text;
        }

        public void setWeekday_text(List<String> weekday_text) {
            this.weekday_text = weekday_text;
        }
    }

    public static class PlacesSearchResultPhotosObj implements Serializable{
        private int height;
        private List<String> html_attributions;
        private String photo_reference;
        private int width;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<String> getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(List<String> html_attributions) {
            this.html_attributions = html_attributions;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    public static class PlacesSearchResultGeometryLocation implements Serializable{
        private String lat;
        private String lng;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }
}
