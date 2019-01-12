package vfh.httpInterface.service.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import vfh.httpInterface.commons.Page;
import vfh.httpInterface.commons.PageRequest;
import vfh.httpInterface.commons.StringUtil;
import vfh.httpInterface.commons.VariableUtils;
import vfh.httpInterface.dao.resourse.ResourseDao;
import vfh.httpInterface.service.ServiceException;

/**
 * TODO 资源管理表
 * @author harry
 * <b> 有问题请联系qq:359705093</b>
 * @create 2016年1月11日
 */
@Service
@Transactional
public class ResourceService {
	@Autowired
	private ResourseDao resourseDao;
	Map<String, Object> returnMap=new HashMap<String, Object>();
	/**
	 * <p>上传保存文件</p>
	 * @param destBasedir 文件保存路径
	 * @param destFileName 保存的文件名
	 * @param srcFile 需要上传的文件
	 * @author 李尚林
	 * @throws IOException 
	 */
	public void saveFile(String destBasedir, String destFileName, File srcFile) throws IOException {
		File destFile = new File(destBasedir, destFileName);
		destFile.getParentFile().mkdirs();
		FileUtils.copyFile(srcFile, destFile);
	}

	/**
	 * <p>保存文件</p>
	 * @param destBasedir 文件保存路径
	 * @param destFileName 保存的文件名
	 * @param srcFileStream 文件二进制字节
	 * @author 李尚林
	 * @throws IOException 
	 */
	public void saveFile(String destBasedir, String destFileName, byte[] srcFileStream) throws IOException{
		File descFile = new File(destBasedir, destFileName);
		descFile.getParentFile().mkdirs();
		FileUtils.writeByteArrayToFile(descFile, srcFileStream);
	}
	
	/**
	 * <p>将文件转换为数组byte[]</p>
	 * @param file 需要转换的文件
	 * @return 文件流数组
	 * @throws IOException
	 * @author 李尚林
	 */
	public byte[] toByteArray(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		return IOUtils.toByteArray(inputStream);
	}
	
	/**
	 * <p>
	 * 保存文件
	 * </p>
	 * @param content
	 *        文件二进制字节
	 * @param fileName
	 *        需要保存的文件名
	 * @param uploadBasedir
	 *        上传路径
	 * @return 文件保存路径
	 * @throws IOException
	 * @author 李尚林
	 */
	public String saveFile(byte[] content, String uploadBasedir, String fileName) throws IOException {
		String filePath = getFilePath(uploadBasedir);
		File file = new File(filePath, fileName);
		file.getParentFile().mkdirs();
		FileUtils.writeByteArrayToFile(file, content);
		return filePath;
	}

