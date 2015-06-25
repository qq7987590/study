package com.example.libingkun.finalwork;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
 * {@link ManageReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View viewRoot;
    private Activity myActivity;
    private EditText eSearch;
    private ListView reportList;
    private MyHandler handler;
    private String result="";
    private static final int MSG_ERROR = 0;
    private static final int MSG_SUCCESS = 1;




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageReportFragment newInstance(String param1, String param2) {
        ManageReportFragment fragment = new ManageReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ManageReportFragment() {
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
        viewRoot = inflater.inflate(R.layout.fragment_manage_report, container, false);
        //获取Activity
        myActivity = this.getActivity();
        //创建Report列表
        createList();
        return viewRoot;
    }
    private void createList(){
        //获得列表view
        reportList = (ListView)viewRoot.findViewById(R.id.repoterList);
        eSearch = (EditText)viewRoot.findViewById(R.id.etSearch);
        //创建Handler
        createHandler();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //请求地址
                String target = "http://" + getString(R.string.server_host) + "/Home/Report/getAllReport";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpRequst = new HttpPost(target);
                //将要传的值保存到List集合中
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("param","post"));
                SharedPreferences sp = myActivity.getSharedPreferences("user",myActivity.MODE_PRIVATE);
                params.add(new BasicNameValuePair("user_name",sp.getString("name","")));
                params.add(new BasicNameValuePair("user_type",sp.getString("type","")));
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
    private void createHandler(){
        handler = new MyHandler();
    }
    class MyHandler extends Handler {
        private ReportListListener listener = new ReportListListener();
        private List<Map<String, Object>> listItems;
        private SimpleAdapter adapter;
        private ArrayList<String> reportStat;
        private ArrayList<String> firstAssessNumber;
        private ArrayList<String> reportMonth;
        private ArrayList<String> assessment;
        private ArrayList<String> location;
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == MSG_ERROR){
                Log.i("res", result);
                Toast.makeText(myActivity, "获取不到用户数据!", Toast.LENGTH_SHORT).show();
            }
            if(msg.what == MSG_SUCCESS){
                Log.i("res",result);
                initReportListByResult();
            }

        }
        private void initReportListByResult() {
            reportStat = new ArrayList<String>();
            firstAssessNumber = new ArrayList<String>();
            reportMonth = new ArrayList<String>();
            assessment = new ArrayList<String>();
            location = new ArrayList<String>();
            try {
                JSONTokener jsonParser = new JSONTokener(result);
                JSONArray jsonResult = (JSONArray) jsonParser.nextValue();
                for (int i = 0; i < jsonResult.length(); i++) {
                    JSONObject jo = jsonResult.getJSONObject(i);
                    reportStat.add(jo.getString("stat"));
                    firstAssessNumber.add(jo.getString("first_assess_number"));
                    reportMonth.add(jo.getString("report_month"));
                    assessment.add(jo.getString("assessment"));
                    location.add(jo.getString("location"));
                }
            }
            catch (Exception e){
                Log.i("exception",e.toString());
            }

            listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < firstAssessNumber.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("report_stat", reportStat.get(i));
                map.put("first_assess_number", firstAssessNumber.get(i));
                map.put("report_month", reportMonth.get(i));
                map.put("assessment", assessment.get(i));
                map.put("location", location.get(i));
                listItems.add(map);
            }
            adapter = new MyAdapter(viewRoot.getContext(), listItems,
                    R.layout.report_list_item, new String[]{"report_stat","first_assess_number","report_month","assessment", "location"}, new int[]{
                    R.id.report_stat,R.id.first_assess_number,R.id.report_month,R.id.assessment, R.id.location});
            reportList.setAdapter(adapter);
            //增加listener
            reportList.setOnItemClickListener(listener);
            set_eSearch_TextChanged();

        }

        class MyAdapter extends SimpleAdapter{
            private int[] colors = { Color.BLUE, Color.RED,Color.BLUE, Color.RED};
            public MyAdapter(Context context,
                                    List<? extends Map<String, ?>> data, int resource,
                                    String[] from, int[] to) {
                super(context, data, resource, from, to);
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView stat = (TextView)view.findViewById(R.id.report_stat);
                switch (stat.getText().toString()){
                    case "0":
                        view.setBackgroundColor(Color.rgb(186,255,173));
                        break;
                    case "1":
                        view.setBackgroundColor(Color.rgb(228,255,153));
                        break;
                    case "2":
                        break;
                }
                return view;
            }
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
                    String data = eSearch.getText().toString();
                    getmDataSub(data);
                }

                private void  getmDataSub(String data)
                {

                    listItems.clear();
                    int length = firstAssessNumber.size();
                    for(int i = 0; i < length; ++i){
                        if(firstAssessNumber.get(i).contains(data) || reportMonth.get(i).contains(data) || assessment.get(i).contains(data)|| location.get(i).contains(data)){
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("report_stat", reportStat.get(i));
                            map.put("first_assess_number", firstAssessNumber.get(i));
                            map.put("report_month", reportMonth.get(i));
                            map.put("assessment", assessment.get(i));
                            map.put("location", location.get(i));
                            listItems.add(map);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });

        }
        //继承listener类
        class ReportListListener implements AdapterView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i =0;i<reportList.getChildCount();i++){
                    if(position == i) {
                        TextView uidText = (TextView)reportList.getChildAt(i).findViewById(R.id.first_assess_number);
                        String uidString = uidText.getText().toString();
                        FragmentManager fragmentManager = getFragmentManager();
                        Fragment thisFragment = EditReportFragment.newInstance(uidString,"");
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
