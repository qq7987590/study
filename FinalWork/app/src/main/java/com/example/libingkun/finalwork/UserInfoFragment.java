package com.example.libingkun.finalwork;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


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

    private EditText userID;
    private EditText name;
    private RadioGroup sex;
    private EditText phone;
    private EditText email;
    private EditText birthday;
    private EditText IDCard;
    private EditText password;
    private Button editInfo;
    private boolean couldEdit = false;

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
        SharedPreferences msp = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
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
        editInfo.setOnClickListener();
        return rootView;
    }

    public class 
}
