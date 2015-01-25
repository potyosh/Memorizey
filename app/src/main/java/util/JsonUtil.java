package util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sdcard.SdcardReader;

/**
 * Created by yoshi on 2015/01/14.
 * Json取得、参照の為のクラス
 */
public class JsonUtil {
    private JSONObject mJsonObject;
    private JSONArray mJsonArray;
    public JsonUtil(String argUri) throws IOException, JSONException {
        String strJson = SdcardReader.loadTextSDCardAbsPath(argUri);
        mJsonObject = new JSONObject(strJson);
        mJsonArray = mJsonObject.getJSONArray("data");
    }

    public JSONObject getJsonObject(){
        return mJsonObject;
    }
    public JSONArray getJsonArray(){ return mJsonArray; }
}
