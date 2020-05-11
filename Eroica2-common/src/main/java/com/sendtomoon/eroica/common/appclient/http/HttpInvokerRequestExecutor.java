package com.sendtomoon.eroica.common.appclient.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.util.StringUtils;

public class HttpInvokerRequestExecutor extends AbstractHttpInvokerRequestExecutor implements InitializingBean {

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (10 * 1000);

	private CloseableHttpClient httpClient;

	private PoolingHttpClientConnectionManager clientConnectionManager;

	private RequestConfig requestConfig;

	private RequestConfig.Builder requestConfigBuilder;

	public HttpInvokerRequestExecutor() {
		clientConnectionManager = new PoolingHttpClientConnectionManager();
		requestConfigBuilder = RequestConfig.custom();
		setReadTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS);
	}

	private RequestConfig getRequestConfig() {
		return requestConfig;
	}

	@Override
	protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
			throws Exception {
		HttpPost request = createPostRequest(config);
		setRequestBody(config, request, baos);
		CloseableHttpResponse response = executePostMethod(config, getHttpClient(), request);
		try {
			validateResponse(config, response);
			InputStream responseBody = getResponseBody(config, response);
			try {
				return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
			} finally {
				if (responseBody != null)
					responseBody.close();
			}
		} finally {
			if (response != null)
				response.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		httpClient = HttpClientBuilder.create().setConnectionManager(clientConnectionManager).build();
		requestConfig = requestConfigBuilder.build();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void setMaxConnectionsPerHost(int maxHostConnections) {
		clientConnectionManager.setDefaultMaxPerRoute(maxHostConnections);
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		clientConnectionManager.setMaxTotal(maxTotalConnections);
	}

	public void setConnectTimeout(int timeout) {
		requestConfigBuilder.setConnectTimeout(timeout);
	}

	public void setReadTimeout(int timeout) {
		requestConfigBuilder.setSocketTimeout(timeout);
	}

	protected HttpPost createPostRequest(HttpInvokerClientConfiguration config) throws IOException {
		HttpPost request = new HttpPost(config.getServiceUrl());
		LocaleContext locale = LocaleContextHolder.getLocaleContext();
		if (locale != null) {
			request.addHeader(HTTP_HEADER_ACCEPT_LANGUAGE, StringUtils.toLanguageTag(locale.getLocale()));
		}
		if (isAcceptGzipEncoding()) {
			request.addHeader(HTTP_HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}
		request.addHeader(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_SERIALIZED_OBJECT);
		//
		request.setConfig(this.getRequestConfig());
		return request;
	}

	protected void setRequestBody(HttpInvokerClientConfiguration config, HttpPost request, ByteArrayOutputStream baos)
			throws IOException {
		request.setEntity(new ByteArrayEntity(baos.toByteArray()));
	}

	protected CloseableHttpResponse executePostMethod(HttpInvokerClientConfiguration config,
			CloseableHttpClient httpClient, HttpPost request) throws IOException {
		return httpClient.execute(request);
	}

	protected void validateResponse(HttpInvokerClientConfiguration config, HttpResponse response) throws HttpException {
		StatusLine status = response.getStatusLine();
		if (status != null && status.getStatusCode() >= 300) {
			throw new HttpException("Did not receive successful HTTP response: status code = " + status.getStatusCode()
					+ ", status message = [" + status.getReasonPhrase() + "]");
		}
	}

	protected InputStream getResponseBody(HttpInvokerClientConfiguration config, HttpResponse response)
			throws IOException {
		InputStream is = (response == null || response.getEntity() == null ? null : response.getEntity().getContent());
		if (is != null) {
			if (isGzipResponse(response)) {
				return new GZIPInputStream(is);
			} else {
				return is;
			}
		}
		return null;
	}

	protected boolean isGzipResponse(HttpResponse response) {
		Header encodingHeader = response.getFirstHeader(HTTP_HEADER_CONTENT_ENCODING);
		return (encodingHeader != null && encodingHeader.getValue() != null
				&& encodingHeader.getValue().toLowerCase().contains(ENCODING_GZIP));
	}

}
