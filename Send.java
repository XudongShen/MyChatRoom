package com.sxd.mychatserver.gui;

/**
 * 客户端所使用的发送线程
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Send implements Runnable {
	private BufferedReader console;
	private DataOutputStream dos;
	private String name;
	private String roomName;
	private boolean isRunning = true;
	
	public Send(){
		console = new BufferedReader (new InputStreamReader(System.in)); 
	}
	
	public Send(Socket client){
		this();
		try {
			dos = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dos,console);
		}
		
	}
	
	public Send(Socket client, String name) {
		this(client);
		this.name = name;
		send(name);
	}
	
	public Send(Socket client, String name, String roomName){
		this(client, name);
		this.roomName = roomName;
		send(roomName);
	}

	private String getMsgFromConsole(){
		try {
			return console.readLine();
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dos,console);
		}
		return "";
	}
	
	public void send(String msg){
		try {
			if(msg!=null&&!msg.isEmpty()&&!msg.trim().isEmpty()){
				dos.writeUTF(msg);
				dos.flush();
			}
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dos,console);
		}
	}
	
	@Override
	public void run() {
		while(isRunning){
			send(getMsgFromConsole());
		}	
	}

}
