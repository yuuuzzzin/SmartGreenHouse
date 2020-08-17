package com.example.smartgreenhouse.ui.plantInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.smartgreenhouse.R;
import java.util.ArrayList;

public class PlantInfoAdapter extends BaseAdapter {
    private ArrayList<PlantInfoItem> arrayList = new ArrayList<>();
    private Context context;
   // private Drawable icon;
    //private int image;
    private String imageUrl;
   // private Bitmap bitmap;
    //private url url;

    //private int imageUrl;
    public PlantInfoAdapter(Context context, ArrayList<PlantInfoItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }
    public String getImageUrl() {return imageUrl;}

   // public Bitmap getBitmap() {return arrayList.;}
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    void addItem(PlantInfoItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        arrayList.add(data);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.activity_plant_info_item, null);

        TextView name = (TextView)v.findViewById(R.id.txtName);
        TextView code = (TextView)v.findViewById(R.id.txtCode);

        ImageView image = (ImageView) v.findViewById(R.id.imageIcon);


        name.setText(arrayList.get(position).getName());
        code.setText(arrayList.get(position).getCode());
//        url.setText(arrayList.get(position).getImageurl());


        //image.setImageDrawable(arrayList.get(position).getIcon());
        v.setTag(arrayList.get(position).getName());

        return v;
    }
}