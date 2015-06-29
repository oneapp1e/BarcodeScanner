package cn.hugo.android.scanner.encode;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author Ryan Tang
 * 
 */
public final class EncodingHandler {
    private static final int BLACK = 0xff000000;

    public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {

        Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别——这里选择最高H级别
        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,
                qrParam);

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap getNewBitMap(Bitmap bmp, String text, int newwidth, int newheight) {
        Bitmap newBitmap = Bitmap.createBitmap(newwidth, newheight, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bmp, (newwidth - bmp.getWidth()) / 2, 0, null);

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(26.0F);
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        textPaint.setColor(0xff46ddfd);
        textPaint.setTypeface(font);
        StaticLayout sl = new StaticLayout(text, textPaint, newBitmap.getWidth() - 8, Layout.Alignment.ALIGN_CENTER,
                1.0f, 0.0f, false);
        canvas.translate(0, bmp.getHeight());
        sl.draw(canvas);
        return newBitmap;
    }
}
