package com.wind.external.sample;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Getter
@Slf4j
public class RestSampleExternalService {


    @Value("${external.service.sample.proxy-host:#{null}}")
    private String proxyHost;

    @Value("${external.service.sample.proxy-port:#{null}}")
    private Integer proxyPort;

    @Bean
    @ConfigurationProperties(prefix = "external.service.sample.connection")
    public HttpComponentsClientHttpRequestFactory coreServiceHttpRequestFactory(
            PoolingHttpClientConnectionManager coreServiceHttpClientConnectionManager) {
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(coreServiceHttpClientConnectionManager)
                .setConnectionManagerShared(true);
        if (!StringUtils.isEmpty(proxyHost) && proxyPort != null) {
            builder.setProxy(new HttpHost(proxyHost, proxyPort));
        }
        CloseableHttpClient closeableHttpClient = builder.build();
        return new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
    }

    @Bean
    public RestTemplate sampleServiceRestTemplate(
            HttpComponentsClientHttpRequestFactory coreServiceHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(coreServiceHttpRequestFactory);
        return restTemplate;
    }

    @Bean
    @ConfigurationProperties(prefix = "rest.core-service.connection-pool")
    public PoolingHttpClientConnectionManager coreServiceHttpClientConnectionManager()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new PoolingHttpClientConnectionManager(getSocketFactoryRegistry());
    }

    public static Registry<ConnectionSocketFactory> getSocketFactoryRegistry()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = SSLContexts.custom();
        builder.loadTrustMaterial(null, (chain, authType) -> true);
        SSLContext sslContext = builder.build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                (s, sslSession) -> s.equalsIgnoreCase(sslSession.getPeerHost()));
        return RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("https", sslSocketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
    }
}
