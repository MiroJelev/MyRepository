package kontrolno;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main(String args[]) {
		
		try {
			Socket soc = new Socket("localhost",6666);
			
			ConnectionThread t1 = new ConnectionThread(soc);
			t1.start();
		}catch(Exception e) {System.out.println("socket problem");}
	}
}


class ConnectionThread extends Thread {
	Socket socket;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str;
	Scanner sc;
	
	ConnectionThread(Socket soc){
		this.socket = soc;
	}
	
	public void run(){
		System.out.println("Client working:::" + Thread.currentThread().getName());
		try {
			OutStream = new DataOutputStream(socket.getOutputStream());
			InStream = new DataInputStream(socket.getInputStream());
		}catch(Exception e) {System.out.println("IO Streams");}
		
		try {
			str = InStream.readUTF();
			System.out.println("Server says:" + str);
			
			sc = new Scanner(System.in);
			Boolean IsOK = true;
			while(IsOK) {
				str = sc.nextLine();
				OutStream.writeUTF(str);
				
				str = InStream.readUTF();
				System.out.println("Server says:" + str);
				if(str.equals("OK")) {
					IsOK = false;
				}
			}
			
			
		}catch(Exception e) {System.out.println("Connection Dropped");}
		
		
	}
}
