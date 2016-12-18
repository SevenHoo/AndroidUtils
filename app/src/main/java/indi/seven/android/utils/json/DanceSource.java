package indi.seven.android.utils.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Description: An example which shows how to use AssetsJsonParser. <br/>
 * Date: 2016/11/3 <br/>
 * @author mr.hoo7793@gmail.com
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
