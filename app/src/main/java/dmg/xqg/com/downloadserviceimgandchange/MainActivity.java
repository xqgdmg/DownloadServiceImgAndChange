package dmg.xqg.com.downloadserviceimgandchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv);
        imageView = (ImageView) findViewById(R.id.iv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_LONG).show();
                getImgAndSave();
            }
        });

        // 第一次进来先显示上一次下载好的，再下载，
        // 有变化才去下载？给个标志位？
        Bitmap bitmapDownloaded = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/com.pigamewallet/Record/" + "niamdeaaaa" + ".jpg");
        Log.e("chris","bitmapDownloaded=" + bitmapDownloaded);
        if (bitmapDownloaded != null){
            imageView.setImageBitmap(bitmapDownloaded);
        };

    }

    private void getImgAndSave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getImageInputStream("http://pic.4j4j.cn/upload/pic/20130617/55695c3c95.jpg");
            }
        }).start();
    }

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public void getImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            // 保存
            saveQrCodePicture(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveQrCodePicture(Bitmap bitmap) {

        File folder = new File(Environment.getExternalStorageDirectory()  + "/com.pigamewallet/Record");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File qrImage = new File(Environment.getExternalStorageDirectory() + "/com.pigamewallet/Record", "niamdeaaaa" + ".jpg");
        if (qrImage.exists()) {
            qrImage.delete();
        }
        try {
            qrImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(qrImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
