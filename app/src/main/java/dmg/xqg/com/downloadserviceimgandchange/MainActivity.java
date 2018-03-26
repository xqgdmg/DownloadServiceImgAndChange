package dmg.xqg.com.downloadserviceimgandchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String mName = "chris01";
    private static String folderName = "/com.pigamewallet/Record/";
    private TextView tvJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.tv);
        tvJump = (TextView) findViewById(R.id.tvJump);
        imageView = (ImageView) findViewById(R.id.iv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImg();
            }
        });

        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });

        // 第一次进来先显示上一次下载好的，再下载，
        // 有变化才去下载？后台给个标志位？
        Bitmap bitmapDownloaded = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + folderName + mName + ".jpg");// 这里多个斜杠的
        if (bitmapDownloaded != null){
            imageView.setImageBitmap(bitmapDownloaded);
        }

    }

    private void handleImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getImgAndSave01("http://pic.4j4j.cn/upload/pic/20130617/55695c3c95.jpg");
            }
        }).start();
    }

    /**
     *
     * 这种是最不提倡的做法，bitmap是不建议的
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public void getImgAndSave01(String imageurl) {
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

            // 显示
            final Bitmap finalBitmap = bitmap;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(finalBitmap);
                }
            });

            // 保存
            savePicture(bitmap,mName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePicture(Bitmap bitmap,String name) {

        File folder = new File(Environment.getExternalStorageDirectory()  + "/com.pigamewallet/Record");
        if (!folder.exists()) {// 文件夹
            folder.mkdirs();
        }
        final File qrImage = new File(folder, name + ".jpg");
        if (qrImage.exists()) {// 每次覆盖掉之前的
            qrImage.delete();
        }
        try {
            qrImage.createNewFile();// 创建新的
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
