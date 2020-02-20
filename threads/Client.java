package threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		
		try {
			Socket s = new Socket("localhost",6666);
			
			CinThread m1 = new CinThread(s);
			Thread t1 = new Thread(m1);	
			
			CoutThread m2 = new CoutThread(s);
			Thread t2 = new Thread(m2);	
	
			t1.start();
			t2.start();
			
		}catch(Exception e) {
			System.out.println("socket problem");
		}
	}
}

class CinThread implements Runnable{
	String str;
	Socket myClientSoc;
	CinThread(Socket s){
		myClientSoc = s;
	}
	public void run(){
		System.out.println("InThread Running");
		
		try {
			DataInputStream dataIn = new DataInputStream(myClientSoc.getInputStream());
			while(true){
				str = dataIn.readUTF();
				System.out.println("Server: " + str);
			}
		}catch(Exception e) {System.out.println("Stream problem");}
	}
}

class CoutThread implements Runnable{
	String str;
	String str1;
	Socket myClientSoc;
	CoutThread(Socket s){
		myClientSoc = s;
	}
	public void run(){
		System.out.println("OutThread Running");
		Scanner sc = new Scanner(System.in);
		
		try {
			DataOutputStream dataOut = new DataOutputStream(myClientSoc.getOutputStream());
			while(true){
				str = sc.nextLine();
				dataOut.writeUTF(str);
				System.out.println("	Sent");
				dataOut.flush();
		}
		}catch(Exception e) {System.out.println("Stream problem");}
	}
}
