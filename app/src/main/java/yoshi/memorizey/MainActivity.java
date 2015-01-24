package yoshi.memorizey;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import yoshi.memorizey.FileDialogActivity.OnFileSelectDialogListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import util.JsonUtil;


public class MainActivity extends ActionBarActivity implements OnFileSelectDialogListener {

    private String mfileName = "word/english_words2.json";

    private JsonUtil mjsonUtil;

    private JSONArray mJsonArray;

    private int mWordIndex = 0;

    private String mQText;
    private String mAText;
    private String mfliped = "Question";

    private Button mNextButton;
    private Button mPrevButton;
    private Button mFlipButton;
    private CheckBox mMemorizedCheckBox;

    private TextView mQTextView;

    //ファイル選択時動作実装
    @Override
    public void onClickFileSelect(File file) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ボタン、テキストを初期化
        mNextButton = (Button) findViewById(R.id.nextButton);
        mPrevButton = (Button) findViewById(R.id.prevButton);
        mQTextView = (TextView) findViewById(R.id.questionText);
        mFlipButton = (Button) findViewById(R.id.flipButton);
        mMemorizedCheckBox = (CheckBox) findViewById(R.id.memorizedCheckBox);

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

        //CheckBox実装
        mMemorizedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                // チェックボックスのチェック状態を取得します
                boolean checked = checkBox.isChecked();
                Log.d("hoge", String.valueOf(checked));
            }
        });
        //Nextボタン実装
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mWordIndex += 1;
                    if(mWordIndex >= mJsonArray.length()){
                        mWordIndex = 0;
                    }
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
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
                    mWordIndex -= 1;
                    if(mWordIndex < 0){
                        mWordIndex = mJsonArray.length() - 1;
                    }
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
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
        }else if(id == R.id.action_file){
            // ファイル選択ダイアログを表示
            FileDialogActivity dialog = new FileDialogActivity(this);
            dialog.setOnFileSelectDialogListener(this);

            // 表示
            dialog.show(Environment.getExternalStorageDirectory().getPath());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
