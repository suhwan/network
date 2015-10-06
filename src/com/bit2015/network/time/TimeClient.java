package com.bit2015.network.time;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TimeClient {
	private static final String SERVER_IP = "192.168.1.88";
	private static final  int SERVER_PORT = 60000;
	private static final int BUFFER_SIZE = 2048;
	
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		
		
		
			byte[] data = "".getBytes();
			try
			{
				//1 UDP 소켓 생성 클라이언트는 포트번호 안넣는다 .
				datagramSocket = new DatagramSocket();
				
				DatagramPacket datagramPacket = new DatagramPacket(data, data.length, new InetSocketAddress(SERVER_IP,SERVER_PORT));
				//2.packet 보내기
				datagramSocket.send(datagramPacket);
				
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);
				
				String dateTime = new String(receivePacket.getData(),0,receivePacket.getLength(),"UTF8");
				log(dateTime);
				
				datagramSocket.close();
			}
			 catch(IOException e)
			{
				 log("error : " + e);
			}
			
			//3.data 받기 
	}
	
	public static void log(String log)
	{
		System.out.println("[Time_Client] "+log);
	}
}
