package exnihiloadscensio.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileSievePress extends TileEntity implements ITickable {
	
	public float progress = 0;
	
	public float speed = 1.0f/10;
	
	public TileSievePress() {}

	@Override
	public void update() {
		if (worldObj.isRemote)
			return;
		
		TileEntity te = worldObj.getTileEntity(pos.add(0, -1, 0));
		if (te == null || !(te instanceof TileSieve))
			return;
		
		progress += speed;

		if (progress >= 1) {
			//Do some sieving
			TileSieve sieve = (TileSieve) te;
			sieve.doSieving(null);
			progress = 0;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setFloat("progress", progress);
		
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		progress = tag.getFloat("progress");
		
		super.readFromNBT(tag);
	}

}
