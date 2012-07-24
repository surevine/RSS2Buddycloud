package org.buddycloud.rssimporter.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigurationParser
{
    private String configurationFile             = "configuration.properties";
    private static final String DEFAULT_INTERVAL = "3600";
    private Integer minimumFeedPollTime;
    private ArrayList<FeedProperty> feedCollection = new ArrayList<FeedProperty>();
    private Properties configuration;

    public void setConfigurationFile(String configurationFile)
    {
    	this.configurationFile = configurationFile;
    }

    public ArrayList<FeedProperty> parse() throws IOException
    {
        Properties conf = new Properties();
        conf.load(new FileInputStream(configurationFile));
        this.configuration  = conf;
    	String[] feeds      = conf.getProperty("feeds").split(" ");
        minimumFeedPollTime = Integer.valueOf(DEFAULT_INTERVAL);

        for (int i=0; i<feeds.length; i++) {
        	FeedProperty feed = new FeedProperty();

        	feed.setFeedUrl(conf.getProperty(feeds[i] + ".feedUrl"));
            feed.setName(feeds[i]);

        	String Interval = conf.getProperty(
                feeds[i] + ".interval", conf.getProperty("interval", DEFAULT_INTERVAL)
            );
        	feed.setInterval(Integer.valueOf(Interval));

        	String host = conf.getProperty(
                feeds[i] + ".buddycloud.host", conf.getProperty("buddycloud.host")
            );
            feed.setHost(host);

        	String username = conf.getProperty(
                feeds[i] + ".buddycloud.username", conf.getProperty("buddycloud.username")
            );
            feed.setUsername(username);

        	String password = conf.getProperty(
               feeds[i] + ".buddycloud.password", conf.getProperty("buddycloud.password")
            );
            feed.setPassword(password);   

        	String channel = conf.getProperty(
                feeds[i] + ".buddycloud.channel", conf.getProperty("buddycloud.channel")
            );
            feed.setChannel(channel);

        	String node = conf.getProperty(
                feeds[i] + ".buddycloud.node", conf.getProperty("buddycloud.node")
            );
            feed.setNode(node);

            if (feed.getInterval() < minimumFeedPollTime) {
            	minimumFeedPollTime = feed.getInterval();
            }

            feedCollection.add(feed);
        }
        return feedCollection;
    }
    
    public ArrayList<FeedProperty> getFeeds()
    {
    	return this.feedCollection;
    }
   
    public Properties getRawConfiguration()
    {
    	return this.configuration;
    }
}