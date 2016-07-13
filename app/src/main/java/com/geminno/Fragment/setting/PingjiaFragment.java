package com.geminno.Fragment.setting;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import geminno.com.hiweek_android.R;

public class PingjiaFragment extends Fragment {
    View root;
    EditText et1;
    EditText et2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("评价");
	root = inflater.inflate(R.layout.pingjiawomen, null);
	et1 = (EditText) root.findViewById(R.id.ping_jia_editText1);
	et2 = (EditText) root.findViewById(R.id.ping_jia_editText2);
	Button btn = (Button) root.findViewById(R.id.ping_jia_button1);
	btn.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (TextUtils.isEmpty(et1.getText())) {
		    Toast.makeText(getActivity(), "内容不为空", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(et2.getText())) {
		    Toast.makeText(getActivity(), "电话不为空", Toast.LENGTH_SHORT).show();
		} else {
		    getActivity().finish();
		}
	    }
	});
	return root;
    }
}
