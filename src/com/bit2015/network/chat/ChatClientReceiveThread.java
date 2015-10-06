package com.bit2015.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ChatClientReceiveThread extends Thread {
	BufferedReader bufferedReader =null;
	public ChatClientReceiveThread(BufferedReader bufferedReader)
	{
		this.bufferedReader = bufferedReader;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			while(true){
				if(bufferedReader == null )
				{
					break;
				}
				String request = bufferedReader.readLine();
				ChatServer.log(request);
			} 
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			ChatServer.log("error : " + e);
		} //한줄씩 읽어들여온다 조인일떄도있고 quit때도있고
			
		}
	
		
	}
	
	

