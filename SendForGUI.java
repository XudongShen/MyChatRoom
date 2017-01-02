package com.sxd.mychatserver.gui;

/**
 * Ϊͼ�ν���Ŀͻ���ר�Ÿ�д�ķ����߳�
 */

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendForGUI {

	private DataOutputStream dos;
	private String name;
	private String roomName;
	
	public SendForGUI(Socket client, String name, String roomName){
		try {
			dos = new DataOutputStream(client.getOutputStream());
			this.name = name;
			this.roomName = roomName;
			send(name);
			send(roomName);
		} catch (IOException e) {
			//e.printStackTrace();
			CloseUtil.closeAll(dos);
		}
	
	}
	
	public void send(String msg){
		try {
			if(msg!=null&&!msg.isEmpty()&&!msg.trim().isEmpty()){
				dos.writeUTF(msg);
				dos.flush();
			}
		} catch (IOException e) {
			//e.printStackTrace();
			CloseUtil.closeAll(dos);
		}
	}
	
	public void close(){
		CloseUtil.closeAll(dos);
	}
	
}
