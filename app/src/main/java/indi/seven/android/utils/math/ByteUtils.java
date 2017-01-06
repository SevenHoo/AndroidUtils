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

        array[0] = (byte) ((src >> 24) & 0xff);
        array[1] = (byte) ((src >> 16) & 0xff);
        array[2] = (byte) ((src >> 8) & 0xff);
        array[3] = (byte) (src & 0xff);

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
     * In Java, byte is a signed number. Convert it to non-signed number using & with 0xff.
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


}
