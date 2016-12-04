package indi.seven.android.utils.image;

/**
 * @author mr.hoo7793@gmail.com
 * @date 2016/10/13
 * @description  封装YUV数据格式
 */

public class YUVFormat {

    public static class NV21{

        private byte[] data;
        private int width;
        private int height;

        public NV21(byte[] data,int width, int height){

            this.data = data;
            if(width % 2 != 0){
                width = width - 1;
            }
            if(height % 2 != 0){
                height = height - 1;
            }
            this.width = width;
            this.height = height;
        }

        public int getLength(){
            if(data != null){
                return data.length;
            }
            return 0;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
