package com.zaozao.comics.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class URLTest {
	
	public static final String APP_KEY = "ea4e645c80bf657fa98b247bbcaf1696";

	public static final String TEST_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=218.4.255.255";
	public static void main(String[] args) {
		new Thread(){
			@Override
			public void run() {
				URL url;
				try {
					url = new URL("http://www.weather.com.cn/data/sk/101010100.html");
					String text = getJsonStr(url);
					System.out.println(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}.start();
	}

	public static String getJsonStr(URL url) throws IOException{
		
		StringBuffer result = new StringBuffer();
		InputStream is = url.openStream();
		byte[] buffer = new byte[1024];
		int temp = 0;
		while((temp = is.read(buffer))!=-1){
			result.append( new String(buffer, 0, temp));
		}
		is.close();
		return result.toString();
	}
}
