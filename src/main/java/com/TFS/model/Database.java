package com.TFS.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.TFS.App;
import com.TFS.model.DBMS;
import com.TFS.model.Database;
import com.TFS.utils.FileProcessor;

public class Database {

	private static final Logger log = Logger.getLogger(Database.class);
	
	private String name;
	
	//private List<String> tables;
	
	private final Map<String, BaseTable> tables = new ConcurrentHashMap<String, BaseTable>();
	
	public Database(String name) {
		this.name = name;
		//this.tables = new ArrayList<String>();
		ReadConfig();
	}
	
	/*
	private void Initialize() {
		File dir = new File("Databases//" + name);
		File[] files = dir.listFiles();
		for (File file : files) {
			String fileName = file.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
			tables.put(fileName, new Table(fileName, name));
		}
	}
	
	public Database(String name) {
		this.name = name;
		System.out.println("数据库" + name + "导入成功");
		Initialize();
	}
	
	public void ShowTables() {
		for (Table table : tables.values()) {
			System.out.println(table.getName());
		}
	}
	
	public void ShowData(String name) {
		Table table = tables.get(name);
		if(table == null) {
			System.out.println("查询错误，没有该表");
		} else {
			table.ShowTable();
		}
	}
	*/
	
	public static Boolean CreateDatabase(String name) {
		log.info("数据库 * " + name + " * 创建...");
		String path = "Databases//" + name;
		String configPath = path + "//" + name + "_config.txt";
		if(FileProcessor.CreateDir(path) && FileProcessor.CreateFile(configPath)){
			List<String> config = new ArrayList<String>();
			config.add("name=" + name + "\r\n");
			config.add("\r\n");
			config.add("tables:\r\n\r\n");
			FileProcessor.write(config, configPath, 1);
			App.dbms.putDatabase(new Database(name));		//添加到DBMS
			log.info("数据库 * " + name + " * 创建成功");
			return true;
		} else {
			log.error("数据库 * " + name + " * 创建失败");
			return false;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void ShowTables() {
		log.info("获取全部数据库--ShowDatabase");
		log.info("数据库数量--" + tables.size());
		for (String name : tables.keySet()) {	
			log.info(name);
		}
	}
	
	public void ReadConfig() {
		String fileName = "Databases//" + name + "//" + name + "_config.txt";
		List<String> results = FileProcessor.read(fileName);
		log.info("读取 * " + name + " * 数据库信息");
		for (int i = 0; i < results.size(); i++) {		
			if(results.get(i).indexOf("tables") != -1) {
				String tableRead = results.get(i + 1);
				if(tableRead.equals("")) {
					log.error("暂时还没有数据表");
				} else {
					String[] tableList = tableRead.split("\t");
					for (String table : tableList) {
						tables.put(table, new BaseTable(table, name));
					}	
				}
			}
		}
		log.info("读取 * " + name + " * 数据库信息完成");
	}
	
	public void WriteConfig() {
		String fileName = "Databases//" + name + "//" + name + "_config.txt";
		List<String> config = new ArrayList<String>();
		config.add("name=" + name + "\r\n");
		
		config.add("\r\n");
		
		config.add("tables:\r\n");
		String tableString = "";
		if(!tables.isEmpty()){
			for (String table : tables.keySet()) {		
				tableString = tableString + table + "\t";
			}
		}
		config.add(tableString + "\r\n\r\n");
		FileProcessor.write(config, fileName, 1);
	}
	
	public void putTable(String tableName) {
		tables.put(tableName, new BaseTable(tableName, name));
		WriteConfig();
	}
	
	public BaseTable getTable(String tableName) {
		return tables.get(tableName);
	}

}
