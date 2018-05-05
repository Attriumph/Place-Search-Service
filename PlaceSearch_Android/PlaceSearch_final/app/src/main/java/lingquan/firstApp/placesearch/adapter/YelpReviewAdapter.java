package lingquan.firstApp.placesearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lingquan.firstApp.placesearch.R;

public class YelpReviewAdapter extends BaseAdapter {
    private JSONArray reviews;
    private LayoutInflater mInflater;
    private Context context ;


    public YelpReviewAdapter(Context context, JSONArray reviews) {
        this.context = context;
        this.reviews = reviews;
        this.mInflater = LayoutInflater.from(context);


    }


    @Override
    public int getCount() {
        return reviews.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return (JSONObject)reviews.get(position);


        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
       return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.container_review,parent,false);
        Log.v("posi:",""+position);

        try {
            JSONObject review = (JSONObject)reviews.get(position);
            JSONObject user = review.getJSONObject("user");
            ImageView img_author = (ImageView) convertView.findViewById(R.id.author_profile);
            Picasso.get().load(user.getString("image_url")).centerCrop().resize(120, 120).into(img_author);

            TextView view_name = (TextView) convertView.findViewById(R.id.author_name);
//            TextView view_rating = (TextView) convertView.findViewById(R.id.review_rating);
            TextView view_time = (TextView) convertView.findViewById(R.id.review_time);
            TextView view_text = (TextView) convertView.findViewById(R.id.review_text);
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar2);
            ratingBar.setIsIndicator(true);
            view_name.setText((user.getString("name")));
//            view_rating.setText(review.getString("rating"));
            view_time.setText(review.getString("time_created"));
            view_text.setText(review.getString("text"));
            ratingBar.setRating(Float.valueOf(review.getString("rating")));
            view_name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.d("click","click name");

                }
            });

        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    JSONObject review = (JSONObject) reviews.get(position);
                    Log.d("click", "click item");
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    String reviewPage = review.getString("url");
                    Log.v("review url", reviewPage);
                    Uri content_url = Uri.parse(reviewPage);

                    intent.setData(content_url);
                    intent.setData(content_url);
                    context.startActivity(intent);

                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                }
            }
        });



        return convertView;
    }


}
