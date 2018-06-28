package com.jt.common.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientService.class);

	@Autowired(required = false)
	private CloseableHttpClient httpClient;

	@Autowired(required = false)
	private RequestConfig requestConfig;

	// Get请求
	public String doGet(String uri, Map<String, String> params, String charset) throws URISyntaxException {
		// uri构造器
		URIBuilder builder = new URIBuilder(uri);
		
		// 判断是否含有参数
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				builder.addParameter(param.getKey(), param.getValue());
			}
		}
		
		// 为空则设置编码
		if (StringUtils.isEmpty(charset)) {
			charset = "UTF-8";
		}
		
		// 构造Get请求对象
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setConfig(requestConfig);
		
		// 发送数据
		try {
			// 执行请求并返回数据
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			// 判断返回的数据是否正确
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 正确后封装为JSON数据
				String result = EntityUtils.toString(httpResponse.getEntity(), charset);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// 重载方法
	public String doGet(String uri, Map<String, String> params) throws URISyntaxException {
		return doGet(uri, params, null);
	}
	// 重载方法二
	public String doGet(String uri) throws URISyntaxException {
		return doGet(uri, null);
	}

	// Post请求
	public String doPost(String uri, Map<String, String> params, String charset) throws UnsupportedEncodingException {
		
		// 定义提交方式
		HttpPost httpPost = new HttpPost(uri);
		// 定义字符编码
		if (StringUtils.isEmpty(charset)) {
			charset = "UTF-8";
		}
		
		// 判断param
		if (params != null) {
			
			// 表单中需要一个list集合
			List<NameValuePair> parameters = new ArrayList<>();
			
			for (Map.Entry<String, String> param : params.entrySet()) {
				// 对象创建需要key,value,所以遍历params集合
				BasicNameValuePair nameValuePair = new BasicNameValuePair(param.getKey(), param.getValue());
				// parameters需要值,故创建BasicNameValuePair对象
				parameters.add(nameValuePair);
			}
			
			// post提交需要一个表单
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, charset);
			// 添加请求中的数据
			httpPost.setEntity(entity);
		}
		
		// 执行请求，接收数据
		try {
			// 执行并接收
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			// 判断接收数据的正确性
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 正确后封装数据
				String result = EntityUtils.toString(httpResponse.getEntity(), charset);
				// 返回JSON格式的字符串 给页面
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	// 重载1
	public String doPost(String uri, Map<String, String> params) throws UnsupportedEncodingException {
		return doPost(uri, params, null);
	}
	// 重载2
	public String doPost(String uri) throws UnsupportedEncodingException {
		return doPost(uri, null, null);
	}
	
}
