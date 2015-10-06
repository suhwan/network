package com.bit2015.network.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {

	private static final String SERVER_IP = "192.168.1.88";
	private static final  int SERVER_PORT = 60000;
	private static final int BUFFER_SIZE = 1024;
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		Scanner scan = null;
		try {
			//1 UDP 소켓 생성 클라이언트는 포트번호 안넣는다 .
			datagramSocket = new DatagramSocket();
			scan = new Scanner(System.in , "euc-kr");
			while(true)
			{
				System.out.print(">>");
				String message = scan.nextLine();
				byte[] data = message.getBytes();
				//2.packet 보내기 
				DatagramPacket datagramPacket = new DatagramPacket(data, data.length,new InetSocketAddress(SERVER_IP,SERVER_PORT));
				
				datagramSocket.send(datagramPacket);
				
				//3.data 받기 
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				if("exit".equals(message))
				{
					break;
				}
				datagramSocket.receive(receivePacket);
				String receiveMessage = new String (receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
				log(receiveMessage);
				
			}
			
			datagramSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("error :" + e);
		}

	}
	
	public static void log(String log)
	{
		System.out.println("[UDP_Client] " + log);
	}
}
