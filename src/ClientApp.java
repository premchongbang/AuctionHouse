import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.*;
import java.net.UnknownHostException;

public class ClientApp {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Comms comms = new ClientComms();
		
		LoginFrame login = new LoginFrame("Login", comms);
		login.init();
	}
}

/*
 * help to prevent repetition of code
 */
class HelperFrame extends JFrame {
	public Comms communicator;
	public JPanel mainFrame;
	public GridBagLayout gb;
	public GridBagConstraints gbc;

	public HelperFrame(String title, Comms com) {

		super(title);
		this.communicator = com;
		
		mainFrame = new JPanel();
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
	}

	public void finalize() {
		
		mainFrame.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setContentPane(mainFrame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		pack();
	}
}

/*
 * frame work for registration window
 * takes login name and password
 */
class LoginFrame extends HelperFrame {

	private JLabel login, password;
	private JTextField login_f;
	private JPasswordField password_f;
	private JButton btn_register, btn_login;
	private Message msg;
	private Authentication_msg authentication_msg;

	public LoginFrame(String title, Comms communicator) {

		super(title, communicator);
	}

	void init() {

		mainFrame.setLayout(gb);

		login = new JLabel("Login ID:");
		gbc.gridx = 0;
		gbc.gridy = 0;

		mainFrame.add(login);

		password = new JLabel("Password:  ");
		gbc.gridy = 1;
		gb.setConstraints(password, gbc);

		mainFrame.add(password);

		login_f = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gb.setConstraints(login_f, gbc);

		mainFrame.add(login_f);

		password_f = new JPasswordField(20);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gb.setConstraints(password_f, gbc);

		mainFrame.add(password_f);

		btn_register = new JButton(" Register ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gb.setConstraints(btn_register, gbc);

		mainFrame.add(btn_register);

		btn_login = new JButton(" Login ");

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gb.setConstraints(btn_login, gbc);

		mainFrame.add(btn_login);

		super.finalize();
		
		// directs to registration frame
		btn_register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				RegistrationFrame register = new RegistrationFrame("Registration", communicator);

				register.init();
				dispose(); // disposing frame on exit
			}

		});

		btn_login.addActionListener(new LoginListener());

	}
	
	/*
	 * listener will check for empty fields
	 * sends login detail to server and confirms the information
	 */
	class LoginListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (login_f.getText().equals("") 
				||login_f.getText().equals(" ") 
				|| password_f.getPassword().length == 0) {

				System.out.println("Please insert user ID and password.");
			} else {

				msg = new Login_msg(login_f.getText(), password_f.getPassword());
				
				try {

					communicator.sendMsg(msg);
					authentication_msg = (Authentication_msg) communicator.getMsg();

					if (authentication_msg.get_authenticaton()) {

						MainFrame mainFrame = new MainFrame("Auction System", communicator, login_f.getText());

						mainFrame.init();
						dispose();
					} else {

						System.out.println("Login failed ");
					}
				} catch (IOException e1) {
					System.err.println(e1);
				} catch (ClassNotFoundException e2) {
					System.err.println(e2);
				}
			}
		}
	}
}

/*
 * frame work for registration window
 * registration Form that takes name, family name, login name and password
 */
class RegistrationFrame extends HelperFrame {

	private JLabel user_name, family_name, login_name, password;
	private JTextField user_f, family_f, login_f;
	private JPasswordField password_f;
	
	private JButton btn_reset, btn_submit;
	
	private Message msg;
	private Authentication_msg authenticated_msg;

	public RegistrationFrame(String title, Comms com) {

		super(title, com);
	}

