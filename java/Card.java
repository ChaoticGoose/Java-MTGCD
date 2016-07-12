//private class to hold information about a card, makes passing information cleaner
class Card
{
	private int CN;
	private String SET;
	private boolean Foil;
	private int QUANTITY;
	
	public Card(int CollectorsNumber, String SetCode, boolean foil, int quantity)
	{
		CN = CollectorsNumber;
		SET = SetCode;
		Foil = foil;
		QUANTITY = quantity;
	}
	
	public Card(int CollectorsNumber, String SetCode, boolean foil)
	{
		CN = CollectorsNumber;
		SET = SetCode;
		Foil = foil;
		QUANTITY = 1;
	}
	
	public int getCN(){return CN;}
	public String getSetCode(){return SET;}
	public boolean getFoil(){return Foil;}
	public int getQuantity(){return QUANTITY;}
	
	public String getSQLFindString()
	{
		return 
			" where Collector_Number = " +
			CN + " AND Set_Code = '" + 
			SET + "' AND Foil = " + 
			(Foil ? "1":"0");
	}
	public String getFormatString()
	{
		return SET + "\t" + CN + "\t" + QUANTITY + "\t" + (Foil ? "Yes":"No");
	}
	
	public static Card getCardFromStringArray(String[] Input)
	{
		//ignore index 0, as that is the command that was passed to the command line
		
		//index 1 should be the set that the card is from
			//ensure that the set is uppercase		
		//index 2 should be the number of the card in the set		
		//index 3 should be 1 if the card is a foil, otherwise 0
		
		String set;
		int cn;
		int f;
		int q=1;
		
		//length should therefore be 4
		if(Input.length == 5)
		{
			q = Integer.parseInt(Input[4]);		
		}
		if(Input.length == 4 || Input.length == 5)
		{
			set = Input[1].toUpperCase();
			cn = Integer.parseInt(Input[2]);
			f = Integer.parseInt(Input[3]);
			
				//if all inputs are in the correct format, generate the card
			if(set.length() == 3)
			{
				if(cn > 0 && q>0)
				{
					if(f == 1 || f ==0)
					{
							Card c = new Card(cn, set, f==1, q);							
							return c;
					}
				}
			}
		}

		return null;
	}
} 
