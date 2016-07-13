package com.geminno.Activities.setting;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.geminno.Fragment.setting.FragGuanYu;
import com.geminno.Fragment.setting.FragMyseting;
import com.geminno.Fragment.setting.FragYiJian;
import com.geminno.Fragment.setting.FragmentCollect;
import com.geminno.Utils.ActionBarUtils;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月28日 上午11:42:08
 */
public class MyselfActivity extends FragmentActivity {
    private Fragment fce, fset, yijian, guanyu;
    private TextView actionbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtils.getInstence(this).initStatusbar();
        initActionBar(getActionBar());
        setContentView(R.layout.activity_myself);
        fce = new FragmentCollect();
        fset = new FragMyseting();
        yijian = new FragYiJian();
        guanyu = new FragGuanYu();
        Intent i = getIntent();
        int g = i.getIntExtra("go", -1);
        if (g == 1) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.myselffrg, fce).commit();
        } else if (g == 2) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.myselffrg, fset).commit();
        } else if (g == 3) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.myselffrg, yijian).commit();
        } else if (g == 4) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.myselffrg, guanyu).commit();
        }
    }

    private void initActionBar(ActionBar actionBar) {
        // 返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 自定义View
        actionBar.setDisplayShowCustomEnabled(false);
        // actionBar.setHomeAsUpIndicator(R.drawable.back2);
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
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getFragmentManager().popBackStack();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
