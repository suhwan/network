package com.bit2015.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	
	private static final String SEVER_ADDRESS ="192.168.1.88";
	private static final int SERVER_PORT = 5001;
	public static void main(String[] args) {
		
		Scanner scanner = null;

		Socket socket = null;

		try {

		   //1. 키보드연결
			scanner = new Scanner(System.in);
		   //2. socket 생성
			socket =new Socket();
		   //3. 연결
			socket.connect(new InetSocketAddress(SEVER_ADDRESS, SERVER_PORT ));
		   //4. reader/ writer 생성

		   BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ,"UTF-8") );

		   PrintWriter printWriter = new PrintWriter( socket.getOutputStream() );

		   //5. join 프로토콜

		   System.out.print("닉네임>>" );

		   String nickname = scanner.nextLine();

		   printWriter.println( "join:" + nickname );
		   
		   printWriter.flush();
		   //ack 받기 
		   String strAck = bufferedReader.readLine();
		   System.out.println(strAck);
		   
		   //6. ChatClientRecevieThread ??
		   Thread thread = new ChatClientReceiveThread(bufferedReader);
		   thread.start();
		   //7. 키보드입력처리

		   while( true ) {
		      String input = scanner.nextLine();

		      if( "quit".equals( input ) == true ) {
		    	  //8. 프로토콜종 료
		    	  printWriter.println("quit:a");
		    	  break;
		      } else {
		    	  
		    	  printWriter.println("message:"+input);
		    	  printWriter.flush();
		      }

		   }
		   //10. 자원정리
		   bufferedReader.close();

		   printWriter.close();

		   if( socket.isClosed() == false ) {

		       socket.close();

		   }

		} catch( IOException ex ) {

		      ChatServer.log( "error:" + ex );

		}
	}

}
