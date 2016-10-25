package exnihiloadscensio.util;

import exnihiloadscensio.ExNihiloAdscensio;

public class LogUtil
{
    public static void info(Object message)
    {
        ExNihiloAdscensio.logger.info(message);
    }
    
    public static void warn(Object message)
    {
        ExNihiloAdscensio.logger.warn(message);
    }

    public static void error(Object message)
    {
        ExNihiloAdscensio.logger.error(message);
    }
    
    public static void debug(Object message)
    {
        ExNihiloAdscensio.logger.debug(message);
    }
}
