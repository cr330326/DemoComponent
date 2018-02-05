package com.cryallen.common.function.comlib.net.xutil;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

import com.cryallen.common.function.comlib.utils.StringUtils;

public class XUtilHelper {
	private static final String TOKEN = "token";
	private static final int TIME_OUT = 50000;

	/**
	 * 发送get请求
	 * @param <T>
	 */
	public static <T> Cancelable Get(String url,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 带有token值发送get请求
	 * @param <T>
	 */
	public static <T> Cancelable GetWithToken(String url,String token,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		params.addHeader(TOKEN,token);
		params.setConnectTimeout(TIME_OUT);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 发送post请求
	 * @param <T>
	 */
	public static <T> Cancelable Post(String url,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 带有token值发送post请求
	 * @param <T>
	 */
	public static <T> Cancelable PostWithToken(String url,String token,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(StringUtils.isNotEmpty(token)){
			params.addHeader(TOKEN,token);
		}
		params.setConnectTimeout(TIME_OUT);
		if(null != map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 发送post请求
	 * @param <T>
	 */
	public static <T> Cancelable PostByJson(String url,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		params.setAsJsonContent(true);
		JSONObject jsonRequest = new JSONObject();
		if(null != map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				try {
					jsonRequest.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		params.setBodyContent(jsonRequest.toString());
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 带有token值发送post请求
	 * @param <T>
	 */
	public static <T> Cancelable PostWithTokenByJson(String url,String token,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(StringUtils.isNotEmpty(token)){
			params.addHeader(TOKEN,token);
		}
		params.setConnectTimeout(TIME_OUT);
		params.setAsJsonContent(true);
		JSONObject jsonRequest = new JSONObject();
		if(null != map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				try {
					jsonRequest.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		params.setBodyContent(jsonRequest.toString());
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 发送get同步请求
	 */
	public static String GetSync(String url, Map<String, Object> map) {
		RequestParams params = new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		Logger.d(new Object[] {url, map});
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		String result = null;
		try {
			if (null == x.http()) {
				return "";
			}
			String ret = x.http().getSync(params, String.class);
			if (null == ret)
			{
				return "";
			}
			result = ret.toString();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送post同步请求
	 */
	public static String PostSync(String url, Map<String, Object> map) {
		RequestParams params = new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		Logger.d(new Object[] {url, map});
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		String result = null;
		try {
			if (null == x.http()) {
				return "";
			}
			String ret = x.http().postSync(params, String.class);
			if (null == ret)
			{
				return "";
			}
			result = ret.toString();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return result;
	}

	/**
	 * 上传文件
	 * @param <T>
	 */
	public static <T> Cancelable UpLoadFile(String url, Map<String,Object> map, Map<String, File> file, CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		if (file != null && !map.isEmpty()) {
			for (Map.Entry<String, File> entry : file.entrySet()) {
				params.addBodyParameter(entry.getKey(), entry.getValue().getAbsoluteFile());
			}
		}
		params.setMultipart(true);
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}

	/**
	 * 下载文件
	 * @param <T>
	 */
	public static <T> Cancelable DownLoadFile(String url,String filepath,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		params.setConnectTimeout(TIME_OUT);
		//设置断点续传
		params.setAutoResume(true);
		params.setSaveFilePath(filepath);
		Cancelable cancelable = x.http().post(params, callback);
		return cancelable;
	}
}
