package yoshi.memorizey;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity {

    //設定値
    private static final String DEFAULT_ENCORDING = "UTF-8";  //デフォルトのエンコード
    private static final int DEFAULT_READ_LENGTH = 8192;      //一度に読み込むバッファサイズ
    private String fileName = "/sdcard/temp/sample.txt";

    //ストリームから読み込み、バイト配列で返す
    public static final byte[] readStream(InputStream inputStream, int readLength) throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();    //一時バッファのように使う
        final byte[] bytes = new byte[readLength];    //read() 毎に読み込むバッファ
        final BufferedInputStream bis = new BufferedInputStream(inputStream, readLength);

        try {
            int len = 0;
            while ((len = bis.read(bytes, 0, readLength)) > 0) {
                byteStream.write(bytes, 0, len);    //ストリームバッファに溜め込む
            }
            return byteStream.toByteArray();    //byte[] に変換

        } finally {
            try {
                byteStream.reset();     //すべてのデータを破棄
                bis.close();            //ストリームを閉じる
            } catch (Exception e) {
                //IOException
            }
        }
    }

    //ストリームから読み込み、テキストエンコードして返す
    public static final String loadText(InputStream inputStream, String charsetName)
            throws IOException, UnsupportedEncodingException {
        return new String(readStream(inputStream, DEFAULT_READ_LENGTH), charsetName);
    }

    //ローカルシステムから、テキストファイルを読み込む
    public static final String loadTextLocal(String fileName) throws IOException, FileNotFoundException {
        InputStream is = new FileInputStream(fileName);
        return loadText(is, DEFAULT_ENCORDING);
    }

    // SDCard のマウント状態をチェックする(Android 用)
    public static final boolean isMountSDCard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;   //マウントされている
        } else {
            return false;  //マウントされていない
        }
    }

    // SDCard のルートディレクトリを取得(Android 用)
    public static final File getSDCardDir() {
        return Environment.getExternalStorageDirectory();
    }

    // SDCard 内の絶対パスに変換(Android 用)
    public static final String toSDCardAbsolutePath(String fileName) {
        //return getSDCardDir().getAbsolutePath() + File.separator + fileName;
        return getSDCardDir() + File.separator + fileName;

    }

    // SDCard から、テキストファイルを読み込む(Android 用)
    public static final String loadTextSDCard(String fileName) throws IOException {
        if (!isMountSDCard()) {  //マウント状態のチェック
            throw new IOException("No Mount");
        }
        String absPath = toSDCardAbsolutePath("sample.txt");  //SDCard 内の絶対パスに変換
        //InputStream is = new FileInputStream(absPath);
        File file = new File(fileName);
        InputStream is = new FileInputStream(absPath);
        return loadText(is, DEFAULT_ENCORDING);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.yoshiButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, fileName,
                        Toast.LENGTH_SHORT).show();
            }
        });

        TextView myText = (TextView) findViewById(R.id.textView2);
        myText.setText("hyooooooooo");

        //"mnt/sdcard/sample.txt" or "sdcard/sample.txt" 等になる(端末による)
        //String fileName = "/sdcard/temp/sample.txt";
        String text = null;
        try {
            text = loadTextSDCard(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("tag", text); でも良い
        System.out.println("hogegegegeg"+fileName);

        File file = new File("storage/emulated/0/", "aaa.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file), 100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
