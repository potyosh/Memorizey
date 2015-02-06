package util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sdcard.SdcardReaderWriter;

/**
 * Created by yoshi on 2015/01/14.
 * Json取得、参照の為のクラス
 */
public class JsonUtil {
    private JSONObject mJsonObject;
    private JSONArray mJsonArray;
    private String mFileUri;
    public JsonUtil(String argUri) throws IOException, JSONException {
        String strJson = SdcardReaderWriter.loadTextSDCardAbsPath(argUri);
        mFileUri = argUri;
        //mJsonObject = new JSONObject(strJson);
        mJsonArray = new JSONArray(strJson);
    }

    public JSONObject getJsonObject(){
        return mJsonObject;
    }
    public JSONArray getJsonArray(){ return mJsonArray; }
    public void fileWriteJsonString(String writeString) throws IOException {
        SdcardReaderWriter.writeTextSDcardAbsPath(mFileUri, writeString);
    }
}
