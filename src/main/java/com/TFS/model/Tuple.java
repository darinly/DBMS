package com.TFS.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.TFS.model.Field;

public class Tuple {
	
	private Map<String, String> tuple;
	
	public Tuple() {
		tuple = new LinkedHashMap<String, String>();
	}
	
	public void setTuple(Map<String, String> map) {
		this.tuple = map;
	}
	
	public Map<String, String> getTuple() {
		return tuple;
	}
	
	public Boolean Add(String key, String value) {
		if (tuple.containsKey(key)) {
			return false;
		} else {
			tuple.put(key, value);
			return true;
		}
	}
	
	public Integer size() {
		return tuple.size();
	}
	
	public Entry<String, String> getFirst() {
		Entry<String, String> result = null;
		for (Entry<String, String> entry : tuple.entrySet()) {
			if (entry.getKey() != null) {
				result = entry;
				break;
			}
		}
		return result;
	}
	
	public Set<String> getKeys() {
		return tuple.keySet();
	}
	
	public Collection<String> getValues() {
		return tuple.values();
	}
	
	public Map<String, String> getMap() {
		return tuple;
	}

	@Override
	public String toString() {
		return "Tuple [tuple=" + tuple + "]";
	}
	
	
	
	/*
	private List<Field> fields;
	
	public Tuple() {
		fields = new ArrayList<Field>();
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
	public Boolean isEmpty() {
		return fields.isEmpty();
	}
	
	public Boolean contains(Field field) {
		for (Field f : fields) {
			if(f.getName().equals(field.getName()) && f.getType().equals(field.getType())) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
	
	public Boolean AddField(Field field) {
		if(contains(field)){
			return false;
		} else {
			fields.add(field);
			return true;
		}
	}

	@Override
	public String toString() {
		return "Tuple [fields=" + fields + "]";
	}
	
	public String toValue() {
		String result = "";
		for (Field field : fields) {
			result = result + field.getName() + "\t";
		}
		return result;
	}
	
	public Boolean setValues(List<String> values) {
		if(values.size() == fields.size()) {
			for (int i = 0; i < values.size(); i++) {
				fields.get(i).setName(values.get(i));
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void setTypes(Tuple tuple) {
		for (Field field : tuple.getFields()) {
			AddField(new Field("", field.getType()));
		}
	}
	*/
}
