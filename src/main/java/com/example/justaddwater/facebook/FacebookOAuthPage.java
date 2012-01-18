package com.example.justaddwater.facebook;

import com.example.justaddwater.model.AuthenticationType;
import com.example.justaddwater.model.DAO;
import com.example.justaddwater.model.User;
import com.example.justaddwater.web.app.AccountPage;
import com.example.justaddwater.web.app.LoginPage;
import com.example.justaddwater.web.app.LoginUtil;
import com.example.justaddwater.web.app.MySession;
import com.visural.common.IOUtil;
import com.visural.common.StringUtil;
import net.ftlines.blog.cdidemo.web.UserAction;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.Strings;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author George Armhold armhold@gmail.com
 */
public class FacebookOAuthPage extends WebPage
{
    @Inject
    Logger log;

    @Inject
    DAO dao;

    @Inject
    EntityManager em;

    @Inject
    LoginUtil loginUtil;

    @Inject
    MySession session;
    
    @Inject
    UserAction action;

    // get these from your FB Dev App
    private static final String FACEBOOK_APP_ID = "your-facebook-app-id";
    private static final String FACEBOOK_APP_SECRET = "your-facebook-app-secret";

    // set this to the list of extended permissions you want
    private static final String[] perms = new String[] { /* "publish_stream", */ "email" };

    public static String getLoginRedirectURL()
    {
        return "https://graph.facebook.com/oauth/authorize?client_id=" +
                FACEBOOK_APP_ID + "&display=page&redirect_uri=" +
                pathToFBOAuthPage() + "&scope=" + StringUtil.delimitObjectsToString(",", perms);
    }

    public static String pathToFBOAuthPage()
    {
        RequestCycle rc = RequestCycle.get();
        return rc.getUrlRenderer().renderFullUrl(Url.parse(rc.urlFor(FacebookOAuthPage.class, null).toString()));
    }
                                           

    public static String getAuthURL(String authCode)
    {
        return "https://graph.facebook.com/oauth/access_token?client_id=" +
                FACEBOOK_APP_ID + "&redirect_uri=" +
                pathToFBOAuthPage() + "&client_secret=" + FACEBOOK_APP_SECRET + "&code=" + authCode;
    }

    public FacebookOAuthPage()
    {
        log.info("reached FacebookOAuthPage from facebook login");

        HttpServletRequest req = (HttpServletRequest) getRequest().getContainerRequest();
        String code = req.getParameter("code");

        if (! Strings.isEmpty(code))
        {
            String authURL = getAuthURL(code);

            try
            {
                String result = readFromURL(authURL);
                String accessToken = null;
                Integer expires = null;
                String[] pairs = result.split("&");

                for (String pair : pairs)
                {
                    String[] kv = pair.split("=");

                    if (kv.length != 2)
                    {
                        throw new RuntimeException("Unexpected auth response");
                    }
                    else
                    {
                        if (kv[0].equals("access_token"))
                        {
                            accessToken = kv[1];
                        }

                        if (kv[0].equals("expires"))
                        {
                            expires = Integer.valueOf(kv[1]);
                        }
                    }
                }

                if (accessToken != null && expires != null)
                {
                    authFacebookLogin(accessToken, expires);
                    setResponsePage(AccountPage.class);
                }
                else
                {
                    throw new RuntimeException("Access token and expires not found");
                }

            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            setResponsePage(LoginPage.class);
        }
    }

    private String readFromURL(String authURL) throws IOException
    {
        URL url = new URL(authURL);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();

        int r;
        while ((r = is.read()) != -1)
        {
            baos.write(r);
        }

        return new String(baos.toByteArray());
    }

    public void authFacebookLogin(String accessToken, int expires)
    {
        try
        {
            JSONObject resp = new JSONObject(IOUtil.urlToString(new URL("https://graph.facebook.com/me?access_token=" + accessToken)));
            String id = resp.getString("id");
            String firstName = resp.getString("first_name");
            String lastName = resp.getString("last_name");

            log.info("firstName: " + firstName + ", lastName: " + lastName);

            String email = resp.getString("email");

            User user = findOrCreateFacebookUser(email);
            session.setUsername(user.getEmail());
            log.info("logged on via facebook as: " + session.getUsername());
        }
        catch (Throwable ex)
        {
            throw new RuntimeException("failed login", ex);
        }
    }


    private User findOrCreateFacebookUser(String email)
    {
        User user = dao.findUserByEmail(email);

        if (user == null)
        {
            user = new User();
            user.setEmail(email);
            user.setAuthenticationType(AuthenticationType.facebook);
            user.setAccountCreationDate(new Date());
            em.persist(user);
            action.apply();

            log.info("created new facebook user account: " + email);
        }
        else if (user.getAuthenticationType() != AuthenticationType.facebook)
        {
            throw new RuntimeException("bad login- account has a local password, not a facebook login");
        }

        return user;
    }

}
