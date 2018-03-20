package tk.hongbo.socket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    BufferedWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect(); //开启连接
    }

    private void connect() {
        try {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();
            SocketFactory factory = client.socketFactory();
            Socket socket = factory.createSocket("192.168.204.86", 8888);
            socket.setKeepAlive(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                Log.d(TAG, str);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送json数据
     *
     * @param jsonStr
     */
    public void sendData(String jsonStr) {
        if (writer != null) {
            try {
                writer.write(jsonStr);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
