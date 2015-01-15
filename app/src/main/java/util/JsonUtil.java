package util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sdcard.SdcardReader;

/**
 * Created by yoshi on 2015/01/14.
 */
public class JsonUtil {
    private JSONObject mJsonObject;
    private JSONArray mJsonArray;
    public JsonUtil(String argUri) throws IOException, JSONException {
        String strJson = SdcardReader.loadTextSDCard(argUri);

        mJsonObject = new JSONObject(strJson);
        mJsonArray = mJsonObject.getJSONArray("words");

        Log.d("tag", mJsonArray.getJSONObject(0).getString("english"));
        Log.d("tag", mJsonArray.getJSONObject(0).getString("japanese"));
        Log.d("tag", argUri);
    }

    public JSONObject getJsonObject(){
        return mJsonObject;
    }
}
