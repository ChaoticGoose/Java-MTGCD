import java.io.*;
import java.sql.*;

class MTGCD
{
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
 	public static void main(String[] argv)
 	{
			//attempt to open the connection to the database, if it fails, exit the program
		if(!DatabaseManager.OpenConnection()) return;
		
		
			//loop to give CLI
		while(true)
		{
			System.out.println("Enter Command");
			String[] Input = {""};
			
			//try to read from command line
			try{Input = reader.readLine().toLowerCase().split(" ");}
				catch(Exception e){System.out.println("Error reading input");}
			
			//jump to the correct part of the program for the action the user is trying to perform
			switch(Input[0])
			{
				case "all":
					PrintAllCards();
					break;
				case "add":
					AddCard(Input);
					break;
				case "remove":
					RemoveCard(Input);
					break;
				case "set":
					PrintSet(Input);
					break;
				case "help":
					Help(Input);
					break;
				case "exit":
					DatabaseManager.CloseConnection();
					return;
			}
		}
	}
	
	private static void Help(String[] Input)
	{
		if(Input.length ==1)
		{
			System.out.println("This application assists with managing a database for a Magic The Gathering Collection.");
			System.out.println("The following commands are available");
			System.out.println("\tADD - add a copy of a card to the database");
			System.out.println("\tREMOVE - remove a copy of a card from the database");
			System.out.println("\tSET - show a list of all cards from a set, which are in the database");
			System.out.println("\tALL - Show a list of all cards in teh database");
			System.out.println("\tEXIT - Exit the application");
			System.out.println("\tHELP - Show this message");
			return;
		}
		
		switch(Input[1])
		{
				case "add":
					System.out.println("Add a card to the database");
						System.out.println("\t usage: add <set> <number> <foil> <quantity>");
							System.out.println("\t\t set: three letter set code");
							System.out.println("\t\t number: the collectors number of the card");
							System.out.println("\t\t foil: 1 for a foil, 0 for non-foil");
							System.out.println("\t\t quantity: number greater than 0 (can be blank)");
					break;
				case "remove":
					System.out.println("Remove a card from the database");
						System.out.println("\t usage: remove <set> <number> <foil> <quantity>");
							System.out.println("\t\t set: three letter set code");
							System.out.println("\t\t number: the collectors number of the card");
							System.out.println("\t\t foil: 1 for a foil, 0 for non-foil");
							System.out.println("\t\t quantity: number greater than 0 (can be blank)");
					break;
				case "set":
					System.out.println("Show information on all cards from a set in the database");
						System.out.println("\tUsage: set <set_code>");
							System.out.println("\t\t set_code: three leter set code");
					break;
		}
	}
	
	private static void AddCard(String[] card)
	{
		Card c = Card.getCardFromStringArray(card);
		
		if(c == null)
		{
			System.out.println("Invalid Card format");
			return;
		}
		
		DatabaseManager.AddCard(c);
	}
	
	private static void RemoveCard(String[] card)
	{
		Card c = Card.getCardFromStringArray(card);
		
		if(c == null)
		{
			System.out.println("Invalid Card format");
			return;
		}
		
		DatabaseManager.RemoveCard(c);
	}
	
	//Prints all the cards in the database, ordered alphabetically by set code 
		//then by collectors number
	private static void PrintAllCards()
	{	
		String[] res = DatabaseManager.PrintAllCards();
		
		for(int i = 0; i< res.length; i++)
		{
			System.out.println(res[i]);
		}
	}
	
	private static void PrintSet(String[] set)
	{
		if(set.length != 2)
		{
			System.out.println("Invalid input");
		}
	
		String[] res = DatabaseManager.PrintSet(set[1]);
		
		for(int i = 0; i< res.length; i++)
		{
			System.out.println(res[i]);
		}
	}
	
}
