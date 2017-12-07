package com.junhsue.ksee.utils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.os.Build;
import android.os.Debug;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.junhsue.ksee.common.Trace;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class BitmapUtil {

	/**
	 * 保持长宽比缩小Bitmap
	 * @param bitmap
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();
		// no need to resize
		if (originWidth < maxWidth && originHeight < maxHeight) {
			return bitmap;
		}
		int width = originWidth;
		int height = originHeight;
		if (originWidth > maxWidth) {
			width = maxWidth;
			double i = originWidth * 1.0 / maxWidth;
			height = (int) Math.floor(originHeight / i);
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		}
		if (height > maxHeight) {
			height = maxHeight;
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		}
		return bitmap;
	}

	public static Bitmap loadBitmap(String path, int mWidth, int mHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = null;
		int be = 1;
		try {
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
			be = (int) Math.max(options.outWidth / mWidth, options.outHeight
					/ mHeight);
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
		} catch (OutOfMemoryError e) {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			options.inSampleSize = be * 2;
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
						null, options);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			Trace.e(e.toString());
		}
		return bitmap;
	}
	
	public static BitmapDrawable loadDrawable(String path){
		return new BitmapDrawable(BitmapFactory.decodeFile(path));
	}

	public static Bitmap loadBitmap(String path, int mWidth, int mHeight,
									boolean isNeedToRotate) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = null;
		int be = 1;
		int needToRotate = 0;
		try {
			if (isNeedToRotate) {
				ExifInterface exif = new ExifInterface(path);
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, -1);
				if (orientation != -1) {
					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						needToRotate = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						needToRotate = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						needToRotate = 270;
						break;
					}
				}
			}
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
			be = (int) Math.max(options.outWidth / mWidth, options.outHeight
					/ mHeight);
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
		} catch (OutOfMemoryError e) {
			Trace.d(e.toString());
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			options.inSampleSize = be * 2;
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
						null, options);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			Trace.e(e.toString());
		}
		if (bitmap != null && isNeedToRotate && needToRotate != 0) {
			Matrix tempMatrix = new Matrix();
			tempMatrix.postRotate(needToRotate);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), tempMatrix, false);
		}
		return bitmap;
	}

	public static void compressBitmap(String path, Bitmap bitmap, int quality, long attachmentMaxSize) {
		BufferedOutputStream bos = null;
		File mFile = new File(path);
		try {
			if (mFile.exists()) {
				mFile.delete();
				mFile.createNewFile();
			}
			bos = new BufferedOutputStream(new FileOutputStream(path));
			if (bitmap != null && bos != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
				if (mFile.exists() && mFile.length() > attachmentMaxSize) {
					compressBitmap(path, bitmap, quality/2, attachmentMaxSize);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int[] getBitmapScale(String path) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null,
					options);
			return new int[] { options.outWidth, options.outHeight };
		} catch (FileNotFoundException e) {
			return new int[]{0,0};
		}
	}

	/**
	 * 图片压缩
	 * @param filePath
	 * @return
     */

	public Bitmap bitmapCompress(String filePath){
		Bitmap bitmap=null;
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		newOpts.inSampleSize = 10;
		return bitmap = BitmapFactory.decodeFile(filePath, newOpts);
	}


	public static byte[] bitmap2Bytes(Bitmap bm, boolean isRecycle) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		if (isRecycle) {
			bm.recycle();
		}
		return baos.toByteArray();
	} 
	
	/**
	 * returns the bytesize of the give bitmap
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static int sizeOf(Bitmap data) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			return data.getRowBytes() * data.getHeight();
		} else {
			return data.getByteCount();
		}
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			// Single color bitmap will be created of 1x1 pixel
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}


	public static Bitmap getMutableBitmap(Resources resources, int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inMutable = true;
		return BitmapFactory.decodeResource(resources, resId, options);
	}

//	public static void testPrintingBitmapSize(int drawable, String tag){
//		Drawable transDrawable = ResourceReader.transDrawable(drawable);
//		Bitmap bitmap = ((BitmapDrawable)transDrawable).getBitmap();
//		int size = sizeOf(bitmap) / 1024;
//		Trace.e("Bmp [" + tag + "] size in memory: " + TextUtil.getFormatNumberWith1Digit(size) + " KB!");
//	}

	public static void logHeap() {
		  Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
		  Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
		  Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);

			Trace.e("debug. =================================");
			Trace.e("debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
			Trace.e("debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
    }

		/**
		 * 将图片转换成字符串
		 * @param bitmap ：图片
		 * @return String字符串
		 */
		public static String converBitmapToString(Bitmap bitmap){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String string =  null;
			bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
			byte[] buffer = baos.toByteArray();
			string = Base64.encodeToString(buffer,Base64.DEFAULT);
			return string;
		}

		/**
		 * 将字符串转成图片
		 * @param string ：字符串
		 * @return Bitmap图片
		 */
		public static Bitmap converStringToBitmap(String string){
			Bitmap bitmap = null;
			try {
				byte[] buffer = Base64.decode(string,Base64.DEFAULT);
				bitmap = BitmapFactory.decodeByteArray(buffer,0,buffer.length);
				return bitmap;
			}catch (Exception e){
				return null;
			}
		}

	public static  Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置采样率

		newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		//其实是无效的,大家尽管尝试
		return bitmap;
	}

		public static Bitmap displayBitmap(View view,Bitmap bitmap){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG,50,baos);
			byte[] buffer = baos.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(buffer,0,buffer.length);
			int viewW = view.getWidth();
			int viewH = view.getHeight();
			int bitW = bitmap.getWidth();
			int bitH = bitmap.getHeight();
			int scale = Math.max((viewW/bitW),(viewH/bitH));
			Matrix matrix = new Matrix();
			matrix.postScale(scale,scale);
