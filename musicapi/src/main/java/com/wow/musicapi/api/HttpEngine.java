package com.wow.musicapi.api;

import okhttp3.*;
import com.wow.musicapi.util.DnsHelper;
import com.wow.musicapi.util.TextUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haohua on 2018/2/11.
 */
public class HttpEngine {

    private final static class OkHttpClientHolder {
        public static OkHttpClient proxyInstance;

        public static final OkHttpClient instance = new OkHttpClient.Builder().dns(new Dns() {
            @Override
            public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                List<InetAddress> systemResult = systemDns(hostname);
                if ("127.0.0.1".equals(hostname)) {
                    return systemResult;
                }
                ArrayList<InetAddress> finalResult = new ArrayList<>();
                for (InetAddress address : systemResult) {
                    if (!"127.0.0.1".equals(address.getHostAddress())) {
                        finalResult.add(address);
                    }
                }
                if (finalResult.isEmpty()) {
                    try {
                        List<String> list = DnsHelper.resolveIp(hostname);
                        for (String ip : list) {
                            if (!TextUtils.isEmpty(ip)) {
                                InetAddress address = InetAddress.getByName(ip);
                                finalResult.add(address);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return finalResult;
            }
        })
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        private static final List<InetAddress> systemDns(String hostname) throws UnknownHostException {
            if (hostname == null) throw new UnknownHostException("hostname == null");
            try {
                return Arrays.asList(InetAddress.getAllByName(hostname));
            } catch (NullPointerException e) {
                UnknownHostException unknownHostException =
                        new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
                unknownHostException.initCause(e);
                throw unknownHostException;
            }
        }
    }

    public static OkHttpClient getHttpClient(boolean useProxy) {
        return useProxy ? OkHttpClientHolder.proxyInstance : OkHttpClientHolder.instance;
    }

    public static void setProxy(String host, int port) {
        if (host != null && port > 0) {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));
            if (OkHttpClientHolder.proxyInstance == null || !proxy.equals(OkHttpClientHolder.proxyInstance.proxy())) {
                OkHttpClientHolder.proxyInstance = OkHttpClientHolder.instance.newBuilder().proxy(proxy).build();
            }
        }
    }

    public static Response requestSync(Request request, boolean useProxy) throws IOException {
        return getHttpClient(useProxy).newCall(request).execute();
    }

    public static void requestAsync(Request request, Callback callback, boolean useProxy) {
        getHttpClient(useProxy).newCall(request).enqueue(callback);
    }
}
