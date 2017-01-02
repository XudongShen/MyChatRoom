package com.sxd.mychatserver.gui;

/**
 * 用于关闭各种通信通道
 */

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
	public static void closeAll(Closeable... io){
		for(Closeable temp:io){
			if(temp!=null){
				try {
					temp.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	}
}
