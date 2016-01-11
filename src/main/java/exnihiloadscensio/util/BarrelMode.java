package exnihiloadscensio.util;

public enum BarrelMode {

	EMPTY(0), COMPOSTING(1), FLUID(2);
	
	private int id;

	private BarrelMode(int id)
	{
		this.id = id;
	}

	public BarrelMode getMode(int number)
	{
		switch (number)
		{
		case 0:
			return EMPTY;

		case 1:
			return COMPOSTING;
			
		case 2:
			return FLUID;

		default:
			return null;
		}

	}

	public int getNumber(BarrelMode mode)
	{
		switch (mode)
		{
		case EMPTY:
			return 0;

		case COMPOSTING:
			return 1;

		case FLUID:
			return 2;
			
		default:
			return 0;
		}
	}

}
