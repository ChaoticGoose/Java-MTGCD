//private class to hold information about a card, makes passing information cleaner
class Card
{
	private int CN;
	private String SET;
	private boolean Foil;
	
	public Card(int CollectorsNumber, String SetCode, boolean foil)
	{
		CN = CollectorsNumber;
		SET = SetCode;
		Foil = foil;
	}
	
	public int getCN(){return CN;}
	public String getSetCode(){return SET;}
	public boolean getFoil(){return Foil;}
	
	public String getSQLFindString()
	{
		return 
			" where Collector_Number = " +
			CN + " AND Set_Code = '" + 
			SET + "' AND Foil = " + 
			(Foil ? "1":"0");
	}
	public String getFormatString(int Quantity)
	{
		return SET + "\t" + CN + "\t" + Quantity + "\t" + (Foil ? "Yes":"No");
	}
} 
