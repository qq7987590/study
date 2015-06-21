package com.example.libingkun.finalwork;

import android.app.Activity;
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
import android.widget.Spinner;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private Activity myActivity;
    private Spinner userType;
    private EditText name;
    private RadioGroup sex;
    private EditText phone;
    private EditText email;
    private EditText birthday;
    private EditText IDCard;
    private EditText password;
    private Button createUser;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewUserFragment newInstance(String param1, String param2) {
        NewUserFragment fragment = new NewUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewUserFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_new_user, container, false);
        myActivity = this.getActivity();
        getViewElement();
        setButtonListener();
        return rootView;
    }

    private void getViewElement() {
        userType = (Spinner) rootView.findViewById(R.id.userType);
        name = (EditText) rootView.findViewById(R.id.name);
        sex = (RadioGroup) rootView.findViewById(R.id.sex);
        phone = (EditText) rootView.findViewById(R.id.phone);
        email = (EditText) rootView.findViewById(R.id.email);
        birthday = (EditText) rootView.findViewById(R.id.birthday);
        IDCard = (EditText) rootView.findViewById(R.id.IDCard);
        password = (EditText) rootView.findViewById(R.id.password);
        createUser = (Button) rootView.findViewById(R.id.createUser);
    }

    private void setButtonListener() {
        createUser.setOnClickListener(createUserListener);
    }

    private View.OnClickListener createUserListener = new View.OnClickListener() {
        private String result = "";
        //handler相关
        private MyHandler handler;
        private static final int MSG_ERROR = 0;
        private static final int MSG_SUCCESS = 1;
        private static final int GET_IFO_SUCCESS = 3;
        private static final int GET_IFO_ERROR = 4;

        @Override
        public void onClick(View v) {
            Log.i("d", "d");
            sendUserInfoToCreate();
        }

        private void sendUserInfoToCreate() {
            //创建Handler
            createHandler();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    //获取数据
                    String nameString = name.getText().toString();
                    String sexString = "";
                    RadioButton mChild = (RadioButton) sex.getChildAt(0);
                    RadioButton fChild = (RadioButton) sex.getChildAt(1);
                    if (mChild.isChecked()) {
                        sexString = "m";
                    } else if (fChild.isChecked()) {
                        sexString = "f";
                    }
                    String userTypeString = userType.getSelectedItem().toString();
                    String phoneString = phone.getText().toString();
                    String emailString = email.getText().toString();
                    String birthdayString = birthday.getText().toString();
                    String IDCardString = IDCard.getText().toString();
                    String passwordString = password.getText().toString();
                    //请求地址
                    String target = "http://" + getString(R.string.server_host) + "/Home/User/addUser";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpRequst = new HttpPost(target);
                    //将要传的值保存到List集合中
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("param", "post"));
                    params.add(new BasicNameValuePair("userType", userTypeString));
                    params.add(new BasicNameValuePair("email", emailString));
                    params.add(new BasicNameValuePair("password", passwordString));
                    params.add(new BasicNameValuePair("name", nameString));
                    params.add(new BasicNameValuePair("sex", sexString));
                    params.add(new BasicNameValuePair("phone", phoneString));
                    params.add(new BasicNameValuePair("birthday", birthdayString));
                    params.add(new BasicNameValuePair("IDCard", IDCardString));
                    //创建HttpGet对象
                    try {
                        //执行HttpClient请求
                        httpRequst.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
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
                    if ("1".equals(result)) {
                        msg.what = MSG_SUCCESS;
                    } else {
                        msg.what = MSG_ERROR;
                    }
                    handler.sendMessage(msg);
                }
            }).start();
        }

        //handler相关2
        private void createHandler() {
            handler = new MyHandler();
        }

        class MyHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_ERROR) {
                    Log.i("res", result);
                    Toast.makeText(myActivity, "添加用户失败!", Toast.LENGTH_SHORT).show();
                }
                if (msg.what == MSG_SUCCESS) {
                    Log.i("res", result);
                    Toast.makeText(myActivity, "添加用户成功!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
}
