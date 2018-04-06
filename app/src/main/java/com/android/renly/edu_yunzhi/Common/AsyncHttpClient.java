package com.android.renly.edu_yunzhi.Common;

import org.jsoup.Connection;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncHttpClient extends SyncHttpClient {
    private final ExecutorService threadPool;

    public AsyncHttpClient() {
        super();
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void get(final String url, final ResponseHandler handler) {
        threadPool.execute(() -> request(url, Connection.Method.GET, null, handler));
    }

    @Override
    public void post(final String url, final Map<String, String> map, final ResponseHandler handler) {
        threadPool.execute(() -> request(url, Connection.Method.POST, map, handler));
    }

    @Override
    public void head(final String url, final Map<String, String> map, final ResponseHandler handler) {
        threadPool.execute(() -> request(url, Connection.Method.HEAD, map, handler));
    }

    @Override
    public void uploadImage(final String url, Map<String, String> map, String imageName, byte[] imageData, final ResponseHandler handler) {
        threadPool.execute(() -> super.uploadImage(url, map, imageName, imageData, handler));
    }
}