	void init() {

		mainFrame.setLayout(gb);
		
		user_name = new JLabel("Name: ");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gb.setConstraints(user_name, gbc);

		mainFrame.add(user_name);

		family_name = new JLabel("Family Name: ");
		gbc.gridy = 1;
		gb.setConstraints(family_name, gbc);

		mainFrame.add(family_name);
		
		login_name = new JLabel("Login Name: ");
		gbc.gridy = 2;
		gb.setConstraints(login_name, gbc);
		
		mainFrame.add(login_name);

		password = new JLabel("Password: ");
		gbc.gridy = 3;
		gb.setConstraints(password, gbc);

		mainFrame.add(password);

		user_f = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gb.setConstraints(user_f, gbc);

		mainFrame.add(user_f);

		family_f = new JTextField(20);
		gbc.gridy = 1;
		gb.setConstraints(family_f, gbc);

		mainFrame.add(family_f);
		
		login_f = new JTextField(20);
		gbc.gridy = 2;
		gb.setConstraints(login_f, gbc);
		
		mainFrame.add(login_f);
		
		password_f = new JPasswordField(20);
		gbc.gridy = 3;
		gb.setConstraints(password_f, gbc);

		mainFrame.add(password_f);
		
		btn_reset = new JButton(" RESET ");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gb.setConstraints(btn_reset, gbc);

		mainFrame.add(btn_reset);

		btn_submit = new JButton(" REGISTER ");
		gbc.gridx = 1;
		gb.setConstraints(btn_submit, gbc);

		mainFrame.add(btn_submit);

		super.finalize();
		
		btn_reset.addActionListener(new ActionListener() {
			// actionListener to clear all text field
			public void actionPerformed(ActionEvent e) {
				
				user_f.setText("");
				family_f.setText("");
				login_f.setText("");
				password_f.setText("");
			}
		});

		btn_submit.addActionListener(new submitListener());
	}
	
	/*
	 *  listener will check for empty fields
	 *  creates registration message and send it to server
	 */
	class submitListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (user_f.getText().equals("") || family_f.getText().equals("") 
											|| login_f.getText().equals("")
											|| password_f.getPassword().length == 0) {

				System.out.println("INCOMPLETE INFORMATION");
			} else {

				msg = new Register_msg(user_f.getText(), family_f.getText(), login_f.getText(), password_f.getPassword());
				
				try {

					communicator.sendMsg(msg);
					
					authenticated_msg = (Authentication_msg) communicator.getMsg();

				} catch (IOException e1) {
					System.err.println(e1);
				} catch (ClassNotFoundException e2) {
					System.err.println(e2);
				}

				MainFrame mainFrame = new MainFrame(" Main Auction System ", communicator, authenticated_msg.get_id());

				mainFrame.init();
				dispose();
			}
		}
	}
}

/*
 * table for displaying auction system items with various field
 */
class TableModel extends DefaultTableModel {
	
	public TableModel(Object[] colName, int numRow){
		
		super(colName,numRow);
		
	}

	public boolean isCellEditable(int row, int column){
		
		return false;	
	}
}

/*
 * frame work for main auction window
 */
class MainFrame extends HelperFrame {

	private String userUnique_id;
	private String user_name;
	
	private JTextField name_f, id_f, date_f;
	
	private JComboBox category_cb;
	private String[] category = { "", "FOOD", "CLOTHES", "SHOES", "LEISURE", "TRANSPORTATION", "ELECTRONIC" };

	private JLabel user_id;
	
	private JTable table;
	private TableModel table_model;
	private JScrollPane jsp;
	private String[] colName = { "ITEM", "DESCRIPTION", "CATEGORY", "SELLER ID", "CLOSING TIME", "RESERVE PRICE", "CURRENT BID", "BIDDER"};

	private JButton btn_search, btn_notice, btn_newItem, btn_status, btn_bid, btn_own_bid, btn_logout;
	private JPanel top_panel, middle_panel, bottom_panel;
	private ArrayList<Item> list = new ArrayList<Item>();

