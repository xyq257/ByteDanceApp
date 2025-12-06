// WebSocketManager.java (新文件)
package com.example.firsttry.remote.socket;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

public class WebSocketManager {

    private static final String TAG = "WebSocketManager";
    private static final String WS_URL = "ws://10.0.2.2:8080/websocket/";
    private static final int RECONNECT_INTERVAL = 5000; // 5秒重连间隔

    private static volatile WebSocketManager instance;

    private OkHttpClient client;
    private WebSocket webSocket;
    private String token;
    private boolean isConnected = false;
    private boolean isConnecting = false;

    // 监听器列表
    private final List<WebSocketListener> listeners = new ArrayList<>();

    // 私有构造函数
    private WebSocketManager() {
        client = new OkHttpClient.Builder()
                .pingInterval(30, TimeUnit.SECONDS) // 设置心跳
                .build();
    }

    // 获取单例实例
    public static WebSocketManager getInstance() {
        if (instance == null) {
            synchronized (WebSocketManager.class) {
                if (instance == null) {
                    instance = new WebSocketManager();
                }
            }
        }
        return instance;
    }

    // 连接 WebSocket
    public void connect(String token) {
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token is null or empty, cannot connect.");
            return;
        }
        this.token = token;

        if (isConnected || isConnecting) {
            Log.d(TAG, "Already connected or connecting.");
            return;
        }
        isConnecting = true;
        Log.d(TAG, "Connecting to WebSocket with token...");
        notifyStatusChanged("正在连接...");

        Request request = new Request.Builder()
                .url(WS_URL + token)
                .build();

        webSocket = client.newWebSocket(request, new okhttp3.WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                isConnected = true;
                isConnecting = false;
                Log.d(TAG, "WebSocket 已连接");
                notifyStatusChanged("已连接");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "收到原始消息: " + text);
                notifyMessageReceived(text);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket 正在关闭: " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                isConnected = false;
                isConnecting = false;
                Log.d(TAG, "WebSocket 已关闭: " + reason);
                notifyStatusChanged("已断开");
                // 可以在这里尝试重连
                // reconnect();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                isConnected = false;
                isConnecting = false;
                Log.e(TAG, "WebSocket 连接失败", t);
                notifyStatusChanged("连接失败");
                // 可以在这里尝试重连
                // reconnect();
            }
        });
    }

    // 断开 WebSocket
    public void disconnect() {
        if (webSocket != null) {
            Log.d(TAG, "Disconnecting WebSocket.");
            webSocket.close(1000, "User disconnected");
            webSocket = null;
            isConnected = false;
            isConnecting = false;
        }
    }

    // 发送消息
    public void sendMessage(String text) {
        if (webSocket != null && isConnected) {
            webSocket.send(text);
        } else {
            Log.e(TAG, "Cannot send message, WebSocket is not connected.");
        }
    }

    // 添加监听器
    public void addListener(WebSocketListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    // 移除监听器
    public void removeListener(WebSocketListener listener) {
        listeners.remove(listener);
    }

    // 通知所有监听器收到新消息
    private void notifyMessageReceived(String text) {
        for (WebSocketListener listener : new ArrayList<>(listeners)) { // 复制列表以避免并发修改异常
            if (listener != null) {
                listener.onWebSocketMessage(text);
            }
        }
    }

    // 通知所有监听器状态变化
    private void notifyStatusChanged(String status) {
        for (WebSocketListener listener : new ArrayList<>(listeners)) {
            if (listener != null) {
                listener.onWebSocketStatusChanged(status);
            }
        }
    }
}