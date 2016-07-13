package com.geminno.Activities.setting;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.geminno.Fragment.setting.FragmentUpLoadMysetting;
import com.geminno.Fragment.setting.LoginFragment;
import com.geminno.Utils.ActionBarUtils;

import geminno.com.hiweek_android.R;

public class PersonActivity extends FragmentActivity {
    private LoginFragment loginFragment;
    private FragmentUpLoadMysetting fragmentUpLoadMysetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActionBarUtils.getInstence(this).initStatusbar();
	initActionBar(getActionBar());
	setContentView(R.layout.login_view);
	if (savedInstanceState == null) {
	    loginFragment = new LoginFragment();
	    fragmentUpLoadMysetting = new FragmentUpLoadMysetting();
	}
	int flag = getIntent().getIntExtra("FLAG", 0);
	if (flag == 1) {
	    getSupportFragmentManager().beginTransaction()
		    .replace(R.id.fll, loginFragment).commit();
	} else {
	    getSupportFragmentManager().beginTransaction()
		    .replace(R.id.fll, fragmentUpLoadMysetting).commit();
	}
    }

    private void initActionBar(ActionBar actionBar) {
	// 返回按钮
	actionBar.setDisplayHomeAsUpEnabled(true);
	// 自定义View
	actionBar.setDisplayShowCustomEnabled(false);
	// 返回主页
	actionBar.setDisplayShowHomeEnabled(false);
	// 显示title
	actionBar.setDisplayShowTitleEnabled(true);

	actionBar.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.actionbar));
	// actionBar.setCustomView(R.layout.actionbar_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
		this.finish();
	    } else {
		getSupportFragmentManager().popBackStack();
	    }
	    break;

	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }
}
