package com.example.libingkun.finalwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOError;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;


public class LoginActivity extends ActionBarActivity {

    private EditText email;
    private EditText passWord;
    private Button login;
    private String result = "";
    private static Handler handler;
    private static final int MSG_LOGERROR = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        email = (EditText)findViewById(R.id.email);
        passWord  = (EditText)findViewById(R.id.password);
        createHandler();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(email.getText().toString())){
                    Toast.makeText(LoginActivity.this,"请输入用户名！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(passWord.getText().toString())){
                    Toast.makeText(LoginActivity.this,"请输入密码！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //创建一个进程，用于发送http请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send();
                        if(result != null){

                            //Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT).show();
                            Log.i("return",result);
                            JSONTokener jsonParser = new JSONTokener(result);
                            try {
                                JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
                                boolean loginStatus=jsonResult.getBoolean("loginStatus");
                                if(loginStatus){
                                    SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("uid",jsonResult.getString("uid"));
                                    editor.putString("email",jsonResult.getString("email"));
                                    editor.putString("type",jsonResult.getString("type"));
                                    editor.putString("name",jsonResult.getString("name"));
                                    editor.putString("password",jsonResult.getString("password"));
                                    editor.putString("phone",jsonResult.getString("phone"));
                                    editor.putString("sex",jsonResult.getString("sex"));
                                    editor.putString("birthday",jsonResult.getString("birthday"));
                                    editor.putString("idcard",jsonResult.getString("idcard"));
                                    editor.commit();

                                    Intent it = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(it);


                                }
                                else {
                                    Message msg = handler.obtainMessage();
                                    msg.what = MSG_LOGERROR;
                                    handler.sendMessage(msg);
                                }
                            }
                            catch (Exception e){
                                Log.i("Exception",e.toString());
                            }

                        }


//                        Toast.makeText(LoginActivity.this,"好了！",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT).show();
//                        System.out.print(result);
                    }
                }).start();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void send(){
        //请求地址
        String emailInput = email.getText().toString();
        String passWordInput = passWord.getText().toString();
        String target="http://"+getString(R.string.server_host)+"/Home/User/login?email="+emailInput+"&password="+passWordInput;
        //创建HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        //创建HttpGet对象
        HttpGet httpRequest = new HttpGet(target);
        HttpResponse httpResponse;
        try{
            //执行HttpClient请求
            httpResponse = httpClient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回获取的字符串
                result = EntityUtils.toString(httpResponse.getEntity());
            }else{
                result = "请求失败!";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void createHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == MSG_LOGERROR){
                        Toast.makeText(LoginActivity.this,"邮箱/密码错误!",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }
}
