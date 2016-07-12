import java.sql.*;

class DatabaseManager
{
	private static Connection DatabaseConnection;
	private static Statement DatabaseStatement;
	
	public static boolean OpenConnection()
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
			return false;
		}
	}
	
	public static boolean CloseConnection()
	{
		try
		{
			DatabaseConnection.close();
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
		//Adds a card to the collection
		//if there is already a copy of the card, then increment the quantity
	public static boolean AddCard(Card c)
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
				//System.out.println("Updating card count");
					//get the number of this card that exist in the database
				int Quantity = res.getInt("Quantity") + c.getQuantity();	
				System.out.println(c.getQuantity());			
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
				//System.out.println("Adding new card to database");
				DatabaseStatement.executeUpdate
				(
					"INSERT into MTG_Collection " +
					"(Set_Code,Collector_Number,Foil,Quantity)" +
					" values ("+
					"'"+c.getSetCode()+"',"+
					c.getCN()+","+
					c.getFoil()+","+
					c.getQuantity() +
					")"
				);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
		//Removes a card from the database
		//if this is the last copy, delete the row
	public static boolean RemoveCard(Card c)
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
				int Quantity = res.getInt("Quantity") -c.getQuantity();				
				int index = res.getInt("id");
				
				if(Quantity > 0)
				{
					//System.out.println("Updating card count");
						//is at least one card still in database, update value
						//execute a query to update the quantity of cards
					DatabaseStatement.executeUpdate
					(
						"update MTG_Collection SET Quantity="+
						Quantity+
						" where id="+
						index
					);
					return true;
				}
				else
				{
					//System.out.println("Removing Card from database");
					//no copys of this card in the database, remove the row
					DatabaseStatement.executeUpdate("delete from MTG_Collection where id="+index);
					return true;
				}
			}
			else
			{
				//System.out.println("No card to remove");
				return false;
			}
		}
		catch(Exception e)
		{
			//System.out.println(e.getMessage());
			return false;
		}
	}
	
		//Prints all the cards in the database, ordered alphabetically by set code 
		//then by collectors number
	public static String[] PrintAllCards()
	{
		try
		{
				//execute a query, and get the response
				//this query selects all recored (all cards) from the MTG_Collection database
			ResultSet res = DatabaseStatement.executeQuery("Select * from MTG_Collection ORDER BY Set_Code ASC, Collector_Number ASC");
			if(res == null)
			{
				System.out.println("Error reading from database");
				return null;
			}
			
				//move to the end of the resultset
			res.last();
			//get the row number
			int rows = res.getRow();
			//move back to the beginning
			res.beforeFirst();
			
			String[] retVal = new String[rows+1];
		
				//print the header for the output
			retVal[0] = "Set \tNumber\tQty\tFoil";
			
				//operate over each result returned
			while(res.next())
			{			
				Card c = new Card
				(
					res.getInt("Collector_Number"),
					res.getString("Set_Code"),
					res.getBoolean("Foil"),
					res.getInt("Quantity")
				);
			
				retVal[res.getRow()] = c.getFormatString();
			}
			
			return retVal;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static String[] PrintSet(String SET)
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
			
			//move to the end of the resultset
			res.last();
			//get the row number
			int rows = res.getRow();
			//move back to the beginning
			res.beforeFirst();
			
			String[] retVal = new String[rows+1];
			
				//print the header for the output
			retVal[0] ="Set \tNumber\tQty\tFoil";
				//operate over each result returned
			while(res.next())
			{
				
				Card c = new Card
				(
					res.getInt("Collector_Number"),
					res.getString("Set_Code"),
					res.getBoolean("Foil"),
					res.getInt("Quantity")
				);
				
				retVal[res.getRow()] = c.getFormatString();
			}
			
			return retVal;
		}
		catch(Exception e)
		{
			//System.out.println(e.getMessage());
			return null;
		}
	}
}
