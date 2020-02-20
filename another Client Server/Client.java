import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	
	public static void main(String[] args) {
		
		try {
			Socket s = new Socket("localhost", 6666);
			
			ConnectionThread t1 = new ConnectionThread(s);
			t1.start();
		}catch(Exception e) {System.out.println("error");}
		
	}
}

class ConnectionThread extends Thread{
	Socket socket;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str;
	Scanner sc;
	boolean IsTrue = true;
	
	ConnectionThread(Socket s){
		this.socket = s;
	}
	
	public void run() {
		System.out.println("Starting Client");
		try {
			OutStream = new DataOutputStream(socket.getOutputStream());
			InStream = new DataInputStream(socket.getInputStream());
		}catch(Exception e) {System.out.println("IO Streams");}
		
		try {
			str = InStream.readUTF();
			System.out.println("Server says:" + str);
			
			sc = new Scanner(System.in);
			str = sc.nextLine();
			OutStream.writeUTF(str);
			
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