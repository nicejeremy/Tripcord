package com.jeremy.tripcord.main.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.database.domain.TripInfo;
import com.jeremy.tripcord.common.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class TripInfoListAdaptor extends ArrayAdapter<TripInfo> {

    private List<Integer> imageLoadedRowPositions;

    public TripInfoListAdaptor(Context context, int resource, List<TripInfo> objects) {
        super(context, resource, objects);

        imageLoadedRowPositions = new ArrayList<Integer>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_trip_info_list, null);
            viewHolder.imageViewBackGroundMap = (ImageView) convertView.findViewById(R.id.imageView_background_map);
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_trip_title);
            viewHolder.textViewFrom = (TextView) convertView.findViewById(R.id.textView_trip_from);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textView_trip_date);
            viewHolder.textViewPhoto = (TextView) convertView.findViewById(R.id.textView_trip_photo);
            viewHolder.linearLayoutPhotos = (LinearLayout) convertView.findViewById(R.id.linearLayout_trip_pictures);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TripInfo tripInfo = getItem(position);

        Bitmap bitmap = ImageUtil.byteArrayToBitmap(tripInfo.getSnapshot());
        viewHolder.imageViewBackGroundMap.setImageBitmap(bitmap);
        viewHolder.textViewFrom.setText(tripInfo.getFrom());
        viewHolder.textViewDate.setText(tripInfo.getCreated().toString());
        viewHolder.textViewPhoto.setText(tripInfo.getPhotoInfoList().size() + " photo(s)");

        if (tripInfo.getPhotoInfoList() != null && tripInfo.getPhotoInfoList().size() != 0) {
            viewHolder.linearLayoutPhotos.setVisibility(View.VISIBLE);
            for (int i = 0 ; i < tripInfo.getPhotoInfoList().size(); i++) {
                if (!imageLoadedRowPositions.contains(position)) {
                    addPhotoView(viewHolder.linearLayoutPhotos, tripInfo.getPhotoInfoList().get(i).getPath());
                }
            }
            imageLoadedRowPositions.add(position);
        } else {
            viewHolder.linearLayoutPhotos.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void addPhotoView(LinearLayout photoScrillView, String imagePath) {

        Bitmap bm = ImageUtil.decodeSampledBitmapFromUri(imagePath, 80, 80);

        LinearLayout linearLayout = (LinearLayout) photoScrillView.findViewById(R.id.linearLayout_trip_pictures);
        linearLayout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(80, 80));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(10, 10));

        linearLayout.addView(imageView);
        linearLayout.addView(view);
    }

    static class ViewHolder {
        ImageView imageViewBackGroundMap;
        TextView textViewTitle;
        TextView textViewFrom;
        TextView textViewDate;
        TextView textViewPhoto;
        LinearLayout linearLayoutPhotos;
    }
}
