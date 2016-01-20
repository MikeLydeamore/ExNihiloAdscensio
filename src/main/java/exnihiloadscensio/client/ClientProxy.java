package exnihiloadscensio.client;

import exnihiloadscensio.CommonProxy;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.items.ENItems;

public class ClientProxy extends CommonProxy {
	
	public void initModels()
	{
		ENItems.initModels();
		ENBlocks.initModels();
	}
	
	@Override
	public boolean runningOnServer()
	{
		return false;
	}
	

}
