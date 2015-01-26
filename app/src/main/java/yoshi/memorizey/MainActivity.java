package yoshi.memorizey;

import android.content.Intent;
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
import yoshi.memorizey.FileDialogActivity.OnFileSelectDialogListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

import util.JsonUtil;


public class MainActivity extends ActionBarActivity implements OnFileSelectDialogListener {

    private static String mfileName = "/storage/emulated/0/word/english_words1.json";

    private JsonUtil mjsonUtil;

    private JSONArray mJsonArray;

    private int mWordIndex = 0;

    private String mQText;
    private String mAText;
    private String mfliped = "Question";
    private String mChecked;

    private Button mNextButton;
    private Button mPrevButton;
    private Button mFlipButton;
    private CheckBox mMemorizedCheckBox;

    private TextView mQTextView;

    //File Selected
    @Override
    public void onClickFileSelect(File file) {
        Log.d("File", file.getPath());
        mfileName = file.getPath();
        reload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize button and text.
        mNextButton = (Button) findViewById(R.id.nextButton);
        mPrevButton = (Button) findViewById(R.id.prevButton);
        mQTextView = (TextView) findViewById(R.id.questionText);
        mFlipButton = (Button) findViewById(R.id.flipButton);
        mMemorizedCheckBox = (CheckBox) findViewById(R.id.memorizedCheckBox);

        //Get the json file
        try {
            mjsonUtil = new JsonUtil(mfileName);
            mJsonArray = mjsonUtil.getJsonArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Initially, start from zero.
        try {
            mQText = mJsonArray.getJSONObject(0).getString("Question");
            mQTextView.setText(mQText);
            //Get check status
            mChecked = mJsonArray.getJSONObject(mWordIndex).getString("Check");
            setCheckBox(mChecked);
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
                Log.d("Checkbox tapped", String.valueOf(checked));
            }
        });

        //Nextボタン実装
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //Go to next
                    mWordIndex += 1;
                    //Going to first, when over the array length
                    if(mWordIndex >= mJsonArray.length()){
                        mWordIndex = 0;
                    }

                    //Get question or answer
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
                    //Get and set check status
                    mChecked = mJsonArray.getJSONObject(mWordIndex).getString("Check");
                    setCheckBox(mChecked);

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
                    //Go previous
                    mWordIndex -= 1;
                    //Go the end of data
                    if(mWordIndex < 0){
                        mWordIndex = mJsonArray.length() - 1;
                    }
                    //Get question or answer
                    mQText = mJsonArray.getJSONObject(mWordIndex).getString(mfliped);
                    mQTextView.setText(mQText);
                    //Get and set check status
                    mChecked = mJsonArray.getJSONObject(mWordIndex).getString("Check");
                    setCheckBox(mChecked);
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

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void setCheckBox(String status){
        if(status.equals("true")){
            mMemorizedCheckBox.setChecked(true);
        }else{
            mMemorizedCheckBox.setChecked(false);
        }
    }
}
