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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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

    private MyHandler handler;
    private String result="";
    private static final int MSG_ERROR = 0;
    private static final int MSG_SUCCESS = 1;

    private ListView userList;
    private UserListListener listener;

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
        View viewRoot = inflater.inflate(R.layout.fragment_manage_user, container, false);
        //获取Activity
        myActivity = this.getActivity();
        //获得列表view
        userList = (ListView)viewRoot.findViewById(R.id.userList);
        //初始化用户列表
        viewRoot = initUserList(viewRoot);
        //增加listener
        userList.setOnItemClickListener(listener);
        return viewRoot;
    }

    private View initUserList(View viewRoot) {

        String[] userName = new String[]{"lbk", "李柄坤", "凉白开"};
        String[] userType = new String[]{"管理员", "扫大街", "不知道"};
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < userName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", userName[i]);
            map.put("userType", userType[i]);
            listItems.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(viewRoot.getContext(), listItems,
                R.layout.user_list_item, new String[]{"userName", "userType"}, new int[]{
                R.id.userName, R.id.userType});
        userList.setAdapter(adapter);
        return viewRoot;

    }
    private void getUserData(){
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
                if("1".equals(result)){
                    msg.what = MSG_SUCCESS;
                }
                else{
                    msg.what = MSG_ERROR;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
    private void createHandler(){
        handler = new MyHandler();
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == MSG_ERROR){
                Log.i("res",result);
                Toast.makeText(myActivity, "信息修改失败!", Toast.LENGTH_SHORT).show();
            }
            if(msg.what == MSG_SUCCESS){
                Log.i("res",result);
                Toast.makeText(myActivity, "信息修改成功!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //继承listener类
    class UserListListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for(int i =0;i<userList.getChildCount();i++){
                if(position == i) {
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment thisFragment = UserInfoFragment.newInstance("","");
//                        CreateReportFragment a= CreateReportFragment.newInstance("","");
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, thisFragment)
                            .commit();
                }
            }
        }
    }

}
