import java.io.*;
import java.sql.*;

class MTGCD
{
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Connection DatabaseConnection;
	private static Statement DatabaseStatement;
	
 	public static void main(String[] argv)
 	{
			//attempt to open the connection to the database, if it fails, exit the program
		if(!OpenConnection()) return;
		
		String Input = "";
		
			//loop to give CLI
		while(Input != "exit")
		{
			System.out.println("Enter Command");
			
			//try to read from command line
			try{Input = reader.readLine().toLowerCase();}
				catch(Exception e){System.out.println("Error reading input");}
			
			//jump to the correct part of the program for the action the user is trying to perform
			switch(Input)
			{
				case "add":
					AddCard();
					break;
				case "remove":
					RemoveCard();
					break;
				case "set":
					PrintSet();
					break;
				case "all":
					PrintAllCards();
					break;
				case "help":
					Help();
					break;
				case "exit":
					CloseConnection();
					return;
			}
		}
	}
	
	private static boolean OpenConnection()
	{
		//define the database to connect to (localhost:3306) and database (MTG_Collection)
		String url = "jdbc:mysql://localhost:3306/MTG_Collection";
			//define the toye of database that is being used (mysql)
		String driver = "com.mysql.jdbc.Driver";
			//database login name (MTG)
		String user = "MTG"; 
			//database login password (MTG)
		String pass = "MTG";
		
			//try-catch to handle errors in connecting to database
		try 
		{
				//load the driver for the database
			Class.forName(driver).newInstance();
				//open a connection to the database
			//Connection conn = DriverManager.getConnection(url,user,pass);
			DatabaseConnection = DriverManager.getConnection
			(
				url +
				"?" +
				"user=" +
				user +
				"&password=" +
				pass +
				"&useSSL=false"
			);
			
			DatabaseStatement = DatabaseConnection.createStatement();
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Error connecting to Database");
			return false;
		}
	}
	
	private static void CloseConnection()
	{
		try
		{
			DatabaseConnection.close();
		}
		catch(Exception ex)
		{
			System.out.println("Error closing connection");
		}
	}
	
	public static void Help()
	{
		System.out.println("\tThis application assists with managing a database for a Magic The Gathering Collection.");
		System.out.println("\tThe following commands are available");
		System.out.println("\t\tADD - add a copy of a card to the database");
		System.out.println("\t\tREMOVE - remove a copy of a card from the database");
		System.out.println("\t\tSET - show a list of all cards from a set, which are in the database");
		System.out.println("\t\tALL - Show a list of all cards in teh database");
		System.out.println("\t\tEXIT - Exit the application");
		System.out.println("\t\tHELP - Show this message");
	}
	
