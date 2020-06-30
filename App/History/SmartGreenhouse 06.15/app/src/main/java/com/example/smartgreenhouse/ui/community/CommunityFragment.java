package com.example.smartgreenhouse.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CommunityFragment extends Fragment {
    private ArrayList<Integer> matched;
    Button button2;

    int postNum; // 전체 게시물 개수

    private ListView listview ;
    private CommunityAdapter adapter;

    Queue<String> queueTitle = new LinkedList<String>();
    Queue<String> queueWriter = new LinkedList<String>();
    Queue<String> queueHit = new LinkedList<String>();
    Queue<String> queueDate = new LinkedList<String>();

   // public interface OnInputListener{
      //  void sendInput(String input);
   // }

    String ID;
    String PW;

   // public OnInputListener mOnInputListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_community, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ID = bundle.getString("ID", "");
            PW = bundle.getString("PW", "");
        }

        Button button2 = layout.findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), CommunityArticleWriteActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
       });
        button2.setFocusable(false);

        adapter = new CommunityAdapter();
        listview = (ListView) layout.findViewById(R.id.List_view);

        //어뎁터 할당

        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);

       // getPostCount(0, 1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(getActivity(), CommunityArticleReadActivity.class);  //MainActivity 대신에 여기다가 게시물 자바 쓰면됨

                String intentStr = String.format("%d", matched.get((int)adapter.getItemId(pos)));
                intent.putExtra("position", intentStr); // 이게 게시물 번호 넘기는거임. position 이란 이름으로 넘기니까 오빠가 받아서 출력하기 ㄱㄱ
                //intent.putExtra("position", matched.get(Long.toString(adapter.getItemId(pos)))); // 이게 게시물 번호 넘기는거임. position 이란 이름으로 넘기니까 오빠가 받아서 출력하기 ㄱㄱ
                startActivity(intent);
            }
        });

        return layout;
    }


    //@Override
    /*public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mOnInputListener = (OnInputListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement OnInputListener");
        }
    }*/

    /*
    public void getPostCount(final int startidx, final int endidx) //DB에서 온습도 정보 가져오기
    {
        RequestQueue queue = Volley.newRequestQueue(getContext()); //고쳤음
        String getFreeBoardListUrl = getString(R.string.getFreeBoardListUrl); //온습도를 받아오는 url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getFreeBoardListUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject  = new JSONObject(response);  // 응답으로 받은 JSONObject
                    int countNum = jsonObject.getInt("COUNT"); // 받은 JSONObject 중 COUNT만 파싱
                    if(countNum > 0)
                    {
                        postNum = countNum;
                        getPost(0, postNum);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "게시물 숫자", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                String first = String.valueOf(startidx);
                String last = String.valueOf(endidx);
                params.put("STARTIDX", first);
                params.put("ENDIDX", last);
                return params;
            }
        };
        queue.add(stringRequest);
    }



    public void getPost(final int startidx, final int endidx) //DB에서 온습도 정보 가져오기
    {
        RequestQueue queue = Volley.newRequestQueue(getContext()); //고쳤음
        String getFreeBoardListUrl = getString(R.string.getFreeBoardListUrl); //온습도를 받아오는 url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getFreeBoardListUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    matched = new ArrayList<>();
                    JSONObject jsonObject  = new JSONObject(response);  // 응답으로 받은 JSONObject
                    JSONArray jarray = jsonObject.getJSONArray("List"); // 대괄호 구별x
                    int getNum = jsonObject.getInt("RESULT"); // 받은 JSONObject 중 COUNT만 파싱
                    if(getNum > 0)
                    {
                        String writer, exTitle, title, date, hit;
                        for (int i = 0; i < getNum; i++)
                        {

                            JSONObject jObject = jarray.getJSONObject(i); // 중괄호 구별
                            matched.add(Integer.parseInt(jObject.optString("num")));
                            hit = jObject.optString("hit"); //조회수
                            writer = jObject.optString("id"); // 작성자
                            exTitle = jObject.optString("title");
                            title = URLDecoder.decode(exTitle, "utf-8"); // 제목에 대한 한글 인코딩 → 고쳐야돼
                            date = jObject.optString("time"); // 작성시간
                            String result = date.substring(0, date.lastIndexOf("+"));
                            adapter.addVO(title,writer,hit,result);
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "게시글 못받음", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                String first = String.valueOf(startidx);
                String last = String.valueOf(endidx);
                params.put("STARTIDX", first);
                params.put("ENDIDX", last);
                return params;
            }
        };
        queue.add(stringRequest);
    }*/
}