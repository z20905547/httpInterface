package vfh.httpInterface.commons;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.log4j.Logger;

public class JsonUtils {
//	private static Logger logger = Logger.getLogger(JsonUtils.class);
	private static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 把对象转换成Json格式，支持JavaBean、Map、List
	 * @param obj
	 * @return
	 * @throws java.io.IOException
	 */
	public static String convertToString(Object obj) throws IOException {
		//通过jackson转换为json值
		ObjectMapper om = new ObjectMapper();
		return om.writeValueAsString(obj);
	}
	
	/**
	 * 把Json字符串读取成对象格式
	 * @param <T>
	 * @param str
	 * @param cla
	 * @return
	 * @throws java.io.IOException
	 */
	public static <T> T readToObject(String str, Class<T> cla) throws IOException{
		ObjectMapper om = new ObjectMapper();
		return om.readValue(str, cla);
	}

	/**
	 *
	 * @param json
	 * @param cla
	 * @param <T>
	 * @return
	 */
	public static <T> T readValue(String json,Class<T> cla){
		T t = null;
		if(mapper == null){
			mapper = new ObjectMapper();
		}
		try {
			t = mapper.readValue(json, cla);
		} catch (Exception e) {
//			logger.error("JSON转换异常",e);
		}
		return  t;
	}
	public static String writeValueAsString(Object obj){
		String res = "";
		if(mapper == null){
			mapper = new ObjectMapper();
		}
		try {
			res = mapper.writeValueAsString(obj);
		} catch (Exception e) {
//			logger.error("JSON转换异常",e);
		}
		return res;
	}



	public static String JSONPCallback(String callback,Map res){
		StringBuffer sb = new StringBuffer(callback);
		sb.append("(").append(writeValueAsString(res)).append(")");
		return sb.toString();
	}

}
