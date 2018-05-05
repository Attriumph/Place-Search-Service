package lingquan.firstApp.placesearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import lingquan.firstApp.placesearch.GlideApp;
import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.SharePreferenceUtils;
import lingquan.firstApp.placesearch.activity.PlacesSearchItemActivity;
import lingquan.firstApp.placesearch.activity.PlacesSearchResultActivtity;
import lingquan.firstApp.placesearch.obj.PlacesSearchResultObj;



public class PlacesSearchResultAdapter extends RecyclerView.Adapter<PlacesSearchResultAdapter.ViewHolder> {

    public interface OnClickFavoriteListener{
        void onClick(PlacesSearchResultObj.PlacesSearchResultItemObj item);
    }
    private Context context;
    private List<PlacesSearchResultObj.PlacesSearchResultItemObj> data;
    private OnClickFavoriteListener listener;

    public PlacesSearchResultAdapter(Context context, List<PlacesSearchResultObj.PlacesSearchResultItemObj> data) {
        this.context = context;
        this.data = data;
    }

    public void setListener(OnClickFavoriteListener listener) {
        this.listener = listener;
    }

    public void refreshList(List<PlacesSearchResultObj.PlacesSearchResultItemObj> data){
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.places_search_result_adapter,parent,false);
        ViewHolder holder = new ViewHolder(convertView);
        holder.img = convertView.findViewById(R.id.img);
        holder.name = convertView.findViewById(R.id.name);
        holder.address = convertView.findViewById(R.id.address);
        holder.favorites = convertView.findViewById(R.id.favorite);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        List<PlacesSearchResultObj.PlacesSearchResultItemObj> myFavorite = SharePreferenceUtils.getInstance(context).getMyFavorite();
        PlacesSearchResultObj.PlacesSearchResultItemObj item = data.get(position);
        GlideApp.with(context).load(Uri.parse(item.getIcon())).centerCrop().
                into(holder.img);
        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getVicinity());
        int index = 0;
        holder.favorites.setImageResource(R.drawable.favor_emp_black);
        for (;index<myFavorite.size();index++){
            if (myFavorite.get(index).getId().equals(data.get(position).getId())){
                holder.favorites.setImageResource(R.drawable.favor_red);
                break;
            }
        }
        holder.favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick(data.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlacesSearchItemActivity.class);
                intent.putExtra("data",data.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;
        public ImageView favorites;
        public TextView name;
        public TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
