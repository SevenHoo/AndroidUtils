package indi.seven.android.utils.json;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author seven.hu@ubtrobot.com
 * @date 2016/11/21
 * @description  舞蹈数据源
 * @modifier
 * @last_modify_time
 */

public class DanceSource {

    @SerializedName("danceList")
    private ArrayList<Dance> mDanceList;

    public ArrayList<Dance> getDanceList() {
        return mDanceList;
    }

    public void setDanceList(ArrayList<Dance> mDanceList) {
        this.mDanceList = mDanceList;
    }

    public int getCount(){
        if(mDanceList != null){
            return mDanceList.size();
        }
        return 0;
    }

    public Dance getDanceByIndex(int index){
        if(mDanceList != null){
            return mDanceList.get(index);
        }
        return null;
    }

    public int getIndex(Dance dance){
        if(dance != null){
            return mDanceList.indexOf(dance);
        }
        return -1;
    }
}
