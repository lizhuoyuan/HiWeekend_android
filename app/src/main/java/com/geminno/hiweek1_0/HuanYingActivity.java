package com.geminno.hiweek1_0;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原
 *         创建时间：2015年10月12日 下午2:02:03
 */
public class HuanYingActivity extends Activity {
    ViewPager vp;
    List<View> views;//集合存放viewpager中显示的view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanying_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        vp = (ViewPager) findViewById(R.id.viewpager);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        views = new ArrayList<View>();
        //将xml转换成view
        LayoutInflater inflater = LayoutInflater.from(this);
        View v1 = inflater.inflate(R.layout.view1, null);
        View v2 = inflater.inflate(R.layout.view2, null);
        View v3 = inflater.inflate(R.layout.view3, null);
        views.add(v1);
        views.add(v2);
        views.add(v3);
        v3.findViewById(R.id.jinru).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HuanYingActivity.this, MainActivity.class);
                startActivity(i);
                HuanYingActivity.this.finish();
            }
        });
        MyPageAdapter adapter = new MyPageAdapter(views);
        vp.setAdapter(adapter);
        //修改后使下次启动不进入欢迎页面直接进入主界面
        SharedPreferences preferences = getSharedPreferences(
                "first_pref", MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.huanying, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        /*if (id==R.id.imageView3){
			this.finish();
			//getFragmentManager().beginTransaction().replace(R.id.huanying, new FragmentA()).commit();
			startActivity(new Intent(this,MainActivity.class));
		}*/
        return super.onOptionsItemSelected(item);
    }

    class MyPageAdapter extends PagerAdapter {
        List<View> views;

        public MyPageAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            //super.destroyItem(container, position, object);
            System.out.println("销毁一个view，位置：" + position);
            container.removeView(views.get(position));
        }

        //创建一个view(Object)，将view放到指定的容器中
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            System.out.println("创建一个view,位置" + position);
            View v = views.get(position);
            container.addView(v);
            return v;
        }

    }

}

