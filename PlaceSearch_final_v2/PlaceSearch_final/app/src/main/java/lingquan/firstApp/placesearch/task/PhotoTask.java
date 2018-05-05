package lingquan.firstApp.placesearch.task;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

public abstract  class PhotoTask extends AsyncTask<String, Void, List<PhotoTask.AttributedPhoto>> {

    private int mHeight;
    private int mWidth;
    private GoogleApiClient mGoogleApiClient;

    public PhotoTask(Context context,int width, int height) {
        mHeight = height;
        mWidth = width;
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity)context, null)
                .build();
    }

    @Override
    protected List<AttributedPhoto> doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }
        final String placeId = params[0];
        AttributedPhoto attributedPhoto = null;

        PlacePhotoMetadataResult result = Places.GeoDataApi
                .getPlacePhotos(mGoogleApiClient, placeId).await();
        List<AttributedPhoto> list = new ArrayList<>();
        if (result.getStatus().isSuccess()) {
            PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
            if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                for (int index = 0;index<photoMetadataBuffer.getCount();index++) {
                    PlacePhotoMetadata photo = photoMetadataBuffer.get(index);
                    CharSequence attribution = photo.getAttributions();
                    Bitmap image = photo.getScaledPhoto(mGoogleApiClient, mWidth, mHeight).await()
                            .getBitmap();

                    attributedPhoto = new AttributedPhoto(attribution, image);
                    list.add(attributedPhoto);
                }
            }
            photoMetadataBuffer.release();
        }
        return list;
    }

    /**
     * Holder for an image and its attribution.
     */
   public class AttributedPhoto {

        public final CharSequence attribution;

        public final Bitmap bitmap;

        public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
            this.attribution = attribution;
            this.bitmap = bitmap;
        }
    }
}