	public MainFrame(String title, Comms communicator, String userID) {

		super(title, communicator);
		this.userUnique_id = userID;
		
		UserName_msg getUser = new UserName_msg(userUnique_id);
		
		try {
			
			communicator.sendMsg(getUser);
			
			Authentication_msg msg = (Authentication_msg) communicator.getMsg();
			
			user_name = msg.get_id();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	void init() {

		mainFrame.setLayout(new BorderLayout());

		top_panel = new JPanel();

		top_panel.setLayout(new GridLayout(1, 5));

		user_id = new JLabel("wELCOME " + user_name);
		user_id.setHorizontalAlignment(JLabel.CENTER);
		top_panel.add(user_id);
		
		name_f = new JTextField("Item Name or ID", 20);
		top_panel.add(name_f);
		
		id_f = new JTextField("Seller ID", 20);
		top_panel.add(id_f);
		
		date_f = new JTextField("Date & Time: yyyy/mm/dd hh:mm", 20);
		top_panel.add(date_f);
		
		category_cb = new JComboBox(category);
		top_panel.add(category_cb);
		
		btn_search = new JButton("SEARCH");
		btn_search.addActionListener(new SearchListener());
		
		top_panel.add(btn_search);

		table_model = new TableModel(colName, 0);
		
		table = new JTable(table_model);
		table.setFillsViewportHeight(true);
		table.setRowHeight(50);
		table.getTableHeader().setReorderingAllowed(false);

		jsp = new JScrollPane(table);
		
		middle_panel = new JPanel();
		middle_panel.setLayout(new GridLayout(1, 0));
		middle_panel.add(jsp);

		bottom_panel = new JPanel();
		bottom_panel.setLayout(new GridLayout(1, 6));
		
		btn_notice = new JButton("NOTIFICATION");
		btn_notice.addActionListener(new NoticeListener());
		
		bottom_panel.add(btn_notice);
		
		btn_newItem = new JButton("SUBMIT ITEM");
		btn_newItem.addActionListener(new ActionListener() {
			
			// listener will open new window 
			public void actionPerformed(ActionEvent e) {

				SubmitFrame sf = new SubmitFrame("New Item", communicator, userUnique_id, category);
				sf.init();
			}
		});
		
		bottom_panel.add(btn_newItem);
		
		btn_status = new JButton("SALE STATUS");
		btn_status.addActionListener(new SaleStatusListener());
		
		bottom_panel.add(btn_status);
		
		btn_own_bid = new JButton("BID STATUS");
		btn_own_bid.addActionListener(new BidStatusListener());
		
		bottom_panel.add(btn_own_bid);
		
		btn_bid = new JButton(" BID ITEM ");
		btn_bid.addActionListener(new ActionListener() {
			
			// listener will check for item selection and run bidding frame
			public void actionPerformed(ActionEvent e) {

				int row = table.getSelectedRow();

				if (row == -1 ) {
					System.out.println("Item not selected");
				} else {

					Item item = list.get(row);
					BidFrame bf = new BidFrame("Bid", item.get_seller(), item.get_closingDate(), 
												communicator, userUnique_id, item.get_itemID());
					bf.init();
				}
			}
		});

		bottom_panel.add(btn_bid);
		
		btn_logout = new JButton("LOGOUT");
		btn_logout.addActionListener(new ActionListener(){
			
			// listener will close current window and close all connection with server
			public void actionPerformed(ActionEvent e) {
				Message msg = new Message("Logout Message");
				
				try {
					
					communicator.sendMsg(msg);
					dispose();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		
		bottom_panel.add(btn_logout);

		mainFrame.add(top_panel, BorderLayout.NORTH);
		mainFrame.add(middle_panel, BorderLayout.CENTER);
		mainFrame.add(bottom_panel, BorderLayout.SOUTH);

		super.finalize();
	}
	
	/*
	 * listener will check each field
	 * send message to server and gets information
	 * store the info in an array
	 * add them to table and repaint
	 */
	class SearchListener implements ActionListener {

		private String item;
		private String seller_id;
		private String dateTime_id;
		private String category;

		public void actionPerformed(ActionEvent e) {

			if (name_f.getText().equals("Item Name or ID") || name_f.getText().equals("")) {

				item = null;
			} else {

				item = name_f.getText();
			}

			if (id_f.getText().equals("Seller ID") || id_f.getText().equals("")) {

				seller_id = null;
			} else {

				seller_id = id_f.getText();
			}

			if (date_f.getText().equals("Date & Time: yyyy/mm/dd hh:mm")|| date_f.getText().equals("")) {

				dateTime_id = null;
			} else {

				dateTime_id = date_f.getText();
			}

			if (category_cb.getSelectedItem().toString().equals("")) {

				category = null;
			} else {

				category = category_cb.getSelectedItem().toString();
			}
			
			try {
				
				Message search_msg = new Search_msg(item, seller_id, dateTime_id, category);
				
				communicator.sendMsg(search_msg);

				Result_msg result_msg = (Result_msg) communicator.getMsg();

				list = result_msg.getList();
				
				table_model.setRowCount(0);
				
				for (Item i : list) {

					Double current_bid = null;
					String bidder = null;

					ArrayList<Bid> temp = i.get_bidArray();

					if (!(temp.size() == 0)) {

						current_bid = temp.get(temp.size() - 1).get_bid();

						bidder = temp.get(temp.size() - 1).get_id();
					}

					Object[] obj = {i.get_title(), 
									i.get_detail(),
									i.get_category(), 
									i.get_seller(), 
									i.get_closingDate(),
									i.get_reserveP(), 
									current_bid, 
									bidder };

					table_model.addRow(obj);
				}

				repaint();
			} catch (IOException e1) {
				System.err.println(e1);
			} catch (ClassNotFoundException e2) {
				System.err.println(e2);
			}
		}
	}
	
	class NoticeListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			try {
				
				Notice_msg check_notice = new Notice_msg(userUnique_id);
				
				table_model.setRowCount(0);
				
				communicator.sendMsg(check_notice);
			
				Result_msg result_msg = (Result_msg) communicator.getMsg();
				
				list = result_msg.getList();
				
				for (Item i : list) {

					Double current_bid = null;
					String bidder = null;

					ArrayList<Bid> temp = i.get_bidArray();

					if (!(temp.size() == 0)) {

						current_bid = temp.get(temp.size() - 1).get_bid();
						bidder = temp.get(temp.size() - 1).get_id() + " (Winner)";
					}

					Object[] obj = {i.get_title(), 
									i.get_detail(),
									i.get_category(), 
									i.get_seller(), 
									i.get_closingDate(),
									i.get_reserveP(), 
									current_bid, 
									bidder };

					table_model.addRow(obj);
				}

				repaint();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/*
	 * sale status of user
	 * sends message to server and retrive user item auction list
	 * display user item auction status
	 */
	class SaleStatusListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			Message sale_msg = new SaleStatus_msg (userUnique_id);

			try {
				
				table_model.setRowCount(0);

				communicator.sendMsg(sale_msg);
				
				Result_msg result_msg = (Result_msg) communicator.getMsg();
				list = result_msg.getList();

				for (Item item : list) {

					Double current_bid = null;
					String bidder = null;

					ArrayList<Bid> temp = item.get_bidArray();
					
					// retriving highest bid and bidder name
					if (!(temp.size() == 0)) {

						current_bid = temp.get(temp.size() - 1).get_bid();
						bidder = temp.get(temp.size() - 1).get_id();
					}

					Object[] fields = { item.get_title(), 
										item.get_detail(), 
										item.get_category(), 
										item.get_seller(), 
										item.get_closingDate(), 
										item.get_reserveP(), 
										current_bid, bidder };

					table_model.addRow(fields);
					repaint();
				}
			} catch (IOException e1) {
				System.err.println(e1);
			} catch (ClassNotFoundException e2) {
				System.err.println(e2);
			}
		}
	}
	
	/*
	 * sends user id and retrive bid list of all item he has bid on
	 * display user bids
	 */
	class BidStatusListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			Message bid_msg = new BidStatus_msg(userUnique_id);

			try {
				
				table_model.setRowCount(0);
				
				communicator.sendMsg(bid_msg);

				Result_msg result_msg = (Result_msg) communicator.getMsg();

				list = result_msg.getList();

				for (Item item : list) {

					Double current_bid = null;
					String bidder = null;

					ArrayList<Bid> temp = item.get_bidArray();
					
					// retriving highest bid and bidder name
					if (!(temp.size() == 0)) {

						current_bid = temp.get(temp.size() - 1).get_bid();
						bidder = temp.get(temp.size() - 1).get_id();
					}

					Object[] fields = { item.get_title(), 
										item.get_detail(), 
										item.get_category(), 
										item.get_seller(), 
										item.get_closingDate(), 
										item.get_reserveP(), 
										current_bid, 
										bidder };

					table_model.addRow(fields);
					repaint();
				}

			} catch (IOException e1) {
				System.err.println(e1);
			} catch (ClassNotFoundException e2) {
				System.err.println(e2);
			}
		}
	}
}

/*
 * frame work for bid window
 */
class BidFrame extends HelperFrame {

	private String seller, user_id, item_id, closing_date;
	
	private JPanel panel;
	
	private JLabel seller_name, closing_d, current_bit;
	private JTextField bid_f;

	private JButton btn_bid;

	public BidFrame(String title, String seller, String closing_date, Comms com, String user_id, String item_id) {

		super(title, com);
		this.seller = seller;
		this.closing_date = closing_date;
		this.user_id = user_id;
		this.item_id = item_id;
	}

	public void init() {
		
		mainFrame.setLayout(new GridLayout(2,2));
		
		seller_name = new JLabel("SELLER: " + seller);
		mainFrame.add(seller_name);
		
		closing_d = new JLabel("CLOSING DATE: " + closing_date);
		mainFrame.add(closing_d);
		
		bid_f = new JTextField("ENTER YOUR BID", 20);
		mainFrame.add(bid_f);
		
		btn_bid = new JButton(" BID ");
		mainFrame.add(btn_bid);

		super.finalize();
		
		/*
		 * sends user bid and user id
		 * server return msg will decide if bid was successful
		 */
		btn_bid.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					Bid bid = new Bid(item_id, Double.parseDouble(bid_f.getText()));

					Bid_msg bidMsg = new Bid_msg(item_id, bid, seller);

					communicator.sendMsg(bidMsg);

					Message msg2 = communicator.getMsg();

					if (msg2.get_msg().equals("BID SUCCESSFUL")) {

						System.out.println(msg2.get_msg());

					} else {
						
						System.out.println(msg2.get_msg());
					}
					
					dispose();
				} catch (IOException e1) {
					System.err.println(e1);
				} catch (ClassNotFoundException e1) {
					System.err.println(e1);
				} catch (NumberFormatException e1) {
					System.out.println("INVALID BID");
				}

			}

		});

	}

}

