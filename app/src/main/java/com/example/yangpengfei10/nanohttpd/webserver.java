package com.example.yangpengfei10.nanohttpd;

import android.content.Context;

import com.socks.library.KLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static android.R.attr.data;

/**
 * Created by yangpengfei10 on 2018/5/19.
 */

public class webserver extends NanoHTTPD {
    private Context context;

    /**
     * Constructs an HTTP server on given port.
     *
     * @param port
     */
    public webserver(Context context, int port) {
        super(port);
        this.context = context;

    }

    /**
     * Constructs an HTTP server on given hostname and port.
     *
     * @param hostname
     * @param port
     */
    public webserver(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        KLog.d(Thread.currentThread());
        KLog.d(session.getHeaders());
        KLog.d(session.getUri());
        KLog.d(session.getInputStream());
        KLog.d(session.getMethod());
        KLog.d(session.getRemoteIpAddress());
        KLog.d(session.getParameters());
        KLog.d(session.toString());

        try {
            session.parseBody(new HashMap<String, String>());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }

        if (session.getUri().equals("/updatedata")) {
            KLog.i("updatedata");
            return responseUpdataDisP(session);
        }

        return newFixedLengthResponse(getHTMLStringFromAssets(context, "settings.html"));

    }

    public static String getHTMLStringFromAssets(Context mContext, String fileName) {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedInputStream = null;
//        File file = new File("file:///android_asset/settings.html");
        try {
            inputStream = mContext.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedInputStream = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedInputStream.readLine()) != null) {
                stringBuilder.append(line + "\r\n");
            }

            inputStreamReader.close();
            bufferedInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

        return stringBuilder.toString();
    }

    private Response responseUpdataDisP(IHTTPSession session) {

        String data1 = getParams(session, "data1");
        String data2 = getParams(session, "data2");
        try {
            data1 = new String(data1.getBytes(), 0, data1.length(), "utf-8");
            data2 = new String(data2.getBytes(), 0, data2.length(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        KLog.i(data1);
        KLog.i(data2);

        updataListener.updata(data1 + data2);




        return newFixedLengthResponse(getHTMLStringFromAssets(context, "ok.html"));


    }

    public static String getParams(IHTTPSession session, String key) {
        if (session.getParameters().get(key) != null) {
            return decodePercent(session.getParameters().get(key).get(0));
        }
        return null;
    }

    private UpdataListener updataListener;

    public void setUpdataListener (UpdataListener udl) {
        this.updataListener = udl;
    }

    public interface UpdataListener {
        void updata(String data);
    }
}


