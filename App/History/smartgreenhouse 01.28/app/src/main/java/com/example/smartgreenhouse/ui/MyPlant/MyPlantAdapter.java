package com.example.smartgreenhouse.ui.MyPlant;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartgreenhouse.R;

import java.util.ArrayList;

public class MyPlantAdapter extends RecyclerView.Adapter<MyPlantAdapter.ViewHolder>
        implements ItemTouchHelperListener, OnDialogListener {

    private ArrayList<MyPlant> dataItems = new ArrayList<>();
    Context context;
    public MyPlantAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyPlantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myplant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlantAdapter.ViewHolder viewHolder, int position) {
        final MyPlant data = dataItems.get(position);
        viewHolder.textView.setText(data.text);
        viewHolder.imageView.setImageResource(data.img);
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void addItem(MyPlant myplant) {
        dataItems.add(myplant);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        MyPlant myplant = dataItems.get(from_position);
        //이동할 객체 삭제
        dataItems.remove(from_position);
        //이동하고 싶은 position에 추가
        dataItems.add(to_position, myplant);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        dataItems.remove(position);
        notifyItemRemoved(position);
    }

    //윗쪽 버튼 누르면 수정할 다이얼로그 띄우기
    @Override
    public void onUpClick(int position, RecyclerView.ViewHolder viewHolder) {
        //수정 버튼 클릭시 다이얼로그 생성
        ModifyMyPlantDialog dialog = new ModifyMyPlantDialog(context, position, dataItems.get(position));
        //화면 사이즈 구하기
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //다이얼로그 사이즈 세팅
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
        wm.copyFrom(dialog.getWindow().getAttributes());
        wm.width = (int) (width * 0.7);
        wm.height = height / 2;
        //다이얼로그 Listener 세팅
        dialog.setDialogListener(this);
        //다이얼로그 띄우기
        dialog.show();
    }

    //아랫쪽 버튼 누르면 아이템 삭제
    @Override
    public void onDownClick(int position, RecyclerView.ViewHolder viewHolder) {
        dataItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onFinish(int position, MyPlant person) {
        dataItems.set(position, person);
        notifyItemChanged(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.plantName);
            imageView = itemView.findViewById(R.id.plantImage);
        }

        public void onBind(MyPlant myplant) {
            textView.setText(myplant.getText());
            imageView.setImageResource(myplant.getImage());
        }
    }
}
