package org.buddycloud.rssimporter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.buddycloud.rssimporter.configuration.ConfigurationParser;
import org.buddycloud.rssimporter.configuration.FeedProperty;
import org.buddycloud.rssimporter.connection.BuddycloudClient;
import org.buddycloud.rssimporter.storage.StorageFactory;
import org.buddycloud.rssimporter.storage.StorageInterface;
import org.horrabin.horrorss.RssFeed;
import org.horrabin.horrorss.RssItemBean;
import org.horrabin.horrorss.RssParser;

public class RssImporter
{
    private List<FeedProperty> feedCollection  = new ArrayList<FeedProperty>();
    private List<BuddycloudClient> connections = new ArrayList<BuddycloudClient>();
    private StorageInterface storage;
    private ConfigurationParser configurationParser;
    private MessageDigest hasher;

    public static void main(String[] args)
    {
    	RssImporter importer = new RssImporter();
    	try {
    	    importer.run();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    		System.exit(1);
    	}
    	System.exit(0);
    }

    public void setConfigurationParser(ConfigurationParser configurationParser)
    {
    	this.configurationParser = configurationParser;
    }
    
    private ConfigurationParser getConfigurationParser()
    {
    	if (configurationParser == null) {
    		configurationParser = new ConfigurationParser();
    	}
    	return configurationParser;
    }

    public void run() throws Exception
    {
    	this.setup();

        while (true) {
        	this.processFeeds();
        }
    }

    private void setup() throws Exception
    {
    	this.getConfigurationParser().parse();
    	this.feedCollection = this.getConfigurationParser().getFeeds();    	
    	this.hasher         = MessageDigest.getInstance("SHA-1");
    	this.storage        = StorageFactory.getStorage(
    	    this.getConfigurationParser().getRawConfiguration()
    	);
    	this.setupConnections();
    }

    private void pauseParsingOfFeeds(int time)
    {
        try {
        	Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void setupConnections() throws MalformedURLException
    {
    	for (FeedProperty feed : feedCollection) {
    	    this.connections.add(
    	        new BuddycloudClient(feed.getHost(), feed.getUsername(), feed.getPassword())
    	    );
    	}
    }

	private void processFeeds()
	{
		int i                     = 0;
    	int nextParse             = 864000;
    	int minimumWaitUntilParse = 864000;
    	for (FeedProperty feed : feedCollection) {
    		try {
    			nextParse = this.storage.timeToNextFeedParse(feed.getName());
    			if (nextParse < minimumWaitUntilParse) {
    				minimumWaitUntilParse = nextParse;
    			}
    			if (nextParse <= 0) {
        			this.processItems(feed, i);
        			long nextCheck = System.currentTimeMillis()/1000 + feed.getInterval();
        			this.storage.setNextCheck(nextCheck, feed.getName());
    			}
    		} catch (Exception e) {
    			System.out.println(e.getMessage());
    			e.printStackTrace();
    		}
    		i++;
    	}
    	if (minimumWaitUntilParse > 0) {
    		this.pauseParsingOfFeeds(minimumWaitUntilParse);
    	}
	}

    private void processItems(FeedProperty feedProperties, Integer index) throws Exception 
    {
    	RssParser rss           = new RssParser();
        RssFeed feed            = rss.load(feedProperties.getFeedUrl());
        List<RssItemBean> items = feed.getItems();
        int itemCount       = items.size();
        for (int i=0; i<itemCount; i++) {
             RssItemBean item = items.get(i); 
             if (true == this.storage.filterItem(this.generateItemIdentifier(item), feedProperties)) {
            	 continue;
             }
             System.out.println("Title: " + item.getTitle());
        	 try {
        	     this.postToBuddycloud(item, index);
        	     this.storage.markItemPosted(
        	        this.generateItemIdentifier(item) ,
        	        this.feedCollection.get(index).toString()
        	     );
        	 } catch (Exception e) {
        		 System.out.println(e.getMessage());
        		 e.printStackTrace();
             }
        }
    }

    private void postToBuddycloud(RssItemBean item, Integer index) throws Exception
    {
    	FeedProperty feed = feedCollection.get(index);
    	String content = item.getTitle();
    	if (null != item.getAuthor()) {
    		content = content + " by " + item.getAuthor();
    	}
    	content = content + "\n\n" 
    	    + item.getDescription() 
    	    + "\n\nSee more: " 
    	    + item.getLink();
    	connections.get(index).createPost(
            feed.getChannel(),
            feed.getNode(), 
            content
        );
    }
    
    public String generateItemIdentifier(RssItemBean item) throws UnsupportedEncodingException
    {
    	String uniqueReference = item.getLink()
    	    + item.getPubDate().toString() 
    	    + item.getDescription() 
    	    + item.getAuthor();
    	this.hasher.update(uniqueReference.getBytes("UTF-8"));
    	byte[] digest = this.hasher.digest();
    	String identifier = "";
    	for (int i=0; i<digest.length; i++) {
    		identifier = identifier+digest[i];
    	}
    	return identifier;
    }
}