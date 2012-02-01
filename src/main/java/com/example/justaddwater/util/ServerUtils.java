/*
 *  Copyright 2012 George Armhold
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.example.justaddwater.util;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * generic server-side utility methods
 */
public class ServerUtils
{
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServerUtils.class);

    protected static final String DATE_FORMAT = "yyyy MM dd HH:mm:ss";

    public static long copyStream(InputStream in, OutputStream out) throws IOException
    {
        int BUFSIZE = 8192;
        byte[] buf = new byte[BUFSIZE];

        BufferedInputStream bin = new BufferedInputStream(in, BUFSIZE);
        BufferedOutputStream bout = new BufferedOutputStream(out, BUFSIZE);

        try
        {
            int c;
            long bytesWritten = 0l;

            while ((c = bin.read(buf)) != -1)
            {
                bout.write(buf, 0, c);
                bytesWritten += c;
            }

            return bytesWritten;
        } finally
        {
            bin.close();
            bout.close();
        }
    }

    public static String readAsString(InputStream is)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buf = new StringBuffer();

        try
        {
            String s;
            while ((s = in.readLine()) != null)
            {
                buf.append(s);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        } finally
        {
            try
            {
                in.close();
            } catch (IOException ignored)
            {
            }
        }

        return buf.toString();
    }

    public static String encodeURL(String url)
    {
        try
        {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static String decodeURL(String url)
    {
        try
        {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public static String dateFormat(Date d)
    {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(d);
    }

    public static long now()
    {
        return new Date().getTime();
    }

}
