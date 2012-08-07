# RSS2buddycloud

This tool was created as a proof of concept by [Surevine](http://www.surevine.com) in order to get more useful information into [buddycloud](https://buddycloud.org) (allowing us to discuss it, etc) increasing its worth to the company.

The system monitors one or more RSS feeds for new posts at a set interval and posts these to a channel of choice. Posts can be made to a topic or personal channel provided the account has permission to write there.

The code makes use of the [buddycloud API](https://buddycloud.org/wiki/buddycloud_HTTP_API) in order to make posts into buddycloud channels.

The application will identify RSS items by hashing a combination of their description, title, author, and publish date. This is because the [HORRORss](http://code.google.com/p/horrorss/) library doesn't currently provide GUID properties. The current hashing mechanism is also not perfect as, for exmaple, the BBC republish the same post with an updated publication date causing multiple posts.

At present only SQLite (and 'no storage') are supported as storage mechanisms. If required new systems can easily be added by implementing _StorageInterface_ and updating the _StorageFactory_.

There are several dependencies which are included in the _build/lib_ directory and are as follows:

* [Apache commons](http://commons.apache.org/) (code, lang, logging)
* [Apache HTTP Components](http://hc.apache.org/) (HttpClient, core, httpmime)
* [HORRORss](http://code.google.com/p/horrorss/)
* [Sparta-XML](http://sparta-xml.sourceforge.net/) (required by HORRORss)
* [SQLite JDBC](http://www.zentus.com/sqlitejdbc/)

## Configuration

Configuration of the application is stored in the file _configuration.properties_.

    storage.type=sqlite
    storage.sqlite.file=data.sqlite

    interval=60

    buddycloud.username=news@example.com
    buddycloud.password=tellnoone
    buddycloud.channel=news@example.com
    buddycloud.node=posts
    #buddycloud.host=XXX

    feeds=bbcnews

    bbcnews.feedUrl=http://feeds.bbci.co.uk/news/rss.xml
    bbcnews.buddycloud.username=bbcnews@example.com
    bbcnews.buddycloud.password=tellnoone
    bbcnews.buddycloud.channel=bbcnews@topics.example.com
    bbcnews.buddycloud.host=https://example.com/api
    bbcnews.interval=60

The first part of configuration is related to the storage mechanism the code will use (to prevent duplicate posts, etc). At present only SQLite is supported so the storage type is SQLite and the only configuration for that is the location of the dataase file.

Next we define some default parameters. These are used if the individual feeds do not provide a value themselves (e.g. if you wanted to use a single user account to do all your RSS posting).

* __interval__: Minimum amount of time between RSS feed checks in seconds (also defaulted in code to 1 hour)
* __buddycloud.username__: The username with which to authenticate with buddycloud (well the XMPP server)
* __buddycloud.password__: Password (as above)
* __buddycloud.channel__: The channel to post to
* __buddycloud.node__: The node to post to 
* __buddycloud.host__: The domain+path to your API install, e.g. https://api.example.com, _no ending slash_

Next we define the feeds that we'd like to process using a space seperated list. Feeds are identified by any valid string which does not contain spaces.

Finally we have X number of feeds defined. These are defined as:

    <feedidentifier>.<config-property>

The minimum configuration required is _feedUrl_ provided that all of the _buddycloud.*_ default configuration properties are defined. To overwrite any of the default configuration simply follow this example:

    bbcnews.buddycloud.channel=someotherchannel@topics.example.com

__Configuration changes require a restart of rss2buddycloud__

## Building

The project is configured using _maven_.

```bash
mvn package
```

__Note:__ Building jar __with dependencies__ is bound to _package_.

## Running

After building, or if you have an already built jar file then from the working directory that contains your _configuration.properties_ file simply run:

```bash
java -jar target/rssimporter-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

The code will run until the process is killed.

## Notes

* This is a very basic but functional piece of code. Please feel free to fork, use, and make pull requests for new features and/or bug fixes.
* I'd later like to add the ability to supply templates via configuration using any of the properties from the RSS feed so you can build custom posts per incoming feed.
* HORRORss really needs an update to include additional item properties, if we could include GUID then I could remove the awful hashing code that is used.
* Authenticated (Basic, Digest) need to be supported (see http://www.java-forums.org/java-net/7242-reading-urls-protected-http-authentication.html) - seems easy enough but will require modifying HORRORss and RSS2buddycloud
* What to do with HTML in RSS posts?
* Redo scheduling of runs using http://quartz-scheduler.org/
* Add a logger (e.g. log4j) rather than writing to console
* Character set encoding issues/fixes

## Licence

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
