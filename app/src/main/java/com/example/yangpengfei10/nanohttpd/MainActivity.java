package com.example.yangpengfei10.nanohttpd;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.socks.library.KLog;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private webserver webserver;
    private TextView textView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                KLog.i();
                String data = (String) msg.obj;
                textView.setText(data);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);
        webserver = new webserver(getApplicationContext(), 8080);
        if (webserver.isAlive()) {
            webserver.stop();
        }

        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            webserver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        webserver.setUpdataListener(new webserver.UpdataListener() {
            @Override
            public void updata(String data) {

                if (handler != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = data;
                    handler.sendMessage(msg);
                }


            }
        });


    }
}
