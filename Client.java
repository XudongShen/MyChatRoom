package com.sxd.mychatserver.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException{
		
		System.out.println("���������ƣ�");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));;
		String name = br.readLine();
		System.out.println("�����뷿������");
		String roomName = br.readLine(); 
		if(name==null||name.isEmpty()||roomName==null||roomName.isEmpty()){
			return ;
		}
		
		Socket client = new Socket("115.159.123.157",6665);//Զ�̷������ĵ�ַ��ͬʱָ�����ӵĶ˿ں�
		//Socket client = new Socket("localhost",6665);
		new Thread(new Send(client,name,roomName)).start();//���������߳�
		new Thread(new Receive(client)).start();//���������߳�
		
	}
}

