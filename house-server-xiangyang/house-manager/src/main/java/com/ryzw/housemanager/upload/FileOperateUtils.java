package com.ryzw.housemanager.upload;

import com.google.common.io.Files;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件相关操作
 *
 * @author
 * @ClassName: FileOperateUtils
 * @Description:
 * @date 2018年7月4日 下午2:30:07
 */
@Slf4j
public class FileOperateUtils {
    /**
     * 将上传的文件进行重命名
     *
     * @param name
     * @return
     * @author
     */
    private static String rename(String name) {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid;
        if (StringUtils.isNotEmpty(name)) {
            fileName += "." + Files.getFileExtension(name);
        }
        return fileName;
    }

    /**
     * 文件上传
     *
     * @param request
     * @param uploadDir
     * @return
     * @throws Exception
     * @author
     */
    public static List<Url> upload(HttpServletRequest request, String uploadDir) throws Exception {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<Url> listFileName = new ArrayList<>();
        if (MapUtils.isNotEmpty(fileMap)) {
            for (MultipartFile mFile : fileMap.values()) {
                Url url = new Url();
                String fileName = mFile.getOriginalFilename();
                String storeName = rename(fileName);
                String noZipName = uploadDir + storeName;
                FileOutputStream outputStream = new FileOutputStream(noZipName);
                FileCopyUtils.copy(mFile.getInputStream(), outputStream);
                url.setRenameFile(storeName);
                url.setOriginalFile(fileName);
                listFileName.add(url);
            }
        }
        return listFileName;
    }

    /**
     * 文件上传--省平台
     *
     * @param request
     * @param uploadDir
     * @return
     * @throws Exception
     * @author
     */
    public static List<Url> unitUpload(HttpServletRequest request, String uploadDir) throws Exception {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<Url> listFileName = new ArrayList<>();
        if (MapUtils.isNotEmpty(fileMap)) {
            for (MultipartFile mFile : fileMap.values()) {
                Url url = new Url();
                String fileName = mFile.getOriginalFilename();
                String storeName = fileName;
                String noZipName = uploadDir + storeName;
                FileOutputStream outputStream = new FileOutputStream(noZipName);
                FileCopyUtils.copy(mFile.getInputStream(), outputStream);
                url.setRenameFile(storeName);
                url.setOriginalFile(fileName);
                listFileName.add(url);
            }
        }
        return listFileName;
    }

    /**
     * 文件下载
     *
     * @param response
     * @param downLoadIdList
     * @param fileName
     */
    public static void fileToZip(HttpServletResponse response, List<DownLoadDto> downLoadIdList, String fileName) {
        try {
            response.setHeader("Content-Disposition", "attachment; fileName=" + fileName + ";filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("File", URLEncoder.encode(fileName, "utf-8"));
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            UrlFilesToZip s = new UrlFilesToZip();
            if (downLoadIdList.size() > 1) {
                for (DownLoadDto downLoadDto : downLoadIdList) {
                    byte[] buffer = new byte[1024];
                    URL url = new URL(downLoadDto.getPath());
                    String path = downLoadDto.getPath();
                    zos.putNextEntry(new ZipEntry(path.substring(path.lastIndexOf("/") + 1)));
                    if (isUrl(path)) {
                        buffer = s.getImageFromURL(path);
                        zos.write(buffer, 0, buffer.length);
                    } else {
                        InputStream fis = url.openConnection().getInputStream();
                        int r = 0;
                        while ((r = fis.read(buffer)) != -1) {
                            zos.write(buffer, 0, r);
                        }
                        fis.close();
                    }
                    zos.flush();
                }
                zos.close();
            } else {
                byte[] buff = new byte[2048];
                String path = downLoadIdList.get(0).getPath();
                if (isUrl(path)) {
                    buff = s.getImageFromURL(path);
                    bis = new BufferedInputStream(new ByteArrayInputStream(buff));
                    bos = new BufferedOutputStream(response.getOutputStream());
                    int bytesRead;
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                    bos.flush();
                } else {
                    bis = new BufferedInputStream(new FileInputStream(downLoadIdList.get(0).getPath()));
                    bos = new BufferedOutputStream(response.getOutputStream());
                    int bytesRead;
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                    bos.flush();
                }
            }
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }

        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 判断是否是网路路径
     *
     * @param urlString
     * @return
     */
    public static boolean isUrl(String urlString) {
        String regex = "^(http?|https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

}