package com.ryzw.housemanager.config;

import com.ryzw.housemanager.exception.ServiceException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


/**
 * <p>
 * 道邦外部接口认证
 * </p>
 *
 * @author yz
 * @since 2019/2/22
 */
@Slf4j
public class AssertFeignConfiguration {

    @Bean
    public AssertRequestInterceptor assertRequestInterceptor() {
        return new AssertRequestInterceptor();
    }


    public class AssertRequestInterceptor implements RequestInterceptor {
        /**
         * 系统分配的接口产品编号
         */
        @Value("${daoBang.appKey}")
        private String appKey;

        /**
         * 系统分配的接口密钥
         */
        @Value("${daoBang.appSecret}")
        private String appSecret;

        @Override
        public void apply(RequestTemplate requestTemplate) {
            String nonce = getRandomString();
            long timeStamp = System.currentTimeMillis() / 1000;
            String urlString = this.getSignature(timeStamp, nonce).toString();
            String bodyString =requestTemplate.body() != null ? new String(requestTemplate.body()):StringUtils.EMPTY;
            String params = urlString + "signature=" + this.createSignature(urlString) +
                    (StringUtils.isNotEmpty(bodyString) ? "&" + bodyString : StringUtils.EMPTY);
            requestTemplate.body(params);
            requestTemplate.header("hostIP", this.getHostIp());
        }


        /**
         * 生成加密字符串
         *
         * @param urlString
         * @return
         */
        private String createSignature(String urlString) {
            try {
                String arrUrl = urlString + "appSecret=" + appSecret;
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.update(arrUrl.getBytes());
                //获取字节数组
                byte messageDigest[] = digest.digest();

                String shaString = byteArrayToHex(messageDigest);


                MessageDigest messageDigests = MessageDigest.getInstance("MD5");
                byte[] inputByteArray = shaString.toString().getBytes();
                //inputByteArray是输入字符串转换得到的字节数组
                messageDigests.update(inputByteArray);
                //转换并返回结果，也是字节数组，包含16个元素
                byte[] resultByteArray = messageDigests.digest();
                //字符数组转换成字符串返回
                return byteArrayToHex(resultByteArray).toUpperCase();
            } catch (Exception e) {
                log.error("error",e);
                throw new ServiceException(e.getMessage());
            }
        }

        /**
         * 获取需要加密的字符串
         *
         * @param timeStamp
         * @param nonce
         * @return
         */
        private StringBuilder getSignature(long timeStamp, String nonce) {
            Map<String, Object> stringMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            stringMap.put("appKey", appKey);
            stringMap.put("timeStamp", timeStamp);
            stringMap.put("nonce", nonce);
            StringBuilder stringBuilder = new StringBuilder();
            stringMap.forEach((k, v) -> stringBuilder.append(k + "=" + v + "&"));
            return stringBuilder;
        }


        /**
         * byte数组转字符串
         *
         * @param byteArray
         * @return
         */
        private String byteArrayToHex(byte[] byteArray) {
            //首先初始化一个字符数组，用来存放每个16进制字符
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            //new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符）
            char[] resultCharArray = new char[byteArray.length * 2];
            //遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
            int index = 0;
            for (byte b : byteArray) {
                resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
                resultCharArray[index++] = hexDigits[b & 0xf];
            }

            //字符数组组合成字符串返回
            return new String(resultCharArray);
        }

        /**
         * 随机生成16位字符串
         *
         * @return
         */
        private String getRandomString() {
            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                int number = random.nextInt(62);
                sb.append(str.charAt(number));
            }
            return sb.toString();
        }

        /**
         * 获取本机IP
         *
         * @return
         */
        private String getHostIp() {
            try {
                InetAddress ia =  ia = InetAddress.getLocalHost();
                String localip = ia.getHostAddress();
                return localip;
            } catch (Exception e) {
               log.error("error",e);
            }
            return null;
        }
    }


}
