package com.example.smartgreenhouse.ui.plantInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class PlantInfoItemActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtName, txtCode, txtDetail;
    private Button selectButton;
    String Name, Code, Info, Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail_page);
        Intent intent = getIntent();

        txtName = findViewById(R.id.txtName);
        txtCode = findViewById(R.id.txtCode);
        txtDetail = findViewById(R.id.txtDetail);
        selectButton = findViewById(R.id.selectButton);
        txtName.setText(intent.getStringExtra("name"));
        txtCode.setText(intent.getStringExtra("code"));
        Name = intent.getStringExtra("name");
        Code = intent.getStringExtra("code");

        StrictMode.enableDefaults();

        //TextView status1 = (TextView) findViewById(R.id.txtName); //파싱된 결과확인!

        boolean initem = false, inNum = false, inName = false, inInfo = false, inManagedemanddo = false;

        String num = null, name = null, info = null, managedemanddo = null;

        try {
            URL url = new URL("http://api.nongsaro.go.kr/service/garden/gardenDtl?" //농사로 OpenAPI
                    + "apiKey="
                    + "20200114KXGDZSXXBNORHYRUSLWYQ"
                    + "&cntntsNo=" + Code
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("cntntsNo")) { //컨텐츠 번호 만나면 내용을 받을수 있게 하자
                            inNum = true;
                        }
                        if (parser.getName().equals("plntbneNm")) { //컨텐츠 번호 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if (parser.getName().equals("adviseInfo")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inInfo = true;
                        }
                        if (parser.getName().equals("managedemanddoCodeNm")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inManagedemanddo = true;
                        }
                        if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                            txtDetail.setText(txtDetail.getText() + "에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }


                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inNum) { //isTitle이 true일 때 태그의 내용을 저장.
                            num = parser.getText();
                            inNum = false;
                        }
                        if (inName) { //isTitle이 true일 때 태그의 내용을 저장.
                            name = parser.getText();
                            inName = false;
                        }
                        if (inInfo) { //isAddress이 true일 때 태그의 내용을 저장.
                            info = parser.getText();
                            inInfo = false;
                        }
                        if (inManagedemanddo) { //isAddress이 true일 때 태그의 내용을 저장.
                            managedemanddo = parser.getText();
                            inManagedemanddo = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            txtDetail.setText("컨텐츠 번호 : " + num + "\n" + "식물 이름: " + name + "\n"+"식물 정보: " + info + "\n" + "관리 난이도: " + managedemanddo + "\n");
                            Info = info;
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }

            // json 형식으로 만들기
            JSONObject obj = new JSONObject();
            try {
                JSONArray jArray = new JSONArray();//배열이 필요할때
                {
                    obj.put("cntntsNo", num);
                    obj.put("plntbneNm",name);
                    obj.put("adviseInfo", info);
                    obj.put("managedemanddoCodeNm", info);

                }
                Total = obj.toString();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            txtDetail.setText("에러");
        }

        selectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlantSettingActivity.class);
                intent.putExtra("name", Name);
                intent.putExtra("code", Code);
                intent.putExtra("info", Info);
                intent.putExtra("total", Total);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}