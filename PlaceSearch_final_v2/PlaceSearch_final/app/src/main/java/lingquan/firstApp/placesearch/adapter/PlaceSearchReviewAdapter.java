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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.jsonscn.json2bean.Review;

public class PlaceSearchReviewAdapter extends BaseAdapter {
    private List<Review> reviews;
    private LayoutInflater mInflater;
    private Context context ;


    public PlaceSearchReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        this.mInflater = LayoutInflater.from(context);


    }


    @Override
    public int getCount() {
        if (reviews == null) return 0;
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.container_review,parent,false);


        ImageView img_author = (ImageView) convertView.findViewById(R.id.author_profile);
        Picasso.get().load(reviews.get(position).getProfile_photo_url()).into(img_author);

        TextView view_name = (TextView) convertView.findViewById(R.id.author_name);
        TextView view_time = (TextView) convertView.findViewById(R.id.review_time);
        TextView view_text = (TextView) convertView.findViewById(R.id.review_text);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar2);

        view_name.setText(reviews.get(position).getAuthor_name());
        view_text.setText(reviews.get(position).getText());
        ratingBar.setRating((float) reviews.get(position).getRating());

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reviews.get(position).getTime()*1000);
        view_time.setText(formatter.format(calendar.getTime()).toString());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("click","click item");
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                String reviewPage = reviews.get(position).getAuthor_url();
                Log.v("review page",reviewPage);
                Uri content_url = Uri.parse(reviewPage);

                intent.setData(content_url);
                intent.setData(content_url);
                context.startActivity(intent);

            }
        });

        return convertView;
    }
}
