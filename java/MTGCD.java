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
					Help();
					break;
				case "exit":
					DatabaseManager.CloseConnection();
					return;
			}
		}
	}
	
	private static void Help()
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
