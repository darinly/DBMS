package com.TFS.model;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.TFS.App;
import com.TFS.model.Database;

public class DBMS {

	private static final Logger log = Logger.getLogger(DBMS.class);
	
	private final Map<String, Database> databases = new ConcurrentHashMap<String, Database>();
	
	public DBMS() {
		File dir = new File("Databases");
		File[] files = dir.listFiles();
		for (File file : files) {
			databases.put(file.getName(), new Database(file.getName()));
		}
	}
	
	public void ShowDatabase() {
		log.info("获取全部数据库--ShowDatabase");
		log.info("数据库数量--" + databases.size());
		for (String name : databases.keySet()) {	
			log.info(name);
		}
	}
	
	public Boolean contains(String name) {
		return databases.containsKey(name);
	}
	
	public Database getDatabase(String name) {
		return databases.get(name);
	}
	
	public void putDatabase(Database database) {
		databases.put(database.getName(), database);
	}
	
}
