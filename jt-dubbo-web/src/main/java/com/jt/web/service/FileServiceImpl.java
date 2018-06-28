package com.jt.web.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;

@Service
public class FileServiceImpl implements FileService {
	
	@Value("${localpath}")
	private String LPath;

	@Value("${urlpath}")
	private String uPath;
	
	/**
	 * 文件上传的步骤：
	 * 	1.判断是否为图片 png,jpg,gif
	 * 	2.判断是否为非法程序 通过ImageBuffer对象
	 * 	3.为了让图片检索快速，我们使用分文件夹存储
	 * 	4.应该尽可能让我们的图片名称不一致
	 * 	5.将文件进行写盘的操作
	 * 	6.url和本地的文件路径的关系
	 * 		url作用：
	 * 			通过用户访问虚拟路径，获取图片的资源
	 * 结论：
	 * 	1.本地的磁盘路径是保存图片的物理地址
	 * 	2.url是用户访问图片的虚拟地址
	 */
	
	@Override
	public PicUploadResult uploadFile(MultipartFile uploadFile) {
		PicUploadResult result = new PicUploadResult();
		// 1.获取文件名称
		String fileName = uploadFile.getOriginalFilename();
		// 2.判断是否为图片类型
		if (!fileName.matches("^.*(jpg|png|gif)$")) {
			result.setError(1);// 表示不是一个图片
			return result;
		}
		// 3.判断是否为恶意程序
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			// 4.获取图片的高度和宽度
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			
			if (height == 0 || width == 0) {
				// 表示不是图片
				result.setError(1);
				return result;
			}
			// 通过程序执行到这里，表示图片正常
			// 5.定义本地磁盘的路径
//			String LPath = "E:/jt-upload/";
			// 6.采用时间的格式，分文件存储yyyy/MM/dd/HH
			String dataPath = new SimpleDateFormat("yyyy/MM/dd/HH").format(new Date());
			// 7.拼接保存的文件夹路径
			String filePath = LPath + dataPath;
			// 8.判断文件夹是否存在
			File file = new File(filePath);
			if (!file.exists()) {// 不存在
				file.mkdirs(); // 创建文件夹
			}
			// 9.编辑文件名，让文件名称尽可能不一致
			String fileType = fileName.substring(fileName.lastIndexOf(".")); // 获取后缀
			String uuid = UUID.randomUUID().toString().replace("-", ""); // 使用UUID
			String realPath = filePath + "/" + uuid + fileType; // 形成文件的完整的路径
			// 10.写盘操作
			uploadFile.transferTo(new File(realPath));
			// 11.封装虚拟路径，用于图片的展现
//			String uPath = "http://image.jt.com/";
			String realUrlPath = uPath + dataPath + "/" + uuid + fileType;
			result.setUrl(realUrlPath);
			result.setHeight(height + "");
			result.setWidth(width + "");
			return result;
		} catch (Exception e) {
			result.setError(1); // 表示非法的图片
			return result;
		}
	}
	
	
}
