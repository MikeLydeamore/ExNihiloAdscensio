package exnihiloadscensio.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil
{
    private static Logger logger = LogManager.getLogger("Ex Nihilo Adscensio");
    private static File logFile;
    private static PrintWriter logWriter;
    
    public static void log(Level level, Object object)
    {
        String message = object == null ? "null" : object.toString();
        
        logger.log(level, message);
        logWriter.write(new SimpleDateFormat("[HH:mm:ss]").format(new Date()));
        logWriter.write(" [");
        logWriter.write(level.name());
        logWriter.write("] ");
        logWriter.write(message);
        logWriter.println();
        logWriter.flush();
    }
    
    public static void info(Object object)
    {
        log(Level.INFO, object);
    }
    
    public static void warn(Object object)
    {
        log(Level.WARN, object);
    }

    public static void error(Object object)
    {
        log(Level.ERROR, object);
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
