package com.geminno.Adapter.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.NearType;
import com.geminno.Utils.ImageUtils;
import com.geminno.Utils.MyPropertiesUtil;
import com.geminno.hiweek1_0.ThreeActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年11月13日 下午3:18:41 本周末，热门 附近的adapter
 */
public class AllAdapter extends BaseAdapter {
    ArrayList<NearType> nearTypes;
    private LayoutInflater inflater;
    private Context context;
    private LatLng endp, startp;
    private DecimalFormat df;
    private int flag;
    private ImageUtils imageUtils;
    private String url;
    private String newUrl;

    public AllAdapter(Context context, ArrayList<NearType> nearTypes, int flag) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.nearTypes = nearTypes;
        startp = new LatLng(MyApplication.getLat(), MyApplication.getLon());
        df = new DecimalFormat("0.00");
        this.flag = flag;
        imageUtils = ImageUtils.getInstence();
        imageUtils.setQueue(context);
        url = MyPropertiesUtil.getProperties(context).getProperty("url")
                + MyPropertiesUtil.getProperties(context).getProperty("img");
    }

    @Override
    public int getCount() {
        return nearTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class ViewHolder {
        ImageView img;
        TextView tvname, tvaddress, tvnum; // 名，地址，评分/距离
    }

    class ViewHolder2 {
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (flag == ThreeActivity.THE_NEAR) {
            view = dealNear(position, convertView, parent);
        } else {
            view = dealOther(position, convertView, parent);
        }
        return view;
    }

    private View dealOther(int position, View convertView, ViewGroup parent) {
        ViewHolder2 holder2;

        if (convertView == null) {
            holder2 = new ViewHolder2();
            convertView = inflater.inflate(R.layout.item_list, null);

            holder2.textView = (TextView) convertView.findViewById(R.id.tt);
            holder2.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            convertView.setTag(holder2);

        } else {

            holder2 = (ViewHolder2) convertView.getTag();
        }

        if (nearTypes.get(position).getType() == NearType.ACTION) {
            newUrl = url + nearTypes.get(position).getUrl();
        } else {
            newUrl = nearTypes.get(position).getUrl();
        }
        // holder2.imageView.setTag(true);
        holder2.imageView.setTag(position);
        // imageUtils.loadImageUseVolley_ImageLoad(holder2.imageView, newUrl,
        // position);
        new MyImageAsyncTask(holder2.imageView, newUrl, position).execute(null,
                null);
        holder2.textView.setText(nearTypes.get(position).getTitle());

        return convertView;
    }

    private View dealNear(int position, View convertView, ViewGroup parent) {
        String distance;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_three_activity, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tvname = (TextView) convertView.findViewById(R.id.tvname);
            holder.tvaddress = (TextView) convertView.findViewById(R.id.tvadd);
            holder.tvnum = (TextView) convertView.findViewById(R.id.tvnum);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvname.setText(nearTypes.get(position).getTitle());
        holder.tvaddress.setVisibility(View.VISIBLE);
        holder.tvnum.setVisibility(View.VISIBLE);
        holder.tvaddress.setText(nearTypes.get(position).getLocation());
        distance = df.format(nearTypes.get(position).getDis());
        holder.tvnum.setText(distance + "Km");
        if (nearTypes.get(position).getType() == NearType.ACTION) {
            holder.img.setImageResource(R.drawable.zishi);
        }
        if (nearTypes.get(position).getType() == NearType.CINEMA) {
            holder.img.setImageResource(R.drawable.cinema_lable);
        }
        if (nearTypes.get(position).getType() == NearType.MOVIE) {
            holder.img.setImageResource(R.drawable.movie_lable);
        }
        return convertView;
    }

    private class MyImageAsyncTask extends AsyncTask<Void, Void, Void> {
        private ImageView imageView;
        private String url;
        private Bitmap mBitmap;
        private int position;

        public MyImageAsyncTask(ImageView imageView, String url, int position) {
            this.imageView = imageView;
            this.url = url;
            this.position = position;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (Integer.parseInt(imageView.getTag().toString()) == position) {
                imageUtils.loadImageUseVolley_ImageLoad(imageView, url);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

    }
}
