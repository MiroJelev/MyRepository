package kontrolno;


import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	ServerSocket ss;

	public void ConnectClients() {

		boolean isRunning = true;
		try {
			ss = new ServerSocket(6666);
		}catch(Exception e) {System.out.println("port not working");}
		
		try {
			while(isRunning) {
				//tuka nekade if za dali e student ili 
				//sekretarka 
				Socket ClientSoc = ss.accept();
				System.out.println("bau");
				new ClientThread(ClientSoc).start();
				
			}

		}catch(Exception e) {System.out.println("Accepting error");}
	}
}

class ClientThread extends Thread{
	Socket CS;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str;
	Scanner sc;
	
	ClientThread(Socket ClientSoc){
		CS = ClientSoc;
	}
	
	public void run(){
		System.out.println("Server working:::" + Thread.currentThread().getName());
		try {
			OutStream = new DataOutputStream(CS.getOutputStream());
			InStream = new DataInputStream(CS.getInputStream());
		}catch(Exception e) {System.out.println("IO exception");}
		
		try {
			OutStream.writeUTF("Choose 1/2 : \n 1.Kandidatstvai za stipendiq za uspeh \n 2. Kandidatstvai za specialna stipendiq");
			
			
			//tuka nekade da gi zapazva v 
			//linked list tva za studenta
			//linked lista trea da moe sekretarkata da go 
			//gleda
			
			
			Boolean IsOK = true;
			while(IsOK) {
				str = InStream.readUTF();
				System.out.println("Client says:" + str);
				if(str.equals("1")) {
					System.out.println("USPEH");
					OutStream.writeUTF("OK");
					IsOK = false;
					file(1);
				}else if(str.equals("2")) {
					System.out.println("Specialno");
					OutStream.writeUTF("OK");
					IsOK = false;
					file(2);
				}else {
					OutStream.writeUTF("Try again");
				}
			}
			
			
			
		} catch (IOException e) {e.printStackTrace();}
		
		
	}
	
	void file(int c) {
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter("D:\\Program Files\\Eclipse Workplace\\java kontrolno 2\\src\\mir" + Thread.currentThread().getName() +".txt", true));
			if(c == 1) {
				buff.write("USPEH\n");
				buff.append("darabara");
			}else if(c == 2){
				buff.write("Specialno");
				
			}
			
			buff.close();
		} catch (IOException e) {e.printStackTrace();}
		
		
	}
}














