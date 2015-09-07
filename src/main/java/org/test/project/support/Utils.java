package org.test.project.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Helper class
 * @author dmikhalishin@provectus-it.com
 */
public class Utils {

    private final static Logger logger = LoggerFactory.getLogger(Utils.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Properties getProperties(){
        final Properties applicationProperties = new Properties();
        try {
            InputStream propertiesInputStream = Utils.class.getClassLoader().getResourceAsStream("project.properties");
            applicationProperties.load(propertiesInputStream);
        } catch (IOException e){
            logger.error(errorToString(e));
        }
        return applicationProperties;
    }

    public static String errorToString(final Throwable e){
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    public static String dateToSting(final Date date){
        return DATE_FORMAT.format(date);
    }

    public static void printResponse(final ServletResponse response, final Object message) throws IOException{
        if(response == null)return;
        final PrintWriter out = response.getWriter();
        out.print(message);
        out.flush();
    }
}
