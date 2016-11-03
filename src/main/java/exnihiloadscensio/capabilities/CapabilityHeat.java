package exnihiloadscensio.capabilities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class CapabilityHeat implements ICapabilityHeat
{
    @Getter
    @Setter
    int heatRate;
    
    public CapabilityHeat()
    {
        
    }
    
    public CapabilityHeat(int heatRate)
    {
        this.heatRate = heatRate;
    }
}
