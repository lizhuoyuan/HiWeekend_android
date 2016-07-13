package com.geminno.Fragment.setting;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geminno.com.hiweek_android.R;

public class lianxiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("联系我们");
        View root = inflater.inflate(R.layout.lianxi, null);

        return root;
    }
}
