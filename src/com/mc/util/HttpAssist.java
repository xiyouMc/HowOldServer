package com.mc.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * �ϴ�ͷ��
 * @author Administrator
 * 2014-8-6
 */
public class HttpAssist {  
    private static final String TAG = "uploadFile";  
    private static final int TIME_OUT = 10 * 10000000; // ��ʱʱ��  
    private static final String CHARSET = "utf-8"; // ���ñ���  
    public static final String FAILURE = "0";  
  
    public static String uploadFile(File file) {  
        String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������  
        String PREFIX = "--", LINE_END = "\r\n";  
        String CONTENT_TYPE = "multipart/form-data"; // ��������  
         
        try {  
        	 String RequestURL = "http://how-old.net/Home/Analyze?isTest=False";
            URL url = new URL(RequestURL);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            conn.setReadTimeout(TIME_OUT);  
            conn.setConnectTimeout(TIME_OUT);  
            conn.setDoInput(true); // ����������  
            conn.setDoOutput(true); // ���������  
            conn.setUseCaches(false); // ������ʹ�û���  
            conn.setRequestMethod("POST"); // ����ʽ  
            conn.setRequestProperty("Charset", CHARSET); // ���ñ���  
            conn.setRequestProperty("connection", "keep-alive");  
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="  
                    + BOUNDARY);  
            if (file != null) {  
                /** 
                 * ���ļ���Ϊ�գ����ļ���װ�����ϴ� 
                 */  
                OutputStream outputSteam = conn.getOutputStream();  
  
                DataOutputStream dos = new DataOutputStream(outputSteam);  
                StringBuffer sb = new StringBuffer();  
                sb.append(PREFIX);  
                sb.append(BOUNDARY);  
                sb.append(LINE_END);  
                /** 
                 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ� 
                 * filename���ļ������֣�������׺���� ����:abc.png 
                 */  
  
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""  
                        + file.getName() + "\"" + LINE_END);  
                sb.append("Content-Type: application/octet-stream; charset="  
                        + CHARSET + LINE_END);  
                sb.append(LINE_END);  
                dos.write(sb.toString().getBytes());  
                InputStream is = new FileInputStream(file);  
                byte[] bytes = new byte[1024];  
                int len = 0;  
                while ((len = is.read(bytes)) != -1) {  
                    dos.write(bytes, 0, len);  
                }  
                is.close();  
                dos.write(LINE_END.getBytes());  
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)  
                        .getBytes();  
                dos.write(end_data);  
                dos.flush();  
                /** 
                 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ���� 
                 */  
                int res = conn.getResponseCode();  
                if (res == 200) {  
                	
                	// ��ȡ��������
            		StringBuffer buffer = new StringBuffer();
            		System.out.println("���ص���");
            		try {
            			BufferedReader br = new BufferedReader(new InputStreamReader(
            					conn.getInputStream(), "UTF-8"));
            			String temp;
            			while ((temp = br.readLine()) != null) {
            				buffer.append(temp);
            			}
            		} catch (Exception e) {
            			e.printStackTrace();
            		}

            		String result = buffer.toString();
            		result = "{"+buffer.substring(buffer.indexOf("\"Faces"),buffer.length()-1);
            		result = result.replace("\\", "");
            		System.out.println(result);
            		return result;
                    //return SUCCESS;  
                }  
            }  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return FAILURE;  
    }  
}  
