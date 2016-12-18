
package indi.seven.android.utils.json;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Description: Parse the json file in the assets folder. <br/>
 * Date: 2016/11/3 <br/>
 * @author mr.hoo7793@gmail.com
 */

public abstract class AssetsJsonParser {

	private static final String TAG = AssetsJsonParser.class.getSimpleName();
	private JsonElement mRoot;

	/**
	 * The subclass should parse its own json file.
	 */
	protected abstract void parse(JsonElement root);

	/**
	 * The subclass should provide the path which is relative to 'assets'.
     */
	protected abstract String getPath();


	public void open(Context context){
		InputStream in = null;
		String configFilePath = getPath();
		try {
			final AssetManager assets = context.getAssets();
			if(!isAssetsFileExist(assets,configFilePath)){
				Log.d(TAG,"config file assets/" + configFilePath + " not exist.");
				return;
			}
			in = assets.open(configFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "read config file failed.");
		}

		final JsonParser jsonParser = new JsonParser();
		try {
			if(in != null){
				mRoot = jsonParser.parse(new InputStreamReader(in));
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					// Ignore.
				}
			}
		}
	}

	/**
	 * check whether the path exists int the assets folder.
	 * @param am AssetManager
	 * @param path the path to be checked
     * @return true represents the path exists.
     */
	private boolean isAssetsPathExist(AssetManager am, String path){
		try {
			String[] files = am.list(path);
			if(files != null){
				Log.d(TAG,"assets/" + path + " exist.");
				return true;
			}
			Log.d(TAG,"assets/" + path + " not exist.");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * check whether the path/file exists int the assets folder.
	 * @param am AssetManager
	 * @param path the relative path of file.
	 * @param file the file name.
     * @return true represents the path/file exists.
     */
	private boolean isAssetsFileExist(AssetManager am, String path,String file){
		try {
			String[] files = am.list(path);
			if(files != null && files.length > 0){
				for (String name: files) {
					if(name.equals(file)){
						Log.d(TAG,"assets/" + path + "/" + file + " exist.");
						return true;
					}
				}
			}
			Log.d(TAG,"assets/" + path + "/" + file + " not exist.");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * check whether the file exists int the assets folder.
	 * @param am AssetManager
	 * @param file the file to be checked
	 * @return true represents the file exists.
	 */
	private boolean isAssetsFileExist(AssetManager am, String file){
		String[] dirs = file.split(File.separator);
		int depth = dirs.length - 1;
		Log.d(TAG,"------> " + depth + ":" + dirs[depth]);

		if(depth > 0){
			int index = file.lastIndexOf(File.separator);
			String path = file.substring(0,index);
			String name = dirs[depth];
			if(isAssetsPathExist(am,path)){
				return isAssetsFileExist(am,path,name);
			}
			return false;
		}else {
			return isAssetsFileExist(am,"",file);
		}
	}

	public void parse() {
		if(mRoot != null){
			parse(mRoot);
		}else {
			Log.e(TAG,"please open a json file first.");
		}
	}


}
