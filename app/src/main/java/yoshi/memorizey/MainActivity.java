package yoshi.memorizey;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import util.JsonUtil;


public class MainActivity extends ActionBarActivity {

    private String mfileName = "word/english_words2.json";
    private JsonUtil mjsonUtil;
    private JSONArray mJsonArray;
    private int mWordIndex = 0;
    private String mQText;
    private String mAText;
    private Button mNextButton;
    private TextView mQTextView;
    private Button mPrevButton;
    private Button mFlipButton;
    private String mfliped = "Question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ボタン、テキストを初期化
        mNextButton = (Button) findViewById(R.id.nextButton);
        mPrevButton = (Button) findViewById(R.id.prevButton);
        mQTextView = (TextView) findViewById(R.id.questionText);
        mFlipButton = (Button) findViewById(R.id.flipButton);

        //パスを参照しJsonを取得
        try {
            mjsonUtil = new JsonUtil(mfileName);
            mJsonArray = mjsonUtil.getJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //初回はゼロ番の問題を取得し表示する
        try {
            mQText = mJsonArray.getJSONObject(0).getString("Question");
            mQTextView.setText(mQText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Nextボタン実装
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
                    mWordIndex += 1;
                    if(mWordIndex >= mJsonArray.length()){
                        mWordIndex = 0;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //Prevボタン実装
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
                    mWordIndex -= 1;
                    if(mWordIndex < 0){
                        mWordIndex = mJsonArray.length() - 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //Flipボタン実装
        mFlipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mfliped.equals("Question")){
                    mfliped = "Answer";
                }else if(mfliped.equals(("Answer"))){
                    mfliped = "Question";
                }

                try {
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
