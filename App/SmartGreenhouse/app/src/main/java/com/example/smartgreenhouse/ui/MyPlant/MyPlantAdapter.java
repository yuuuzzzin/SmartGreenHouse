package com.example.smartgreenhouse.ui.MyPlant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.MainActivity;
import com.example.smartgreenhouse.MyService;
import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.home.HomeFragment;
import com.example.smartgreenhouse.ui.plantInfo.PlantInfoItemActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPlantAdapter extends RecyclerView.Adapter<MyPlantAdapter.ViewHolder>
        implements ItemTouchHelperListener, OnDialogListener {
    ImageButton btnWater;

    private ArrayList<MyPlant> dataItems = new ArrayList<>();
    Context context;
    public MyPlantAdapter(Context context) {
        this.context = context;
    }

    public MyPlantAdapter(ArrayList<MyPlant> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public MyPlantAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myplant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlantAdapter.ViewHolder viewHolder, final int position) {
        final MyPlant data = dataItems.get(position);

        TextView textView = viewHolder.textView;
        textView.setText(data.getPlantNickname());

        ImageView imageView = viewHolder.imageView;
        imageView.setImageBitmap(data.getImage());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageButton btnWater = viewHolder.btnWater;
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SetWaterActivity.class);
                intent.putExtra("position", data.getPlantPosition());
                context.startActivity(intent);
                //Log.d("aaaa", "버튼을 누른 아이템의 위치는 " + data.getPlantPosition());

            }

        });

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
        ModifyMyPlantDialog dialog = new ModifyMyPlantDialog(context, "아무거나쓴거", dataItems.get(position));
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
        MyPlant plant;
        plant = dataItems.get(position);
        Toast.makeText(context, plant.getPlantPosition(), Toast.LENGTH_SHORT).show();
        deleteUserPlant(readId(), dataItems.get(position).getPlantPosition());
        dataItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onFinish(int position, MyPlant myPlant) {
        dataItems.set(position, myPlant);
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public ImageButton btnWater;

        public String position;

        public ViewHolder(final View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.plantName);
            imageView = itemView.findViewById(R.id.plantImage);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            btnWater = itemView.findViewById(R.id.waterButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // TODO : use pos.
                        final MyPlant data = dataItems.get(pos);
                        // Toast.makeText(context, data.getPlantPosition(), Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(context, UserPlantInfoActivity.class);
                        intent2.putExtra("position", data.getPlantPosition());
                        intent2.putExtra("nickname", data.getPlantNickname());
                        Toast.makeText(context, data.getPlantNickname(), Toast.LENGTH_SHORT).show();
                        context.startActivity(intent2);

                    }


                }
            });
        }

        public void onBind(MyPlant myplant) {
            textView.setText(myplant.getPlantNickname());
            imageView.setImageBitmap(myplant.getImage());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
    public String readId() {
        String str = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(context.getFilesDir() + "/" + "id.txt"));
            str = null;
            str = br.readLine();
            //Toast.makeText(getApplicationContext(), str+"readId 함수 안", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return str;
    }

    public void deleteUserPlant(final String id, final String position) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String deleteUserPlantUrl = context.getString(R.string.deleteUserPlantUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteUserPlantUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
                // Response
                try {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if (result.equals("1")) {
                        Toast.makeText(context, "식물 삭제 완료", Toast.LENGTH_SHORT).show();
                    }
                    /*if(result.equals("2"))
                    {
                        Toast.makeText(getApplicationContext(), "꽉 참", Toast.LENGTH_SHORT).show();
                    }*/
                    else {
                        Toast.makeText(context, "식물 삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (org.json.JSONException e) {
                    //Toast.makeText(getApplicationContext(), "캐치 에러", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error Handling
                Toast.makeText(context, "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("position", position);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}