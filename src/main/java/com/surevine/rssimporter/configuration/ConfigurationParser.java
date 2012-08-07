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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigurationParser
{
    private static final String DEFAULT_INTERVAL = "3600";
    
    private String configurationFile               = "configuration.properties";
    private ArrayList<FeedProperty> feedCollection = new ArrayList<FeedProperty>();
    private int minimumFeedPollTime;
    private Properties configuration;

    public void setConfigurationFile(String configurationFile)
    {
    	this.configurationFile = configurationFile;
    }

    public List<FeedProperty> parse() throws IOException
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
    
    public List<FeedProperty> getFeeds()
    {
    	return this.feedCollection;
    }
   
    public Properties getRawConfiguration()
    {
    	return this.configuration;
    }
}