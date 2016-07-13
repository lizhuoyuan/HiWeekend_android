package com.geminno.View;

/**
 * @author 李卓原
 * 创建时间：2015年10月22日 下午1:22:08 
 * 
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.lidroid.xutils.bitmap.core.AsyncDrawable;

/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。
 * 
 * @author Alan
 */
/**
 * 自定义
 * 
 * @author 李卓原 创建时间：2015年10月22日 下午2:53:25
 */
public class RoundImageView extends ImageView {

    public RoundImageView(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    public RoundImageView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

	Drawable drawable = getDrawable();

	if (drawable == null) {
	    return;
	}

	if (getWidth() == 0 || getHeight() == 0) {
	    return;
	}
	Bitmap b = null;
	if (drawable instanceof BitmapDrawable) {
	    b = ((BitmapDrawable) drawable).getBitmap();
	} else if (drawable instanceof AsyncDrawable) {
	    b = Bitmap
		    .createBitmap(
			    getWidth(),
			    getHeight(),
			    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				    : Bitmap.Config.RGB_565);
	    Canvas canvas1 = new Canvas(b);
	    // canvas.setBitmap(bitmap);
	    drawable.setBounds(0, 0, getWidth(), getHeight());
	    drawable.draw(canvas1);
	}
	Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

	int w = getWidth(), h = getHeight();

	Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
	canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
	Bitmap sbmp;
	if (bmp.getWidth() != radius || bmp.getHeight() != radius)
	    sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
	else
	    sbmp = bmp;
	Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
		Config.ARGB_8888);
	Canvas canvas = new Canvas(output);

	final int color = 0xffa19774;
	final Paint paint = new Paint();
	final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

	paint.setAntiAlias(true);
	paint.setFilterBitmap(true);
	paint.setDither(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(Color.parseColor("#BAB399"));
	canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
		sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	canvas.drawBitmap(sbmp, rect, rect, paint);

	return output;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	int size;
	size = widthSize > heightSize ? heightSize : widthSize;
	setMeasuredDimension(size, size);

    }

}