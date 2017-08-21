package exnihiloadscensio.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil
{
    private static Logger logger = LogManager.getLogger("Ex Nihilo Adscensio");
    private static File logFile;
    private static PrintWriter logWriter;
    
    public static void log(Level level, Object object)
    {
        String message = object == null ? "null" : object.toString();
        
        String preLine = new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + " [" + level.name() + "] ";
        
        for(String line : message.split("\\n"))
        {
            logger.log(level, line);
            logWriter.println(preLine + line);
        }
        
        logWriter.flush();
    }
    
    public static <T extends Throwable> T throwing(T thrown)
    {
        return throwing(Level.ERROR, thrown);
    }
    
    public static <T extends Throwable> T throwing(Level level, T thrown)
    {
        log(level, ExceptionUtils.getStackTrace(thrown));
        
        return thrown;
    }
    
    public static void fatal(Object object)
    {
        log(Level.FATAL, object);
    }

    public static void error(Object object)
    {
        log(Level.ERROR, object);
    }

    public static void warn(Object object)
    {
        log(Level.WARN, object);
    }

    public static void info(Object object)
    {
        log(Level.INFO, object);
    }
    
    public static void debug(Object object)
    {
        log(Level.DEBUG, object);
    }
    
    public static void trace(Object object)
    {
        log(Level.TRACE, object);
    }
    
    public static void setup()
    {
        File logDirectory = new File("./logs/exnihiloadscensio/");
        logDirectory.mkdirs();
        
        String baseName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        int i = 0;
        
        // One-liners for the win
        for (; (logFile = new File(logDirectory, baseName + "-" + i + ".log")).exists(); i++);
        
        try
        {
            logFile.createNewFile();
            logWriter = new PrintWriter(new FileWriter(logFile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
