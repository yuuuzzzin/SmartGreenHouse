package com.example.smartgreenhouse.ui.mail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartgreenhouse.R;

public class EmailActivity extends AppCompatActivity {

    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("문의 메일 보내기");

        final TextView edittextEmailAddress = (TextView)findViewById(R.id.email_address);
        final EditText edittextEmailSubject = (EditText)findViewById(R.id.email_subject);
        final EditText edittextEmailText = (EditText)findViewById(R.id.email_text);
        Button buttonSendEmail_intent = (Button)findViewById(R.id.sendemail_intent);

        buttonSendEmail_intent.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View arg0) {

                // TODO Auto-generated method stub
                String emailAddress = "wodus0130@naver.com";    // 목적지 이메일 주소
                String emailSubject = edittextEmailSubject.getText().toString();
                String emailText = edittextEmailText.getText().toString();
                String emailAddressList[] = {emailAddress};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, emailAddressList);
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailText);

                startActivity(Intent.createChooser(intent, "메일을 보낼 앱을 선택하세요."));

            }});

    }


}
