
package indi.seven.android.utils.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * @date 2016/11/3
 * @author seven.hu@ubtrobot.com
 * @Description 解析本地配置文件中的Dance数据
 * @modifier
 * @modify_time;
 */

public class DanceJsonParser extends AssetsJsonParser {

	private final static String TAG = DanceJsonParser.class.getSimpleName();
	private final static String DANCE_FILE_PATH = "dance.js";

	private Gson gson = new Gson();
	private DanceSource source;


	@Override
	protected String getPath(){
		return DANCE_FILE_PATH;
	}

	@Override
	protected void parse(JsonElement root) {

		try {
			JsonObject obj = root.getAsJsonObject();
			Log.d(TAG,"get dance json list: " + obj.toString());
			source = gson.fromJson(obj.toString(), DanceSource.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DanceSource getDataSource() {
		return source;
	}
}
