package com.bit2015.network.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final  int SERVER_PORT = 60000;
	private static final int BUFFER_SIZE = 1024;
	public static void main(String[] args)
	{
		DatagramSocket datagramSocket = null;
		SimpleDateFormat format = null;
		DatagramPacket receivePacket = null;
		//1.UDP 서버 소켓 생성 
		try {
			datagramSocket = new DatagramSocket(SERVER_PORT);
			
			while(true)
			{
				receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				log("서버 대기");
				datagramSocket.receive(receivePacket);
				format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss a");
				String data = format.format( new Date() );
				DatagramPacket sendPacket = new DatagramPacket(data.getBytes(), data.getBytes().length,receivePacket.getAddress(),receivePacket.getPort());
				datagramSocket.send(sendPacket);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			datagramSocket.close();
		}
		//2.수신대기 
		
		//수신데이터 출력
		
		//수신된데이터 보내기 
	}
	
	public static void log(String log)
	{
		System.out.println("[Time_Server] " + log);
	}
}
