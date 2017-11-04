import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
	
	private String type;	
	private String msg;
	
	// sets message type
	public Message(String type) {

		this.type = type;
	}
	
	// get message type
	public String get_type() {

		return type;
	}
	
	// set message for output
	public void set_msg(String msg){
		
		this.msg = msg;	
	}
	
	// gets message
	public String get_msg(){
		
		return msg;
	}
}

/*
 * for checking logn details
 */
class Login_msg extends Message {

	private String login;
	private char[] pw;

	public Login_msg(String login_n, char[] pw) {

		super("Login Message");
		this.login = login_n;
		this.pw = pw;
	}
	
	public String get_login(){
		
		return login;
	}

	public char[] get_pw() {

		return pw;
	}
}

/*
 * for registering users
 */
class Register_msg extends Message {

	private String user_name;
	private String family_name;
	private String login_name;
	private char[] pw;

	public Register_msg(String user_n, String family_n, String login_n, char[] pw) {

		super("Register Message");

		this.user_name = user_n;
		this.family_name = family_n;
		this.login_name = login_n;
		this.pw = pw;
	}

	public String get_userName() {

		return user_name;
	}

	public String get_familyName() {

		return family_name;
	}
	
	public String get_loginName(){
		
		return login_name;
	}
	
	public char[] get_pw() {

		return pw;
	}
}

/*
 * for sending string or integers
 */
class Authentication_msg extends Message {

	private boolean find;
	private String id;

	public Authentication_msg(boolean bool, String id) {

		super("Authetication Message");
		this.find = bool;
		this.id = id;
	}

	public boolean get_authenticaton() {

		return find;
	}

	public String get_id() {

		return id;
	}
}

/*
 * for searching using id
 */
class Search_msg extends Message {

	private String item;
	private String seller_id;
	private String dateTime;
	private String category;

	public Search_msg(String title, String id, String dateTime, String category) {

		super("Search Message");

		this.item = title;
		this.seller_id = id;
		this.dateTime = dateTime;
		this.category = category;
	}

	public String get_item() {

		return item;
	}

	public String get_sellerID() {

		return seller_id;
	}

	public String get_Date() {

		return dateTime;
	}

	public String get_Cat() {

		return category;
	}
}

/*
 * for sending items
 */
class Item_msg extends Message {

	private Item i;

	public Item_msg(Item i) {

		super("Item Message");
		this.i = i;
	}

	public Item getItem() {

		return i;
	}
}

/*
 * for sending arrays
 */
class Result_msg extends Message {

	private ArrayList<Item> list;

	public Result_msg(ArrayList<Item> list) {

		super("Result Message");
		this.list = list;
	}

	public ArrayList<Item> getList() {

		return list;
	}
}

/*
 * for bidding using item id
 */
class Bid_msg extends Message{
	
	private Bid bid;	
	private String item_id, seller_id;
	
	public Bid_msg(String item_id, Bid bid, String seller_id){
		
		super("Bid Message");	
		this.bid = bid;
		this.item_id = item_id;	
		this.seller_id = seller_id;
	}
	
	public Bid get_bid(){
		
		return bid;	
	}
	
	public String get_itemID(){
		
		return item_id;	
	}
	
	public String get_sellerID(){
		
		return seller_id;
	}
}

/*
 * for retriving bid status of user
 */
class BidStatus_msg extends Message{
	
	String id;
	
	public BidStatus_msg(String id){
		
		super("Bid Status Message");	
		this.id = id;
	}
	
	public String getID(){
		
		return id;	
	}
}

/*
 * for retriving sale status of user
 */
class  SaleStatus_msg extends Message {
	
	String user_id;
	
	public SaleStatus_msg(String user_id) {
		
		super("Sale Message");
		this.user_id = user_id;
	}
	
	public String get_userID() {
		return user_id;
	}
}

/*
 * for retriving user name
 */
class UserName_msg extends Message {
	
	String user_name;
	
	public UserName_msg(String unique_id) {
		super("User Name Message");
		this.user_name = unique_id;
	}
	
	public String get_ID(){
		
		return user_name;
	}
}

class Notice_msg extends Message {
	
	String user_id;
	
	public Notice_msg(String id){
		super("Notice Message");
		this.user_id = id;
	}
	
	public String get_userID(){
		
		return user_id;
	}
}