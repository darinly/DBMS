package com.TFS.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.TFS.App;
import com.TFS.model.Field;
import com.TFS.utils.FileProcessor;
import com.TFS.model.Tuple;

public class BaseTable {

	private static final Logger log = Logger.getLogger(BaseTable.class);
	
	private String name;

	private String database;
	
	private Integer size;
	
	private Tuple primarykey;
	
	private Tuple index;
	
	private Tuple fields;
	
	private Map<String, Tuple> table;
	
	public BaseTable(String name, String database) {
		this.name = name;
		this.database = database;
		this.fields = new Tuple();
		this.size = 0;
		this.primarykey = new Tuple();
		this.index = new Tuple();
		this.table = new TreeMap<String, Tuple>();
		ReadConfig();
	}
	
	public void ReadConfig() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + "_config.txt";
		List<String> results = FileProcessor.read(fileName);
		log.info("读取 * " + name + " * 数据表信息");
		for (int i = 0; i < results.size(); i++) {	
			//读取size
			if(results.get(i).indexOf("size") != -1) {
				size = Integer.parseInt(results.get(i).substring(results.get(i).indexOf("=") + 1));
				log.info(size.toString());
			}
			//读取primarykey
			if(results.get(i).indexOf("primarykey") != -1) {	
				if(results.get(i + 1).equals("")) {
					log.error("暂时还没有主键");
				} else {
					String primarykeyName = results.get(i + 1);
					String primarykeyType = results.get(i + 2);
					String[] primarykeyNameList = primarykeyName.split("\t");
					String[] primarykeyTypeList = primarykeyType.split("\t");
					for (int j = 0; j < primarykeyNameList.length; j++) {
						primarykey.Add(primarykeyNameList[j], primarykeyTypeList[j]);
					}
				}
				log.info(primarykey);
			}
			//读取index
			if(results.get(i).indexOf("index") != -1) {
				if(results.get(i + 1).equals("")) {
					log.error("暂时还没有索引");
				} else {
					String indexName = results.get(i + 1);
					String indexType = results.get(i + 2);
					String[] indexNameList = indexName.split("\t");
					String[] indexTypeList = indexType.split("\t");
					for (int j = 0; j < indexNameList.length; j++) {
						index.Add(indexNameList[j], indexTypeList[j]);
					}
				}
				log.info(index);
			}
			//读取字段
			if(results.get(i).indexOf("fields") != -1) {
				if(results.get(i + 1).equals("")) {
					log.error("暂时还没有字段");
				} else {
					String fieldsName = results.get(i + 1);
					String fieldsType = results.get(i + 2);
					String[] fieldsNameList = fieldsName.split("\t");
					String[] fieldsTypeList = fieldsType.split("\t");
					for (int j = 0; j < fieldsNameList.length; j++) {
						fields.Add(fieldsNameList[j], fieldsTypeList[j]);
					}
				}
				log.info(fields);
			}
		}
	}
	
	public void WriteConfig() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + "_config.txt";
		List<String> config = new ArrayList<String>();
		config.add("name=" + name + "\r\n");
		config.add("database=" + database + "\r\n");
		config.add("size=" + size + "\r\n");
		
		config.add("\r\n");
		
		config.add("primarykey:\r\n");
		if(primarykey.size() != 0) {
			String primarykeyNameString = "";
			String primarykeyTypeString = "";
			for (String keyname : primarykey.getKeys()) {
				primarykeyNameString = primarykeyNameString + keyname + "\t";
			}
			for (String keytype : primarykey.getValues()) {
				primarykeyTypeString = primarykeyTypeString + keytype + "\t";
			}
			config.add(primarykeyNameString + "\r\n");
			config.add(primarykeyTypeString + "\r\n");
		}
		config.add("\r\n");
		
		config.add("index:\r\n");
		if (index.size() != 0) {
			String indexNameString = "";
			String indexTypeString = "";
			for (String indexname : index.getKeys()) {
				indexNameString = indexNameString + indexname + "\t";
			}
			for (String indextype : index.getValues()) {
				indexTypeString = indexTypeString + indextype + "\t";
			}
			config.add(indexNameString + "\r\n");
			config.add(indexTypeString + "\r\n");
			
		}
		config.add("\r\n");
		
		config.add("fields:\r\n");
		if(fields.size() != 0) {
			String fieldsNameString = "";
			String fieldsTypeString = "";
			for (String fieldname : fields.getKeys()) {
				fieldsNameString = fieldsNameString + fieldname + "\t";
			}
			for (String fieldtype : fields.getValues()) {
				fieldsTypeString = fieldsTypeString + fieldtype + "\t";
			}
			config.add(fieldsNameString + "\r\n");
			config.add(fieldsTypeString + "\r\n");	
		}
		config.add("\r\n");
		
		FileProcessor.write(config, fileName, 1);
	}
	
	public void ReadTable() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + ".txt";
		try {
			FileInputStream fStream = new FileInputStream(new File(fileName));
			InputStreamReader iReader = new InputStreamReader(fStream,"gbk");
			BufferedReader bReader = new BufferedReader(iReader);
			
			//读取字段信息
			String line = bReader.readLine();
			String[] names = line.split("\t");
			
			while ((line = bReader.readLine()) != null) {
				String[] results = line.split("\t");
				Tuple tuple = new Tuple();
				for (int i = 0; i < results.length; i++) {
					tuple.Add(results[i], names[i]);
				}
				table.put(results[0], tuple);
			}
			bReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void WriteTable() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + ".txt";
		try {
			File writename = new File(fileName); 	
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			String fieldsName = "";
			for (String fieldName : fields.getKeys()) {
				fieldsName = fieldsName + fieldName + "\t"; 
			}
			out.write(fieldsName + "\r\n");
			for (Tuple tuple : table.values()) {
				String result = "";
				for (String string : tuple.getKeys()) {
					result = result + string + "\t";
				}
				out.write(result + "\r\n");
			}
			out.flush();
			out.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void ShowTable() {
		String message = "数据表名：" + name + "\n" +
				"所属据库名：" + database + "\n" +
				"数据表大小：" + size + "\n";
		message += "主键名称：";
		for (String primarykeyName : primarykey.getKeys()) {
			message = message + primarykeyName + "\t";
		}
		message = message + "\n主键类型：";
		for (String primarykeyType : primarykey.getValues()) {
			message = message + primarykeyType + "\t";
		}
		
		message = message + "\n索引名称：";
		for (String indexName : index.getKeys()) {
			message = message + indexName + "\t";
		}
		message = message + "\n索引类型：";
		for (String indexType : index.getValues()) {
			message = message + indexType + "\t";
		}
		
		message = message + "\n字段名称：";
		for (String fieldsName : fields.getKeys()) {
			message = message + fieldsName + "\t";
		}
		message = message + "\n字段类型：";
		for (String fieldsType : fields.getValues()) {
			message = message + fieldsType + "\t";
		}
		System.out.println(message);
	}
	
	public static Integer CreateTable(String name, String database) {
		log.info("数据表 * " + name + " * 创建...");
		if(!App.dbms.contains(database)){
			log.error("数据表 * " + name + " * 创建失败--没有 * " + database + " * 数据库");
			return 1;     //没有找到该数据库
		}
		String dir = "Databases//" + database + "//" + name;
		String tablePath = dir + "//" + name + ".txt";
		String configPath = dir + "//" + name + "_config.txt";
		/* 创建配置文件以及数据表文件 */
		if(FileProcessor.CreateDir(dir) && FileProcessor.CreateFile(tablePath) && FileProcessor.CreateFile(configPath)){
			List<String> config = new ArrayList<String>();
			config.add("name=" + name + "\r\n");
			config.add("database=" + database + "\r\n");
			config.add("size=0" + "\r\n");
			
			config.add("\r\n");
			
			config.add("primarykey:\r\n\r\n");
			config.add("index:\r\n\r\n");
			config.add("fields:\r\n\r\n");
			FileProcessor.write(config, configPath, 1);
			
			Database targetDatabase = App.dbms.getDatabase(database);
			targetDatabase.putTable(name);
			
			log.info("数据表 * " + name + " * 创建成功");
			return 0;    //创建成功
		} else {
			log.error("数据表 * " + name + " * 创建失败");
			return 2;   //数据表创建失败
		}
	}
	
	public void AddField(String name, String type) {
		if (fields.Add(name, type)) {
			WriteConfig();
			log.info("字段 * " + name + " * 添加成功");
		} else {
			log.error("字段 * " + name + " * 添加失败");
		}
	}
	
	public void query() {
		for (Tuple tuple : table.values()) {
			for (String string : tuple.getKeys()) {
				System.out.print(string + "\t");
			}
			System.out.println("\n");
		}
	}
	
	public Boolean insert(Tuple tuple) {
		if (table.put(tuple.getFirst().getKey(), tuple) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void delete() {
		
	}
	
	public void update() {
		
	}
	/*
	public String getName() {
		return name;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public Tuple getFields() {
		return fields;
	}
	
	public void ShowTable() {
		String fileName = "Databases//" + database + "//" + name + ".txt";
		List<String> results = FileProcessor.read(fileName);
		for (String string : results) {
			log.info(string);
		}
	}
	
	public static Boolean CreateTable(String name, String database) {
		log.info("数据表 * " + name + " * 创建...");
		if(!App.dbms.contains(database)){
			log.error("数据表 * " + name + " * 创建失败--没有 * " + database + " * 数据库");
			return false;
		}
		String dir = "Databases//" + database + "//" + name;
		String tablePath = dir + "//" + name + ".txt";
		String configPath = dir + "//" + name + "_config.txt";
		if(FileProcessor.CreateDir(dir) && FileProcessor.CreateFile(tablePath) && FileProcessor.CreateFile(configPath)){
			List<String> config = new ArrayList<String>();
			config.add("name=" + name + "\r\n");
			config.add("database=" + database + "\r\n");
			config.add("size=0" + "\r\n");
			
			config.add("\r\n");
			
			config.add("primarykey:\r\n\r\n");
			config.add("index:\r\n\r\n");
			config.add("fields:\r\n\r\n");
			FileProcessor.write(config, configPath, 1);
			
			Database targetDatabase = App.dbms.getDatabase(database);
			targetDatabase.putTable(name);
			
			log.info("数据表 * " + name + " * 创建成功");
			return true;
		} else {
			log.error("数据表 * " + name + " * 创建失败");
			return false;
		}
	}
	
	public void AddField(Field field) {
		if (fields.AddField(field)) {
			WriteConfig();
			log.info("字段 * " + field.getName() + " * 添加成功");
		} else {
			log.error("字段 * " + field.getName() + " * 添加失败");
		}
	}
	
	public void setPrimarykey(Tuple tuple) {
		//判断Tuple里的字段是否全都在fields里面
		List<Field> primarykeyList = tuple.getFields();
		Boolean TF = true;
		for (Field field : primarykeyList) {
			if(!fields.contains(field)) {
				TF = false;
			}
		}
		if(TF) {
			primarykey = tuple;
			for (Field field : primarykeyList) {
				if(!index.contains(field)) {
					index.AddField(field);
				}
			}
			WriteConfig();
			log.info("设置主键成功");
		} else {
			log.error("没有该字段，设置主键失败");
		}	
	}
	
	public Tuple getPrimarykey() {
		if (!primarykey.isEmpty()) {
			return primarykey;
		} else {
			log.error("没有主键");
			return null;
		}
		
	}
	
	public void AddIndex(Field field) {
		if(fields.contains(field)) {
			if (index.AddField(field)) {
				WriteConfig();
				log.info("索引 * " + field.getName() + " * 添加成功");
			} else {
				log.info("索引 * " + field.getName() + " * 添加失败--已有该索引");
			}
		} else {
			log.info("索引 * " + field.getName() + " * 添加失败--没有该字段");
		}
	}
	
	public void ReadConfig() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + "_config.txt";
		List<String> results = FileProcessor.read(fileName);
		log.info("读取 * " + name + " * 数据表信息");
		for (int i = 0; i < results.size(); i++) {	
			//读取size
			if(results.get(i).indexOf("size") != -1) {
				size = Integer.parseInt(results.get(i).substring(results.get(i).indexOf("=") + 1));
				log.info(size.toString());
			}
			//读取primarykey
			if(results.get(i).indexOf("primarykey") != -1) {	
				if(results.get(i + 1).equals("")) {
					log.error("暂时还没有主键");
				} else {
					String primarykeyName = results.get(i + 1);
					String primarykeyType = results.get(i + 2);
					String[] primarykeyNameList = primarykeyName.split("\t");
					String[] primarykeyTypeList = primarykeyType.split("\t");
					for (int j = 0; j < primarykeyNameList.length; j++) {
						primarykey.AddField(new Field(primarykeyNameList[j], primarykeyTypeList[j]));
					}
				}
				log.info(primarykey);
			}
			//读取index
			if(results.get(i).indexOf("index") != -1) {
				if(results.get(i + 1).equals("")) {
					log.error("暂时还没有索引");
				} else {
					String indexName = results.get(i + 1);
					String indexType = results.get(i + 2);
					String[] indexNameList = indexName.split("\t");
					String[] indexTypeList = indexType.split("\t");
					for (int j = 0; j < indexNameList.length; j++) {
						index.AddField(new Field(indexNameList[j], indexTypeList[j]));
					}
				}
				log.info(index);
			}
			//读取字段
			if(results.get(i).indexOf("fields") != -1) {
				if(results.get(i + 1).equals("")) {
					log.error("暂时还没有字段");
				} else {
					String fieldsName = results.get(i + 1);
					String fieldsType = results.get(i + 2);
					String[] fieldsNameList = fieldsName.split("\t");
					String[] fieldsTypeList = fieldsType.split("\t");
					for (int j = 0; j < fieldsNameList.length; j++) {
						fields.AddField(new Field(fieldsNameList[j], fieldsTypeList[j]));
					}
				}
				log.info(fields);
			}	
		}
		log.info("读取 * " + name + " * 数据表信息完成");
	}
	
	public void WriteConfig() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + "_config.txt";
		List<String> config = new ArrayList<String>();
		config.add("name=" + name + "\r\n");
		config.add("database=" + database + "\r\n");
		config.add("size=" + size + "\r\n");
		
		config.add("\r\n");
		
		config.add("primarykey:\r\n");
		if(primarykey.getFields().size() != 0) {
			String primarykeyNameString = "";
			String primarykeyTypeString = "";
			for (Field field : primarykey.getFields()) {
				primarykeyNameString = primarykeyNameString + field.getName() + "\t";
			}
			for (Field field : primarykey.getFields()) {
				primarykeyTypeString = primarykeyTypeString + field.getType() + "\t";
			}
			config.add(primarykeyNameString + "\r\n");
			config.add(primarykeyTypeString + "\r\n");
		}
		config.add("\r\n");
		
		config.add("index:\r\n");
		if (index.getFields().size() != 0) {
			String indexNameString = "";
			String indexTypeString = "";
			for (Field field : index.getFields()) {
				indexNameString = indexNameString + field.getName() + "\t";
			}
			for (Field field : index.getFields()) {
				indexTypeString = indexTypeString + field.getType() + "\t";
			}
			config.add(indexNameString + "\r\n");
			config.add(indexTypeString + "\r\n");
			
		}
		config.add("\r\n");
		
		config.add("fields:\r\n");
		if(fields.getFields().size() != 0) {
			String fieldsNameString = "";
			String fieldsTypeString = "";
			for (Field field : fields.getFields()) {
				fieldsNameString = fieldsNameString + field.getName() + "\t";
			}
			for (Field field : fields.getFields()) {
				fieldsTypeString = fieldsTypeString + field.getType() + "\t";
			}
			config.add(fieldsNameString + "\r\n");
			config.add(fieldsTypeString + "\r\n");	
		}
		config.add("\r\n");
		
		FileProcessor.write(config, fileName, 1);
	}
	
	public Boolean insert(Tuple tuple) {
		//判断插入元组是否符合字段
		List<Field> tupleFields = tuple.getFields();
		List<Field> fieldsFields = fields.getFields();
		if(tupleFields.size() == fieldsFields.size()) {
			for (int i = 0; i < tupleFields.size(); i++) {
				if(!tupleFields.get(i).getType().equals(fieldsFields.get(i).getType())) {
					return false;
				}
			}
		} else {
			log.error("插入信息与该表字段不符");
			return false;
		}
		String fileName = "Databases//" + database + "//" + name + "//" + name + ".txt";
		FileProcessor.WriteTuple(tuple.toValue(), fileName);
		size++;
		WriteConfig();
		log.info("成功插入该元组");
		return true;
	}
	
	public Boolean delete() {
		return false;
	}
	
	public void ReadTable() {
		String fileName = "Databases//" + database + "//" + name + "//" + name + ".txt";
		try {
			FileInputStream fStream = new FileInputStream(new File(fileName));
			InputStreamReader iReader = new InputStreamReader(fStream,"gbk");
			BufferedReader bReader = new BufferedReader(iReader);
			
			String line = "";
			while ((line = bReader.readLine()) != null) {
				String[] results = line.split("\t");
				
				for (int i = 0; i < results.length; i++) {
					Tuple tuple = new Tuple();
					tuple.AddField(new Field(string, fields.))
				}
				for (String string : results) {
					Tuple tuple = new Tuple();
					tuple.AddField(new Field(string, type))
				}
				
				table.put(results[0], value)
				results.add(line);
			}
			bReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
