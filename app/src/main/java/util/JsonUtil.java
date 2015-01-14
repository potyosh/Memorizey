package util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sdcard.SdcardReader;

/**
 * Created by yoshi on 2015/01/14.
 */
public class JsonUtil {
    private String mJsonUri;
    private JSONObject mJsonObject;
    public JsonUtil(String argUri) throws IOException, JSONException {
        mJsonUri = argUri;
        String strJson = SdcardReader.loadTextSDCard(mJsonUri);
        mJsonObject = new JSONObject(strJson);
    }

    public JSONObject getJsonObject(){
        return mJsonObject;
    }
}
