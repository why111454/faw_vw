package com.fax.utils.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * 方便使用http请求的工具类
 * 
 */
public class HttpUtils {
//	static final String connectFail="错误：连接失败";
//	static final String NetWorkError="错误：网络异常";
	static private DefaultHttpClient httpClient;
    private static final int DEFAULT_MAX_CONNECTIONS = 10;//最大连接数
    private static final int DEFAULT_SOCKET_TIMEOUT = 8 * 1000;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;//8K
    
    private static final int DEFAULT_TRY_TIME =2;
    private static final int DEFAULT_RETRY_DELAY = 1000;
    public static boolean DEBUG = false;
	public static synchronized DefaultHttpClient getHttpClient() {
	    if(httpClient == null) {
//            httpClient = (DefaultHttpClient) new AsyncHttpClient().getHttpClient();

	        HttpParams httpParams = new BasicHttpParams();

            // timeout: get connections from connection pool
            ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
            // timeout: connect to the server
            HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
            // timeout: transfer data from server
            HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT/2);

	        // set max connections per host
	        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
	        // set max total connections
	        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

	        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

            HttpConnectionParams.setTcpNoDelay(httpParams, true);
//	        HttpClientParams.setRedirecting(httpParams, false);

	        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

	        // scheme: http and https
	        SchemeRegistry schemeRegistry = new SchemeRegistry();
	        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	        ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

	        httpClient = new DefaultHttpClient(manager, httpParams);
            httpClient.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_TRY_TIME, DEFAULT_RETRY_DELAY));

	    }
	    return httpClient;  
	}
	/**使用WebView的Cookie机制 */
	public static void setWebViewCookieStore(Context context){
		getHttpClient().setCookieStore(new WebviewCookieStore(context));
	}
	public static void clearCookies(){
		getHttpClient().getCookieStore().clear();
	}
	public static int reqStatusCodeForPost(String postUrl,Map<String,ContentBody> map){
        HttpRequestBase httpPost = RequestFactory.createPost(postUrl,map);
		return executeForStatusCode(httpPost);
	}
	public static String reqForPost(String postUrl,Map<String,ContentBody> map){
        if(TextUtils.isEmpty(postUrl)) return null;
        HttpRequestBase httpPost = RequestFactory.createPost(postUrl,map);
		return executeForString(httpPost);
	}
	public static String reqForPost(String postUrl, NameValuePair... pairs){
        if(TextUtils.isEmpty(postUrl)) return null;
        HttpRequestBase httpPost = RequestFactory.createPost(postUrl, pairs);
        return executeForString(httpPost);
	}
	public static String reqForPost(String postUrl, List<NameValuePair> params) {
        if(TextUtils.isEmpty(postUrl)) return null;
        HttpRequestBase httpPost = RequestFactory.createPost(postUrl, params);
        return executeForString(httpPost);
    }
	public static String reqForPut(String postUrl) {
        if(TextUtils.isEmpty(postUrl)) return null;
        HttpRequestBase httpPost = RequestFactory.createPost(postUrl);
        return executeForString(httpPost);
	}
	public static String reqForGet(String getURL, List<NameValuePair> params) {
		if(TextUtils.isEmpty(getURL)) return null;
		HttpRequestBase httpGet = RequestFactory.createGet(getURL, params);
		return executeForString(httpGet);
	}
    public static String reqForGet(String getURL) {
        if(TextUtils.isEmpty(getURL)) return null;
        HttpRequestBase httpGet = RequestFactory.createGet(getURL);
        return executeForString(httpGet);
    }
    /**返回code，请求异常为-1 */
	public static int executeForStatusCode(HttpRequestBase httpRequest){
		for(int i=0;i< DEFAULT_TRY_TIME;i++){
			try {
				return executeForStatusCodeImp(httpRequest);
			} catch (Exception e) {
				if(DEBUG){
					Log.e("fax", "execute error:"+httpRequest.getURI());
					e.printStackTrace();
				}
                try {
                    Thread.sleep(DEFAULT_RETRY_DELAY);
                } catch (Exception e2) {
                    return -1;//break
                }
			}
		}
//        httpRequest.abort();
		return -1;
	}
	private static int executeForStatusCodeImp(HttpRequestBase httpRequestBase) throws Exception{
			int code = getHttpClient().execute(httpRequestBase).getStatusLine().getStatusCode();
			if(DEBUG) Log.d("fax", "execute Code:" + code);
			return code;
	}
	public static String executeForString(HttpRequestBase httpRequest){
        StringResponse response = execute(httpRequest);
        if(response!=null) return response.getContent();
		return null;
	}
    public static StringResponse execute(HttpRequestBase httpRequest){
        for(int i=0;i< DEFAULT_TRY_TIME;i++){
            try {
				return executeImp(httpRequest);
            } catch (Exception e) {
                if(DEBUG){
                    Log.e("fax", "execute error:"+httpRequest.getURI());
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(DEFAULT_RETRY_DELAY);
                } catch (Exception e2) {
                    return null;//break
                }
            }
        }
//        httpRequest.abort();
        return null;
    }
	@SuppressLint("DefaultLocale")
    private static StringResponse executeImp(HttpRequestBase httpRequest) throws Exception {
        httpRequest.setHeader("Accept-Encoding", "gzip");
        HttpResponse httpResponse = getHttpClient().execute(httpRequest);

        HttpEntity entity = httpResponse.getEntity();
        InputStream is = entity.getContent();
        //gzip 流已设置在http中
        try {
            String encoding = httpResponse.getEntity().getContentEncoding().getValue().toLowerCase();
            if (encoding.equals("gzip")) is = new GZIPInputStream(is);
        } catch (Exception e) {
        }
        String strResult = readInputStream(new InputStreamReader(is));
        int code = httpResponse.getStatusLine().getStatusCode();
        if (code != HttpStatus.SC_OK && DEBUG) {
            Log.d("fax", "execute may Fail,Code:" + httpResponse.getStatusLine().getStatusCode() + ",Entity:" + strResult);
        }
        entity.consumeContent();
        try {
            is.close();
        } catch (Exception ignore) {
        }
//        httpRequest.abort();
        return new StringResponse(strResult, code);
    }

    /**
	 *  * 从输入流中读入数据  *   * @param request  * @param response  * @param s  
	 */
	public static String readInputStream(InputStreamReader in) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(in);
			char[] temp=new char[1024];
			int length;
			while ((length = reader.read(temp)) != -1) {
				if(Thread.currentThread().isInterrupted()) break;
				sb.append(temp,0,length);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return (sb.toString());
	}

    public static File reqForDownload(String url, File file, boolean continueDown, DownloadListener listener){
        return reqForDownload(new HttpGet(url), file, continueDown, listener);
    }
    public static File reqForDownload(HttpRequestBase httpRequest, File file, boolean continueDown, DownloadListener listener){
        try {
            if(file==null) file = File.createTempFile("temp_"+httpRequest.getURI().hashCode(), null);
            long loaded = 0;
            if(continueDown && file.isFile()){//断点续传
                if(file.length()>0){
                    loaded = file.length();
                    httpRequest.setHeader("Range", "bytes="+file.length()+"-");
                }
            }

            HttpResponse httpResponse = getHttpClient().execute(httpRequest);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code != HttpStatus.SC_OK && DEBUG) {
                Log.d("fax", "execute may fail,Code:" + httpResponse.getStatusLine().getStatusCode() );
            }
            HttpEntity entity = httpResponse.getEntity();
            InputStream is = entity.getContent();
            try {
                FileOutputStream fos = new FileOutputStream(file, continueDown);
                byte[] temp=new byte[DEFAULT_SOCKET_BUFFER_SIZE];
                int length;
                long total = loaded + entity.getContentLength();

                while ((length = is.read(temp)) != -1) {
                    if(Thread.currentThread().isInterrupted()) break;
                    fos.write(temp, 0, length);
                    loaded+=length;
                    if(listener!=null) listener.onDownloading(loaded, total);
                }
                fos.close();
                if(listener!=null) listener.onDownloadFinish(file);
                return file;
            } catch (Exception e) {
                if(DEBUG) e.printStackTrace();
                if(listener!= null) listener.onDownloadError(e.getMessage());
            }finally{
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            entity.consumeContent();

//            httpRequest.abort();
        } catch (Exception e) {
            if(DEBUG) e.printStackTrace();
            file.delete();
            if(listener!= null) listener.onDownloadError(e.getMessage());
        }
        return null;
    }
    public interface DownloadListener{
        public void onDownloadFinish(File file);
        public void onDownloading(long loaded, long total);
        public void onDownloadError(String error);
    }
    /**
     * Checks the InputStream if it contains  GZIP compressed data
     *
     * @param inputStream InputStream to be checked
     * @return true or false if the stream contains GZIP compressed data
     * @throws java.io.IOException
     */
    public static boolean isInputStreamGZIPCompressed(final PushbackInputStream inputStream) throws IOException {
        if (inputStream == null)
            return false;

        byte[] signature = new byte[2];
        int readStatus = inputStream.read(signature);
        inputStream.unread(signature);
        int streamHeader = ((int) signature[0] & 0xff) | ((signature[1] << 8) & 0xff00);
        return readStatus == 2 && GZIPInputStream.GZIP_MAGIC == streamHeader;
    }
}
