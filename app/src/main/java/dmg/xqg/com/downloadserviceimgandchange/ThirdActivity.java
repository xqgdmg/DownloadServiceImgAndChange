package dmg.xqg.com.downloadserviceimgandchange;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qhsj on 2018/3/26.
 */

public class ThirdActivity extends Activity {

    private String mName = "chris03";
    private static String folderName = "/com.pigamewallet/Record/";
    private TextView textView;
    private TextView tvJump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        textView = (TextView) findViewById(R.id.tv);
        tvJump = (TextView) findViewById(R.id.tvJump);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImg();
            }
        });

        tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void handleImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ThirdActivity.this,"开始下载。。",Toast.LENGTH_LONG).show();
                download("http://pic.4j4j.cn/upload/pic/20130617/55695c3c95.jpg");
            }
        }).start();
    }


    private void download(String imageUrl) {
        Request request = new Request.Builder().url(imageUrl).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                Toast.makeText(ThirdActivity.this,"下载失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[8192];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = initDir();
                try {
                    is = response.body().byteStream();// 转换成字节流
                    long total = response.body().contentLength();// 文件总长度
                    File file = new File(savePath, mName + ".jpg");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        Log.e("chris","progress==" + progress);
                    }
                    fos.flush();

                    // 下载完成
//                    listener.onDownloadSuccess();
                    Toast.makeText(ThirdActivity.this,"下载完成",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
//                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String initDir() throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory() + "/com.pigamewallet/Record");
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }


}
