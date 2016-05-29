package com.abin.lee.pagani.common.http.test;

import com.abin.lee.pagani.common.http.HttpClientUtil;
import com.abin.lee.pagani.common.util.SignatureUtil;
import com.google.common.collect.Maps;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: tinkpad
 * Date: 16-5-29
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public class HttpAsyncClientTest {
    private static final String httpURL ="http://172.16.2.132:8000/push/send";
    @Test
    public void testHttpAsyncClient(){
        CloseableHttpAsyncClient httpAsyncClient = HttpClientUtil.getHttpAsyncClient();
        try {
            httpAsyncClient.start();
            HttpPost httpPost = new HttpPost(httpURL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("targetType", "user"));
            Map<String, String> request = Maps.newHashMap();
            for (Iterator<NameValuePair> iterator = nvps.iterator(); iterator.hasNext();) {
                NameValuePair nav = (NameValuePair) iterator.next();
                request.put(nav.getName(), nav.getValue());
            }
            String token = SignatureUtil.signature(request, "11111");
            nvps.add(new BasicNameValuePair("token", token));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            Future<HttpResponse> future = httpAsyncClient.execute(httpPost, null);
            HttpResponse response = future.get();
            System.out.println("Response: " + response.getStatusLine());
            System.out.println("Response Data: " + EntityUtils.toString(response.getEntity()));
            System.out.println("Shutting down");
        } catch(Exception e){
             e.printStackTrace();
        }finally {
            try {
                httpAsyncClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done");
    }
}
