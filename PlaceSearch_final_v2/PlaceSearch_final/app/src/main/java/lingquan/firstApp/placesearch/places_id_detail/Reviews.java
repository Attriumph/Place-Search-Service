package lingquan.firstApp.placesearch.places_id_detail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class Reviews implements Serializable{

    private List<Aspects> aspects;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("author_url")
    private String authorUrl;
    private String language;
    private int rating;
    private String text;
    private int time;
    public void setAspects(List<Aspects> aspects) {
         this.aspects = aspects;
     }
     public List<Aspects> getAspects() {
         return aspects;
     }

    public void setAuthorName(String authorName) {
         this.authorName = authorName;
     }
     public String getAuthorName() {
         return authorName;
     }

    public void setAuthorUrl(String authorUrl) {
         this.authorUrl = authorUrl;
     }
     public String getAuthorUrl() {
         return authorUrl;
     }

    public void setLanguage(String language) {
         this.language = language;
     }
     public String getLanguage() {
         return language;
     }

    public void setRating(int rating) {
         this.rating = rating;
     }
     public int getRating() {
         return rating;
     }

    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

    public void setTime(int time) {
         this.time = time;
     }
     public int getTime() {
         return time;
     }

}