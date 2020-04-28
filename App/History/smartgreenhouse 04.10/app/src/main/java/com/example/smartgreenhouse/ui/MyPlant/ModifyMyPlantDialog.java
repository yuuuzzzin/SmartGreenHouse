package com.example.smartgreenhouse.ui.MyPlant;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartgreenhouse.R;

public class ModifyMyPlantDialog extends Dialog {
    private OnDialogListener listener;
    private Context context;
    private Button mod_bt;
    private EditText mod_text, mod_location;
    private String text, position;
    private int image;

    public ModifyMyPlantDialog(Context context, String position, MyPlant myPlant) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.modify_my_plant_dialog);

        text = myPlant.getPlantNickname();
        position = myPlant.getPlantPosition();

        mod_text = findViewById(R.id.mod_text);
        mod_text.setText(String.valueOf(text));

        mod_bt = findViewById(R.id.mod_bt);
        mod_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //EditText의 수정된 값 가져오기
                    String name = mod_text.getText().toString();
                    String position = mod_text.getText().toString();
                    int location = Integer.parseInt(mod_location.getText().toString());
                    //MyPlant myPlant = new MyPlant(name, image, position);
                    //Listener를 통해서 person객체 전달
                    //listener.onFinish(position, myPlant);
                    //다이얼로그 종료
                    dismiss();
                }
            }
        });
    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }

}