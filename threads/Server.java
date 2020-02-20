package threads;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	ServerSocket ss;
		
	public void CreateServer() {
		
		Boolean isRunning = true;	

		try {
			ss = new ServerSocket(6666);	
			}catch(Exception e) {System.out.println("Server not created on port 6666");}
				
		try {
			while(isRunning){
				System.out.println("before Listening");
				
				Socket ClientSoc = ss.accept();
				System.out.println("after Listening");
				
				InThread m1 = new InThread(ClientSoc);
				Thread t1 = new Thread(m1);	
				
				OutThread m2 = new OutThread(ClientSoc);
				Thread t2 = new Thread(m2);	
				
				t1.start();
				t2.start();
			}
		}catch(Exception e) {System.out.println("Accepting connection error");}
		
		System.out.println("Closing connection");
	}
}

class InThread implements Runnable{
	String str;
	Socket myClientSoc;
	InThread(Socket ClientSoc){
		myClientSoc = ClientSoc;
	}
	public void run(){
		System.out.println("InThread Running");
		
		try {
			DataInputStream dataIn = new DataInputStream(myClientSoc.getInputStream());
			while(true){
				str = dataIn.readUTF();
				
				System.out.println("Client: " + str);
			}
		}catch(Exception e) {System.out.println("Stream problem");}
	}
}

class OutThread implements Runnable{
	String str;
	Socket myClientSoc;
	File file;
	BufferedWriter writer;
	Boolean flag= false;
	OutThread(Socket ClientSoc){
		myClientSoc = ClientSoc;
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
				
				if(flag == true) {writer.append(str);}
				
				if(str.equals("yes")) {
					file = new File("D:\\Program Files\\Eclipse Workplace\\threads\\src\\threads\\Mir.txt");
					file.createNewFile();
					writer = new BufferedWriter(new FileWriter("Mir.txt", true));
					flag = true;
				}
				
				if(str.equals("over")) {
					System.out.println("CLOSING");
					writer.close();
					System.exit(1);
				}
				writer.flush();
				
				dataOut.flush();
				// close witer sc i tam vrazkata
				// premesti logvaneto v otdelen metod synchronizirai go 
				// i zapisvai IN i OUT 
				//i drugo uslovie za sazdavane na fila
				// dali ima veche sazdaden
				// yes uslovieto V INthread a ne v OUT 
				// OPRAVI PISANETO V FAILA
		}
		}catch(Exception e) {System.out.println("Stream problem");}
		
	}
}