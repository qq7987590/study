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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


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
    private String mParam1;
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
            mParam1 = getArguments().getString(ARG_PARAM1);
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
        SharedPreferences msp = myActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        userID.setText(msp.getString("uid",""));
        name.setText(msp.getString("name", ""));
        if("m".equals(msp.getString("sex","")))
            sex.check(R.id.male);
        else
            sex.check(R.id.female);
        phone.setText(msp.getString("phone",""));
        email.setText(msp.getString("email",""));
        birthday.setText(msp.getString("birthday",""));
        IDCard.setText(msp.getString("idcard",""));
        password.setText(msp.getString("password",""));
        editInfo.setOnClickListener(editInfoListener);
        return rootView;
    }

    private View.OnClickListener editInfoListener = new View.OnClickListener() {
        private MyHandler handler;
        private static final int MSG_ERROR = 0;
        @Override
        public void onClick(View v) {
            switch (buttonAction){
                case "editText":
                    allowEdit();
                    changeActionTo("submitChange");
                    break;
                case "submitChange":
                    notAllowEdit();
                    changeActionTo("editText");
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
                    sendInfoToChange();
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
                    SharedPreferences msp = myActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                    String emailString = msp.getString("email", "");
                    String passwordString = msp.getString("email", "");

                    String nameString = name.getText().toString();
                    int sexId = sex.getFocusedChild().getId();
                    String sexString = "";
                    if (sexId == 0) {
                        sexString = "m";
                    } else {
                        sexString = "f";
                    }
                    String phoneString = phone.getText().toString();
                    String newEmailString = email.getText().toString();
                    String birthdayString = birthday.getText().toString();
                    String IDCardString = IDCard.getText().toString();
                    String newPasswordString = password.getText().toString();
                    //请求地址
                    String target = "http://" + getString(R.string.server_host) + "/Home/User/changeInfo?email=" + emailString
                            + "&password=" + passwordString
                            + "&name=" + nameString
                            + "&sex=" + sexString
                            + "&phone=" + phoneString
                            + "&newEmail" + newEmailString
                            + "&birthday" + birthdayString
                            + "&IDCard" + IDCardString
                            + "&newPassword" + newPasswordString;
                    //创建HttpClient对象
                    HttpClient httpClient = new DefaultHttpClient();
                    //创建HttpGet对象
                    HttpGet httpRequest = new HttpGet(target);
                    HttpResponse httpResponse;
                    try {
                        //执行HttpClient请求
                        httpResponse = httpClient.execute(httpRequest);
                        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            //返回获取的字符串
                            result = EntityUtils.toString(httpResponse.getEntity());
                        } else {
                            result = "请求失败!";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //用handler处理消息
                    Message msg = handler.obtainMessage();
                    msg.what = MSG_ERROR;
                    handler.sendMessage(msg);
                }
            });
        }
        private void createHandler(){
            handler = new MyHandler();
        }
        class MyHandler extends Handler {
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == MSG_ERROR){
                    Toast.makeText(myActivity, "修改错误!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
}
