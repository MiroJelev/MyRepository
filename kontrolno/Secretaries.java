package kontrolno;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Secretaries {

	public static void main(String[] args) {
		try {
			Socket soc = new Socket("localhost",6666);
			
			SecretarieThread t1 = new SecretarieThread(soc);
			t1.start();
		}catch(Exception e) {System.out.println("socket problem");}
	}

}

class SecretarieThread extends Thread {
	Socket socket;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str;
	Scanner sc;
	
	SecretarieThread(Socket soc){
		this.socket = soc;
	}
}
