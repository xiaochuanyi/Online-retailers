package mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import mall.commom.utils.FastDFSClient;

/*
 * 图片上传的controller
 */
@Controller
public class PictureController {
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	@RequestMapping("/pic/upload")
	@ResponseBody
	public Map uploadFile(MultipartFile uploadFile){
		//把图片上传到图片服务器
		try {
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//得到一个图片地址和文件名
			//取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".")+1);
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(),extName) ;
			//补充为完整的url
			url = IMAGE_SERVER_URL+url;
			//封装到map中返回
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("message", "图片上传失败");
			return result;
		}
		
	}
}
