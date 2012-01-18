package com.example.justaddwater.web.app;

import org.apache.wicket.util.string.Strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * @author http://elasticemail.com/api-documentation/send
 */
public class ElasticEmail
{
    public static final String ELASTIC_EMAIL_USERNAME = "your-elastic-email-account@example.com";
    public static final String ELASTIC_EMAIL_APIKEY = "your-private-elastic-email-api-key";

    public static final Logger log = Logger.getLogger(ElasticEmail.class.getName());

    public static String sendEmail(String from, String fromName, String subject, String body, String to, String bcc)
    {
        try
        {
            //Construct the data
            String data = "userName=" + ELASTIC_EMAIL_USERNAME;
            data += "&api_key=" + ELASTIC_EMAIL_APIKEY;
            data += "&from=" + from;
            data += "&from_name=" + fromName;
            data += "&subject=" + URLEncoder.encode(subject, "UTF-8");
            data += "&body_html=" + URLEncoder.encode(body, "UTF-8");
            data += "&to=" + to;

            if (! Strings.isEmpty(bcc))
            {
                // to list is really just a semicolon-separated bcc list
                data += ";" + bcc;
            }

            //Send data
            URL url = new URL("https://api.elasticemail.com/mailer/send");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = rd.readLine();
            wr.close();
            rd.close();

            log.info("sent email to: " + to + ", with id: " + result);

            return result;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}

