package com.example.smartgreenhouse.ui.Account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.MainActivity;
import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity
{
    EditText inputId, inputPassword, checkPassword, inputName, inputPhone;
    TextView idMsg, passwordMsg, checkPasswordMsg, nameMsg, phoneMsg;
    Button btnSignup, btnCheckDuplicated;
    String txtId, txtPw, txtCheckPw, txtName, txtPhone ;
    boolean isCheckId = false;
    boolean isCheckPw = false;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputId = (EditText) findViewById(R.id.inputId);
        idMsg = (TextView) findViewById(R.id.idMsg);

        inputPassword = (EditText) findViewById(R.id.inputPassword);
        passwordMsg = (TextView) findViewById(R.id.passwordMsg);

        checkPassword = (EditText) findViewById(R.id.checkPassword);
        checkPasswordMsg = (TextView) findViewById(R.id.checkPasswordMsg);

        inputName = (EditText) findViewById(R.id.inputName);
        nameMsg = (TextView) findViewById(R.id.nameMsg);

        inputPhone = (EditText) findViewById(R.id.inputPhone);
        phoneMsg = (TextView) findViewById(R.id.phoneMsg);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnCheckDuplicated = (Button)findViewById(R.id.btnCheckDuplicated);

        inputName.setFilters(new InputFilter[]{filterKor}); // 이름 Edittext 한글만 입력가능
        inputId.setFilters(new InputFilter[]{filterEng}); // 아이디 Edittext 영어만 입력가능
        inputPassword.setFilters(new InputFilter[]{filterEngNum}); // 비밀번호 Edittext 영어숫자만 입력가능
        checkPassword.setFilters(new InputFilter[]{filterEngNum}); // 비밀번호확인 Edittext 영어숫자만 입력가능

        //포커스 이동시 Edittext 내용 형식 확인 → TextView 알림
        inputId.setOnFocusChangeListener(new View.OnFocusChangeListener()
        { //포커스를 얻었을 때
            @Override
            public void onFocusChange(View v, boolean hasFocus) //포커스가 한 뷰에서 다른 뷰로 바뀔 때
            {
                if (hasFocus == false) // // 포커스를 잃었을 때
                {
                    String id = inputId.getText().toString();
                    if (id.equals("")) // id가 입력되지 않았으면
                        idMsg.setText("아이디를 입력해주세요");
                }
                else
                    idMsg.setText("");
            }
        });

        // 아이디 중복체크하고 edittext를 수정할 때 리스너
        inputId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isCheckId == true)
                {
                    isCheckId = false;
                    btnCheckDuplicated.setText("CHECK");
                    btnCheckDuplicated.setBackgroundColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String password = inputPassword.getText().toString();
                if (hasFocus == false)
                {
                    if (password.equals("")) // 비밀번호가 입력되지 않았을 시에
                        passwordMsg.setText("비밀번호를 입력해주세요");
                    else
                    {
                        if (password.length() < 8 || password.length() > 16) //비밀번호는 입력됐으나 형식에 안맞을때
                            passwordMsg.setText("비밀번호는 8~16자 이내의 영문과 숫자의 조합으로 이루어져야 합니다");
                    }
                }
                else
                    passwordMsg.setText("");
            }
        });


        checkPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txtPw = inputPassword.getText().toString(); // 입력한 비밀번호
                txtCheckPw = checkPassword.getText().toString(); // 비밀번호 확인
                boolean result = checkPw(txtPw, txtCheckPw);
                if (hasFocus == false)
                {
                    if(txtCheckPw.equals(""))
                        checkPasswordMsg.setText("비밀번호 확인을 입력해주세요");
                    else
                    {
                        if (result==false) // 비밀번호 확인은 입력됐으나 비밀번호와 다를 때
                            checkPasswordMsg.setText("비밀번호 확인이 다릅니다");
                    }
                }
                else
                    checkPasswordMsg.setText("");
            }
        });

        inputName.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String name = inputName.getText().toString();
                if (hasFocus == false)
                {
                    if (name.equals("")) // 아무것도 입력되지 않았을 시에
                        nameMsg.setText("이름을 입력해주세요");
                    else
                    {
                        if(name.length()<2)
                            nameMsg.setText("이름은 두 글자 이상 입력가능합니다");
                    }
                }
                else
                    nameMsg.setText("");
            }
        });

        inputPhone.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String phoneNum = inputPhone.getText().toString();
                if (hasFocus == false)
                {
                    if (phoneNum.equals("")) // 아무것도 입력되지 않았을 시에
                        phoneMsg.setText("휴대전화 번호를 입력해주세요.");
                    else
                    {
                        if(phoneNum.length()<10)
                            phoneMsg.setText("휴대전화 번호가 올바르지 않습니다.");
                    }
                }
                else
                    phoneMsg.setText("");
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtId = inputId.getText().toString();
                txtPw = inputPassword.getText().toString();
                txtName = inputName.getText().toString();
                txtPhone = inputPhone.getText().toString();

                signup(txtId, txtPw, txtName, txtPhone); // 데이터베이스에 데이터 전송 및 저장
            }
        });

        btnCheckDuplicated.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtId = inputId.getText().toString();
                checkDuplicated(txtId);
            }
        });
    }


    public InputFilter filterKor = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅣ가-힣]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
    public InputFilter filterEngNum = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("[a-zA-Z0-9]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    public InputFilter filterEng = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("[a-zA-Z]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    public boolean checkPw(final String password, final String checkPassword)
    {
        if( !(password.equals(checkPassword)) ) // 비밀번호 확인이 다르면
            isCheckPw = false;
        else  //비밀번호 확인이 비밀번호와 같으면
        {
            isCheckPw = true;
            checkPasswordMsg.setText("");
        }

        return isCheckPw;
    }

    public void signup(final String id, final String password, final String name, final String phone)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String signupUrl = getString(R.string.signupUrl); //가입하기 jsp

        StringRequest stringRequest = new StringRequest(Request.Method.POST, signupUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if(result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class );
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(org.json.JSONException e)
                {
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })


        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("PASSWORD", password);
                params.put("NAME ", name);
                params.put("PHONE", phone);
                return params;
            }
        };
        queue.add(stringRequest);
    }



    public void checkDuplicated(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String checkIdUrl = getString(R.string.checkIdrUrl); // 아이디 중복체크 jsp

        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkIdUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT"); // 아이디가 중복되었을 시에 1을 리턴
                    if(result.equals("1"))  // 아이디 중복 O
                        idMsg.setText("이 아이디는 사용하실 수 없습니다");
                    else                    // 아이디 중복 X
                    {
                        isCheckId = true;
                        btnCheckDuplicated.setBackgroundColor(Color.GREEN);
                        btnCheckDuplicated.setTextColor(Color.WHITE);
                        btnCheckDuplicated.setText("OK");
                    }
                }
                catch(org.json.JSONException e)
                {
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}