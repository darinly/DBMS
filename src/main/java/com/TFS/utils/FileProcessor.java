package com.TFS.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.TFS.model.Tuple;

public class FileProcessor {
	
	public static List<String> read(String fileName) {
		List<String> results = new ArrayList<String>();
		try {
			
			FileInputStream fStream = new FileInputStream(new File(fileName));
			InputStreamReader iReader = new InputStreamReader(fStream,"gbk");
			BufferedReader bReader = new BufferedReader(iReader);
			
			String line = "";
			while ((line = bReader.readLine()) != null) {
				results.add(line);
			}
			bReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public static void write(List<String> text, String fileName, Integer mode) {
		try {
			FileWriter writer = null;
			switch (mode) {
			case 1:	//覆盖
				writer = new FileWriter(fileName);
				break;
			case 2:	//追加
				writer = new FileWriter(fileName, true);
				break;
			default:
				break;
			}
			//File writename = new File(fileName); 	
			//BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			for (String string : text) {
				writer.write(string);
				//out.write(string);
			}
			writer.close();
			//out.flush();
			//out.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void WriteTuple(String tuple, String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(tuple + "\r\n");
			writer.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	public static Integer FindTuple(Integer hashCode, String fileName) {
		File file = new File(fileName);
		FileReader in = new FileReader(file);
		LineNumberReader reader = new LineNumberReader(in);
		String string = reader.readLine();
		if () {
			
		}
	}
	*/
	
	public static Boolean CreateFile(String path) {
		File file = new File(path);
		if(file.exists()){
			return false;
		}
		try {
			if(file.createNewFile()){
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Boolean CreateDir(String path) {
		File dir = new File(path);
		if(dir.exists()){
			return false;
		}
		if(dir.mkdirs()){
			return true;
		} else {
			return false;
		}
	}
}
