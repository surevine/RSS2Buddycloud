package org.buddycloud.rssimporter.storage;

import java.util.Properties;

import org.buddycloud.rssimporter.configuration.FeedProperty;

public class NullStorage implements StorageInterface
{
	public NullStorage(Properties configuration)
	{ 
		System.out.println("WARNING: Using NULL storage adapter");
	}

	public boolean filterItem(String itemId, FeedProperty feed) throws StorageException
	{
		return false;
	}

	public void markItemPosted(String itemId, String feedId) throws StorageException {}

	public int timeToNextFeedParse(String feedId) throws StorageException
	{
		return -1;
	}
	
	public void setNextCheck(Long nextCheck, String feedId) throws StorageException {}
}
