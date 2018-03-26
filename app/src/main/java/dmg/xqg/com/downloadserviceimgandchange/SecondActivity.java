package dmg.xqg.com.downloadserviceimgandchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qhsj on 2018/3/26.
 */

public class SecondActivity extends Activity {

    private static String mName = "chris02";
    private static String folderName = "/com.pigamewallet/Record/";
    private TextView textView;
    private TextView tvJump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = (TextView) findViewById(R.id.tv);
        tvJump = (TextView) findViewById(R.id.tvJump);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this,"下载中",Toast.LENGTH_LONG).show();
                handleImg();
            }
        });

        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,ThirdActivity.class));
            }
        });

    }

    private void handleImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                download("http://pic.4j4j.cn/upload/pic/20130617/55695c3c95.jpg");
            }
        }).start();
    }


    private boolean download(String imageUrl) {

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int size = conn.getContentLength();// 获取总长度
            boolean success = false;

            //创建下载中文件并写入
            File file = new File(getFilePath(imageUrl) + ".dl");
            if (file.exists()) {
                file.delete();
            }
            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                inputStream = conn.getInputStream();

                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024*2];
                int lenght = 0 ;
                int sum = 0;
                while (-1 !=(lenght= inputStream.read(buffer))) {
                    sum+=lenght;
                    outputStream.write(buffer,0,lenght);
                }
                //下载完成后重命名去掉下载中状态的后缀名
                if (sum > 0 && sum == size){
                    file.renameTo(new File(getFilePath(imageUrl)));
                    success = true;
                    Log.e("chris","下载成功：sum==" + sum);
                    final int finalSum = sum;
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SecondActivity.this,"下载成功：sum==" + finalSum,Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            conn.disconnect();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String getFilePath(String url) {
        return Environment.getExternalStorageDirectory().getPath()
                + folderName + mName + ".jpg";
    }



}
