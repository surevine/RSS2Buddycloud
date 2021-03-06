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
package main.java.com.surevine.rssimporter.storage;

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

	public boolean filterItem(String itemId, FeedProperty feed) throws StorageException
	{
		try {
			PreparedStatement alreadyPosted = this.connection.prepareStatement(
			    "SELECT COUNT(*) FROM processed WHERE item = ? AND feed = ?"
			);
			alreadyPosted.setString(1, itemId);
			alreadyPosted.setString(2, feed.toString());
			ResultSet items = alreadyPosted.executeQuery();
			int results = items.getInt(1);
			if (results > 0) {
				return true;
			}
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());

		}
		return false;
	}

	public void markItemPosted(String itemId, String feedId) throws StorageException
	{
		try {
			PreparedStatement itemPosted = this.connection.prepareStatement(
			    "INSERT INTO processed VALUES(?, ?);"
		    );
			itemPosted.setString(1, feedId);
			itemPosted.setString(2, itemId);
			itemPosted.execute();
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
	}

	public int timeToNextFeedParse(String feedId) throws StorageException
	{
		try {
			PreparedStatement nextCheck = this.connection.prepareStatement(
			    "SELECT (next_check - strftime('%s', 'now')) AS howLong FROM next_check WHERE feed = ?;"
			);
			nextCheck.setString(1, feedId);
			ResultSet items = nextCheck.executeQuery();
			int time = items.getInt(1);
			return time;
		} catch (SQLException e) {
			if (e.getMessage() == "ResultSet closed") {
				return -1;
			}
			throw new StorageException(e.getMessage());
		}
	}

    private void setupDatabase() throws StorageException 
    {
    	try {
	    	Statement tableCheck = this.connection.createStatement();
	    	ResultSet tableCount = tableCheck.executeQuery("SELECT * FROM sqlite_master WHERE type='table';");

	    	if (tableCount.next()) { return; }

	    	Statement createTables = this.connection.createStatement();
	        createTables.execute("CREATE TABLE next_check(feed TEXT PRIMARY KEY, next_check TEXT);");
	    	createTables.execute("CREATE TABLE processed(feed TEXT, item TEXT);");
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
    }
    
    public void setNextCheck(Long nextCheck, String feedId) throws StorageException
    {
    	try {
			PreparedStatement itemPosted = this.connection.prepareStatement(
				"INSERT OR REPLACE INTO next_check VALUES(?, ?);"
		    );
			itemPosted.setString(1, feedId);
			itemPosted.setLong(2, nextCheck);
			itemPosted.execute();
		} catch (SQLException e) {
			throw new StorageException(e.getMessage());
		}
    }
}