	public static Card GetCard()
	{
		System.out.println
		(
			"Please provide Set Code, Collector Number and if the card is foil(1) or not(0)"
		);
		
		while(true)
		{
			String[] Input = {};
			
			try
			{
				Input = reader.readLine().split(" ");
			}
			//literally the worst way to handle an error (ignore it)
			//the rest of this code block checks all conditions anyway
			catch(Exception e){}
			
			if(Input.length == 3)
			{
				try
				{
					String SET = Input[0];
					int CN = Integer.parseInt(Input[1]);
					int F = Integer.parseInt(Input[2]);
					
					//if all inputs are in the correct format continue to add the value to the database
					if(SET.length() == 3)
					{
						if(CN > 0)
						{
							if(F == 1 || F ==0)
							{
									Card c = new Card(CN, SET, F==1);
									return c;
							}
							else
							{
								System.out.println("The value for foil should be 1 or 0");
							}
						}
						else
						{
							System.out.println("The card number should be greater than 0");
						}
					}
					else
					{
						System.out.println("The set code should be a three character string");
					}

				}
				catch(Exception e){System.out.println("Invalid Syntax for card number or foil");}
			}
			else
			{
				System.out.println("Please provide all values on one line, separated by spaces");
			}
		}
	}
	
	
	//Adds a card to the collection
		//if there is already a copy of the card, then increment the quantity
	public static void AddCard(Card c)
	{
		try
		{
				//execute a query, and get the response
				//this query selects all recored (all cards) from the MTG_Collection database
			ResultSet res = DatabaseStatement.executeQuery("Select * from MTG_Collection" + c.getSQLFindString());
			
				//if a card was found, we need to update the quantity
				//otherwise create a new card in the database
			if(res.next())
			{
				System.out.println("Updating card count");
					//get the number of this card that exist in the database
				int Quantity = res.getInt("Quantity") + 1;				
				int index = res.getInt("id");
				
				//execute a query to update the quantity of cards
				DatabaseStatement.executeUpdate
				(
					"update MTG_Collection SET Quantity="+
					Quantity+
					" where id="+
					index
				);
			}
			else
			{
				System.out.println("Adding new card to database");
				DatabaseStatement.executeUpdate
				(
					"INSERT into MTG_Collection " +
					"(Set_Code,Collector_Number,Foil,Quantity)" +
					" values ("+
					"'"+c.getSetCode()+"',"+
					c.getCN()+","+
					c.getFoil()+","+
					"1)"
				);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void AddCard()
	{
		AddCard(GetCard());
	}
	
	//Removes a card from the database
		//if this is the last copy, delete the row
	public static void RemoveCard(Card c)
	{
		try
		{
				//execute a query, and get the response
				//this query selects all recored (all cards) from the MTG_Collection database
			ResultSet res = DatabaseStatement.executeQuery("Select * from MTG_Collection" + c.getSQLFindString());
			
				//if a card was found, we need to update the quantity
				//otherwise create a new card in the database
			if(res.next())
			{
					//get the number of this card that exist in the database
				int Quantity = res.getInt("Quantity") -1;				
				int index = res.getInt("id");
				
				if(Quantity > 0)
				{
					System.out.println("Updating card count");
						//is at least one card still in database, update value
						//execute a query to update the quantity of cards
					DatabaseStatement.executeUpdate
					(
						"update MTG_Collection SET Quantity="+
						Quantity+
						" where id="+
						index
					);
				}
				else
				{
					System.out.println("Removing Card from database");
					//no copys of this card in the database, remove the row
					DatabaseStatement.executeUpdate("delete from MTG_Collection where id="+index);
				}
			}
			else
			{
				System.out.println("No card to remove");
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void RemoveCard()
	{
		RemoveCard(GetCard());
	}
	
	//Prints all the cards in the database, ordered alphabetically by set code 
		//then by collectors number
	public static void PrintAllCards()
	{
		try
		{
				//execute a query, and get the response
				//this query selects all recored (all cards) from the MTG_Collection database
			ResultSet res = DatabaseStatement.executeQuery
			(
				"Select * from MTG_Collection ORDER BY Set_Code ASC, Collector_Number ASC"
			);
			
				//print the header for the output
			System.out.println("Set \tNumber\tQty\tFoil");
				//operate over each result returned
			while(res.next())
			{
					//get the number of this card that exist in the database
				int Quantity = res.getInt("Quantity");
				
				Card c = new Card
				(
					res.getInt("Collector_Number"),
					res.getString("Set_Code"),
					res.getBoolean("Foil")
				);
				
				System.out.println(c.getFormatString(Quantity));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void PrintSet(String SET)
	{
		try
		{
				//execute a query, and get the response
				//this query selects all recored (all cards) from the MTG_Collection database
			ResultSet res = DatabaseStatement.executeQuery
			(
				"Select * from MTG_Collection "+
				"where Set_Code='"+ SET +
				"' ORDER BY Set_Code ASC, Collector_Number ASC"
			);
			
				//print the header for the output
			System.out.println("Set \tNumber\tQty\tFoil");
				//operate over each result returned
			while(res.next())
			{
					//get the number of this card that exist in the database
				int Quantity = res.getInt("Quantity");
				
				Card c = new Card
				(
					res.getInt("Collector_Number"),
					res.getString("Set_Code"),
					res.getBoolean("Foil")
				);
				
				System.out.println(c.getFormatString(Quantity));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void PrintSet()
	{
		while(true)
		{
			try
			{
				System.out.println("Enter set code");
				String SET = reader.readLine().toUpperCase();
				PrintSet(SET);
				return;
			}
			catch(Exception e)
			{
				System.out.println("Error reading input");
			}
		}
	}
}
