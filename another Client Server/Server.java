import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	ServerSocket sc;
	public void ConnectClient() {

		AllEvents.createEvents();
		
		try {
			sc = new ServerSocket(6666);
		}catch(Exception e) {System.out.println("Problem with port");}
		
		
		try {
			while(true) {
				Socket ClientSoc = sc.accept();
				new ClientThread(ClientSoc).start();
			}
		}catch(Exception e) {System.out.println("Problem with server");}
		
		
		
	}
}

class ClientThread extends Thread{
	Socket CS;
	//ArrayList <Event> Events;
	DataOutputStream OutStream;
	DataInputStream InStream;
	String str;
	String str2;
	String str3;
	int num;
	
	ClientThread(Socket clientSoc){//, ArrayList <Event> events){
		this.CS = clientSoc;
		//this.Events = events;
	}

	public void run() {
		
		System.out.println("Starting C thread");
		try {
			OutStream = new DataOutputStream(CS.getOutputStream());
			InStream = new DataInputStream(CS.getInputStream());
		}catch(Exception e) {System.out.println("Stream ERROR");}
		
		try {
			OutStream.writeUTF("Choose: Show Events or add new people");
			str = InStream.readUTF();
			
			
			if(str.equals("Show Events")) {
				OutStream.writeUTF("Choose: City");
				OutStream.writeUTF("OK");
				str = InStream.readUTF();
				
				OutStream.writeUTF("Choose: date");
				OutStream.writeUTF("OK");
				str2 = InStream.readUTF();
				
				
				  for (int counter = 0; counter < AllEvents.Events.size(); counter++) {
					  
			         if(AllEvents.Events.get(counter).city.equals(str) && 
			        		 AllEvents.Events.get(counter).date.equals(str2)) {
			        	 
					  System.out.println(AllEvents.Events.get(counter).id);
			         // OutStream.writeUTF(AllEvents.Events.get(counter).id);
			          str3 = AllEvents.Events.get(counter).id +"  " + 
			        		  AllEvents.Events.get(counter).city + "  " +
			        		  AllEvents.Events.get(counter).description + "  " +
			        		  AllEvents.Events.get(counter).date + "  " +
			        		  AllEvents.Events.get(counter).availableNumberOfparticipants;
			          OutStream.writeUTF(str3);
			         }    
				  }
				  OutStream.writeUTF("End");
			}else if (str.equals("add new people")) {
				OutStream.writeUTF("ID of Event");
				OutStream.writeUTF("OK");
				str = InStream.readUTF();
				
				OutStream.writeUTF("num of new participants");
				OutStream.writeUTF("OK");
				num = Integer.valueOf(InStream.readUTF());//num of participants
				
				for (int counter = 0; counter < AllEvents.Events.size(); counter++) {
			         if(AllEvents.Events.get(counter).id.equals(str) &&
			        	AllEvents.Events.get(counter).availableNumberOfparticipants > 0 &&
			        	AllEvents.Events.get(counter).availableNumberOfparticipants > num){
			        	
			        	// System.out.println("2NUM is " + num);
			        	 System.out.println(AllEvents.Events.get(counter).availableNumberOfparticipants);
			        	 AllEvents.changeParticipants(counter, num);
			        	 System.out.println(AllEvents.Events.get(counter).availableNumberOfparticipants);
			         }
			     }
				
				OutStream.writeUTF("done");
				OutStream.writeUTF("End");
			}else {
				OutStream.writeUTF("try again");
			}
			
		}catch (IOException e){System.out.println("IO eroor");}
		
		
	}
}

class AllEvents{
	static ArrayList <Event> Events;
	
	AllEvents(ArrayList <Event> Events){
		AllEvents.Events = Events;
	}
	
	public static void createEvents() {
		Events = new ArrayList<Event>();
		Event ev1 = new Event("1","varna","description","20.03.2020",5);
		Event ev2 = new Event("2","paris","description","20.03.2020",20);
		Event ev3 = new Event("3","rim","description","20.03.2020",100);
		Event ev4 = new Event("4","varna","description","20.03.2020",10);
		
		
		Events.add(ev3);
		Events.add(ev1);
		Events.add(ev2);
		Events.add(ev4);

	}
	 public static synchronized void changeParticipants(int counter, int num) {
		 AllEvents.Events.get(counter).availableNumberOfparticipants =
		AllEvents.Events.get(counter).availableNumberOfparticipants - num;
	}
	
}
