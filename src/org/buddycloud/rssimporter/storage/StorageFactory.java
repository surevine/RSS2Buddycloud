package org.buddycloud.rssimporter.storage;

import java.util.Properties;

public class StorageFactory
{
    public static StorageInterface getStorage(Properties configuration) throws Exception
    {
    	Methods name = Methods.valueOf(configuration.getProperty("storage.type").toUpperCase());
    	switch (name) {
    	    case SQLITE:
    	    	return new Sqlite(configuration);
    	    case NONE:
    	    	return new NullStorage(configuration);
    	    /* Add more in future? */
    	}
    	return new NullStorage(configuration);
    }

	public enum Methods
	{
		SQLITE,
		NONE
	}
}