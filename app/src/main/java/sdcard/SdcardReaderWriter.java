package sdcard;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by yoshi on 2015/01/10.
 * SD Cardにアクセスするためのクラス
 */
public class SdcardReaderWriter {

    private static final String DEFAULT_ENCORDING = "UTF-8";
    private static final int DEFAULT_BUFF_LENGTH = 8192;
    private static final int DEFAULT_READ_LENGTH = DEFAULT_BUFF_LENGTH;
    private static final int DEFAULT_WRITE_LENGTH = DEFAULT_BUFF_LENGTH;

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

    public static void writeStream(OutputStream outputStream, String writeString, int writeLength) throws IOException {

        //一時バッファのように使う
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        //write() 毎に読み込むバッファ
        final byte[] bytes = new byte[writeLength];
        final BufferedOutputStream bis = new BufferedOutputStream(outputStream, writeLength);
        final PrintWriter writer = new PrintWriter(outputStream);
        writer.append(writeString);
        writer.close();

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
     *
     * @param filePath ファイルの絶対パス
     * @return ファイル文字列
     * @throws IOException
     */
    public static final String loadTextSDCardAbsPath(String filePath) throws IOException {
        //マウント状態のチェック
        if (!isMountSDCard()) {
            throw new IOException("No Mount");
        }

        InputStream is = new FileInputStream(filePath);
        Log.d("SD card loaded", filePath);
        return loadText(is, DEFAULT_ENCORDING);
    }

    public static void writeTextSDcardAbsPath(String filePath, String writeString) throws IOException {
        File writeFile = new File(filePath);
        //Check the mount state
        if (!isMountSDCard()) {
            throw new IOException("No Mount");
        }

        createFile(writeFile, writeString);

    }

    public static void createFile(File file, String writeText) {
        try {
            file.createNewFile();
            if (file.canWrite()) {
                FileWriter fw = new FileWriter(file);
                fw.append(writeText);
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readFile(File file) {
        if (file.exists() && file.canRead()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String body;
                if ((body = br.readLine()) != null) {
                    Log.i("readFile", "テキストファイルから読み込んだ文字列: " + body);
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