//			Bitmap b = Bitmap.createBitmap(bitmap,0,0,100,100,matrix,true);
			return bitmap;
		}


	public static byte[] weChatBitmapToByteArray(Bitmap bmp, boolean needRecycle) {

		// 首先进行一次大范围的压缩
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
		float zoom = (float)Math.sqrt(32 * 1024 / (float)output.toByteArray().length); //获取缩放比例

		// 设置矩阵数据
		Matrix matrix = new Matrix();
		matrix.setScale(zoom, zoom);

		// 根据矩阵数据进行新bitmap的创建
		Bitmap resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

		output.reset();

		resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

		// 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
		while(output.toByteArray().length > 30 * 1024){
			matrix.setScale(0.9f, 0.9f);//每次缩小 1/10

			resultBitmap = Bitmap.createBitmap(
					resultBitmap, 0, 0,
					resultBitmap.getWidth(), resultBitmap.getHeight(), matrix,true);

			output.reset();
			resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		}
		if (needRecycle) {
			resultBitmap.recycle();
		}
		return output.toByteArray();
	}

	/**
	 * 图片压缩
	 * @param resultBitmap
	 * @param needRecycle
	 * @return
	 */
	public static byte[] huoHuaBitmapToByteArray(Bitmap resultBitmap, boolean needRecycle) {

		// 首先进行一次大范围的压缩
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		float zoom = (float)Math.sqrt(32 * 1024 / (float)output.toByteArray().length); //获取缩放比例

		// 设置矩阵数据
		Matrix matrix = new Matrix();
		matrix.setScale(zoom, zoom);

		// 根据矩阵数据进行新bitmap的创建
		resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

		output.reset();

		resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

		// 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
		while(output.toByteArray().length > 30 * 1024){
			matrix.setScale(0.9f, 0.9f);//每次缩小 1/10

			resultBitmap = Bitmap.createBitmap(
					resultBitmap, 0, 0,
					resultBitmap.getWidth(), resultBitmap.getHeight(), matrix,true);

			output.reset();
			resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		}
		if (needRecycle) {
			resultBitmap.recycle();
		}
		return output.toByteArray();
	}


	/**
	 * 发帖图片压缩
	 * @param path
	 * @param needRecycle
	 * @return
	 */
	public static byte[] huoHuaStringToByteArray(String path, boolean needRecycle) {
		Bitmap resultBitmap = getBitmapFromSDCard(path, 800, 800);
		// 首先进行一次大范围的压缩
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		float zoom = (float)Math.sqrt(320 * 1024 / (float)output.toByteArray().length); //获取缩放比例

		// 设置矩阵数据
		Matrix matrix = new Matrix();
		matrix.setScale(zoom, zoom);

		// 根据矩阵数据进行新bitmap的创建
		resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

		output.reset();

		resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

		// 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
		while(output.toByteArray().length > 300 * 1024){
			matrix.setScale(0.9f, 0.9f);//每次缩小 1/10

			resultBitmap = Bitmap.createBitmap(
					resultBitmap, 0, 0,
					resultBitmap.getWidth(), resultBitmap.getHeight(), matrix,true);

			output.reset();
			resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
		}
		if (needRecycle) {
			resultBitmap.recycle();
		}
		return output.toByteArray();
	}

	/**
	 * 从SD卡中获取图片并且比例压缩
	 * @param path 路径
	 * @param mHeight 自定义高度
	 * @param mWidth 自定义宽度
	 * @return
	 */
	public static Bitmap getBitmapFromSDCard(String path, int mHeight, int mWidth) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		//计算比例值
		options.inSampleSize = calculateInSampleSize(options,mHeight,mWidth);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 计算压缩比例值inSampleSize
	 * @param options 压缩的参数设置
	 * @param mHeight 想要的高度
	 * @param mWidth 想要的宽度
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int mHeight, int mWidth)
	{
		//原尺寸大小
		int yHeight = options.outHeight;
		int yWidth = options.outWidth;

		int inSampleSize = 1;
		//如果宽度大的话根据宽度固定大小缩放
		if (yWidth > yHeight && yWidth > mWidth) {
			inSampleSize = (int) (yWidth / mWidth);
		}
		//如果高度高的话根据宽度固定大小缩放
		else if (yWidth < yHeight && yHeight > mHeight)
		{
			inSampleSize = (int) (yHeight / mHeight);
		}
		if (inSampleSize <= 0)
			inSampleSize = 1;
		return inSampleSize;
	}
}
