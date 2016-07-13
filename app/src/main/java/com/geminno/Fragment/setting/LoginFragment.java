package com.geminno.Fragment.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.Constants;
import com.geminno.Bean.User;
import com.geminno.Utils.MyPropertiesUtil;
import com.geminno.hiweek1_0.MainActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import geminno.com.hiweek_android.R;

public class LoginFragment extends Fragment implements OnClickListener {
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAIL = -1;
    private static final int NO_SUCH_USER = 0;
    private static final int SYSTEM_ERROR = 3;
    private EditText etname, etpass;
    private Button bnlogin;
    private TextView tvregiest;
    private int flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ((TextView)
        // getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_title)).setText("登录");
        getActivity().getActionBar().setTitle("登录");
        View view = inflater.inflate(R.layout.activity_login, null);
        etname = (EditText) view.findViewById(R.id.username_edit);
        etpass = (EditText) view.findViewById(R.id.password_edit);
        bnlogin = (Button) view.findViewById(R.id.Login_button);
        tvregiest = (TextView) view.findViewById(R.id.register_link);
        bnlogin.setOnClickListener(this);
        tvregiest.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Login_button:
                if (!PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getBoolean(Constants.USER_STATUS, false)) {
                    if (changeAccess()) {
                       /* new myAsyncTask().execute(new String[]{
                                etname.getText().toString(),
                                etpass.getText().toString()});*/
                        xutils();
                    }
                } else {
                    Toast.makeText(getActivity(), "您已经登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_link:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fll, new RegisterFragment())
                        .addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

    private void xutils() {

        HttpUtils http = new HttpUtils();
        String ip = MyPropertiesUtil.getProperties(getActivity()).getProperty("url");
        String url = ip + "/HiWeek/servlet/client/UserLoginServlet";
        http.send(HttpRequest.HttpMethod.GET,
                url + "?u_tel=" + etname.getText().toString() + "&u_pwd="
                        + etpass.getText().toString(),
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        if ("0".equals(result)) {
                            flag = NO_SUCH_USER;
                        } else if ("-1".equals(result)) {
                            flag = LOGIN_FAIL;
                        } else {
                            flag = LOGIN_SUCCESS;
                            Gson gson = new Gson();
                            User user = gson.fromJson(result, User.class);
                            Log.e("TAG", "onSuccess: " + user.toString());
                            saveInSP(user);
                            saveInApp(user);
                        }
                        onPostExecute();
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("TAG", "onFailure: " + msg);
                        Toast.makeText(getActivity(), "服务器链接失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }

    private void onPostExecute() {
        switch (flag) {
            case LOGIN_FAIL:
                Toast.makeText(getActivity(), "密码输入错误", Toast.LENGTH_SHORT).show();
                break;
            case LOGIN_SUCCESS:
                // 记录登陆状态
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit().putBoolean(Constants.USER_STATUS, true).apply();
                MainActivity.login = true;
                Intent intent = new Intent();
                intent.putExtra("result", "欢迎回来");
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                break;
            case NO_SUCH_USER:
                Toast.makeText(getActivity(), "此用户不存在", Toast.LENGTH_SHORT).show();
                break;
            case SYSTEM_ERROR:
                Toast.makeText(getActivity(), "系统异常", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private boolean changeAccess() {
        if (TextUtils.isEmpty(etname.getText().toString())) {
            Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etpass.getText().toString())) {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

   /* private class myAsyncTask extends AsyncTask<String, Void, Void> {
        private int flag;

        @Override
        protected Void doInBackground(String... params) {
            URL url;
            try {
                url = new URL(InternetService.ServletURL
                        + "UserLogin?u_tel=" + params[0] + "&u_pwd="
                        + params[1]);

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        stringBuilder.append(str);
                    }
                    String result = stringBuilder.toString();
                    if ("0".equals(result)) {
                        flag = NO_SUCH_USER;
                    } else if ("-1".equals(result)) {
                        flag = LOGIN_FAIL;
                    } else {
                        flag = LOGIN_SUCCESS;
                        Gson gson = new Gson();
                        User user = gson.fromJson(result, User.class);
                        saveInSP(user);
                        saveInApp(user);
                    }
                } else {
                    flag = SYSTEM_ERROR;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                flag = SYSTEM_ERROR;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            switch (flag) {
                case LOGIN_FAIL:
                    Toast.makeText(getActivity(), "密码输入错误", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_SUCCESS:
                    // 记录登陆状态
                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .edit().putBoolean(Constants.USER_STATUS, true).apply();
                    Intent intent = new Intent();
                    intent.putExtra("result", "欢迎回来");
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                    break;
                case NO_SUCH_USER:
                    Toast.makeText(getActivity(), "此用户不存在", Toast.LENGTH_SHORT).show();
                    break;
                case SYSTEM_ERROR:
                    Toast.makeText(getActivity(), "系统异常", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }*/

    private void saveInApp(User user) {
        MyApplication.setCredit(user.getU_credit());
        MyApplication.setU_id(user.getU_id());
        MyApplication.setU_paynum(user.getU_paynum());
        MyApplication.setPic_url(user.getU_pic());
        MyApplication.setYue(user.getU_yue());
        MyApplication.setU_name(user.getU_name());
    }

    private void saveInSP(User user) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("hiweek",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor imgeditor = mySharedPreferences.edit();
        Editor editor = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).edit();
        imgeditor.putBoolean("login", true);
        imgeditor.putString("user_picurl", MyPropertiesUtil.getProperties(
                getActivity()).getProperty("url") + "/HiWeek/Image/" + user.getU_pic());
        imgeditor.commit();
        editor.putInt("user_id", user.getU_id());
        editor.putString("user_tel", user.getU_tel());
        editor.putInt("user_payNum", user.getU_paynum());
        editor.putFloat("user_YuE", (float) user.getU_yue());
        editor.putInt("user_credit", user.getU_credit());
        editor.putString("user_name", user.getU_name());
        editor.putString("user_picurl", MyPropertiesUtil.getProperties(getActivity()).getProperty("url")
                + "/HiWeek/Image/" + user.getU_pic());
        editor.apply();
    }
}
