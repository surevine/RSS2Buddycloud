package org.buddycloud.rssimporter.connection;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class BuddycloudClient
{
	private String host;
	private DefaultHttpClient httpClient;
	private String session;

	private static String SESSION_HEADER = "X-Session-Id";

    public BuddycloudClient(String host, String username, String password) throws MalformedURLException
    {
    	this.host  = host;
    	httpClient = new DefaultHttpClient();
    	httpClient.getCredentialsProvider().setCredentials(
            new AuthScope(new URL(host).getHost(), 443),
            new UsernamePasswordCredentials(username, password)
        );
    }

    public String createPost(String channel, String node, String content) throws Exception
    {
    	StringEntity post = new StringEntity(
    	    "<entry xmlns=\"http://www.w3.org/2005/Atom\"><content>" 
            + StringEscapeUtils.escapeXml(content) + "</content></entry>"
        );
    	HttpPost httpPost = new HttpPost(host + "/channels/" + channel + "/" + node);
    	httpPost.setEntity(post);
    	HttpResponse response = execute(httpPost);
    	if (response == null) {
    		return null;
    	}
    	return "postid";
    }

    private HttpResponse execute(HttpRequestBase request) throws Exception
    {
    	if (session != null) {
    		request.addHeader(SESSION_HEADER, session);
    	}
	    HttpResponse response = httpClient.execute(request);
    	if (true == response.containsHeader(SESSION_HEADER)) {
    		this.session = response.getFirstHeader(SESSION_HEADER).getValue();
    	}
        response.getEntity().consumeContent();
    	return response;
    }
}
