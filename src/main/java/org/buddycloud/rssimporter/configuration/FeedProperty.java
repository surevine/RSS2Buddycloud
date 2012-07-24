package org.buddycloud.rssimporter.configuration;

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