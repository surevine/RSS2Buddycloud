RSS2Buddycloud
===============

This tool was created as a proof of concept by [Surevine](http://www.surevine.com) in order to get more useful information into [Buddycloud](https://buddycloud.org) (allowing us to discuss it, etc) increasing its worth to the company.

The system monitors one or more RSS feeds for new posts at a set interval and posts these to a channel of choice. Posts can be made to a topic or personal channel provided the account has permission to write there.

The code makes use of the [buddycloud API](https://buddycloud.org/wiki/Buddycloud_HTTP_API) in order to make posts into buddycloud channels.

The application will identify RSS items by hashing a combination of their description, title, author, and publish date. This is because the [HORRORss](http://code.google.com/p/horrorss/) library doesn't currently provide GUID properties. The current hashing mechanism is also not perfect as, for exmaple, the BBC republish the same post with an updated publication date causing multiple posts.

At present only SQLite (and 'no storage') are supported as storage mechanisms. If required new systems can easily be added by implementing _StorageInterface_ and updating the _StorageFactory_.

There are several dependencies which are included in the _build/lib_ directory and are as follows:

* [Apache commons](http://commons.apache.org/) (code, lang, logging)
* [Apache HTTP Components](http://hc.apache.org/) (HttpClient, core, httpmime)
* [HORRORss](http://code.google.com/p/horrorss/)
* [Sparta-XML](http://sparta-xml.sourceforge.net/) (required by HORRORss)
* [SQLite JDBC](http://www.zentus.com/sqlitejdbc/)

Configuration
-------------

Configuration of the application is stored in the file _configuration.properties_

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

* **interval**: Minimum amount of time between RSS feed checks in seconds (also defaulted in code to 1 hour)
* **buddycloud.username**: The username with which to authenticate with buddycloud (well the XMPP server)
* **buddycloud.password**: Password (as above)
* **buddycloud.channel**: The channel to post to
* **buddycloud.node**: The node to post to 
* **buddycloud.host**: The domain+path to your API install, e.g. https://api.example.com, _no ending slash_

Next we define the feeds that we'd like to process using a space seperated list. Feeds are identified by any valid string which does not contain spaces.

Finally we have X number of feeds defined. These are defined as:

    <feedidentifier>.<config-property>

The minimum configuration required is _feedUrl_ provided that all of the _buddycloud.*_ default configuration properties are defined. To overwrite any of the default configuration simply follow this example:

    bbcnews.buddycloud.channel=someotherchannel@topics.example.com

Building
--------

To build simply use _ant_ as follows:

```bash
ant -f ./build/build.xml 
```

This will place the built _rss2buddycloud.jar_ file in _target/lib_ directory.

Running
-------

After building, or if you have an already built jar file then from the working directory that contains your _configuration.properties_ file simply run:

```bash
java -jar /path/to/your/rss2buddycloud.jar
```

The code will run until the process is killed.

Notes
-----

* This is a very basic but functional piece of code. Please feel free to fork, use, and make pull requests for new features and/or bug fixes.
* I'd later like to add the ability to supply templates via configuration using any of the properties from the RSS feed so you can build custom posts per incoming feed
