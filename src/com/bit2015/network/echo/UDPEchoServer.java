package com.bit2015.network.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPEchoServer {

	private static final int PORT = 60000;
	private static final int BUFFER_SIZE = 1024;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatagramSocket datagramSocket = null;
		
		try {
			//1. UDP 서버 소켓 생성 
			datagramSocket = new DatagramSocket(PORT);
			//2. 수신대기 
			log("packet 수신대기 ");
			DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE],BUFFER_SIZE) ;
			
			while(true)
			{
				datagramSocket.receive(receivePacket);
				//3. 수신데이터 출력 
				String message = new String (receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
				log(message);
				if("exit".equals(message))
				{
					break;
				}
				//4. 수신된데이터 보내기 
				DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(),receivePacket.getLength(),receivePacket.getAddress(),receivePacket.getPort());
				datagramSocket.send(sendPacket);
			}
			
	
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(datagramSocket.isClosed() == false )
			{
				datagramSocket.close();
			}
		}
		
				
	}
	public static void log(String log)
	{
		System.out.println("[UDP_Server] "+log);
	}

}
