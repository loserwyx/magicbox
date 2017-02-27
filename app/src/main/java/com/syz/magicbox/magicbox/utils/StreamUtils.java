package com.syz.magicbox.magicbox.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	/**
	 * 流转字符串
	 * @param stream
	 * @return
	 */
	public static String streamToString(InputStream stream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int temp = -1;
			while ((temp = stream.read(buffer)) != -1) {
			       baos.write(buffer, 0, temp);			
			}
			return baos.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				baos.close();
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
