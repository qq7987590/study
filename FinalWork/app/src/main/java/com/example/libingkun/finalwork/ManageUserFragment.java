package com.example.libingkun.finalwork;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity myActivity;
    private View viewRoot;

    private MyHandler handler;
    private String result="";
    private static final int MSG_ERROR = 0;
    private static final int MSG_SUCCESS = 1;

    private ListView userList;
    private EditText eSearch;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageUserFragment newInstance(String param1, String param2) {
        ManageUserFragment fragment = new ManageUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ManageUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_manage_user, container, false);
        //获取Activity
        myActivity = this.getActivity();
        //创建用户列表
        createList();

        return viewRoot;
    }


    private void createList(){
        //获得列表view
        userList = (ListView)viewRoot.findViewById(R.id.userList);
        eSearch = (EditText)viewRoot.findViewById(R.id.etSearch);
        //创建Handler
        createHandler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //请求地址
                String target = "http://" + getString(R.string.server_host) + "/Home/User/getAllUser";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpRequst = new HttpPost(target);
                //将要传的值保存到List集合中
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("param","post"));
                //创建HttpGet对象
                try {
                    //执行HttpClient请求
                    httpRequst.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
                    HttpResponse httpResponse = httpClient.execute(httpRequst);
//                        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    result = EntityUtils.toString(httpResponse.getEntity());
//                        }
//                        else{
//                            result="请求失败";
//                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //用handler处理消息
                Message msg = handler.obtainMessage();
                if(!"-1".equals(result)){
                    msg.what = MSG_SUCCESS;
                }
                else{
                    msg.what = MSG_ERROR;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    private String getUserType(String typeNumber){
        switch (typeNumber){
            case "0":
                return "派单员";
            case "1":
                return "业务员";
            case "2":
                return "评估员";
            case "3":
                return "财务员";
            case "4":
                return "文员";
            case "5":
                return "一级评估师";
            case "6":
                return "二级评估师";
            case "7":
                return "管理员";
        }
        return "";
    }
    private void createHandler(){
        handler = new MyHandler();
    }
    class MyHandler extends Handler {
        private UserListListener listener = new UserListListener();
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == MSG_ERROR){
                Log.i("res",result);
                Toast.makeText(myActivity, "获取不到用户数据!", Toast.LENGTH_SHORT).show();
            }
            if(msg.what == MSG_SUCCESS){
                Log.i("res",result);
                initUserListByResult();
            }

        }
        private void initUserListByResult() {
            ArrayList<String> userID = new ArrayList<String>();
            ArrayList<String> userName = new ArrayList<String>();
            ArrayList<String> userType = new ArrayList<String>();
            try {
                JSONTokener jsonParser = new JSONTokener(result);
                JSONArray jsonResult = (JSONArray) jsonParser.nextValue();
                for (int i = 0; i < jsonResult.length(); i++) {
                    JSONObject jo = jsonResult.getJSONObject(i);
                    userID.add(jo.getString("uid"));
                    userName.add(jo.getString("name"));
                    userType.add(jo.getString("type"));
                }
            }
            catch (Exception e){
                Log.i("exception",e.toString());
            }

            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < userName.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userID", userID.get(i));
                map.put("userName", userName.get(i));
                map.put("userType", getUserType(userType.get(i)));
                listItems.add(map);
            }
            SimpleAdapter adapter = new SimpleAdapter(viewRoot.getContext(), listItems,
                    R.layout.user_list_item, new String[]{"userID","userName", "userType"}, new int[]{
                    R.id.userID,R.id.userName, R.id.userType});
            userList.setAdapter(adapter);
            //增加listener
            userList.setOnItemClickListener(listener);
            set_eSearch_TextChanged();

        }

        private void set_eSearch_TextChanged()
        {

            eSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    //这个应该是在改变的时候会做的动作吧，具体还没用到过。
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                    //这是文本框改变之前会执行的动作
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    /**这是文本框改变之后 会执行的动作
                     * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                     * 所以这里我们就需要加上数据的修改的动作了。
                     */
                    Log.i("text","changed");
                }
            });

        }
        //继承listener类
        class UserListListener implements AdapterView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i =0;i<userList.getChildCount();i++){
                    if(position == i) {
                        TextView uidText = (TextView)userList.getChildAt(i).findViewById(R.id.userID);
                        String uidString = uidText.getText().toString();
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment thisFragment = UserInfoFragment.newInstance(uidString,"");
//                        CreateReportFragment a= CreateReportFragment.newInstance("","");
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, thisFragment)
                                .commit();
                    }
                }
            }
        }
    }



}
