package indi.seven.android.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description: Transform NV21 to ARGB && ARGB to NV21.<br/>
 * Date: 2016/10/13 <br/>
 * @author mr.hoo7793@gmail.com
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    /**
     * 获取Bitmap的ARGB数据
     * @param bitmap
     * @param width 图片的宽
     * @param height 图片的高
     * @return
     */
    public static int[] getColorByBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        int size = width * height;
        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        return pixels;
    }


    public static byte[] getBytePixelByBitmap(Bitmap bitmap, int width, int height){
        return intArrayToByteArray(getColorByBitmap(bitmap,width,height));
    }


    private static byte[] intArrayToByteArray(int[] intArray){
        if(intArray == null){
            return null;
        }
        int len = intArray.length * 4;
        byte[] byteArray = new byte[len];
        for(int i = 0; i < intArray.length; i++){
            byteArray[i] = (byte)( (intArray[i] >> 24) & 0x000000FF);
            byteArray[i + 1] = (byte)( (intArray[i] >> 16) & 0x000000FF);
            byteArray[i + 2] = (byte)( (intArray[i] >> 8) & 0x000000FF);
            byteArray[i + 3] = (byte)( intArray[i] & 0x000000FF);
        }
        return  byteArray;
    }


    /**
     * convert Bitmap to NV21.
     * @param bitmap will be converted.
     * @return NV21 object which include yuv raw data, standardized image width and height.
     */
    public static YUVFormat.NV21 getNV21ByBitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //adjust height&&width
        if(width % 2 != 0){
            width = width - 1;
        }
        if(height % 2 != 0){
            height = height - 1;
        }
        int[] argb = getColorByBitmap(bitmap,width,height);

        byte[] yuv = argb2NV21(argb, width, height);

        YUVFormat.NV21 nv21 = new YUVFormat.NV21(yuv,width,height);
        return nv21;
    }


    /**
     * 将ARGB数据转化为YUV数据
     * @param pixels
     * @param width 图片宽度，取2的倍数
     * @param height 图片高度，取2的倍数
     * @return
     */
    public static byte[] argb2NV21(int[] pixels, int width, int height) {

        int len = width * height;
        //yuv格式数组大小，y亮度占len长度，u,v各占len/4长度。
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        Log.d("huyq","width = " + width + " height = " + height);

        for (int i = 0; i < height; i++) {
            Log.d("huyq","i = " + i);
            for (int j = 0; j < width; j++) {
                //屏蔽ARGB的透明度值
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                //像素的颜色顺序为bgr，移位运算。
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                //套用公式
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                //调整
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                //赋值
                yuv[i * width + j] = (byte) y;
                yuv[len + (i >> 1) * width + (j & ~1)] = (byte) u;
                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    /**
     * 获取指定区域的YUV数据
     * @param src
     * @param srcWidth
     * @param srcHeight
     * @param rect
     * @return
     */
    public static YUVFormat.NV21 getNV21(byte[] src, int srcWidth, int srcHeight, Rect rect){

        int x = rect.left;
        int y = rect.top;
        int width = rect.right - x;
        int height = rect.bottom - y;

        Log.d(TAG,"getNV21：width = " + width + " height = " + height);

        if(width % 2 != 0){
            width = width - 1;
        }
        if(height % 2 != 0){
            height = height - 1;
        }

        int srcLen = srcWidth * srcHeight;
        int len = width * height;
        byte [] yuv = new byte[len * 3 /2];


        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                //Y
                yuv[i * width + j] = src[(i + y) * srcWidth + x + j];
                //U
                yuv[len + (i >> 1) * width + (j & ~1)] = src[srcLen + ((i + y) >> 1) * srcWidth + ((j + x) & ~1)];
                //V
                yuv[len + (i >> 1) * width + (j & ~1) + 1] = src[srcLen + ((i + y) >> 1) * srcWidth + ((j + x) & ~1) + 1];
            }
        }

        YUVFormat.NV21 nv21 = new YUVFormat.NV21(yuv,width,height);
        return nv21;
    }


    /**
     * 根据YUV原始数据指定区域的数据创建Bitmap，该方法先获取指定区域的YUV数据，再进行转化,效率较高。
     * @param yuv
     * @param w
     * @param h
     * @param rect
     * @return
     */
    public static Bitmap getBitmapByNV21(byte[] yuv, int w, int h, Rect rect) {

        YUVFormat.NV21 nv21 = getNV21(yuv,w,h,rect);
        byte data[] = nv21.getData();
        int width = nv21.getWidth();
        int height = nv21.getHeight();
        Log.d(TAG,"getBitmapByNV21：width = " + width + " height = " + height + " dataLen = " + data.length);

        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {

                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1)]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }

        Bitmap bmp = Bitmap.createBitmap(rgba,0,width,width,height, Bitmap.Config.ARGB_8888);
        Log.d(TAG,"getBitmapByNV21：bitmap.width = " + bmp.getWidth()  + ",bitmap.height = " + bmp.getHeight());
        return bmp;
    }

    /**
     * 根据YUV原始数据指定区域的数据创建Bitmap，该方法先转化整张图数据再进行裁剪,效率很低。耗时1S左右。
     * @param yuv
     * @param w
     * @param h
     * @param rect
     * @return
     */
    @Deprecated
    public static Bitmap getBitmapByYUV420SP(byte[] yuv, int w, int h, Rect rect) {

        YUVFormat.NV21 nv21 = new YUVFormat.NV21(yuv,w,h);
        byte data[] = nv21.getData();
        int width = nv21.getWidth();
        int height = nv21.getHeight();
        Log.d(TAG,"getBitmapByNV21：width = " + width + " height = " + height + " dataLen = " + data.length);

        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {

                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1)]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }

        Bitmap bmp = Bitmap.createBitmap(rgba,0,width,width,height, Bitmap.Config.ARGB_8888);

        if(rect != null){

            int x = rect.left;
            int y = rect.top;
            width = rect.right - x;
            height = rect.bottom - y;

            Log.d(TAG,"x = " + x + " y = " + y + " bitmap.width = " + bmp.getWidth()  + ",bitmap.height = " + bmp.getHeight());

            Bitmap bmp2 = Bitmap.createBitmap(bmp,x,y,width,height);
            bmp.recycle();
            bmp = bmp2;
        }

        Log.d(TAG,"getBitmapByNV21：bitmap.width = " + bmp.getWidth()  + ",bitmap.height = " + bmp.getHeight());
        return bmp;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap)
    {
        if(bitmap == null){
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] result = outputStream.toByteArray();
        try{
            outputStream.close();
            return result;
        }catch (IOException e){
            e.printStackTrace();
            return result;
        }
    }

    public static Bitmap getBitmap(byte[] src){
        if(src == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }


}
