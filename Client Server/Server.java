import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Server {

	ServerSocket sc;
	public void ConnectClient() {
		
		AllClients List = new AllClients();
		List.makeList();
		AllClients.makeVoteRegister();
		AllClients.makeHashVotes();
		
		try {
		sc = new ServerSocket(1211);
		}catch(Exception e) {System.out.println("Socket problem");}
		
		try {
			while(true) {
				Socket ClientSoc = sc.accept();
		
				new HelperThread(ClientSoc).start();
			}
		}catch(Exception e) {System.out.println("accept Error");}
	}
}

class HelperThread extends Thread{
	Socket cs;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str = null;
	String EGN;
	
	HelperThread(Socket ClientSoc){
		this.cs = ClientSoc;
	}
	public void run() {
		try {
		OutStream = new DataOutputStream(cs.getOutputStream());
		InStream = new DataInputStream(cs.getInputStream());
		}catch(Exception e) {System.out.println("Stream Error");}
		try {
			OutStream.writeUTF("EGN/Password");
			OutStream.writeUTF("OK");
			EGN = InStream.readUTF();
			System.out.println("before checklist " + str + EGN);
			str = AllClients.checkList(EGN);
			
			if(str.equals("CITIZEN")) {
				System.out.println("starting citizen thread");
				
				new CitizenThread(cs, EGN).start();
				
			}else if(str.equals("ADMIN")) {
				System.out.println("starting ADMIN thread");
				new AdminThread(cs).start();
				
			}else if(str.equals("Wrong value")){
				OutStream.writeUTF("REFUSED");
			}
		}catch(Exception e) {System.out.println("Comunication error1");}
	}
}

class AllClients{
	static LinkedList<AdminsOrClients> ListClients;
	static HashMap<Integer, Integer> map;
	static LinkedList<Citizen> ListOfVotedClients;
	public void makeList() {
		
		ListClients = new LinkedList<AdminsOrClients>();
		AdminsOrClients z1 = new AdminsOrClients("123" ,"ADMIN");
		AdminsOrClients z2 = new AdminsOrClients("1234" ,"CITIZEN");
		AdminsOrClients z3 = new AdminsOrClients("12345" ,"CITIZEN");
		
		ListClients.add(z1);
		ListClients.add(z2);
		ListClients.add(z3);
	}
	
	public static String checkList(String EGN) {
		String rights = null;
		
		 for(int counter = 0; counter < ListClients.size(); counter++) {
			 if(EGN.equals(ListClients.get(counter).EGN)) {
				 return rights = ListClients.get(counter).Rights;
			 }else {
				 rights = "Wrong value"; 
			 }
		 }
		 return rights;
	}
	
	public static void makeHashVotes() {
		map = new HashMap<Integer, Integer>(); // k No na kandidat  v = broi glasove
		map.put(0,0);
		map.put(1,0);
		map.put(2,0);
	}
	public synchronized static void AddVotes(int Key) {
		int num;
		num = map.get(Key);
		System.out.println("num in hash is" + num);
		System.out.println("key in hash is" + Key);
		num++;
		map.put(Key, num);
	}
	/*public static void getVotes() {
		int Value = 0;
		LinkedHashMap <Integer, Integer> list= new LinkedHashMap<>();
		
		for(int counter = 0; counter<map.size(); counter++) {
			Value = map.get(counter);
			
			list.put(counter, Value);
			
			list.entrySet().stream()
		}
	}
	*/
	public static void makeVoteRegister() {
		ListOfVotedClients = new LinkedList<Citizen>();
	}
	
}

class CitizenThread extends Thread{
	Socket cs;
	String EGN;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str1;
	String str2;
	String NUM;
	String candidates[] = { "ime1","ime2","ime3"};
	CitizenThread(Socket clientSoc, String eGN){
		this.cs = clientSoc;
		this.EGN = eGN;
	}
	
	public void run() {
		try {
		OutStream = new DataOutputStream(cs.getOutputStream());
		InStream = new DataInputStream(cs.getInputStream());
		}catch(Exception e) {System.out.println("Stream Error");}
		
		try {
			System.out.println("sending names of candidates");
			for(int counter = 0; counter < candidates.length; counter++) {
			OutStream.writeUTF( "NUM " + counter + ": "+ candidates[counter]);
			}
			OutStream.writeUTF("send your names");
			OutStream.writeUTF("OK");
			str1 = InStream.readUTF();
			
			OutStream.writeUTF("Adress");
			OutStream.writeUTF("OK");
			str2 = InStream.readUTF();
			
			OutStream.writeUTF("send NUM for candidate");
			OutStream.writeUTF("OK");
			NUM = InStream.readUTF();
			System.out.println("predi VOTE REEGISTER");
			//AllClients.map.put(num, value)
			AllClients.ListOfVotedClients.add(new Citizen(str1, str2, EGN));
			
			for(int counter = 0; counter<AllClients.ListOfVotedClients.size(); counter++) {
				System.out.println("VOTEd REEGISTER: " + AllClients.ListOfVotedClients.get(counter).Address +
					AllClients.ListOfVotedClients.get(counter).Name +
					AllClients.ListOfVotedClients.get(counter).EGN);
			}
			System.out.println("NUM = " + NUM);
			
			AllClients.AddVotes(Integer.valueOf(NUM));
			
			AllClients.map.forEach((key,value) -> System.out.println(key + " = " + value));
			
			OutStream.writeUTF("End");
			
		}catch(Exception e) {System.out.println("Comunication error2");}
	}
}

class AdminThread extends Thread{
	Socket cs;
	DataOutputStream OutStream;
	AdminThread(Socket ClientSoc){
		this.cs = ClientSoc;
	}
	
	public void run(){
		try {
			OutStream = new DataOutputStream(cs.getOutputStream());
			//getFinalVotes();
		}catch(Exception e) {System.out.println("ne sortirra");}
			AllClients.map.forEach((key,value) -> {
				try {
					OutStream.writeUTF(key + " = " + value);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		
	}
	
	/*private Map<Integer, Integer> getFinalVotes() {
		
		
		return AllClients.map;
	}*/
}
