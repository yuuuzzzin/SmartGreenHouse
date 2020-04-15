package com.example.smartgreenhouse.ui.MyPlant;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    boolean onItemMove(int from_position, int to_position);
    void onItemSwipe(int position);
    void onUpClick(int position, RecyclerView.ViewHolder viewHolder);
    void onDownClick(int position, RecyclerView.ViewHolder viewHolder);
}
