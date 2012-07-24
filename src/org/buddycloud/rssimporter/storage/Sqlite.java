package org.buddycloud.rssimporter.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.buddycloud.rssimporter.configuration.FeedProperty;

public class Sqlite implements StorageInterface
{
	private Connection connection;

	public Sqlite(Properties configuration) throws Exception
	{
		Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection(
            "jdbc:sqlite:" + configuration.getProperty("storage.sqlite.file"),
            "",
            ""
        );
        connection.setAutoCommit(true);
        setupDatabase();
	}

	public Boolean filterItem(String itemId, FeedProperty feed) throws SQLException
	{
		PreparedStatement alreadyPosted = this.connection.prepareStatement(
		    "SELECT COUNT(*) FROM processed WHERE item = ? AND feed = ?"
		);
		alreadyPosted.setString(1, itemId);
		alreadyPosted.setString(2, feed.toString());
		ResultSet items = alreadyPosted.executeQuery();
		Integer results = items.getInt(1);
		if (results > 0) {
			return true;
		}
		return false;
	}

	public void markItemPosted(String itemId, String feedId) throws SQLException
	{
		PreparedStatement itemPosted = this.connection.prepareStatement(
		    "INSERT INTO processed VALUES(?, ?);"
	    );
		itemPosted.setString(1, feedId);
		itemPosted.setString(2, itemId);
		itemPosted.execute();
	}

	public Integer timeToNextFeedParse(String feedId) throws SQLException
	{
		PreparedStatement nextCheck = this.connection.prepareStatement(
		    "SELECT (next_check - strftime('%s', 'now')) AS howLong FROM next_check WHERE feed = ?;"
		);
		nextCheck.setString(1, feedId);
		try {
			ResultSet items = nextCheck.executeQuery();
			Integer time = items.getInt(1);
			return time;
		} catch (SQLException e) {
			if (e.getMessage() == "ResultSet closed") {
				return -1;
			}
			throw e;
		}
	}

    private void setupDatabase() throws SQLException 
    {
    	Statement tableCheck = this.connection.createStatement();
    	ResultSet tableCount = tableCheck.executeQuery("SELECT * FROM sqlite_master WHERE type='table';");
    	Integer rows = 0;
    	if (tableCount.next()) { rows = rows + 1; }
    	if (0 == rows) {
    		Statement createTables = this.connection.createStatement();
    		createTables.execute("CREATE TABLE next_check(feed TEXT PRIMARY KEY, next_check TEXT);");
    		createTables.execute("CREATE TABLE processed(feed TEXT, item TEXT);");
    	}
    }
    
    public void setNextCheck(Long nextCheck, String feedId) throws SQLException
    {
		PreparedStatement itemPosted = this.connection.prepareStatement(
			"INSERT OR REPLACE INTO next_check VALUES(?, ?);"
	    );
		itemPosted.setString(1, feedId);
		itemPosted.setLong(2, nextCheck);
		itemPosted.execute();
    }
}