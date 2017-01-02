package com.sxd.mychatserver.gui;

/**
 * 客户端所使用的接收线程
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

public class Receive implements Runnable{
	private DataInputStream dis;
	private boolean isRunning = true;
	private JTextArea textReceive;
	private JTextArea textCustomerName;

	Receive(){
		
	}
	
	Receive(Socket client){
		try {
			dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
	}
	
	Receive(Socket client, JTextArea textReceive, JTextArea textCustomerName){
		this(client);
		this.textReceive = textReceive;
		this.textCustomerName = textCustomerName;
	}
	
	private String receive(){
		String msg = "";
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
			//e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
		return msg;
	}
	
	@Override
	public void run() {
		while(isRunning){
			String msg = receive();
			String[] temps = msg.split(":");
			if(temps.length==4){
				if(temps[3].trim().isEmpty())
					continue;
			}
			if(msg.contains("@")&&msg.contains("系统消息: ")){
				String[] customerName = msg.split("@");
				msg = customerName[0];
				textCustomerName.setText("");
				for(int i=1;i<customerName.length;i++){
					textCustomerName.append(customerName[i]+"\n");
				}
				textCustomerName.setCaretPosition(textCustomerName.getText().length());
			}
			if(!msg.isEmpty()){
				if(textReceive.getText().isEmpty()){
					textReceive.setText(msg+"\n");
					textReceive.setCaretPosition(textReceive.getText().length());
				}
				else{
					textReceive.append("\n"+msg+"\n");
					textReceive.setCaretPosition(textReceive.getText().length());
				}
			}
		}
	}

}
