package com.geminno.Fragment.setting;

/**
 * @author 李卓原
 * 创建时间：2015年11月2日 上午10:57:12
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.User;
import com.geminno.Service.InternetService;
import com.geminno.Utils.AndroidUtil;
import com.geminno.Utils.ImageUtils;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import geminno.com.hiweek_android.R;

public class FragmentUpLoadMysetting extends Fragment {
    User user;
    private ImageView img;
    private Button btnUpload;
    private HttpUtils httpUtils;
    private String[] items = {"拍照", "相册"};
    private String title = "选择照片";
    private String fileName;
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    SharedPreferences sharedPreferences;

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        fileName = sdf.format(date) + ".png";
        return fileName;
    }

    private RadioGroup rg;
    View root;
    private RadioButton r1, r2;
    String u_sex;
    EditText etname, etpaynum;
    TextView tvtel;

    /*
     * Handler handler = new Handler() { public void handleMessage(Message msg)
     * { if (msg.what == 1) { etname.setText(user.getU_name());
     * etpaynum.setText((int)user.getU_paynum());
     * tvtel.setText(user.getU_tel()); if (user.getU_sex().equals("女")) {
     * r2.setChecked(true); } else { r1.setChecked(true); } } }; };
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("修改信息");
        root = inflater.inflate(R.layout.person_upload2, null);
        img = (ImageView) root.findViewById(R.id.main_img);
        sharedPreferences = getActivity().getSharedPreferences("hiweek",
                Activity.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("login", false)) {
            String Url = sharedPreferences.getString("user_picurl", "");
            if (!TextUtils.isEmpty(Url)) {
                ImageUtils.getInstence().setQueue(getActivity());
                ImageUtils.getInstence().loadImageUseVolley_ImageLoad(img, Url);
            } else {
                img.setImageResource(R.drawable.defaultportrait);

            }
        } else {
            img.setImageResource(R.drawable.defaultportrait);

        }

        btnUpload = (Button) root.findViewById(R.id.btnsub);
        btnUpload.setOnClickListener(clickListener);
        img.setOnClickListener(clickListener);
        init();
        return root;
    }

    private void init() {
        rg = (RadioGroup) root.findViewById(R.id.rgsex);
        r1 = (RadioButton) root.findViewById(R.id.radio1);
        r2 = (RadioButton) root.findViewById(R.id.radio2);
        rg.setOnCheckedChangeListener(mChangeRadio);
        tvtel = (TextView) root.findViewById(R.id.tvtel);
        etname = (EditText) root.findViewById(R.id.etname);
        etpaynum = (EditText) root.findViewById(R.id.etpaynum);
        HttpUtils http = new HttpUtils();
        http.send(HttpMethod.GET, MyPropertiesUtil.getProperties(getActivity())
                .get("url")
                + "/HiWeek/servlet/SelectUserById?u_id="
                + MyApplication.getU_id(), new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                user = gson.fromJson(arg0.result, User.class);
                etname.setText(user.getU_name());
                etpaynum.setText(user.getU_paynum() + "");
                tvtel.setText(user.getU_tel());
                if (user.getU_sex().equals("女")) {
                    r2.setChecked(true);
                } else {
                    r1.setChecked(true);
                }
                // handler.sendEmptyMessage(1);
            }
        });

    }

    private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if (checkedId == r1.getId()) {
                u_sex = r1.getText().toString();
            } else if (checkedId == r2.getId()) {
                // 把mRadio2的内容传到mTextView1
                u_sex = r2.getText().toString();
            }
        }
    };

    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_img:
                    AlertDialog.Builder dialog = AndroidUtil.getListDialogBuilder(
                            getActivity(), items, title, dialogListener);
                    dialog.show();
                    break;
                case R.id.btnsub:
                    upload();
                    break;

                default:
                    break;
            }

        }
    };

    // 上传文件到服务器
    protected void upload() {
        httpUtils = new HttpUtils(10000);
        RequestParams params = new RequestParams();
        params.addBodyParameter("u_id", MyApplication.getU_id() + "");
        params.addBodyParameter("u_sex", u_sex);
        //URLDecoder.decode(etname.getText().toString(), "UTF-8");
        params.addBodyParameter("u_name", etname.getText().toString());
        params.addBodyParameter("u_paynum", etpaynum.getText().toString());
        params.addBodyParameter(tempFile.getPath().replace("/", ""), tempFile);
        Properties p = MyPropertiesUtil.getProperties(getActivity());
        String URL = p.get("url") + "/HiWeek/servlet/UploadServlet";
        System.out.println(tempFile);
        httpUtils.send(HttpMethod.POST, URL, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {
                        Toast.makeText(getActivity(), "上传失败，检查一下服务器地址是否正确",
                                Toast.LENGTH_SHORT).show();
                        Log.i("MainActivity", e.getExceptionCode() + "====="
                                + msg);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Toast.makeText(getActivity(),
                                "上传成功，马上去服务器看看吧！" + responseInfo.result,
                                Toast.LENGTH_SHORT).show();
                        // 修改成功，更改本地数据
                        //实例化SharedPreferences.Editor对象（第二步）
                        Editor imgeditor = sharedPreferences.edit();
                        imgeditor.putInt("user_payNum",
                                Integer.parseInt(etpaynum.getText().toString()));

                        imgeditor.putString("user_picurl", MyPropertiesUtil.getProperties(getActivity()).getProperty("url")
                                + "/HiWeek/Image/" + fileName);
                        imgeditor.commit();
                        MyApplication.setU_name(etname.getText().toString());
                        MyApplication.setPic_url("http://" + InternetService.IP
                                + ":8080/HiWeek/Image/" + fileName);
                        MyApplication.setU_paynum(Integer.parseInt(etpaynum
                                .getText().toString()));
                        Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(getActivity())
                                .edit();

                        editor.putInt("user_payNum",
                                Integer.parseInt(etpaynum.getText().toString()));

                        editor.putString("user_picurl", "http://"
                                + InternetService.IP + ":8080/HiWeek/Image/"
                                + fileName);
                        editor.apply();
                        /*BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
                        // 加载网络图片
                        Log.e("TAG", "init: " + Url);
                        bitmapUtils.display(img, Url);
                        getActivity().findViewById(R.id.user_img)*/
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                        Log.i("MainActivity", "====upload_error====="
                                + responseInfo.result);
                    }
                });
    }

    private android.content.DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    // 调用拍照
                    startCamera(dialog);
                    break;
                case 1:
                    // 调用相册
                    startPick(dialog);
                    break;

                default:
                    break;
            }
        }
    };

    // 调用系统相机
    protected void startCamera(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, PHOTO_CARMERA);
    }

    // 调用系统相册
    protected void startPick(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_CARMERA:
                startPhotoZoom(Uri.fromFile(tempFile), 300);
                break;
            case PHOTO_PICK:
                if (null != data) {
                    startPhotoZoom(data.getData(), 300);
                }
                break;
            case PHOTO_CUT:
                if (null != data) {
                    setPicToView(data);
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 调用系统裁剪
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CUT);
    }

    // 将裁剪后的图片显示在ImageView上
    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (null != bundle) {
            final Bitmap bmp = bundle.getParcelable("data");
            img.setImageBitmap(bmp);
            saveCropPic(bmp);
            Log.i("MainActivity", tempFile.getAbsolutePath());
        }
    }

    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(tempFile);
            fis.write(baos.toByteArray());
            fis.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
