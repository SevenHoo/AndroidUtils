package indi.seven.android.utils.json;

/**
 * @author seven.hu@ubtrobot.com
 * @date 2016/11/21
 * @description Dance定义
 * @modifier
 * @last_modify_time
 */

public class Dance {

    /**
     * 舞蹈动作
     */
    private String dance;
    /**
     * 舞蹈音乐
     */
    private String music;
    /**
     * 舞蹈灯光
     */
    private int light;

    public String getDance() {
        return dance;
    }

    public void setDance(String dance) {
        this.dance = dance;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
