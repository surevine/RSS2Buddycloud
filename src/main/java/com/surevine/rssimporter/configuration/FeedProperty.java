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
package main.java.com.surevine.rssimporter.configuration;

public class FeedProperty
{
    private String  feedUrl;
    private int     interval;
    private String  host;
    private String  username;
    private String  password;
    private String  channel;
    private String  node;
    private String  name;

    public String getName()
    {
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setInterval(int interval)
    {
        this.interval = interval;
    }

    public String getFeedUrl()
    {
        return this.feedUrl;
    }
     
    public void setFeedUrl(String feedUrl)
    {
        this.feedUrl = feedUrl;
    }

    public int getInterval()
    {
    	return interval;
    }

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}
	
	public String toString()
	{
		return this.name;
	}
}