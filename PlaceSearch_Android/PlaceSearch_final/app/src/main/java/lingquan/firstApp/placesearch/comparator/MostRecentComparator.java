package lingquan.firstApp.placesearch.comparator;

import java.util.Comparator;

import lingquan.firstApp.placesearch.jsonscn.json2bean.Review;

public class MostRecentComparator implements Comparator<Review> {
public int compare(Review r1,Review r2){
        return (int)r2.getTime()-(int)r1.getTime();
        }
}
