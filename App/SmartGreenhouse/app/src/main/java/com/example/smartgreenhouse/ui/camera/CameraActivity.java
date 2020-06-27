package com.example.smartgreenhouse.ui.camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgreenhouse.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity  {

    Button btnCamera;
    ImageView img;

    //request code
    final int CAMERA_REQUEST_CODE = 1;


    public static void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){        // 사진을 갤러리에 저장하는 함수
        String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath(); // Get Absolute Path in External Sdcard
        String folder_name = "/"+folder+"/";                // 저장할 폴더명
        String file_name = name+".jpg";                     // 저장할 사진명
        String string_path = ex_storage+folder_name;        // 파일 경로
        File file_path;
        try{
            file_path = new File(string_path);
            if(!file_path.isDirectory()){           // 지정한 경로에 디렉토리가 있는지 확인
                file_path.mkdirs();
            }
            file_path.createNewFile();
            FileOutputStream out = new FileOutputStream(string_path+file_name);     // 파일을 쓸 수 있는 스트림 준비
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  // 스트림에 비트맵 저장
            out.close();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("사진 찍기");

        btnCamera = (Button)findViewById(R.id.btnCamera);
        img = (ImageView)findViewById(R.id.img);

        btnCamera.setOnClickListener(new View.OnClickListener(){                        //카메라 버튼이 눌리면
            @Override
            public void onClick(View v) {
                if(IsCameraAvailable()){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);                // 카메라 어플 실행
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CAMERA_REQUEST_CODE){
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap)bundle.get("data");
            saveBitmaptoJpeg(bitmap, "Camera", "picture1" );        // Camera 폴더에 picture1 이라는 이름으로 사진 저장
            img.setImageBitmap(bitmap);         // 화면에 찍은 사진 표시
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //카메라 유무
    public boolean IsCameraAvailable(){
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}


