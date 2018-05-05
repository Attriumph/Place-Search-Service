package lingquan.firstApp.placesearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import lingquan.firstApp.placesearch.GlideApp;
import lingquan.firstApp.placesearch.R;
import lingquan.firstApp.placesearch.Utils.Utils;
import lingquan.firstApp.placesearch.task.PhotoTask;



public class PlacesSearchItemPhotoAdapter extends RecyclerView.Adapter<PlacesSearchItemPhotoAdapter.ViewHolder> {
    private Context context;
    private List<PhotoTask.AttributedPhoto> data;

    public PlacesSearchItemPhotoAdapter(Context context, List<PhotoTask.AttributedPhoto> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.places_search_item_photo_adapter, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.img = view.findViewById(R.id.img);
        ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.width = Utils.getWidth(context) - 2 * 30;
        layoutParams.height = (int) (layoutParams.width * 0.6);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GlideApp.with(context).asBitmap().centerCrop().load(data.get(position).bitmap).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refreshList(List<PhotoTask.AttributedPhoto> attributedPhotos) {
        this.data = attributedPhotos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
