import java.io.Serializable;
import java.sql.Time;
import java.util.*;

public class Item implements Serializable {
	
	private static final long serialVersionUID = 6478975754221017250L;
	
	private String id;
	private String title;
	private String full_detail;
	private String category;
	private String seller_id;
	private Date start_time;
	private String closing_date;
	private double reserve_price;
	private double current_bid;
	
	// stores item bid price and id
	private ArrayList<Bid> bids;
	
	/*
	 * stores seller id, item name, item detail, item category, start date, end date, reserve price and current highest price 
	 */
	
	public Item(String seller_id, String title, String detail, String category, Date start, String end, double reserve) {

		this.seller_id = seller_id;
		this.title = title;
		this.full_detail = detail;
		this.category = category;
		this.start_time = start;
		this.closing_date = end;
		this.reserve_price = reserve;
		
		// initiating array
		bids = new ArrayList<Bid>();
	}

	public void set_itemID(String id) {

		this.id = id;
	}

	public String get_itemID() {

		return id;
	}

	public ArrayList<Bid> get_bidArray() {

		return bids;
	}

	public String get_seller() {

		return seller_id;
	}

	public String get_title() {

		return title;
	}

	public String get_detail() {

		return full_detail;
	}

	public String get_category() {

		return category;
	}

	public Date get_startTime() {

		return start_time;
	}

	public String get_closingDate() {

		return closing_date;
	}

	public double get_reserveP() {

		return reserve_price;
	}
	
	public void set_bid(double bid){
		
		this.current_bid = bid;	
	}	
	
	public double get_bid() {
		
		return this.current_bid;
	}
	
	public void add_bid(Bid bid){
		
		bids.add(bid);	
	}	
}

/*
 * it store user or item id and bid price
 */
class Bid implements Serializable{
	
	private String id;
	private double bid;
	
	public Bid(String id, double bid){
		
		this.id = id;
		this.bid = bid;
	}
	
	public String get_id(){
		
		return id;
	}
	
	public double get_bid() {
		
		return bid;
	}
}
