package com.example.smartgreenhouse.ui.plantInfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlantInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private PlantInfoAdapter adapter;
    private ArrayList<PlantInfoItem> arrayList = new ArrayList<PlantInfoItem>();
    private TextView txtName, txtCode;
    String splitcourse ,splitfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("식물 도감");
        txtName = findViewById(R.id.txtName);
        txtCode = findViewById(R.id.txtCode);

        ListView listView = findViewById(R.id.listView_plantInfo);
        adapter = new PlantInfoAdapter(this, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlantInfoItemActivity.class);
                intent.putExtra("name", arrayList.get(position).getName());
                intent.putExtra("code", arrayList.get(position).getCode());
                intent.putExtra("course", arrayList.get(position).getCourse());
                intent.putExtra("filename", arrayList.get(position).getFilename());
                startActivity(intent);
            }
        });

        StrictMode.enableDefaults();

        boolean initem = false, inNum = false, inName = false, inCourse = false, inFilename = false;
        String num = null, name = null, course = null, filename = null;

        try{
            URL url = new URL("http://api.nongsaro.go.kr/service/garden/gardenList?"    //'농사로' OpenAPI
                    + "numOfRows="
                    + "217"
                    + "&apiKey="
                    + "20200114KXGDZSXXBNORHYRUSLWYQ"
                 //   +"&cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054002%2C054004%2C054006&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054002&grwhstleChk=054004&grwhstleChk=054006&priceTypeSel=&waterCycleSel="
               //   +"&cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054001%2C054004%2C054006&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054001&grwhstleChk=054004&grwhstleChk=054006&priceTypeSel=&waterCycleSel="
             //   +"&cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054001%2C054004&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054001&grwhstleChk=054004&priceType=3&priceTypeSel=&waterCycleSel="
                // +"&cntntsNo=&pageNo=1&word=&lightChkVal=055003&grwhstleChkVal=054001%2C054004&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=057004%2C057005&sType=sCntntsSj&sText=&wordType=cntntsSj&lightChk=055003&grwhstleChk=054001&grwhstleChk=054004&winterLwetChk=057004&winterLwetChk=057005&priceType=medium&priceTypeSel=&waterCycleSel="
                  +"&cntntsNo=&pageNo=1&word=&lightChkVal=055003&grwhstleChkVal=054001%2C054002%2C054004&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=057004%2C057005&sType=sCntntsSj&sText=&wordType=cntntsSj&lightChk=055003&grwhstleChk=054001&grwhstleChk=054002&grwhstleChk=054004&winterLwetChk=057004&winterLwetChk=057005&priceType=medium&priceTypeSel=&waterCycleSel="
                    //  http://api.nongsaro.go.kr/service/garden/gardenList?numOfRows=217&apiKey=20200114KXGDZSXXBNORHYRUSLWYQ
            ); //검색 URL부분
          //  http://api.nongsaro.go.kr/service/garden/gardenList?numOfRows=217&apiKey=20200114KXGDZSXXBNORHYRUSLWYQ&cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054002%2C054004%2C054006&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054002&grwhstleChk=054004&grwhstleChk=054006&priceTypeSel=&waterCycleSel=
          //  http://api.nongsaro.go.kr/service/garden/gardenList?numOfRows=217&apiKey=20200114KXGDZSXXBNORHYRUSLWYQ&cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054001&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054001&priceTypeSel=&waterCycleSel=
          //  http://18.218.71.35/gardenList.jsp?cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054001&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054001&priceTypeSel=&waterCycleSel=
          //  http://18.218.71.35/gardenList.jsp?cntntsNo=&pageNo=1&word=&lightChkVal=&grwhstleChkVal=054001%2C054002%2C054004%2C054006&lefcolrChkVal=&lefmrkChkVal=&flclrChkVal=&fmldecolrChkVal=&ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054001&grwhstleChk=054002&grwhstleChk=054004&grwhstleChk=054006&priceTypeSel=&waterCycleSel=

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("cntntsNo")){ //컨텐츠 번호 만나면 내용을 받을수 있게 하자
                            inNum = true;
                        }
                        if(parser.getName().equals("cntntsSj")){ //식물 이름 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("rtnFileCours")){ //식물 이름 만나면 내용을 받을수 있게 하자
                            inCourse = true;
                        }
                        if(parser.getName().equals("rtnStreFileNm")){ //식물 이름 만나면 내용을 받을수 있게 하자
                            inFilename = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                             //status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inNum){ //isTitle이 true일 때 태그의 내용을 저장.
                            num = parser.getText();
                            inNum = false;
                        }
                        if(inName){ //isAddress이 true일 때 태그의 내용을 저장.
                            name = parser.getText();
                            inName = false;
                        }
                        if(inCourse){ //isAddress이 true일 때 태그의 내용을 저장.
                            course = parser.getText();
                            inCourse = false;
                          //  int idx = course.indexOf("|");
                           // splitcourse = course.substring(0, idx);
                            //inCourse = false;
                        }
                        if(inFilename){ //isAddress이 true일 때 태그의 내용을 저장.
                            filename = parser.getText();
                            inFilename = false;
                           // String array [] = filename.split("\\|"); // 여러개의 파일 이름이 있어 |를 구분하여 자름
                         //   splitfile = array[0];
                          //  inFilename = false;

                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            PlantInfoItem plantinfoitem = new PlantInfoItem();
                            plantinfoitem.setName(name);
                            plantinfoitem.setCode(num);
                            plantinfoitem.setCourse(course);
                            plantinfoitem.setFilename(filename);

                           // plantinfoitem.setImageurl("http://www.nongsaro.go.kr/"+splitcourse+"/"+splitfile);

                            adapter.addItem(plantinfoitem);
                            //status1.setText(status1.getText()+"컨텐츠 번호 : "+ num +"\n 식물명: "+ name +"\n");
                            initem = false;

                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }




    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }
}