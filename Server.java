package com.sxd.mychatserver.gui;

/**
 * 服务器上所跑的程序
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private List<MyChannel> all = new ArrayList<MyChannel>();
	
	public static void main(String[] args) throws IOException{
		/*ServerSocket server = new ServerSocket(6665);
		
		DataInputStream dis = new DataInputStream(client.getInputStream());
		DataOutputStream dos = new 	DataOutputStream(client.getOutputStream());

		String msg = dis.readUTF();
		dos.writeUTF("get msg: " + msg);
		dos.flush();*/
		new Server().start();
		
	}
	
	public void start() throws IOException{
		ServerSocket server = new ServerSocket(6665);
		while(true){
			Socket client = server.accept(); 
			MyChannel channel = new MyChannel(client);
			all.add(channel);
			new Thread(channel).start();
		}
	}
	
	private class MyChannel implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning = true;
		private String name;
		private String roomName;
		private SimpleDateFormat df;
		private String time;
		
		public MyChannel(Socket client){
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				this.name = dis.readUTF();
				this.roomName = dis.readUTF();
				df = new SimpleDateFormat("HH:mm:ss ");
				time = df.format(System.currentTimeMillis());
				this.send(time+"系统消息: 欢迎进入" + roomName + "聊天室"+"@"+this.name+getCustomerName());
				this.sendOthers(this.name + "进入聊天室"+"@"+this.name, true);
			} catch (IOException e) {
				//e.printStackTrace();
				isRunning = false;
				CloseUtil.closeAll(dis,dos);
				all.remove(this);
				time = df.format(System.currentTimeMillis());
				this.sendOthers(this.name + "离开了聊天室", true);
			}
		}
		
		private String receive(){
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				//e.printStackTrace();
				isRunning = false;
				CloseUtil.closeAll(dis,dos);
				all.remove(this);
				this.sendOthers(this.name + "离开了聊天室", true);
			} 
			return msg;
		}
		
		private void send(String msg){
			if(msg==null||msg.isEmpty()){
				return ;
			}
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				//e.printStackTrace();
				isRunning = false;
				CloseUtil.closeAll(dis,dos);
				all.remove(this);
				this.sendOthers(this.name + "离开了聊天室", true);
			}
		}
		
		private void sendOthers(String msg, boolean sys){
			if(msg.startsWith("@")&&msg.contains(" ")){
				String name = msg.substring(1, msg.indexOf(" "));
				String content = msg.substring(msg.indexOf(" ")+1);
				for(MyChannel other:all){
					if(other.name.equals(name)){
						time = df.format(System.currentTimeMillis());
						other.send(time+this.name+"对你悄悄地说:\n"+content);
						this.send(time+"你对"+name+"悄悄地说:\n"+content);
					}
				}
			}
			else{
				for(MyChannel other:all){
					if(!other.roomName.equals(this.roomName))
						continue;
					if(sys){
						time = df.format(System.currentTimeMillis());
						other.send(time+"系统消息: "+ msg + getCustomerName());
					}
					else{
						if(!msg.trim().isEmpty())
							time = df.format(System.currentTimeMillis());
							other.send(time +this.name + ":\n"+ msg);
					}
				}
			}
		}
		
		private String getCustomerName(){
			String result = "";
			for(MyChannel other:all){
				if(!other.roomName.equals(this.roomName))
					continue;
				result+="@"+other.name;
			}
			return result;
		}
		
		@Override
		public void run() {
			while(isRunning){
				sendOthers(receive(), false);
			}
		}
		
	}
}
