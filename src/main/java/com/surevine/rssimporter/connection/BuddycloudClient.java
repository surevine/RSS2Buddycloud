/**
 * RSS2Buddycloud
 *
 * LICENSE
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * @copyright	Copyright (c) 2012 Surevine (http://www.surevine.com)
 * @license		http://www.gnu.org/licenses/	GPL Version 3
 */
package main.java.com.surevine.rssimporter.connection;

import java.io.IOException;
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
import org.apache.http.util.EntityUtils;

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

    public String createPost(String channel, String node, String content) throws IOException
    {
    	StringEntity post = new StringEntity(
    	    "<entry xmlns=\"http://www.w3.org/2005/Atom\"><content>" 
            + StringEscapeUtils.escapeXml(content) + "</content></entry>"
        );
    	HttpPost httpPost = new HttpPost(host + "/" + channel + "/content/" + node);
    	httpPost.setEntity(post);
    	HttpResponse response = this.execute(httpPost);
    	if (response == null) {
    		return null;
    	}
    	return "postid";
    }

    private HttpResponse execute(HttpRequestBase request) throws IOException
    {
    	if (session != null) {
    		request.addHeader(SESSION_HEADER, session);
    	}
	    HttpResponse response = httpClient.execute(request);
    	if (true == response.containsHeader(SESSION_HEADER)) {
    		this.session = response.getFirstHeader(SESSION_HEADER).getValue();
    	}
    	EntityUtils.consume(response.getEntity());
    	return response;
    }
}
