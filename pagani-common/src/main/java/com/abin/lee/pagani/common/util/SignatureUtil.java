package com.abin.lee.pagani.common.util;

import java.util.*;
import java.util.Map.Entry;

public class SignatureUtil {

	public static String signature(Map<String,String> params, String secret){
		List<String> list = new ArrayList<String>();
		Iterator<Entry<String, String>> iter = params.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Entry<String, String> entry = (Entry<String, String>) iter.next();
		    String key = entry.getKey(); 
		    Object val = entry.getValue();
		    list.add(key + "=" + val);//(1)connecting all parameters
		} 
        Collections.sort(list);//(2)sort all strings
        StringBuilder buf = new StringBuilder();
        for (String s : list) {
            buf.append(s);
        }
        buf.append(secret);//(3)append secret key
        return MD5Util.md5(buf.toString());
	}
	
	public static void main(String[] args) {
        String secret = "c60de7822a0cee7d6bd152bc9c985000"; //secret key (32bit)
		Map<String,String> map = new HashMap<String, String>();
		map.put("key2", "1");
		map.put("key1", "2");
		String str = SignatureUtil.signature(map, secret);
		System.out.println(str);
		str = SignatureUtil.signature(map, secret);
		System.out.println(str);
		str = SignatureUtil.signature(map, secret);
		System.out.println(str);
		str = SignatureUtil.signature(map, secret);
		System.out.println(str);
		str = SignatureUtil.signature(map, secret);
		System.out.println(str);
	}
	
}
