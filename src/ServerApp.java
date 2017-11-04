import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerApp extends Comms {

	public static ArrayList<User> storeUsers = new ArrayList<User>();
	public static ArrayList<Item> storeItems = new ArrayList<Item>();
	public static HashMap<String, ArrayList<Item>> hm = new HashMap<String, ArrayList<Item>>();
	public static ArrayList<ServerThread> storeThreads = new ArrayList<ServerThread>();
	
	Message msg = null;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ServerApp comm = new ServerApp();
		
		comm.runServer();
	}
	
	public void runServer() throws ClassNotFoundException, IOException {
		
		serverSocket = new ServerSocket(444, 100);
			
			try {
				
				System.out.println("Waiting For Connection ..");
				clientSocket = serverSocket.accept();
				
				// keeps on looking for message
				while(true) {
					
					processMsg(this);
				}
				//Thread t = new Thread(new ServerThread(clientSocket));
				//t.start();
				
			} catch (IOException e) {
				System.err.println(e);
			}
	}

	public static synchronized void processMsg(Comms com) throws IOException, ClassNotFoundException {	
				
				Message msg = com.getMsg();
				
				if (msg.get_type().equals("Register Message")) {

					com.sendMsg(RegisterUser((Register_msg) msg));
				}
				
				if (msg.get_type().equals("Login Message")) {

					com.sendMsg(LoginUser((Login_msg) msg));
				}

				if (msg.get_type().equals("Item Message")) {

					AddItem((Item_msg) msg);
				}

				if (msg.get_type().equals("Search Message")) {

					com.sendMsg(SearchItem((Search_msg) msg));
				}
				
				if (msg.get_type().equals("Notice Message")) {

					com.sendMsg(WatchAuction((Notice_msg) msg));
				}
				
				if (msg.get_type().equals("Sale Message")) {

					com.sendMsg(SearchSaleItem((SaleStatus_msg) msg));
				}

				if (msg.get_type().equals("Bid Message")) {

					com.sendMsg(BidItem((Bid_msg) msg));
				}

				if (msg.get_type().equals("Bid Status Message")) {

					com.sendMsg(SearchBidding((BidStatus_msg) msg));
				}
				
				if(msg.get_type().equals("User Name Message")) {
					
					com.sendMsg(getUserName((UserName_msg) msg));
				}
				
				if (msg.get_type().equals("Logout Message")) {

					com.closeConnection();
				}

	}

	private static Authentication_msg getUserName(UserName_msg msg) {
		
		String user_name = null;
		
		for(User u: storeUsers){
			
			if(u.get_userID().equals(msg.get_ID())){
				
				user_name = u.get_name();
			}
		}
		
		return (new Authentication_msg(true, user_name));
	}
	
	/*
	 * cheks login name and password
	 */
	public static Authentication_msg LoginUser(Login_msg msg) {

		User identity;

		Authentication_msg check = new Authentication_msg(false, msg.get_login());

		Iterator<User> itr = storeUsers.iterator();

		while (itr.hasNext()) {

			identity = itr.next();
			
			if (identity.get_login().equals(msg.get_login())) {

				if (Arrays.equals(identity.get_pw(), msg.get_pw())) {

					check = new Authentication_msg(true, msg.get_login());
				}
			}
		}

		return check;
	}

	/*
	 * gives each user unique id and add user to ArrayList
	 */
	public static Authentication_msg RegisterUser(Register_msg msg) {

		User user = new User(msg.get_userName(), msg.get_familyName(), msg.get_loginName(), msg.get_pw());

		user.set_userID(msg.get_familyName() + storeUsers.size());

		storeUsers.add(user);
		
		return (new Authentication_msg(true, user.get_userID()));
	}
	
	/*
	 * add items to arraylist
	 * gives each item unique id
	 */
	public static void AddItem(Item_msg msg) {

		Item item = msg.getItem();

		item.set_itemID(item.get_title() + storeItems.size());
		
		storeItems.add(item);
	}
	
	
	/*
	 *  checks auction closing time
	 *  and notify if the user wins
	 */
	public static Result_msg WatchAuction(Notice_msg msg){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm");
		ArrayList<Item> closedItems = new ArrayList<Item>(); // store winner
		ArrayList<Integer>  store = new ArrayList<Integer>(); // store item to be closed
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("BST"));
			
		for(int i = 0; i < storeItems.size(); i++) {
			
			if (storeItems.get(i).get_seller().equals(msg.get_userID())) {
				
				try {
					
					if(cal.getTime().after(dateFormat.parse(storeItems.get(i).get_closingDate()))) {
						
						ArrayList<Bid> temp = storeItems.get(i).get_bidArray();

						if (temp.size() != 0) {
							
							if(temp.get(temp.size() - 1).get_id().equals(storeItems.get(i).get_itemID())) {
								
								closedItems.add(storeItems.get(i));
								store.add(i);
							}
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		// removing closed auction
		//for(int n: store){
		//	storeItems.remove(n);
		//}
		
		return new Result_msg(closedItems);
	}
	
	/*
	 * search each item on list
	 * returns selected item list
	 */
	public static Result_msg SearchItem(Search_msg msg) {
		
		// list to add items
		ArrayList<Item> list = new ArrayList<Item>();
		list.clear();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		
		for (Item i : storeItems) {
			
			try {
				if ( i.get_title().equals(msg.get_item())) {
					
					list.add(i);
				} else if(i.get_itemID().equals(msg.get_item())) {
					
					list.add(i);
				} else if (i.get_seller().equals(msg.get_sellerID())) {
					
					list.add(i);
				} else if (i.get_category().equals(msg.get_Cat())) {
					
					list.add(i);
				} else if(msg.get_Date() !=  null) {
					 if (i.get_startTime().before(dateFormat.parse(msg.get_Date()))) {
						 System.out.println(i.get_startTime());
						 System.out.println(dateFormat.parse(msg.get_Date()));
					
						 list.add(i);
					}
				} else if(msg.get_item() == null && msg.get_Cat() == null && msg.get_sellerID() == null && msg.get_Date().equals("1111/01/01 00:00")) {

					list = storeItems;
				} else { 
					
					break;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return new Result_msg(list);
	}
	
	/*
	 * search for item with specific users
	 * returns selected item list
	 */
	private static Message SearchSaleItem(SaleStatus_msg msg) {
		
		// list to add items
		ArrayList<Item> list = new ArrayList<Item>();
		
		for (Item i : storeItems) {
			
			if (i.get_seller().equals(msg.get_userID()) ) {
				
				list.add(i);
			}
		}
		
		return new Result_msg(list);
	}
	
	/*
	 * checks bid on specific item
	 * compare with current bid and add bid to arraylist
	 * return msg
	 */
	public static Message BidItem(Bid_msg msg) {

		Message pmsg = new Message("Message");

		Bid bid = msg.get_bid();

		for (Item i : storeItems) {
			
			if (i.get_itemID().equals(msg.get_itemID())) {
				
				if(i.get_seller().equals(msg.get_sellerID())) {
					
					pmsg.set_msg("SELLER NOT ALLLOWED TO BID ON HIS OWN ITEM");
				} else {
				
					if (bid.get_bid() > i.get_bid()) {

						i.set_bid(bid.get_bid());
						i.add_bid(bid);
						pmsg.set_msg("BID SUCCESSFUL");
					} else {
						
						pmsg.set_msg("INVALID BID");
					}
				}
			}
		}

		return pmsg;
	}
	
	/*
	 * for searching user bid items
	 */
	public static Result_msg SearchBidding(BidStatus_msg msg) {

		ArrayList<Item> temp = new ArrayList<Item>();
		
		for (Item i : storeItems) {
			
			ArrayList<Bid> list = i.get_bidArray();
					
			if(i.get_seller().equals(msg.getID())) {
						
				for (Bid bid : list) {

					if (bid.get_id().equals(i.get_itemID())) {
						
						temp.add(i);
					}
				}
			}
		}
		
		return new Result_msg(temp);
	}
}

/*
 * failed multiple client system
 */
class ServerThread extends Comms implements Runnable {  
	
    public ServerThread(Socket clientSocket) throws IOException{
    	
        this.clientSocket = clientSocket;
    }

   public void run() {
    			
    	try {
    		
			ServerApp.processMsg(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
   } 
} 
