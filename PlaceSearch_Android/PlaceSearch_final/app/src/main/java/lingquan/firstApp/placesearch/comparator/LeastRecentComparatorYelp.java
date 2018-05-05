package lingquan.firstApp.placesearch.comparator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class LeastRecentComparatorYelp implements Comparator<JSONObject> {
        //You can change "Name" with "ID" if you want to sort by ID
        private static final String TIME = "time_created";

        @Override
        public int compare(JSONObject a, JSONObject b) {
                long valA = 0;
                long valB = 0;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                        try{
                                valA = simpleDateFormat.parse(a.getString(TIME)).getTime();
                                valB = simpleDateFormat.parse(b.getString(TIME)).getTime();

                        } catch (ParseException e) {

                        }

                }
                catch (JSONException e) {
                        //do something
                }

                return new Long(valA-valB).intValue() ;

        }
}
