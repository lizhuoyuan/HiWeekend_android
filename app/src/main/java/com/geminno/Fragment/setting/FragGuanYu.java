package com.geminno.Fragment.setting;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geminno.com.hiweek_android.R;

public class FragGuanYu extends Fragment {

    public FragGuanYu() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("关于");
	View root = inflater.inflate(R.layout.guanyu, null);
	return root;
    }

}
