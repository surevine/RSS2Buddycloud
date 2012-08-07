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