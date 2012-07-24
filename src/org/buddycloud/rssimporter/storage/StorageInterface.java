package org.buddycloud.rssimporter.storage;

import java.sql.SQLException;
import org.buddycloud.rssimporter.configuration.FeedProperty;

public interface StorageInterface
{
	public Boolean filterItem(String itemId, FeedProperty feed) throws SQLException;
    
    public void markItemPosted(String itemId, String feedId) throws SQLException;
    
    public Integer timeToNextFeedParse(String feedId) throws SQLException;
    
    public void setNextCheck(Long nextCheck, String feedId) throws SQLException;
}