/*
 * frame work for submit window
 */
class SubmitFrame extends HelperFrame {

	private String user_id;
	private JLabel title, full_detail, category, close_d, end_T, reserve_p;
	private JTextField title_f, detail_f, closeDate_f, reserve_f;

	private JComboBox category_cb, endTime_hrs, endTime_min;

	private String[] category_array;
	private String[] hours = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
	private String[] mins = {"00", "10", "20", "30", "40", "50"};
	
	private JButton submit;

	public SubmitFrame(String title, Comms communicator, String user_id, String[] cat) {

		super(title, communicator);
		this.user_id = user_id;
		category_array = cat;

	}

	public void init() {

		mainFrame.setLayout(gb);

		title = new JLabel("AUCTION TITLE :");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gb.setConstraints(title, gbc);

		mainFrame.add(title);

		title_f = new JTextField(20);
		gbc.gridx = 1;
		gb.setConstraints(title_f, gbc);

		mainFrame.add(title_f);

		full_detail = new JLabel("DESCRIPTION : ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gb.setConstraints(full_detail, gbc);

		mainFrame.add(full_detail);

		detail_f = new JTextField(20);
		gbc.gridx = 1;
		gb.setConstraints(detail_f, gbc);

		mainFrame.add(detail_f);

		category = new JLabel("CATEGORY : ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gb.setConstraints(category, gbc);

		mainFrame.add(category);

		category_cb = new JComboBox(category_array);
		gbc.gridx = 1;
		gb.setConstraints(category_cb, gbc);

		mainFrame.add(category_cb);

		close_d = new JLabel("CLOSING DATE : ");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gb.setConstraints(close_d, gbc);

		mainFrame.add(close_d);
		closeDate_f = new JTextField("yyyy/mm/dd", 20);
		gbc.gridx = 1;
		gb.setConstraints(closeDate_f, gbc);

		mainFrame.add(closeDate_f);
		
		end_T = new JLabel("END TIME : ");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gb.setConstraints(end_T, gbc);
		
		mainFrame.add(end_T);
		
		JPanel time_panel = new JPanel();
		time_panel.setLayout(new GridLayout(1, 3));
		gbc.gridx = 1;
		gb.setConstraints(time_panel, gbc);
		
		endTime_hrs = new JComboBox(hours);
		endTime_hrs.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
		endTime_min = new JComboBox(mins);
		
		time_panel.add(endTime_hrs);
		time_panel.add(endTime_min);
		
		mainFrame.add(time_panel);
		
		reserve_p = new JLabel("RESERVE PRICE : ");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gb.setConstraints(reserve_p, gbc);

		mainFrame.add(reserve_p);
		
		reserve_f = new JTextField(20);
		gbc.gridx = 1;
		gb.setConstraints(reserve_f, gbc);

		mainFrame.add(reserve_f);

		submit = new JButton(" SUBMIT ");
		gbc.gridy = 6;
		gb.setConstraints(submit, gbc);

		mainFrame.add(submit);

		super.finalize();

		submit.addActionListener(new SubmitListener());
	}
	
	/*
	 * lister will check for empty fields
	 * create new item object
	 * sends message to server which takes item as parameter for storing
	 */
	class SubmitListener implements ActionListener {
		
		// formatting time and date display
		SimpleDateFormat dataFormat2 = new SimpleDateFormat("yyyy/mm/dd hh:mm");

		public void actionPerformed(ActionEvent e) {
			
			//Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("BST"));
			
			Calendar cal = new GregorianCalendar();
			TimeZone timeZone = TimeZone.getDefault();
			timeZone = cal.getTimeZone();
			cal = Calendar.getInstance();
			
			if (title_f.getText().equals("") 
					|| detail_f.getText().equals("")
					|| closeDate_f.getText().equals("yyyy/mm/dd")
					|| closeDate_f.getText().equals("") 
					|| reserve_f.getText().equals("")
					|| category_cb.getSelectedItem().toString().equals("")
					|| endTime_hrs.getSelectedItem().toString().equals("")) {

				System.out.println("INCOMPLETE INFORMAITION");
			} else {

				try {
					
					// concatenation of date and time 
					String endTime = closeDate_f.getText() +  " " + endTime_hrs.getSelectedItem() + ":" + endTime_min.getSelectedItem();
					
					DateFormat dateFormats = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					
					// checking for invalid closing date
					if(cal.getTime().after(dateFormats.parse(endTime))) {
						System.err.print("INVALID DATE \n");
					} else {
						Item item = new Item(user_id, 
										 title_f.getText(), 
										 detail_f.getText(), 
										 category_cb.getSelectedItem().toString(),
										 cal.getTime(),
										 endTime,
										 Double.parseDouble(reserve_f.getText()));

						Message imsg = new Item_msg(item);

						communicator.sendMsg(imsg);
					
						dispose();
					}
				} catch (IOException e1) {
					System.err.println(e1);
				} catch (NumberFormatException e1){
					System.out.println("INVALID BID PRICE");			
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}