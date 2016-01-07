package vfh.httpInterface.commons;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

public class JsonFileUtil {
	private static Map<String,String> conflict = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		
//		String rootPath = JsonFileUtil.class.getResource ("/").getPath();
//		System.out.println(rootPath);
//		
//		String JsonContext = new JsonFileUtil().ReadFile(rootPath.replace("WEB-INF/classes/", "staticFileRoot/")+"public/uncheck/city-alias.json");
//		JSONObject obj = JSONObject.fromObject(JsonContext);
//		for (Iterator iter = obj.keys(); iter.hasNext();) { //先遍历整个 people 对象
//			String key = (String)iter.next();
//			System.out.println(key+":"+obj.getString(key));
//		}
		
		
		 System.out.println(JsonFileUtil.getConflictByCity("济南"));
	}

	public String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
	//获取冲突城市
	public static String getConflictByCity(String city){
		
		if(conflict==null){
			String rootPath = JsonFileUtil.class.getResource ("/").getPath();
			String JsonContext = new JsonFileUtil().ReadFile(rootPath.replace("WEB-INF/classes/", "staticFileRoot/")+"public/uncheck/conflict-city.json");
			JSONObject obj = JSONObject.fromObject(JsonContext);
			conflict = new HashMap<String,String>();
			for (Iterator iter = obj.keys(); iter.hasNext();) { //先遍历整个 people 对象
				String key = (String)iter.next();
				conflict.put(key, obj.getString(key));
			} 
		}
		return  conflict.get(city)==null?"":conflict.get(city);
	}
}
