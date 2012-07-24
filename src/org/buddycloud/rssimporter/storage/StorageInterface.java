package org.buddycloud.rssimporter.storage;

import org.buddycloud.rssimporter.configuration.FeedProperty;

public interface StorageInterface
{
	public boolean filterItem(String itemId, FeedProperty feed) throws StorageException;
    
    public void markItemPosted(String itemId, String feedId) throws StorageException;
    
    public int timeToNextFeedParse(String feedId) throws StorageException;
    
    public void setNextCheck(Long nextCheck, String feedId) throws StorageException;
}
