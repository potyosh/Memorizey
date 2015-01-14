package yoshi.memorizey;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sdcard.SdcardReader;
import util.JsonUtil;


public class MainActivity extends ActionBarActivity {

    private String fileName = "Download/english_words.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.yoshiButton);
        String text = null;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "aaa",
                        Toast.LENGTH_SHORT).show();
            }
        });


        try {
            text = SdcardReader.loadTextSDCard(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView myText = (TextView) findViewById(R.id.textView2);
        myText.setText(text);
        Log.d("tag", text);

        JsonUtil jsonUtil;
        JSONObject jsonObject;
        try {
            jsonUtil = new JsonUtil(fileName);
            jsonObject = jsonUtil.getJsonObject();
            Log.d("json", jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("hogegegegeg"+fileName);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
