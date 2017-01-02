package com.sxd.mychatserver.gui;

/**
 * һ����Ϊ�򵥵Ŀͻ��˽���
 */

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame{
	public static void main(String[] args){
		ClientGUI frame = new ClientGUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	JLabel labYourName;
	JLabel labRoomName;
	JButton btnChangeName;
	JButton btnEnterRoom;
	JButton btnSend;
	JButton btnTips;
	JTextField textYourName;
	JTextField textRoomName;
	JTextArea textReceive;
	JTextArea textSend;
	JTextArea textCustomerName;
	JPanel blank;
	JScrollPane scroll1;
	JScrollPane scroll2;
	JScrollPane scroll3;
	
	String yourName;
	String roomName;
	
	SendForGUI sendThread;
	Receive receiveThread;
	
	public ClientGUI(){
		init();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(600,600);
		this.setLocation((dim.width - this.getWidth()) / 2, (dim.height - this.getHeight()) / 2);
		this.setVisible(true);
		this.setTitle("��ͷ����");
	}
	
	public void init(){
		labYourName = new JLabel("������Ĵ���");
		labRoomName = new JLabel("�������ǽ�ͷ�İ��ţ�");
		btnChangeName = new JButton("�޸Ĵ���");
		btnEnterRoom = new JButton("ȷ�ϰ���");
		btnTips = new JButton("С��ʾ");
		btnSend = new JButton("���ͣ�Alt+Enter��");
		textYourName = new JTextField();
		textRoomName = new JTextField();
		blank = new JPanel();
		textReceive = new JTextArea();
		textSend = new JTextArea();
		textCustomerName = new JTextArea();
		
		textReceive.setLineWrap(true);
		textReceive.setWrapStyleWord(true);
		
		textSend.setLineWrap(true);
		textSend.setWrapStyleWord(true);
		
		scroll1 = new JScrollPane(textReceive);
		scroll2 = new JScrollPane(textSend);
		scroll3 = new JScrollPane(textCustomerName);
			
		btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER); 
		
		btnChangeName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(textYourName.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "��������Ĵ���");
				}
				else{
					yourName = textYourName.getText();
					JOptionPane.showMessageDialog(null, "�޸ĳɹ������ڸ��İ���ʱ��Ч");
				}
			}	
		});
		
		btnEnterRoom.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(textYourName.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "��������Ĵ���");
				}
				else if(textRoomName.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "������ǿյİ���");
				}
				else if(textRoomName.getText().equals(roomName)){
					JOptionPane.showMessageDialog(null, "���Ѿ�������");
				}
				else{
					try {
						if(sendThread!=null){
							sendThread.close();
						}
						yourName = textYourName.getText();
						roomName = textRoomName.getText();
						Socket client = new Socket("115.159.123.157",6665);
						//Socket client = new Socket("localhost",6665);
						sendThread = new SendForGUI(client,yourName,roomName);
						receiveThread = new Receive(client,textReceive,textCustomerName);
						new Thread(receiveThread).start();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "��ͷʧ��");
					}
					
				}
				
			}
			
		});
		
		btnTips.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "ʹ����ʾ��\n    ����д���Լ��Ĵ���\n    Ȼ��д���С��������õİ��Ų�ȷ��\n    Ȼ��Ϳ�������������ͷ��\n\n�緿�����Ļ����ܣ�\n    ����@���� ��Ϣ��ע��ո�");		
			}
			
		});
		
		btnSend.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(roomName == null||roomName.isEmpty()){
					JOptionPane.showMessageDialog(null, "����ȷ�ϸ�����");
				}
				else if(textSend.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "������ǿյ���Ϣ����");
				}
				else{
					sendThread.send(textSend.getText());
					textSend.setText("");
					textSend.requestFocus();
				}
				
			}
			
		});
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		
		this.add(textYourName);
		this.add(btnChangeName);
		this.add(textRoomName);
		this.add(btnEnterRoom);
		this.add(scroll1);
		this.add(scroll3);
		this.add(scroll2);
		this.add(btnTips);
		this.add(blank);
		this.add(btnSend);
		
		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		
		s.insets.set(20, 20, 0, 0);
		s.gridwidth = 5;
		s.weightx = 1;
		s.weighty = 0;
		layout.setConstraints(textYourName, s);
		
		s.insets.set(20, 10, 0, 20);
		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(btnChangeName, s);
		
		s.insets.set(5, 20, 0, 0);
		s.gridwidth = 5;
		s.weightx = 1;
		s.weighty = 0;
		layout.setConstraints(textRoomName, s);
		
		s.insets.set(5, 10, 0, 20);
		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(btnEnterRoom, s);
		
		textReceive.setEditable(false);
		s.insets.set(20, 20, 10, 0);
		s.gridwidth = 5;
		s.gridheight = 5;
		s.weightx = 0;
		s.weighty = 0.6;
		layout.setConstraints(scroll1, s);
		
		textReceive.setEditable(false);
		s.insets.set(20, 10, 10, 20);
		s.gridwidth = 0;
		s.gridheight = 5;
		s.weightx = 0;
		s.weighty = 0.6;
		layout.setConstraints(scroll3, s);
		
		s.insets.set(10, 20, 10, 20);
		s.gridwidth = 0;
		s.gridheight = 3;
		s.weightx = 1;
		s.weighty = 0.4;
		layout.setConstraints(scroll2, s);
		
		s.insets.set(10, 20, 20, 20);
		s.gridwidth = 1;
		s.gridheight = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(btnTips, s);
		
		s.insets.set(0, 0, 0, 0);
		s.gridwidth = 4;
		//s.gridheight = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(blank, s);
		
		s.insets.set(10, 10, 20, 20);
		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(btnSend, s);
		
	}
}
