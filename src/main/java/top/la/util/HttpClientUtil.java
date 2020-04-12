package top.la.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import static org.apache.commons.codec.CharEncoding.UTF_8;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/12 2:06
 * @Description
 */


public class HttpClientUtil {
    private static int SocketTimeout = Integer.parseInt("30000");   //3秒
    private static int ConnectTimeout = Integer.parseInt("30000");  //3秒
    private static Boolean SetTimeOut = true;

    /**
     *
     * @param url 爬取的链接
     * @param params 请求的参数
     * @param headers 请求头
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        String responseBody = "";
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) {
                    sb.append("?");
                    first = false;
                } else {
                    sb.append("&");
                }
                String value = entry.getValue() == null ? "" : entry.getValue();
                sb.append(entry.getKey()).append("=").append(value);
            }
        }
        HttpGet httpGet = new HttpGet(sb.toString());
        if (SetTimeOut) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(SocketTimeout)
                    .setConnectTimeout(ConnectTimeout).build(); // 设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
        }
        if(headers != null){
            headers.forEach(httpGet::addHeader);
        }
        try (
                CloseableHttpClient httpClient= HttpClients.createDefault()
                //支持https CloseableHttpClient httpClient = getHttpClient();
        ){
            // 请求数据
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine());
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity, UTF_8);
            } else {
                throw new RuntimeException(" " + status);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseBody;
    }

    private static CloseableHttpClient getHttpClient(List<Header> header) {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        // 指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        // 构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).
                setRetryHandler(new DefaultHttpRequestRetryHandler()).
                setDefaultHeaders(header).
                build();
    }
}
