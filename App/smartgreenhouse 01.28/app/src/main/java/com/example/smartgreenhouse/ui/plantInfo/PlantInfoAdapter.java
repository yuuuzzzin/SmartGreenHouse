package com.example.smartgreenhouse.ui.plantInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartgreenhouse.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlantInfoAdapter extends BaseAdapter {
    //variables
    Context mContext;
    LayoutInflater inflater;
    List<PlantInfoModel> modellist;
    ArrayList<PlantInfoModel> arrayList;

    //constructor
    public PlantInfoAdapter(Context context, List<PlantInfoModel> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<PlantInfoModel>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int postition, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_plant_info_item, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.mainDesc);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(postition).getTitle());
        holder.mDescTv.setText(modellist.get(postition).getDesc());
        //set the result in imageview
        holder.mIconIv.setImageResource(modellist.get(postition).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (modellist.get(postition).getTitle().equals("Plant1")){
                    //start ItemActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, PlantInfoItemActivity.class);
                    intent.putExtra("actionBarTitle", "Plant1");
                    intent.putExtra("contentTv", "This is Battery detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("Plant2")){
                    //start ItemActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, PlantInfoItemActivity.class);
                    intent.putExtra("actionBarTitle", "Plant2");
                    intent.putExtra("contentTv", "This is Plant detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("Plant3")){
                    //start ItemActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, PlantInfoItemActivity.class);
                    intent.putExtra("actionBarTitle", "Plant3");
                    intent.putExtra("contentTv", "This is Plant detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("Plant4")){
                    //start ItemActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, PlantInfoItemActivity.class);
                    intent.putExtra("actionBarTitle", "Plant4");
                    intent.putExtra("contentTv", "This is Memory detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("Plant5")){
                    //start ItemActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, PlantInfoItemActivity.class);
                    intent.putExtra("actionBarTitle", "Plant5");
                    intent.putExtra("contentTv", "This is Plant detail...");
                    mContext.startActivity(intent);
                }
                if (modellist.get(postition).getTitle().equals("Plant6")){
                    //start ItemActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, PlantInfoItemActivity.class);
                    intent.putExtra("actionBarTitle", "Plant6");
                    intent.putExtra("contentTv", "This is Plant detail...");
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length()==0){
            modellist.addAll(arrayList);
        }
        else {
            for (PlantInfoModel model : arrayList){
                if (model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