	/**
	 * <p>
	 * 下载文件
	 * </p>
	 * @param path
	 *        文件路径
	 * @param response
	 *        响应
	 * @return 文件名
	 * @author 李尚林
	 */
	public String downloadFile(String path, HttpServletResponse response) {
		File file = new File(path);
		if ((!file.exists()) || (!file.isFile())) {
			return "file not exists";
		}
		String filename = file.getName();
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
		response.addHeader("Content-Length", "" + file.length());
		response.setContentType("application/pdf;charset=utf-8");
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			IOUtils.write(FileUtils.readFileToByteArray(file), output);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != output)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return filename;
	}

	/**
	 * <p>
	 * 删除文件
	 * </p>
	 * @param path
	 *        ,文件保存路径
	 * @return
	 * @author 李尚林
	 */
	public boolean deleteFile(String path) {
		File file = new File(path);
		if ((!file.exists()) || (!file.isFile())) {
			return false;
		} else {
			return file.delete();
		}
	}

	/**
	 * <p>
	 * 删除目录或者文件
	 * </p>
	 * @param file
	 * @return
	 * @author 李尚林
	 */
	public boolean deleteDirectory(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (!deleteDirectory(files[i])) {
					return false;
				}
			}
		} else {
			return file.delete();
		}
		return file.delete();
	}

	/**
	 * <p>
	 * 取得保存文件的相对路径
	 * </p>
	 * @param uploadBasedir
	 * @return
	 * @author 李尚林
	 */
	private String getFilePath(String uploadBasedir) {
		String now = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		return uploadBasedir + "/" + now + "/";
	}
	
	/**
	 * <p>获取11位长度的时间戳文件名</p>
	 * @return
	 * @author 李尚林
	 */
	public String generateFileName(){
		return Long.toHexString(System.currentTimeMillis());
	}
	
	/**
	 * <p>
	 * 保存上传的多个文件
	 * </p>
	 * @param content
	 *        文件二进制字节
	 * @param fileName
	 *        需要保存的文件名
	 * @param uploadBasedir
	 *        上传路径
	 * @param skuNumber
	 *        sku编号
	 * @return 文件保存路径
	 * @throws IOException
	 * @author 李尚林
	 */
	public String saveMultipleFile(byte[] content, String fileName, String uploadBasedir, String skuNumber) throws IOException {
		String filePath = uploadBasedir;
		if (null != skuNumber) {
			File arraysFile = new File(filePath, skuNumber);
			if (!arraysFile.exists()) {
				arraysFile.mkdirs();
			}
		}
		File file = new File(filePath, fileName);
		file.getParentFile().mkdirs();
		FileUtils.writeByteArrayToFile(file, content);
		return filePath;
	}

	/**
	 * <p>
	 * 删除指定文件
	 * </p>
	 * @param fileName
	 *        需要删除的文件名
	 * @param uploadBaseDir
	 *        文件路径
	 * @author 李尚林
	 */
	public void deleteFile(String fileName, String uploadBaseDir) {
		File file = new File(uploadBaseDir + fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * <p>
	 * 获取文件名后缀
	 * </p>
	 * @param fileName
	 * @return
	 * @author 李尚林
	 */
	public String getFileNamePostfix(String fileName) {
		return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
	}

	/**
	 * TODO 条件查询资源列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年3月1日
	 */
	public Page<Map<String, Object>> findResource(PageRequest pageRequest,
			Map<String, Object> filter, Model model) {
		long total = resourseDao.count(filter);
		List<Map<String, Object>> content = resourseDao
				.find(filter);
		return new Page<Map<String, Object>>(pageRequest, content, total);
		
	}
    public Map<String, Object> getPic(Long id) {
        return resourseDao.get(id);
    }
	 /**
     * 删除图片
     *
     * @param ids 用户主键 ID 集合
     */
    public void deletePics(List<Long> ids) {
        for(Long id : ids) {
          //  Map<String, Object> entity = getPic(id);
          //  System.out.println(entity.toString());
          //  if (MapUtils.isNotEmpty(entity)) {
            	deletePic(id);
          //  }
        }
    }
    
    /**
     * 删除图片
     *
     * 
     */
 
    public void deletePic(Long id) {


       
        
        resourseDao.delete(id);
    }
	/**
	 * TODO 封装列表
	 * @author harry
	 * <b> 有问题请联系qq:359705093</b>
	 * @param filter
	 * @return
	 * @create 2016年3月1日
	 */
	public Map<String, Object> findResourceList(Map<String, Object> filter){
		returnMap.clear();
		Map<String, Object> pagedata=new HashMap<String, Object>();
		//查总条数
		long total = resourseDao.count(filter);
		if(StringUtil.isNotEmptyObject(filter.get("first"))&&StringUtil.isNotEmptyObject(filter.get("last"))){
			filter.put("first", Integer.parseInt(filter.get("first").toString()));
			filter.put("last", Integer.parseInt(filter.get("last").toString()));
		}
		List<Map<String, Object>> resourceList=resourseDao.find(filter);
		if(StringUtil.isNotEmptyList(resourceList)){
			pagedata.put("total", total);
			pagedata.put("list", resourceList);
    		returnMap.put("returnCode", "000000");
    		returnMap.put("data",pagedata);
    		returnMap.put("returnMsg", "获取资源列表！");
    	}else{
    		returnMap.put("returnCode", "1111");
    		returnMap.put("returnMsg", "获取资源列表失败！");
    	}
		
		
	    return returnMap;
	}


}
