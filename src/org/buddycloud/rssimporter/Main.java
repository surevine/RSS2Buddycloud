package org.buddycloud.rssimporter;

public class Main
{
    public static void main(String[] args)
    {
    	RssImporter importer = new RssImporter();
    	try {
    	    importer.run();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    		System.exit(1);
    	}
    	System.exit(0);
    }
}