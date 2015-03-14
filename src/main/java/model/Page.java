package model;

import java.util.HashMap;
import java.util.Map;

public class Page {

	private Map<String,String> properties; //this map properties and value match...

	public Page()
	{
		properties=new HashMap<String,String>();
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
