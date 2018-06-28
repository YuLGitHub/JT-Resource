package com.jt.web.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropUtils {
	
	private static Properties prop=new Properties();
	
	static{
		try {
			String path=PropUtils.class.getClassLoader().getResource("properties/merchantInfo.properties").getPath();
			prop.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private PropUtils(){}
	
	public static Properties getProp(){
		return prop;
	}
	
	public static String getProp(String key){
		return prop.getProperty(key);
	}

}
