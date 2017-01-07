package indi.seven.android.utils.math;

/**
 * Description: TODO <br/>
 * Date: 2017/1/6 <br/>
 *
 * @author seven.hu@ubtrobot.com
 */

public class ByteUtils {

    /**
     * convert int to byte array. (BIG_ENDIAN)
     * @param src
     * @return
     */
    public static byte[] intToByteArray(int src){
        byte[] array = new byte[4];

        copyIntToByteArray(src,array,0);

        return array;
    }


    public static void copyIntToByteArray(int src, byte[] des, int offset){

        if(des == null || offset < 0){
            return;
        }

        des[offset] = (byte) ((src >> 24) & 0xff);
        des[offset + 1] = (byte) ((src >> 16) & 0xff);
        des[offset + 2] = (byte) ((src >> 8) & 0xff);
        des[offset + 3] = (byte) (src & 0xff);
    }

    public static byte[] intArrayToByteArray(int[] src){

        if(src == null || src.length <= 0){
            return null;
        }

        byte[] des = new byte[4 * src.length];
        int offset = 0;
        for (int i: src) {
            copyIntToByteArray(i,des,offset);
            offset = offset + 4;
        }
        return des;
    }

    /**
     * Java会将byte作有符号数处理,将其与0xff与，得到其无符号数
     * @param src
     * @param offset
     * @return
     */
    public static int byteArrayToInt(byte[] src, int offset){

        if(src == null || src.length - offset < 4){
            return -1;
        }

        return (src[offset] & 0xff) << 24 |
                (src[offset + 1] & 0xff) << 16 |
                (src[offset + 2] & 0xff) << 8 |
                src[offset + 3] & 0xff;

    }

    public static int byteArrayToInt(byte[] src){
        return byteArrayToInt(src,0);
    }


    public static int[] byteArrayToIntArray(byte[] src){

        if(src == null || src.length <= 0){
            return null;
        }

        if(src.length % 4 != 0){
            return null;
        }

        int[] des = new int[src.length / 4];
        int offset = 0;
        for (int i = 0; i < des.length; i++){
            des[i] = byteArrayToInt(src,offset);
            offset = offset + 4;
        }
        return des;
    }

    /**
     * convert long to byte array. (BIG_ENDIAN)
     * @param src
     * @return
     */
    public static byte[] longToByteArray(long src){
        byte[] array = new byte[8];

        copyLongToByteArray(src,array,0);

        return array;
    }


    public static void copyLongToByteArray(long src, byte[] des, int offset){

        if(des == null || offset < 0){
            return;
        }

        des[offset] = (byte) ((src >> 56) & 0xff);
        des[offset + 1] = (byte) ((src >> 48) & 0xff);
        des[offset + 2] = (byte) ((src >> 40) & 0xff);
        des[offset + 3] = (byte) ((src >> 32) & 0xff);
        des[offset + 4] = (byte) ((src >> 24) & 0xff);
        des[offset + 5] = (byte) ((src >> 16) & 0xff);
        des[offset + 6] = (byte) ((src >> 8) & 0xff);
        des[offset + 7] = (byte) (src & 0xff);
    }

    public static byte[] longArrayToByteArray(long[] src){

        if(src == null || src.length <= 0){
            return null;
        }

        byte[] des = new byte[8 * src.length];
        int offset = 0;
        for (long i: src) {
            copyLongToByteArray(i,des,offset);
            offset = offset + 8;
        }
        return des;
    }

    /**
     * Java会将byte作有符号数处理,将其与0xff与，得到其无符号数
     * @param src
     * @param offset
     * @return
     */
    public static long byteArrayToLong(byte[] src, int offset){

        if(src == null || src.length - offset < 8){
            return -1;
        }

        return  (src[offset] & 0xffL) << 56 |
                (src[offset + 1] & 0xffL) << 48 |
                (src[offset + 2] & 0xffL) << 40 |
                (src[offset + 3] & 0xffL) << 32 |
                (src[offset + 4] & 0xffL) << 24 |
                (src[offset + 5] & 0xffL) << 16 |
                (src[offset + 6] & 0xffL) << 8 |
                (src[offset + 7] & 0xffL);

    }

    public static long byteArrayToLong(byte[] src){
        return byteArrayToLong(src,0);
    }


    public static long[] byteArrayToLongArray(byte[] src){

        if(src == null || src.length <= 0){
            return null;
        }

        if(src.length % 8 != 0){
            return null;
        }

        long[] des = new long[src.length / 8];
        int offset = 0;
        for (int i = 0; i < des.length; i++){
            des[i] = byteArrayToLong(src,offset);
            offset = offset + 8;
        }
        return des;
    }


}
