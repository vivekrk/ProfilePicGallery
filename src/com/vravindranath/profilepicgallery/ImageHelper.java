package com.vravindranath.profilepicgallery;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class ImageHelper {
	//final static int ROUNDED_PIXEL = 16;
    public static Bitmap getRoundBitmap(Bitmap input) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, 100, 100);
        //final RectF rectF = new RectF(rect);
        //final float roundPx = ROUNDED_PIXEL;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(50, 50, 50, paint);
        
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(input, rect, rect, paint);
        
        return output;
    }
}
