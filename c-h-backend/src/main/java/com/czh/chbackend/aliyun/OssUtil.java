package com.czh.chbackend.aliyun;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.exceptions.ClientException;

import java.io.*;
import java.net.URL;
import java.util.Date;

import static com.czh.chbackend.common.CommonConstant.LOCAL_SAVE_ADDRESS;
import static com.czh.chbackend.common.CommonConstant.OSS_FORMAT_LRC;

/**
 * @author czh
 * @version 1.0.0
 * 2024/5/24 15:46
 */
//@Component
public class OssUtil {

    private static final String ENDPOINT = "https://oss-cn-guangzhou.aliyuncs.com";
    private static final String BUCKET_NAME = "china-hiphop";
    private static OSS ossClient;

    // 静态代码块初始化OSSClient实例
    static {
        EnvironmentVariableCredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件 目前仅支持MP3上传
     * // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
     * // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
     * //  String filePath= "D:\\localpath\\examplefile.txt";
     */
    public static void upload(String filePath, String fileName, String type, String format) throws ClientException {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = type + "/" + fileName + format;

        URL signedUrl = null;
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, new File(filePath));
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);
            // 上传文件。
            ossClient.putObject(putObjectRequest);

            // 指定生成的签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);

            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(BUCKET_NAME, objectName, HttpMethod.GET);
            // 设置过期时间。
            request.setExpiration(expiration);

            // 通过HTTP GET请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
            System.out.println(signedUrl);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Exception ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
    }

    /**
     * 下载文件
     */
    public static void downLoad(String fileName, String type, String format) throws ClientException {
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = type + "/" + fileName + format;

        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元数据。
            OSSObject ossObject = ossClient.getObject(BUCKET_NAME, objectName);
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            if (content != null) {
                // 创建本地文件输出流
                OutputStream outputStream = new FileOutputStream(LOCAL_SAVE_ADDRESS + fileName + format);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(content);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                // 关闭流
                bufferedInputStream.close();
                outputStream.close();
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                content.close();
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Exception ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
    }

    /**
     * 删除文件
     */
    public static void delete(String fileName, String type, String format) {
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        String objectName = type + "/" + fileName + format;

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(BUCKET_NAME, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Exception ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 生成带有临时访问权限的URL
     */
    public static String generatePresignedUrl(String fileName, String type, String format) {
        // 填写Object完整路径，例如exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = type + "/" + fileName + format;

        URL signedUrl = null;
        try {
            // 指定生成的签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);

            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(BUCKET_NAME, objectName, HttpMethod.GET);
            // Set Content-Type
            if (format.equals(OSS_FORMAT_LRC)) {
                request.addQueryParameter("response-content-type", "text/plain");
            } else {
                request.addQueryParameter("response-content-type", "audio/mpeg");
            }

//            request.addUserMetadata("Content-Type", "audio/mpeg");
            // 设置过期时间。
            request.setExpiration(expiration);

            // 通过HTTP GET请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
            return signedUrl.toString();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Exception ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
        return null;
    }

    /**
     * 判断生成的url是否过期
     */
    public static boolean isUrlExpired(String urlString) {
        try {
            URL url = new URL(urlString);
            String query = url.getQuery();
            if (query == null) {
                return true; // No query parameters, assuming expired
            }

            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("Expires")) {
                    long expires = Long.parseLong(keyValue[1]);
                    long currentTime = new Date().getTime() / 1000; // Current time in seconds
                    return currentTime > expires;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true; // In case of any error, assuming expired
        }
        return true; // If "Expires" parameter is not found, assuming expired
    }
}































