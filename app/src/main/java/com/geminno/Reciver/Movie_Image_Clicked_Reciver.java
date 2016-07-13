package com.geminno.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Movie_Image_Clicked_Reciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	Toast.makeText(context, intent.getExtras().getString("movieID"), 0)
		.show();
    }

}
