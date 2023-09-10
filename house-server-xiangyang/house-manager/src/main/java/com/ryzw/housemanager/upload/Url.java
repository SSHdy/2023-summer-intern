package com.ryzw.housemanager.upload;

import lombok.Data;

/**
 * 文件上传中，设置url
 */
@Data
public class Url {
	/**原图片名
	 * 
	 */
	private String OriginalFile;
	
	/**
	 *重命名 
	 */
	private String renameFile;

}
