package com.geminno.Fragment.setting;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.geminno.Bean.User;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import geminno.com.hiweek_android.R;

public class RegisterFragment extends Fragment implements OnClickListener,
        Callback {
    public static final int MSG_CREATE_RESULT = 1;
    private EditText u_pwd1;
    private EditText u_tel;
    private EditText u_name;
    private RadioGroup u_sex;
    private EditText u_pwd2;
    private EditText pay_pwd;
    private Button btnSubmit;
    private Button btnReset;
    private ProgressDialog progress;
    private Handler handler;
    private User user;

    public RegisterFragment() {
        handler = new Handler(this);
        user = new User();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ((TextView)
        // getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_title)).setText("注册");
        getActivity().getActionBar().setTitle("注册");
        View view = inflater.inflate(R.layout.activity_resgist, null);

        u_name = (EditText) view.findViewById(R.id.new_username);
        u_pwd1 = (EditText) view.findViewById(R.id.new_password_1);
        u_pwd2 = (EditText) view.findViewById(R.id.new_password_2);
        u_sex = (RadioGroup) view.findViewById(R.id.new_radio_group_gender);
        u_tel = (EditText) view.findViewById(R.id.new_phone);
        pay_pwd = (EditText) view.findViewById(R.id.pay_pwd);
        btnSubmit = (Button) view.findViewById(R.id.new_btn_submit);
        btnReset = (Button) view.findViewById(R.id.new_btn_reset);
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_btn_submit:
                handleCreateAccount();
                break;
            case R.id.new_btn_reset:
                handleReset();
                break;
        }
    }

    private void handleReset() {
        u_name.setText("");
        u_pwd1.setText("");
        u_pwd2.setText("");
        ((RadioButton) (u_sex.getChildAt(0))).setChecked(true);
        u_tel.setText("");
        pay_pwd.setText("");

    }

    private void handleCreateAccount() {
        if (TextUtils.isEmpty(u_name.getText().toString())) {
            Toast.makeText(getActivity(), "用户名不正确，请重新输入", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        user.setU_name(u_name.getText().toString());

        int pwdResult = checkPassword();
        if (pwdResult == 1) {
            Toast.makeText(getActivity(), "两次输入的密码不一致，请确认！", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (pwdResult == 2) {
            Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        user.setU_pwd(u_pwd1.getText().toString());

        if (TextUtils.isEmpty(u_tel.getText().toString())
                || u_tel.getText().toString().length() != 11) {
            Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        user.setU_tel(u_tel.getText().toString());

        if (TextUtils.isEmpty(pay_pwd.getText().toString())) {
            Toast.makeText(getActivity(), "支付密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        user.setU_paynum(Integer.parseInt(pay_pwd.getText().toString()));
        createAccount();
    }

    private void createAccount() {

        // TODO Auto-generated method stub
        progress = new ProgressDialog(getActivity());
        progress.setTitle("注册中。。。。。");
        progress.show();
        new Thread(new Runnable() {
            String url = MyPropertiesUtil.getProperties(getActivity()).getProperty("url")
                    + "/HiWeek/servlet/client/"  + "UserRegister";

            @Override
            public void run() {
                try {
                    // 封装成对象
                    Gson gson = new Gson();
                    String jsonString = URLEncoder.encode(gson.toJson(user),
                            "UTF-8");
                    HttpURLConnection connection = (HttpURLConnection) new URL(
                            url).openConnection();
                    // 设置连接属性
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonString.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(connection
                                        .getInputStream()));
                        Message msg = new Message();
                        msg.arg1 = Integer.parseInt(reader.readLine());
                        handler.sendMessage(msg);
                        reader.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int checkPassword() {
        String pwd1 = u_pwd1.getText().toString();
        String pwd2 = u_pwd2.getText().toString();
        if (!pwd1.equals(pwd2)) {
            return 1;
        } else if (TextUtils.isEmpty(pwd1)) {
            return 2;
        } else {
            return 0;
        }
    }

    private boolean checkUsername() {
        String username = u_name.getText().toString();
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        return true;
    }

    // 用户注册Servlet,注册成功返回>0,注册失败返回-1，用户已存在返回0；
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case -1:
                Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                break;
            case 0:
                Toast.makeText(getActivity(), "用户已存在", Toast.LENGTH_SHORT).show();

                break;

            default:
                Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fll, new LoginFragment()).commit();

                break;
        }
        progress.dismiss();

        return true;
    }
}
