package com.example.libingkun.finalwork;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userIDString;
    private String mParam2;

    private Activity myActivity;
    private EditText userID;
    private EditText name;
    private RadioGroup sex;
    private EditText phone;
    private EditText email;
    private EditText birthday;
    private EditText IDCard;
    private EditText password;
    private Button editInfo;
    private String buttonAction = "editText";
    private String result = "";
    private String emailString = "";
    private String passwordString = "";

    //handler相关
    private MyHandler handler;
    private static final int MSG_ERROR = 0;
    private static final int MSG_SUCCESS = 1;
    private static final int GET_IFO_SUCCESS = 3;
    private static final int GET_IFO_ERROR = 4;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userIDString = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        userID = (EditText)rootView.findViewById(R.id.userID);
        name = (EditText)rootView.findViewById(R.id.name);
        sex = (RadioGroup)rootView.findViewById(R.id.sex);
        phone = (EditText)rootView.findViewById(R.id.phone);
        email = (EditText)rootView.findViewById(R.id.email);
        birthday = (EditText)rootView.findViewById(R.id.birthday);
        IDCard = (EditText)rootView.findViewById(R.id.IDCard);
        password = (EditText)rootView.findViewById(R.id.password);
        editInfo = (Button)rootView.findViewById(R.id.editInfo);
        rootView = initUserInfoFragement(rootView);
        return rootView;
    }

    private View initUserInfoFragement(View rootView){
        myActivity = this.getActivity();
        if("".equals(userIDString)) {
            SharedPreferences msp = myActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
            userIDString = msp.getString("uid", "");
        }
        //创建Handler
        createHandler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //请求地址
                String target = "http://" + getString(R.string.server_host) + "/Home/User/getUserInfo";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpRequst = new HttpPost(target);
                //将要传的值保存到List集合中
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("param","post"));
                params.add(new BasicNameValuePair("uid",userIDString));
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
                    msg.what = GET_IFO_SUCCESS;
                }
                else{
                    msg.what = GET_IFO_ERROR;
                }
                handler.sendMessage(msg);
            }
        }).start();
        editInfo.setOnClickListener(editInfoListener);
        return rootView;
    }

    private View.OnClickListener editInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (buttonAction){
                case "editText":
                    emailString = email.getText().toString();
                    passwordString = password.getText().toString();
                    allowEdit();
                    changeActionTo("submitChange");
                    break;
                case "submitChange":
                    notAllowEdit();
                    changeActionTo("editText");
                    sendInfoToChange();
                    break;
            }
        }
        private void allowEdit(){
            name.setEnabled(true);
            sex.getChildAt(0).setEnabled(true);
            sex.getChildAt(1).setEnabled(true);
            phone.setEnabled(true);
            email.setEnabled(true);
            birthday.setEnabled(true);
            IDCard.setEnabled(true);
            password.setEnabled(true);
        }
        private void notAllowEdit(){
            name.setEnabled(false);
            sex.getChildAt(0).setEnabled(false);
            sex.getChildAt(1).setEnabled(false);
            phone.setEnabled(false);
            email.setEnabled(false);
            birthday.setEnabled(false);
            IDCard.setEnabled(false);
            password.setEnabled(false);
        }
        private void changeActionTo(String nextAction){
            switch (nextAction){
                case "editText":
                    editInfo.setText("修改资料");
                    buttonAction = "editText";
                    break;
                case "submitChange":
                    editInfo.setText("提交修改");
                    buttonAction = "submitChange";
                    break;
            }
        }
        private void sendInfoToChange() {
            //创建Handler
            createHandler();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    //获取数据
                    String nameString = name.getText().toString();
                    String sexString = "";
                    RadioButton mChild= (RadioButton)sex.getChildAt(0);
                    RadioButton fChild= (RadioButton)sex.getChildAt(1);
                    if (mChild.isChecked()) {
                        sexString = "m";
                    }
                    else if(fChild.isChecked()){
                        sexString = "f";
                    }
                    String phoneString = phone.getText().toString();
                    String newEmailString = email.getText().toString();
                    String birthdayString = birthday.getText().toString();
                    String IDCardString = IDCard.getText().toString();
                    String newPasswordString = password.getText().toString();
                    //请求地址
                    String target = "http://" + getString(R.string.server_host) + "/Home/User/changeInfo";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpRequst = new HttpPost(target);
                    //将要传的值保存到List集合中
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("param","post"));
                    params.add(new BasicNameValuePair("email",emailString));
                    params.add(new BasicNameValuePair("password",passwordString));
                    params.add(new BasicNameValuePair("name",nameString));
                    params.add(new BasicNameValuePair("sex",sexString));
                    params.add(new BasicNameValuePair("phone",phoneString));
                    params.add(new BasicNameValuePair("newEmail",newEmailString));
                    params.add(new BasicNameValuePair("birthday",birthdayString));
                    params.add(new BasicNameValuePair("IDCard",IDCardString));
                    params.add(new BasicNameValuePair("newPassword",newPasswordString));
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
    };

    //handler相关2
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
            if(msg.what == GET_IFO_ERROR){
                Log.i("res",result);
                Toast.makeText(myActivity, "获取信息失败!", Toast.LENGTH_SHORT).show();
            }
            if(msg.what == GET_IFO_SUCCESS){
                Log.i("res",result);
                initUserInfo();
                //Toast.makeText(myActivity, "获取信息成功!", Toast.LENGTH_SHORT).show();
            }

        }

        private void initUserInfo() {
            JSONTokener jsonParser = new JSONTokener(result);
            try {
                JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
                userID.setText(jsonResult.getString("uid"));
                name.setText(jsonResult.getString("name"));
                if ("m".equals(jsonResult.getString("sex")))
                    sex.check(R.id.male);
                else
                    sex.check(R.id.female);
                phone.setText(jsonResult.getString("phone"));
                email.setText(jsonResult.getString("email"));
                birthday.setText(jsonResult.getString("birthday"));
                IDCard.setText(jsonResult.getString("idcard"));
                password.setText(jsonResult.getString("password"));
            }
            catch (Exception e){
                Log.i("exce",e.toString());
            }
        }
    }
}
