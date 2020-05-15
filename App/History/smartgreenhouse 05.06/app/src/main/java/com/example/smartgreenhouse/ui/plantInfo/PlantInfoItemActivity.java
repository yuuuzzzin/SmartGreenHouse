package com.example.smartgreenhouse.ui.plantInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
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

        boolean initem = false, inNum = false, inName = false, inInfo = false, inLevel = false, inHdcode = false, inSpring = false, inSummer = false, inAutumn = false, inWinter = false, inHeight= false, inWidth= false, inTemp= false;

        String num = null, name = null, info = null, level = null, hdcode= null, spring = null, summer = null, autumn = null, winter = null, height = null, width = null, temp = null;

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
                        if (parser.getName().equals("managelevelCodeNm")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inLevel= true;
                        }
                        if (parser.getName().equals("growthHgInfo")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inHeight = true;
                        }
                        if (parser.getName().equals("growthAraInfo")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inWidth = true;
                        }
                        if (parser.getName().equals("grwhTpCodeNm")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inTemp = true;
                        }
                        if (parser.getName().equals("hdCode")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inHdcode = true;
                        }
                        if (parser.getName().equals("watercycleSpringCode")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inSpring = true;
                        }
                        if (parser.getName().equals("watercycleSummerCode")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inSummer = true;
                        }
                        if (parser.getName().equals("watercycleAutumnCode")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inAutumn = true;
                        }
                        if (parser.getName().equals("watercycleWinterCode")) { //식물 이름 만나면 내용을 받을수 있게 하자
                            inWinter = true;
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
                        if (inInfo) {
                            info = parser.getText();
                            inInfo = false;
                        }
                        if (inLevel) {
                            level = parser.getText();
                            inLevel = false;
                        }
                        if (inHeight) {
                            height = parser.getText();
                            inHeight = false;
                        }
                        if (inWidth) {
                            width = parser.getText();
                            inWidth = false;
                        }
                        if (inTemp) {
                            temp = parser.getText();
                            inTemp = false;
                        }

                        if (inHdcode) { //isAddress이 true일 때 태그의 내용을 저장.
                            hdcode = parser.getText();
                            inHdcode = false;
                        }
                        if (inSpring) { //isAddress이 true일 때 태그의 내용을 저장.
                            spring = parser.getText();
                            inSpring = false;
                        }
                        if (inSummer) { //isAddress이 true일 때 태그의 내용을 저장.
                            summer = parser.getText();
                            inSummer = false;
                        }
                        if (inAutumn) { //isAddress이 true일 때 태그의 내용을 저장.
                            autumn = parser.getText();
                            inAutumn = false;
                        }

                        if (inWinter) { //isAddress이 true일 때 태그의 내용을 저장.
                            winter = parser.getText();
                            inWinter = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            txtDetail.setText("컨텐츠 번호 : " + num + "\n" + "식물 이름: " + name + "\n"+"식물 정보: " + info + "\n"+
                                    "관리 수준 : " +level + "\n"+  "성장 높이 : " + height + "\n"+  "성장 넓이 : " + width + "\n"+  "생육 온도 : " + temp + "\n");
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
                    obj.put("managelevelCodeNm",level );
                    obj.put("grwhTpCodeNm", temp);
                    obj.put("growthHgInfo", height);
                    obj.put("growthAraInfo", width);
                    obj.put("hdCode", hdcode);
                    obj.put("watercycleSpringCode", spring);
                    obj.put("watercycleSummerCode", summer );
                    obj.put("watercycleAutumnCode,", autumn );
                    obj.put("watercycleWinterCode",winter );

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