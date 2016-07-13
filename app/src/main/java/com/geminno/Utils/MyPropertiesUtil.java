package com.geminno.Utils;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * 
 * @author 李卓原 创建时间：2015年10月12日 下午2:08:06
 */
public class MyPropertiesUtil {
    private static Properties urlProps;

    public static Properties getProperties(Context c) {
	Properties props = new Properties();
	try {
	    // 方法一：通过activity中的context攻取setting.properties的FileInputStream
	    InputStream in = c.getAssets().open("Url.properties");
	    // 方法二：通过class获取setting.properties的FileInputStream
	    // InputStream in =
	    // PropertiesUtill.class.getResourceAsStream("/assets/
	    // setting.properties "));
	    props.load(in);
	} catch (Exception e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	urlProps = props;
	return urlProps;
    }

}