package com.example.libingkun.finalwork;

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

import java.io.IOError;
import java.io.IOException;
import java.util.logging.LogRecord;


public class LoginActivity extends ActionBarActivity {

    private EditText userName;
    private EditText passWord;
    private Button login;
    private String result = "";
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        userName = (EditText)findViewById(R.id.username);
        passWord  = (EditText)findViewById(R.id.password);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(result != null){

                    Toast.makeText(LoginActivity.this,result,Toast.LENGTH_SHORT).show();

                }

            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(userName.getText().toString())){
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
//                        Toast.makeText(LoginActivity.this,"发送前！",Toast.LENGTH_SHORT).show();
                        send();
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);

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
        String target="http://10.0.2.2:80/text/text.html";
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
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
