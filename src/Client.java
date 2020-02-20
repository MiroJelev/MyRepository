import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	//Socket cs;
	public static void main(String[] args) {
		Socket cs;
		try {
		cs = new Socket("localhost", 1211);
		
		ConnectionThread t1 = new ConnectionThread(cs);
		t1.start();
		}catch(Exception e) {System.out.println("port problem");}
	}
}

class ConnectionThread extends Thread{
	Socket cs;
	String str;
	Scanner sc;
	DataOutputStream OutStream;
	DataInputStream InStream;
	boolean IsTrue = true;
	ConnectionThread(Socket CS){
		this.cs = CS;
	}
	
	public void run(){
		try {
			OutStream = new DataOutputStream(cs.getOutputStream());
			InStream = new DataInputStream(cs.getInputStream());
		}catch(Exception e) {System.out.println("IO exception");}
		
		try {
		//	str = InStream.readUTF();
		//	System.out.println("Server says:" + str);
			
			sc = new Scanner(System.in);
			//str = sc.nextLine();
			//OutStream.writeUTF(str);
			
			while(IsTrue) {
				str = InStream.readUTF();    // priemash nesta
				System.out.println("Server says:" + str);
				
					if(str.equals("OK")) {
						str = sc.nextLine();
						OutStream.writeUTF(str);   // vavedash nesta
					}
					if(str.equals("End")) {
						IsTrue = false;
					}
			}
			
		}catch(Exception e) {System.out.println("Connection Dropped");}
		
		
		
	}
}