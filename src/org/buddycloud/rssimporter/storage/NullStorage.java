package org.buddycloud.rssimporter.storage;

import java.sql.SQLException;
import java.util.Properties;

import org.buddycloud.rssimporter.configuration.FeedProperty;

public class NullStorage implements StorageInterface
{
	public NullStorage(Properties configuration)
	{ 
		System.out.println("WARNING: Using NULL storage adapter");
	}

	public Boolean filterItem(String itemId, FeedProperty feed) throws SQLException
	{
		return false;
	}

	public void markItemPosted(String itemId, String feedId) throws SQLException {}

	public Integer timeToNextFeedParse(String feedId) throws SQLException
	{
		return -1;
	}
	
	public void setNextCheck(Long nextCheck, String feedId) throws SQLException {}
}
