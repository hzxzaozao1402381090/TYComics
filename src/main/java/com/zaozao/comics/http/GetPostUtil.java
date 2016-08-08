package com.zaozao.comics.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class GetPostUtil {


	public static String sendGet(String url, String params){
		
		BufferedReader br = null;
		String result = "";
		
		String urlName = url + "?"+params;
		URL readUrl;
		try {
			readUrl = new URL(urlName);
			URLConnection conn = readUrl.openConnection();
			//��������
			conn.connect();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = br.readLine())!=null){
				result += "\n"+line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
