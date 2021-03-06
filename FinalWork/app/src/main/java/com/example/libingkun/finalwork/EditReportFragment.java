package com.example.libingkun.finalwork;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String thisReportNumber = "";
    private String mParam2;

    //view控件群
    private EditText distributor;
    private Spinner saleman;
    private EditText firstAccessNumber;
    private EditText reportMonth;
    private EditText contacts;
    private EditText contactsPhone;
    private EditText firstDistributor;
    private EditText secondDistributor;
    private EditText thirdDistributor;
    private Spinner assessment;
    private EditText street;
    private EditText location;
    private EditText assessRemark;
    private EditText villageName;
    private EditText assessDate;
    private EditText outsideTime;
    private EditText assessPrice;
    private Spinner  firstAppraiser;
    private EditText firstAppraiserRemark;
    private Spinner  secondAppraiser;
    private EditText secondAppraiserRemark;
    private EditText reportNumber;
    private EditText reportDate;
    private Spinner reportType;
    private EditText clerkRemark;
    private EditText fee;
    private EditText treasuterRemark;
    private Button reportButton;

    private Activity myActivity;
    private View viewRoot;
    private String result;
    private String listJsonResult;
    private String orginData;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditReportFragment newInstance(String param1, String param2) {
        EditReportFragment fragment = new EditReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            thisReportNumber = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_edit_report, container, false);
        myActivity = this.getActivity();
        getViewControl(viewRoot);
        addSpanerItem();
        addButtonListener();
        return viewRoot;
    }


    private void getOrginData(){
        //创建Handler
        createHandler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //
                SharedPreferences sp = myActivity.getSharedPreferences("user",myActivity.MODE_PRIVATE);
                //请求地址
                Log.i("thisReportNumber",thisReportNumber);
                String target = "http://" + getString(R.string.server_host) + "/Home/Report/getReportById";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpRequst = new HttpPost(target);
                //将要传的值保存到List集合中
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("param","post"));
                params.add(new BasicNameValuePair("id",thisReportNumber));
                //创建HttpGet对象
                try {
                    //执行HttpClient请求
                    httpRequst.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
                    HttpResponse httpResponse = httpClient.execute(httpRequst);
//                        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    orginData = EntityUtils.toString(httpResponse.getEntity());
//                        }
//                        else{
//                            result="请求失败";
//                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //用handler处理消息
                Message msg = handler.obtainMessage();
                if(!"-1".equals(orginData)){
                    msg.what = ORGIN_DATA_SUCCESS;
                }
                else{
                    msg.what = ORGIN_DATA_ERROR;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void addSpanerItem(){
        //创建Handler
        createHandler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //
                SharedPreferences sp = myActivity.getSharedPreferences("user",myActivity.MODE_PRIVATE);
                //请求地址
                String target = "http://" + getString(R.string.server_host) + "/Home/User/getItemList";
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
                    listJsonResult = EntityUtils.toString(httpResponse.getEntity());
//                        }
//                        else{
//                            result="请求失败";
//                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //用handler处理消息
                Message msg = handler.obtainMessage();
                if("-1".equals(listJsonResult)){
                    msg.what = LIST_ERROR;
                }
                else{
                    msg.what = LIST_SUCCESS;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void addButtonListener(){
        reportButton.setOnClickListener(createReport);
    }

    private View.OnClickListener createReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("w", "w");
            sendInfoToCreate();
        }
        private void sendInfoToCreate(){
            //创建Handler
            createHandler();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    //
                    SharedPreferences sp = myActivity.getSharedPreferences("user",myActivity.MODE_PRIVATE);
                    //请求地址
                    String target = "http://" + getString(R.string.server_host) + "/Home/Report/updateReport";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpRequst = new HttpPost(target);
                    //将要传的值保存到List集合中
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("param","post"));
                    params.add(new BasicNameValuePair("distributor",distributor.getText().toString()));
                    if(saleman.getSelectedItem()!=null) {
                        params.add(new BasicNameValuePair("saleman", saleman.getSelectedItem().toString()));
                    }
                    if(assessment.getSelectedItem()!=null) {
                        params.add(new BasicNameValuePair("assessment", assessment.getSelectedItem().toString()));
                    }
                    if(firstAppraiser.getSelectedItem()!=null) {
                        params.add(new BasicNameValuePair("fist_appraiser", firstAppraiser.getSelectedItem().toString()));
                    }
                    if(secondAppraiser.getSelectedItem()!=null) {
                        params.add(new BasicNameValuePair("second_appraiser", secondAppraiser.getSelectedItem().toString()));
                    }
                    params.add(new BasicNameValuePair("first_assess_number",firstAccessNumber.getText().toString()));
                    params.add(new BasicNameValuePair("report_month",reportMonth.getText().toString()));
                    params.add(new BasicNameValuePair("contacts",contacts.getText().toString()));
                    params.add(new BasicNameValuePair("contacts_phone",contactsPhone.getText().toString()));
                    params.add(new BasicNameValuePair("first_distributor",firstDistributor.getText().toString()));
                    params.add(new BasicNameValuePair("second_distributor",secondDistributor.getText().toString()));
                    params.add(new BasicNameValuePair("third_distributor",thirdDistributor.getText().toString()));
                    params.add(new BasicNameValuePair("street",street.getText().toString()));
                    params.add(new BasicNameValuePair("location",location.getText().toString()));
                    params.add(new BasicNameValuePair("assessment_remark",assessRemark.getText().toString()));
                    params.add(new BasicNameValuePair("village_name",villageName.getText().toString()));
                    params.add(new BasicNameValuePair("assess_date",assessDate.getText().toString()));
                    params.add(new BasicNameValuePair("outside_time",outsideTime.getText().toString()));
                    params.add(new BasicNameValuePair("assess_price",assessPrice.getText().toString()));
                    params.add(new BasicNameValuePair("fist_appraiser_remark",firstAppraiserRemark.getText().toString()));
                    params.add(new BasicNameValuePair("second_appraiser_remark",secondAppraiserRemark.getText().toString()));
                    params.add(new BasicNameValuePair("report_number",reportNumber.getText().toString()));
                    params.add(new BasicNameValuePair("report_date",reportDate.getText().toString()));
                    if(reportType.getSelectedItem()!=null) {
                        params.add(new BasicNameValuePair("report_type", reportType.getSelectedItem().toString()));
                    }
                    params.add(new BasicNameValuePair("clerk_remark",clerkRemark.getText().toString()));
                    params.add(new BasicNameValuePair("fee",fee.getText().toString()));
                    params.add(new BasicNameValuePair("treasuter_remark",treasuterRemark.getText().toString()));
                    params.add(new BasicNameValuePair("last_uid",sp.getString("uid","")));

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
    };

    //handler相关
    private MyHandler handler;
    private static final int MSG_ERROR = 0;
    private static final int MSG_SUCCESS = 1;
    private static final int LIST_SUCCESS = 2;
    private static final int LIST_ERROR = 3;
    private static final int ORGIN_DATA_SUCCESS = 4;
    private static final int ORGIN_DATA_ERROR = 5;


    //handler相关2
    private void createHandler(){
        handler = new MyHandler();
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_ERROR) {
                Log.i("res", result);
                Toast.makeText(myActivity, "修改失败!", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == MSG_SUCCESS) {
                Log.i("res", result);
                Toast.makeText(myActivity, "修改成功!", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == LIST_SUCCESS){
                Log.i("res", listJsonResult);
                Toast.makeText(myActivity, "获取信息成功!", Toast.LENGTH_SHORT).show();
                setSalemanItems();
                setAssessmentItems();
                setFirstAppraisertemss();
                setSecondAppraisertemss();
                getOrginData();
                authorityControl();
            }
            if(msg.what == LIST_ERROR){
                Log.i("res", listJsonResult);
                Toast.makeText(myActivity, "获取信息失败!", Toast.LENGTH_SHORT).show();
            }
            if(msg.what == ORGIN_DATA_ERROR){
                Log.i("res", orginData);
            }
            if(msg.what == ORGIN_DATA_SUCCESS){
                Log.i("res", orginData);
                initFormByOrginData();
            }
        }
    };
    private void authorityControl(){
        SharedPreferences sp = myActivity.getSharedPreferences("user",myActivity.MODE_PRIVATE);
        String userType = sp.getString("type","");
        switch (userType){
            case "0":
                //派单员
                break;
            case "1":
                //业务员
                distributor.setVisibility(viewRoot.GONE);
                saleman.setVisibility(viewRoot.GONE);
                firstAccessNumber.setEnabled(false);
                reportMonth.setEnabled(false);
                assessment.setEnabled(false);
                assessDate.setEnabled(false);
                outsideTime.setVisibility(viewRoot.GONE);
                assessPrice.setEnabled(false);
                firstAppraiser.setEnabled(false);
                firstAppraiserRemark.setEnabled(false);
                secondAppraiser.setEnabled(false);
                secondAppraiserRemark.setEnabled(false);
                reportNumber.setEnabled(false);
                reportDate.setEnabled(false);
                reportType.setEnabled(false);
                clerkRemark.setVisibility(viewRoot.GONE);
                fee.setEnabled(false);
                treasuterRemark.setVisibility(viewRoot.GONE);

                break;
            case "2":
                //评估员
                distributor.setVisibility(viewRoot.GONE);
                saleman.setVisibility(viewRoot.GONE);
                firstAccessNumber.setEnabled(false);
                reportMonth.setEnabled(false);
                firstDistributor.setEnabled(false);
                secondDistributor.setEnabled(false);
                thirdDistributor.setEnabled(false);
                assessment.setEnabled(false);
                street.setEnabled(false);
                assessPrice.setEnabled(false);
                firstAppraiserRemark.setEnabled(false);
                secondAppraiser.setEnabled(false);
                secondAppraiserRemark.setEnabled(false);
                reportNumber.setEnabled(false);
                reportDate.setEnabled(false);
                reportType.setEnabled(false);
                clerkRemark.setVisibility(viewRoot.GONE);
                fee.setEnabled(false);
                treasuterRemark.setVisibility(viewRoot.GONE);
                break;
            case "3":
                //财务员
                distributor.setVisibility(viewRoot.GONE);
                saleman.setVisibility(viewRoot.GONE);
                firstAccessNumber.setEnabled(false);
                reportMonth.setEnabled(false);
                contacts.setEnabled(false);
                contactsPhone.setEnabled(false);
                firstDistributor.setEnabled(false);
                secondDistributor.setEnabled(false);
                thirdDistributor.setEnabled(false);
                assessment.setEnabled(false);
                street.setEnabled(false);
                assessDate.setEnabled(false);
                location.setVisibility(viewRoot.GONE);
                assessRemark.setVisibility(viewRoot.GONE);
                villageName.setVisibility(viewRoot.GONE);
                assessDate.setEnabled(false);
                firstAppraiser.setEnabled(false);
                firstAppraiserRemark.setEnabled(false);
                secondAppraiser.setEnabled(false);
                secondAppraiserRemark.setEnabled(false);
                clerkRemark.setVisibility(viewRoot.GONE);
                break;
            case "4":
                //文员
                distributor.setVisibility(viewRoot.GONE);
                saleman.setVisibility(viewRoot.GONE);
                firstAccessNumber.setEnabled(false);
                reportMonth.setEnabled(false);
                contacts.setEnabled(false);
                contactsPhone.setEnabled(false);
                firstDistributor.setEnabled(false);
                secondDistributor.setEnabled(false);
                thirdDistributor.setEnabled(false);
                assessment.setEnabled(false);
                street.setEnabled(false);
                location.setEnabled(false);
                firstAppraiser.setEnabled(false);
                firstAppraiserRemark.setEnabled(false);
                secondAppraiser.setEnabled(false);
                secondAppraiserRemark.setEnabled(false);
                treasuterRemark.setVisibility(viewRoot.GONE);
                break;
            case "5":
                //一级评估师
                distributor.setVisibility(viewRoot.GONE);
                saleman.setVisibility(viewRoot.GONE);
                firstAccessNumber.setEnabled(false);
                reportMonth.setEnabled(false);
                contacts.setVisibility(viewRoot.GONE);
                contactsPhone.setVisibility(viewRoot.GONE);
                firstDistributor.setEnabled(false);
                secondDistributor.setEnabled(false);
                thirdDistributor.setVisibility(viewRoot.GONE);
                assessment.setEnabled(false);
                street.setEnabled(false);
                location.setEnabled(false);
                assessRemark.setVisibility(viewRoot.GONE);
                villageName.setEnabled(false);
                assessDate.setEnabled(false);
                assessPrice.setEnabled(false);
                reportNumber.setEnabled(false);
                reportType.setEnabled(false);
                reportDate.setEnabled(false);
                outsideTime.setVisibility(viewRoot.GONE);
                firstAppraiser.setVisibility(viewRoot.GONE);
                secondAppraiser.setVisibility(viewRoot.GONE);
                secondAppraiserRemark.setVisibility(viewRoot.GONE);
                clerkRemark.setVisibility(viewRoot.GONE);
                fee.setVisibility(viewRoot.GONE);
                treasuterRemark.setVisibility(viewRoot.GONE);
                break;
            case "6":
                //二级评估师
                distributor.setVisibility(viewRoot.GONE);
                saleman.setVisibility(viewRoot.GONE);
                firstAccessNumber.setEnabled(false);
                reportMonth.setEnabled(false);
                contacts.setVisibility(viewRoot.GONE);
                contactsPhone.setVisibility(viewRoot.GONE);
                firstDistributor.setEnabled(false);
                secondDistributor.setEnabled(false);
                thirdDistributor.setVisibility(viewRoot.GONE);
                assessment.setEnabled(false);
                street.setEnabled(false);
                location.setEnabled(false);
                assessRemark.setVisibility(viewRoot.GONE);
                villageName.setEnabled(false);
                assessDate.setEnabled(false);
                assessPrice.setEnabled(false);
                reportNumber.setEnabled(false);
                reportType.setEnabled(false);
                reportDate.setEnabled(false);
                outsideTime.setVisibility(viewRoot.GONE);
                firstAppraiser.setVisibility(viewRoot.GONE);
                secondAppraiser.setVisibility(viewRoot.GONE);
                firstAppraiserRemark.setVisibility(viewRoot.GONE);
                clerkRemark.setVisibility(viewRoot.GONE);
                fee.setVisibility(viewRoot.GONE);
                treasuterRemark.setVisibility(viewRoot.GONE);
                break;
            case "7":
                //管理员
                break;
        }
    }
    private void initFormByOrginData(){
        JSONTokener jsonParser = new JSONTokener(orginData);
        try {
            JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
            distributor.setText(jsonResult.getString("distributor"));
            setSpinnerItemSelectedByValue(saleman,jsonResult.getString("saleman"));
            setSpinnerItemSelectedByValue(assessment,jsonResult.getString("assessment"));
            setSpinnerItemSelectedByValue(firstAppraiser,jsonResult.getString("fist_appraiser"));
            setSpinnerItemSelectedByValue(secondAppraiser,jsonResult.getString("second_appraiser"));
            firstAccessNumber.setText(jsonResult.getString("first_assess_number"));
            reportMonth.setText(jsonResult.getString("report_month"));
            contacts.setText(jsonResult.getString("contacts"));
            contactsPhone.setText(jsonResult.getString("contacts_phone"));
            firstDistributor.setText(jsonResult.getString("first_distributor"));
            secondDistributor.setText(jsonResult.getString("second_distributor"));
            thirdDistributor.setText(jsonResult.getString("third_distributor"));
            street.setText(jsonResult.getString("street"));
            location.setText(jsonResult.getString("location"));
            assessRemark.setText(jsonResult.getString("assessment_remark"));
            villageName.setText(jsonResult.getString("village_name"));
            assessDate.setText(jsonResult.getString("assess_date"));
            outsideTime.setText(jsonResult.getString("outside_time"));
            assessPrice.setText(jsonResult.getString("assess_price"));
            firstAppraiserRemark.setText(jsonResult.getString("fist_appraiser_remark"));
            secondAppraiserRemark.setText(jsonResult.getString("second_appraiser_remark"));
            reportNumber.setText(jsonResult.getString("report_number"));
            reportDate.setText(jsonResult.getString("report_date"));
            setSpinnerItemSelectedByValue(reportType,jsonResult.getString("report_type"));
            clerkRemark.setText(jsonResult.getString("clerk_remark"));
            treasuterRemark.setText(jsonResult.getString("treasuter_remark"));
            fee.setText(jsonResult.getString("fee"));
        }catch(Exception e){
            Log.i("exc1",e.toString());
        }
    }
    /**
     * 根据值, 设置spinner默认选中:
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }
    private void setSalemanItems(){
        JSONTokener jsonParser = new JSONTokener(listJsonResult);
        try {
            JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
            JSONArray salemanArray = (JSONArray) jsonResult.getJSONArray("saleman");
            List<String> sl = new ArrayList<String>();
            for (int i = 0; i < salemanArray.length(); i++) {
                sl.add(salemanArray.getJSONObject(i).getString("name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewRoot.getContext(),R.layout.saleman_list_item, R.id.one_saleman,sl);
            saleman.setAdapter(adapter);
        }catch(Exception e){
            Log.i("exc",e.toString());
        }
    }
    private  void setAssessmentItems(){
        JSONTokener jsonParser = new JSONTokener(listJsonResult);
        try {
            JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
            JSONArray assessmentArray = (JSONArray) jsonResult.getJSONArray("assessment");
            List<String> sl = new ArrayList<String>();
            for (int i = 0; i < assessmentArray.length(); i++) {
                sl.add(assessmentArray.getJSONObject(i).getString("name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewRoot.getContext(),R.layout.assessment_list_item, R.id.one_assessment,sl);
            assessment.setAdapter(adapter);
        }catch(Exception e){
            Log.i("exc",e.toString());
        }
    }
    private  void setFirstAppraisertemss(){
        JSONTokener jsonParser = new JSONTokener(listJsonResult);
        try {
            JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
            JSONArray firstAppraiserArray = (JSONArray) jsonResult.getJSONArray("firstAppraiser");
            List<String> sl = new ArrayList<String>();
            for (int i = 0; i < firstAppraiserArray.length(); i++) {
                sl.add(firstAppraiserArray.getJSONObject(i).getString("name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewRoot.getContext(),R.layout.first_appraiser_item, R.id.one_first_appraiser,sl);
            firstAppraiser.setAdapter(adapter);
        }catch(Exception e){
            Log.i("exc",e.toString());
        }
    }
    private  void setSecondAppraisertemss(){
        JSONTokener jsonParser = new JSONTokener(listJsonResult);
        try {
            JSONObject jsonResult = (JSONObject) jsonParser.nextValue();
            JSONArray secondAppraiserArray = (JSONArray) jsonResult.getJSONArray("secondAppraiser");
            List<String> sl = new ArrayList<String>();
            for (int i = 0; i < secondAppraiserArray.length(); i++) {
                sl.add(secondAppraiserArray.getJSONObject(i).getString("name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewRoot.getContext(),R.layout.second_appraiser_item, R.id.one_second_appraiser,sl);
            secondAppraiser.setAdapter(adapter);
        }catch(Exception e){
            Log.i("exc",e.toString());
        }
    }
    private void getViewControl(View viewRoot){
        distributor = (EditText)viewRoot.findViewById(R.id.distributor);
        saleman = (Spinner)viewRoot.findViewById(R.id.saleman);
        firstAccessNumber = (EditText)viewRoot.findViewById(R.id.first_assess_number);
        reportMonth = (EditText)viewRoot.findViewById(R.id.report_month);
        contacts = (EditText)viewRoot.findViewById(R.id.contacts);
        contactsPhone = (EditText)viewRoot.findViewById(R.id.contacts_phone);
        firstDistributor = (EditText)viewRoot.findViewById(R.id.first_distributor);
        secondDistributor = (EditText)viewRoot.findViewById(R.id.second_distributor);
        thirdDistributor = (EditText)viewRoot.findViewById(R.id.third_distributor);
        assessment = (Spinner)viewRoot.findViewById(R.id.assessment);
        street = (EditText)viewRoot.findViewById(R.id.street);
        location = (EditText)viewRoot.findViewById(R.id.location);
        assessRemark = (EditText)viewRoot.findViewById(R.id.assessment_remark);
        villageName = (EditText)viewRoot.findViewById(R.id.village_name);
        assessDate = (EditText)viewRoot.findViewById(R.id.assess_date);
        outsideTime = (EditText)viewRoot.findViewById(R.id.outside_time);
        assessPrice = (EditText)viewRoot.findViewById(R.id.assess_price);
        firstAppraiser = (Spinner)viewRoot.findViewById(R.id.fist_appraiser);
        firstAppraiserRemark = (EditText)viewRoot.findViewById(R.id.fist_appraiser_remark);
        secondAppraiser = (Spinner)viewRoot.findViewById(R.id.second_appraiser);
        secondAppraiserRemark = (EditText)viewRoot.findViewById(R.id.second_appraiser_remark);
        reportNumber = (EditText)viewRoot.findViewById(R.id.report_number);
        reportDate = (EditText)viewRoot.findViewById(R.id.report_date);
        reportType = (Spinner)viewRoot.findViewById(R.id.report_type);
        clerkRemark = (EditText)viewRoot.findViewById(R.id.clerk_remark);
        fee = (EditText)viewRoot.findViewById(R.id.fee);
        treasuterRemark = (EditText)viewRoot.findViewById(R.id.treasuter_remark);
        reportButton = (Button)viewRoot.findViewById(R.id.report_button);
    }

}
