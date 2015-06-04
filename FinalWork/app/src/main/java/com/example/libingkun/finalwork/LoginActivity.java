package com.example.libingkun.finalwork;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOError;
import java.io.IOException;


public class LoginActivity extends ActionBarActivity {
    private EditText userName;
    private EditText passWord;
    private Button login;
    private String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        userName = (EditText)findViewById(R.id.username);
        passWord  = (EditText)findViewById(R.id.password);
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
//      请求地址
        String target="";
        //创建HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        //创建HttpGet对象
        HttpGet httpRequest = new HttpGet(target);
        HttpResponse httpResponse;
        try{
            //执行HttpClient请求
            httpRequest = httpClient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回获取的字符串
                result = EntityUtils.toString(httpResponse.getEntity());
            }else{
                result = "请求失败!";
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }
    }
}
