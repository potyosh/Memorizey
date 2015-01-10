package sdcard;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by yoshi on 2015/01/10.
 */
public class SdcardReader {

    private static final String DEFAULT_ENCORDING = "UTF-8";
    private static final int DEFAULT_READ_LENGTH = 8192;

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
}
