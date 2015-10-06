package com.bit2015.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class ChatServerProcessThread extends Thread {
	private static final String PROTOCOL_DIVIDER =":";

	private Socket socket;
	private String nickname;
	private List<PrintWriter> listPrintWriters;

	public ChatServerProcessThread(Socket socket, List<PrintWriter> listPrintWriters){
		this.socket =socket;
		this.listPrintWriters = listPrintWriters;
	}

	@Override  //오버라이드에서 super는 부모껄 사용한다는 뜻 서버에서 스타트를하면 얘가 실행된다
	public void run() {
		BufferedReader bufferedReader  = null; //스트링으로 감싸주는것
		PrintWriter printWriter =null;  // 비트형으로 다시바꿔주는것
		
		try{
		//1. 스트림얻기
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8")); //바이트를 문자열로 받아준다//받아올떄쓴다
		printWriter = new PrintWriter( socket.getOutputStream()); //프린트라이터를 이요해서 문자열을 사용가능//내보낼때 쓴다
		
		//2. 리모트 호스트 정보 얻기
		socket.getRemoteSocketAddress();
		InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		String remoteHostAddress = inetSocketAddress.getHostName();
		int remoteHostPort = inetSocketAddress.getPort();
		ChatServer.log("연결됨 from "+ remoteHostAddress+": "+ remoteHostPort);//어디서 연결됐는지 로그냄겨주는거
		
		//3. 요청처리
		while(true){
			String request = bufferedReader.readLine(); //한줄씩 읽어들여온다 조인일떄도있고 quit때도있고
			
			if(request ==null){ //상대쪽에서 연결끊은거
				doQuit(printWriter);
				break;
			}
			String[] tokens = request.split(PROTOCOL_DIVIDER); // PROTOCOL_DIVIDER이 :얘로 나눈다는거다
			if("join".equals(tokens[0])){
				doJoin(printWriter, tokens[1]);
				
			}else if("message".equals(tokens[0]))
			{
				doMessage(tokens[1]);
			}
			else if("quit".equals(tokens[0]))
			{
				doQuit(printWriter);
				break;
			}
			else{
				ChatServer.log("에러: 알수 없는 요청명령("+tokens[0]+")");
			}
		}
		
		//4. 자원정리
		bufferedReader.close();
		printWriter.close();
		if(socket.isClosed() ==false){
			socket.close();
		}
		}catch(IOException ex){
			ChatServer.log("error: "+ex); //쳇서버에있는 애를 가져다쓴다	
			
		}
	}
	private void doQuit(PrintWriter printWriter) {
		// printwriter 제거 
		removePrintWriter(printWriter);
		// 퇴장 메시지
		String data = nickname + "님이 퇴장 하였습니다.";
		broadCast(data);
	}
	private void removePrintWriter(PrintWriter printWriter)
	{
		//remove 가 알아서 같은 printWriter 를 지운다
		synchronized (listPrintWriters) {
			listPrintWriters.remove(printWriter);
		}
	}
	private void doMessage(String message) {
		
		String data = nickname + ":" + message;
		broadCast(data);
	}
	
	private void doJoin(PrintWriter printWriter, String nickname){
		//1. 닉네임저장
		this.nickname = nickname;
		
		//2. 메세지 브로드캐스팅
		String messsage  = nickname+" 님이 입장했습니다.";
		broadCast(messsage);

		//3.
//		listPrintWriters.add(printWriter); 얘처럼하면 동시에 접근시에 문제가생길수있어서 그문제를 없에기위해 동기화해주려고 따로 메소드를 만들고 밑에처럼쓴다
		addPrintWriter(printWriter);
		
		//4 ack
		printWriter.println("join:OK");
		printWriter.flush();
	}
	private void addPrintWriter(PrintWriter printWriter){
		synchronized(listPrintWriters){ //동기화를 걸어주면 이메소드를 동시에 사용했을때 나중에 들어오는 애를 락(슬립)을 걸어줘서 이코드는 쓰레드들끼리 한코드들만 들어갈수있게 해주는것이다.
			listPrintWriters.add(printWriter);
		}
	}
	private void broadCast(String data){  
		
//		for(PrintWriter printWriter : listPrintWriters){
//			printWriter.println(data);                      밑에 애랑 똑같은거
//		}
		int count = listPrintWriters.size();
		for(int i =0; i<count; i++){
			PrintWriter printWriter = listPrintWriters.get(i);
			printWriter.println(data);
			printWriter.flush();
		}
		
		
	}
	
}	













