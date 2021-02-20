package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.Map;

public interface IData {
	public void add(Map<String, Object> data);
	public Map<String, Object> getData();
	public static Class<IData> returnClass() {
		return IData.class;
	}
}
