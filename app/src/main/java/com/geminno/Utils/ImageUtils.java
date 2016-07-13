package com.geminno.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.geminno.Cache.MyImageCache;

import java.net.URL;

import geminno.com.hiweek_android.R;

/**
 * 
 * @ClassName: ImageUtils
 * @Description: 图片加载，处理、工具类，
 * @author: XU
 * @date: 2015年10月19日 上午11:16:33
 */
public class ImageUtils {
	private static ImageUtils imageUtils = null;
	private RequestQueue requestQueue;
	private MyImageCache imageCache;
	private ImageLoader loader;
	private Bitmap bitmap;
	private Context context;

	private ImageUtils() {
		imageCache = new MyImageCache();
	}

	private static class GetClass {
		static ImageUtils imageUtils = new ImageUtils();
	}

	public static ImageUtils getInstence() {
		if (imageUtils == null) {
			imageUtils = GetClass.imageUtils;
		}

		return imageUtils;
	}

	public void loadImageUseAsyncTask(ImageView iv, URL url) {

	}

	/*
	 * 
	 */
	public void setQueue(Context context) {
		this.context = context;
		this.requestQueue = Volley.newRequestQueue(context);
		loader = new ImageLoader(requestQueue, imageCache);

	}

	public void loadImageUseVolley_ImageLoad(ImageView iv, String url) {
		ImageListener iListener = ImageLoader.getImageListener(iv, R.drawable.loading, R.drawable.imagerror);

		loader.get(url, iListener);
	}

	public Bitmap getBitmap(String url) {
		String key = getCacheKey(url);
		bitmap = imageCache.getBitmap(key);
		return bitmap;
	}

	private static String getCacheKey(String url, int maxWidth, int maxHeight) {
		return new StringBuilder(url.length() + 12).append("#W").append(maxWidth).append("#H").append(maxHeight)
				.append(url).toString();
	}

	private String getCacheKey(String url) {
		return new StringBuilder(url.length() + 12).append("#W").append(0).append("#H").append(0).append(url)
				.toString();
	}

	/**
	 * 
	 * @Title: blur
	 * @Description: 对图片进行模糊处理.先裁剪，
	 * @param context
	 * @param bkg
	 * @return
	 * @Author XU
	 */
	@SuppressLint("NewApi")
	public Bitmap blur(Context context, Bitmap bkg) {
		long startMs = System.currentTimeMillis();
		float radius = 4;

		bkg = small(bkg);
		Bitmap bitmap = bkg.copy(bkg.getConfig(), true);

		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);

		bitmap = big(bitmap);
		rs.destroy();
		Log.d("zhangle", "blur take away:" + (System.currentTimeMillis() - startMs) + "ms");
		return bitmap;
	}

	private static Bitmap big(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(2f, 2f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	@SuppressLint("NewApi")
	public Bitmap blurBitmap(Context context, Bitmap bitmap) {

		// Let's create an empty bitmap with the same size of the bitmap we want
		// to blur
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

		// Instantiate a new Renderscript
		RenderScript rs = RenderScript.create(context.getApplicationContext());

		// Create an Intrinsic Blur Script using the Renderscript
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

		// Create the Allocations (in/out) with the Renderscript and the in/out
		// bitmaps
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

		// Set the radius of the blur
		blurScript.setRadius(10.f);

		// Perform the Renderscript
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);

		// Copy the final bitmap created by the out Allocation to the outBitmap
		allOut.copyTo(outBitmap);

		// recycle the original bitmap
		bitmap.recycle();

		// After finishing everything, we destroy the Renderscript.
		rs.destroy();
		System.out.println("模糊处理后：字节数：" + outBitmap.getByteCount());

		return outBitmap;

	}

	@SuppressLint("NewApi")
	public void blur(Context context, Bitmap bkg, View view) {
		float radius = 10;
		float scaleFactor = 8;

		Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = FastBlur.doBlur(bkg, (int) radius, false);
		view.setBackground(new BitmapDrawable(context.getResources(), overlay));
	}

	@SuppressLint("NewApi")
	public void loadImageUseVolley_ImageLoad(final ImageView movie_image, String url, final LinearLayout layout) {
		ImageListener iListener = ImageLoader.getImageListener(movie_image, R.drawable.loading, R.drawable.imagerror);

		loader.get(url, new ImageListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				movie_image.setImageDrawable(context.getResources().getDrawable(R.drawable.imagerror));

			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate) {
				if (response.getBitmap() != null) {
					movie_image.setImageBitmap(response.getBitmap());
					blur(context, response.getBitmap(), layout);

				} else {
					movie_image.setImageDrawable(context.getResources().getDrawable(R.drawable.loading));
				}
			}
		});
	}
}
