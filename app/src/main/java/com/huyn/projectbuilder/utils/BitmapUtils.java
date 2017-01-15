package com.huyn.projectbuilder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class BitmapUtils {
	public static Bitmap addBitmap(Bitmap src, Bitmap watermark, float left, float top){
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap dst = Bitmap.createBitmap( w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(dst);
		canvas.drawBitmap(BitmapUtils.setAlpha(src, 50),0,0,null);
		canvas.drawBitmap(BitmapUtils.scale(watermark, src.getWidth(), (int) (src.getHeight() / 3.0f)), left, top, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return dst;	
	}
	public static Bitmap addProgress(Bitmap src, Bitmap pro, int progress){
		int w = pro.getWidth();
		int h = pro.getHeight();
		Bitmap dst = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(dst);
		canvas.drawBitmap(src, 0, 0, null);
		canvas.drawBitmap(pro, 0, pro.getHeight() * (100 - progress) / 100.0f, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return dst;	
	}
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
				.getWidth(), sourceImg.getHeight());
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
				.getHeight(), Config.ARGB_8888);

		return sourceImg;
	}
	
	public static Bitmap addWatermark(Bitmap src, float left, float top, String content, int alpha){
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap dst = Bitmap.createBitmap( w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(dst);
		Paint p = new Paint();
		String fontName = "sans";
		Typeface font = Typeface.create(fontName, Typeface.BOLD);
		p.setColor(Color.RED);
		p.setTypeface(font);
		p.setTextSize(22);
		p.setAlpha(alpha* 255 / 100);
		canvas.drawText(content, left, top, p);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return dst;
	}
	public static Bitmap addWatermark(Bitmap src, float left, float top, Bitmap img, int alpha){
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap dst = Bitmap.createBitmap( w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(dst);
		Paint p = new Paint();
		canvas.drawBitmap(src, 0, 0, p);
		p.setAlpha(alpha* 255 / 100);
		canvas.drawBitmap(img, left, top, p);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return dst;
	}
	public static Bitmap scale(Bitmap bitmap, float scaleWidth, float scaleHeight){
		int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight); 
      	return Bitmap.createBitmap(bitmap, 0, 0, width,height, matrix, true);
	}
	public static Bitmap scale(Bitmap bitmap, int newWidth, int newHeight){
		int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        float scaleWidth = ((float) newWidth) / width;  
        float scaleHeight = ((float) newHeight) / height;  
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight); 
      	return Bitmap.createBitmap(bitmap, 0, 0, width,height, matrix, true);
	}

	public static Bitmap roundedCornerBitmap(Bitmap bitmap, float roundPx) {
		return bitmap;
//		try {
//			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);
//			Canvas canvas = new Canvas(output);
//
//			final int color = 0xff424242;
//			final Paint paint = new Paint();
//			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//			final RectF rectF = new RectF(rect);
//
//			paint.setAntiAlias(true);
//			canvas.drawARGB(0, 0, 0, 0);
//			paint.setColor(color);
//			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//			canvas.drawBitmap(bitmap, rect, rect, paint);
//			return output;
//		} catch(Exception e) {
//			e.printStackTrace();
//			return null;
//		} catch (OutOfMemoryError e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap,
						deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()+ reflectionGap, paint);
		return bitmapWithReflection;
	}
	 public static Bitmap createReflectedImage(Bitmap originalImage) {
	        final int reflectionGap = 4;  //倒影和原图片间的距离  
	        int width = originalImage.getWidth();     
	        int height = originalImage.getHeight();  
	          
	        Matrix matrix = new Matrix();
	        matrix.preScale(1, -1);    
	          
	        //倒影部分  
	        Bitmap reflectionImage = Bitmap.createBitmap(originalImage,
	                0, height / 2, width, height / 2, matrix, false);    
	        //要返回的倒影图片  
	        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
	                (height + height / 2), Config.ARGB_8888);
	          
	        Canvas canvas = new Canvas(bitmapWithReflection);
	        //画原来的图片  
	        canvas.drawBitmap(originalImage, 0, 0, null);    
	          
	        Paint defaultPaint = new Paint();
	        //倒影和原图片间的距离  
	        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);    
	        //画倒影部分  
	        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);    
	          
	        Paint paint = new Paint();
	        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(),
	                0, bitmapWithReflection.getHeight() + reflectionGap,   
	                0x70ffffff, 0x00ffffff,     
	                TileMode.MIRROR);
	        paint.setShader(shader);    
	        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);    
	        return bitmapWithReflection;     
	    }  
	public static Bitmap Bytes2Bimap(byte[] b){
        if(b.length!=0){  
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }  
        else {  
            return null;  
        }  
	} 
	public static byte[] Bitmap2Bytes(Bitmap bm){
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	    return baos.toByteArray();  
	 } 
	public static String byte2hex(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			tmp = (Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
		}
		return sb.toString().toUpperCase(); 
	}
	public static Bitmap scaleFile(String filepath, int maxWidth, int maxHeight){
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;  
	        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options); //此时返回bm为空
	        options.inJustDecodeBounds = false;  
	        options.inSampleSize = computeSampleSize(options, -1, maxWidth * maxHeight);
	        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
	        bitmap= BitmapFactory.decodeFile(filepath,options);
	        return bitmap;
		} catch(OutOfMemoryError e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatPicToHex(String fileName) {
		try {
			File file = new File(fileName);
			if(!file.exists())
				return "";

			InputStream in = new FileInputStream(file);

			int length = in.available();
			byte[] buffer = new byte[length];

			in.read(buffer);

			String str = BitmapUtils.byte2hex(buffer);

			in.close();

			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static Bitmap resizeBitmap(String filepath, int maxWidth, int maxHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        options.inJustDecodeBounds = false;  
        int originWidth  = options.outWidth;
        int originHeight =options.outHeight;
        // no need to resize
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }
        float wb=originWidth/maxWidth;
        float hb=originHeight/maxHeight;
        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth||originHeight>maxHeight) {
        	if(wb>=hb){
	            int i =(int) Math.floor(originWidth * 1.0 / maxWidth);
	            options.inSampleSize = i;  
	            
        	}else{
        		int i = (int) Math.floor(originHeight * 1.0 / maxHeight);
	            options.inSampleSize = i;  
        	}
        }
        bitmap= BitmapFactory.decodeFile(filepath,options);
        return bitmap;
	}
	public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
	        int originWidth  = bitmap.getWidth();
	        int originHeight = bitmap.getHeight();
	        // no need to resize
	        if (originWidth < maxWidth && originHeight < maxHeight) {
	            return bitmap;
	        }
	        int width  = originWidth;
	        int height = originHeight;
	        float wb=originWidth/maxWidth;
	        float hb=originHeight/maxHeight;
	        // 若图片过宽, 则保持长宽比缩放图片
	        if (originWidth > maxWidth||originHeight>maxHeight) {
	        	if(wb>hb){
	        		width = maxWidth;
		            double i = originWidth * 1.0 / maxWidth;
		            height = (int) Math.floor(originHeight / i);
		            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
	        	}else{
	        		height = maxHeight;
		            double i = originHeight * 1.0 / maxHeight;
		            width = (int) Math.floor(originWidth / i);
		            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
	        	}
	        }
	        return bitmap;
	 }
	public static void bitmapToFile(Bitmap bm, String path){
		File file=new File(path);
        try {
        	file.createNewFile();
            FileOutputStream out=new FileOutputStream(file);
            if(bm.compress(Bitmap.CompressFormat.JPEG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8 ) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
	
	public static Bitmap getBitmapFromDrawable(Drawable drawable, int width, int height) {
		Bitmap bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bm);
		drawable.draw(canvas);
		return bm;
	}

	public static Bitmap getBitmapFromDrawable(ImageView view){
		if(view != null && view.getDrawable() != null) {
			Drawable drawable = view.getDrawable();
			System.out.println("-------------------" + (drawable.getIntrinsicHeight()*1f/drawable.getIntrinsicWidth()));
			if(drawable instanceof BitmapDrawable) {
				System.out.println("---bitmap:" + ((BitmapDrawable) drawable).getBitmap().getWidth() + "/" + ((BitmapDrawable) drawable).getBitmap().getHeight());
				return ((BitmapDrawable) drawable).getBitmap();
			} else if(drawable instanceof GlideBitmapDrawable) {
				return ((GlideBitmapDrawable) drawable).getBitmap();
			}
		}
		return null;
	}

	public static Bitmap zoomImg(Bitmap bmp, float scaleWidth, float scaleHeight) {
		// 获取这个图片的宽和高
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}
	
	public static Bitmap adjustRotateBitmap(String filePath, int maxWidth, int maxHeight) {
		int rotateDegree = getPhotoDegree(filePath);
		Matrix matrix = new Matrix();
		matrix.postRotate(rotateDegree);
		Bitmap srcBitmap = decodeImageFromExternalDevice(filePath, maxWidth, maxHeight);
		WeakReference<Bitmap> resizedBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(srcBitmap,
        		0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true));
        return resizedBitmap.get();
	}
	
	public static int getPhotoDegree(String filePath) {
        int degree  = 0;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
	
	public static Bitmap decodeImageUri(Context context, Uri uri, int reqWidth, int reqHeight) {
    	try {
        	InputStream is = context.getContentResolver().openInputStream(uri);
        	byte[] data = readStream(is);
        	return decodeImageByteArray(data, reqWidth, reqHeight);
    	} catch (Exception e) {
        	e.printStackTrace();
    	}
    	return null;
	}
	
	public static Bitmap decodeImageByteArray(byte[] data, int reqWidth, int reqHeight) {
    	final BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeByteArray(data, 0, data.length, options);
    	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
    	options.inJustDecodeBounds = false;
    	return BitmapFactory.decodeByteArray(data, 0, data.length, options);
}
	
	public static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }
	
	public static Bitmap decodeImageFromExternalDevice(String filePath, int reqWidth, int reqHeight) {
    	final BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(filePath, options);
    	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
    	options.inJustDecodeBounds = false;
    	return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static Bitmap readBitMap(Context context, int resId, BitmapFactory.Options opt) {
		if(opt == null)
			return readBitMap(context, resId);
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
										   int actualSecondary) {
		// If no dominant value at all, just return the actual.
		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}

		// If primary is unspecified, scale primary to match secondary's scaling ratio.
		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	private static int findBestSampleSize(
			int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}
		return (int) n;
	}

	public static Bitmap decodeBitmapFromBuffer(byte[] data, int[] size) {
		return decodeBitmapFromBuffer(data, size[0], size[1]);
	}

	public static Bitmap decodeBitmapFromBuffer(byte[] data, int mMaxWidth, int mMaxHeight) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
			if (mMaxWidth == 0 && mMaxHeight == 0) {
				decodeOptions.inPreferredConfig = Config.ARGB_8888;
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
			} else {
				// If we have to resize this image, first get the natural bounds.
				decodeOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
				int actualWidth = decodeOptions.outWidth;
				int actualHeight = decodeOptions.outHeight;

				// Then compute the dimensions we would ideally like to decode to.
				int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
						actualWidth, actualHeight);
				int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
						actualHeight, actualWidth);

				// Decode to the nearest power of two scaling factor.
				decodeOptions.inJustDecodeBounds = false;
				decodeOptions.inPreferredConfig = Config.ARGB_8888;
				decodeOptions.inInputShareable = true;
				decodeOptions.inPurgeable = true;
				// Do we need this or is it okay since API 8 doesn't support it?
				// decodeOptions.inPreferQualityOverSpeed = PREFER_QUALITY_OVER_SPEED;
				decodeOptions.inSampleSize =
						findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
				Bitmap tempBitmap =
						BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);

				// If necessary, scale down to the maximal acceptable size.
				if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth ||
						tempBitmap.getHeight() > desiredHeight)) {
					bitmap = Bitmap.createScaledBitmap(tempBitmap,
							desiredWidth, desiredHeight, true);
					tempBitmap.recycle();
				} else {
					bitmap = tempBitmap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    	final int height = options.outHeight;
    	final int width = options.outWidth;
    	int inSampleSize = 1;
    	if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight	|| (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
    	return inSampleSize;
	}

	public static int calculateInSampleSizeStrict(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((height / inSampleSize) > reqHeight || (width / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		Utils.Log(Utils.LogType.ERROR, "CalculateResult", width + "x" + height + "  :  " + reqWidth + 'x' + reqHeight + "  :  " + inSampleSize);
		return inSampleSize;
	}
	
	public static Bitmap changeConfig(Bitmap bitmap, Config config) {
		try {
		    Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
		    Canvas canvas = new Canvas(convertedBitmap);
		    Paint paint = new Paint();
		    paint.setColor(Color.BLACK);
		    canvas.drawBitmap(bitmap, 0, 0, paint);
		    return convertedBitmap;
		} catch (Exception e) {
			return null;
		} catch (OutOfMemoryError e) {
			System.gc();
			return null;
		}
	}

	public static Bitmap copyBitmap(Bitmap bitmap) {
		return bitmap.copy(Config.ARGB_8888,true);
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 转换图片成圆形
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>30) {	//循环判断如果压缩后图片是否大于30kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap parsePic(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 160f;
		float ww = 120f;
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

}
