package sdcard;

import android.os.Environment;
import android.util.Log;

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
 * SD Cardにアクセスするためのクラス
 *
 */
public class SdcardReader {

    private static final String DEFAULT_ENCORDING = "UTF-8";
    private static final int DEFAULT_READ_LENGTH = 8192;

    //ストリームから読み込み、バイト配列で返す
    public static final byte[] readStream(InputStream inputStream, int readLength) throws IOException {

        //一時バッファのように使う
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        //read() 毎に読み込むバッファ
        final byte[] bytes = new byte[readLength];
        final BufferedInputStream bis = new BufferedInputStream(inputStream, readLength);

        try {
            int len = 0;
            while ((len = bis.read(bytes, 0, readLength)) > 0) {
                //ストリームバッファに溜め込む
                byteStream.write(bytes, 0, len);
            }
            //byte[] に変換
            return byteStream.toByteArray();

        } finally {
            try {
                //すべてのデータを破棄
                byteStream.reset();
                //ストリームを閉じる
                bis.close();
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
            //マウントされている
            return true;
        } else {
            //マウントされていない
            return false;
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
        if (!isMountSDCard()) {
            //マウント状態のチェック
            throw new IOException("No Mount");
        }
        //SDCard 内の絶対パスに変換
        String absPath = toSDCardAbsolutePath(fileName);
        InputStream is = new FileInputStream(absPath);
        Log.d("SD card loaded", absPath);
        return loadText(is, DEFAULT_ENCORDING);
    }

    /**
     * SDCard から、テキストファイルを読み込む(Android 用)
     * @param fileName ファイルの絶対パス
     * @return ファイル文字列
     * @throws IOException
     */
    public static final String loadTextSDCardAbsPath(String fileName) throws IOException {
        //マウント状態のチェック
        if (!isMountSDCard()) {
            throw new IOException("No Mount");
        }

        InputStream is = new FileInputStream(fileName);
        Log.d("SD card loaded", fileName);
        return loadText(is, DEFAULT_ENCORDING);
    }
}
