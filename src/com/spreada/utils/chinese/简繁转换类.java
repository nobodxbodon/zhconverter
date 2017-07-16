package com.spreada.utils.chinese;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class 简繁转换类 {


	private Properties 字符表= new Properties();
	private Set conflictingSets  = new HashSet();

	public static final int TRADITIONAL = 0;
	public static final int SIMPLIFIED = 1;
	private static final int NUM_OF_CONVERTERS = 2;
	private static final 简繁转换类[] converters = new 简繁转换类[NUM_OF_CONVERTERS];
	private static final String[]  propertyFiles = new String[2];

	static {
		propertyFiles[TRADITIONAL] = "zh2Hant.properties";
		propertyFiles[SIMPLIFIED] = "zh2Hans.properties";
	}



	/**
	 *
	 * @param converterType 0 for traditional and 1 for simplified
	 * @return
	 */
	public static 简繁转换类 getInstance(int converterType) {

		if (converterType >= 0 && converterType < NUM_OF_CONVERTERS) {

			if (converters[converterType] == null) {
				synchronized(简繁转换类.class) {
					if (converters[converterType] == null) {
						converters[converterType] = new 简繁转换类(propertyFiles[converterType]);
					}
				}
			}
			return converters[converterType];

		} else {
			return null;
		}
	}

	public static String convert(String text, int converterType) {
		简繁转换类 instance = getInstance(converterType);
		return instance.convert(text);
	}


	private 简繁转换类(String propertyFile) {

	    InputStream is = null;


	    is = getClass().getResourceAsStream(propertyFile);

		//File propertyFile = new File("C:/Temp/testMDB/TestTranslator/abc.txt");
		if (is != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				字符表.load(reader);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (reader != null)
						reader.close();
					if (is != null)
						is.close();
				} catch (IOException e) {
				}
			}
		}
		initializeHelper();
	}

	private void initializeHelper() {
		Map stringPossibilities = new HashMap();
		Iterator iter = 字符表.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key.length() >= 1) {

				for (int i = 0; i < (key.length()); i++) {
					String keySubstring = key.substring(0, i + 1);
					if (stringPossibilities.containsKey(keySubstring)) {
						Integer integer = (Integer)(stringPossibilities.get(keySubstring));
						stringPossibilities.put(keySubstring, new Integer(
								integer.intValue() + 1));

					} else {
						stringPossibilities.put(keySubstring, new Integer(1));
					}

				}
			}
		}

		iter = stringPossibilities.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (((Integer)(stringPossibilities.get(key))).intValue() > 1) {
				conflictingSets.add(key);
			}
		}
	}

	public String convert(String in) {
		StringBuilder outString = new StringBuilder();
		StringBuilder stackString = new StringBuilder();

		for (int i = 0; i < in.length(); i++) {

			char c = in.charAt(i);
			String key = "" + c;
			stackString.append(key);

			if (conflictingSets.contains(stackString.toString())) {
			} else if (字符表.containsKey(stackString.toString())) {
				outString.append(字符表.get(stackString.toString()));
				stackString.setLength(0);
			} else {
				CharSequence sequence = stackString.subSequence(0, stackString.length()-1);
				stackString.delete(0, stackString.length()-1);
				flushStack(outString, new StringBuilder(sequence));
			}
		}

		flushStack(outString, stackString);

		return outString.toString();
	}


	private void flushStack(StringBuilder outString, StringBuilder stackString) {
		while (stackString.length() > 0){
			if (字符表.containsKey(stackString.toString())) {
					outString.append(字符表.get(stackString.toString()));
					stackString.setLength(0);

				} else {
					outString.append("" + stackString.charAt(0));
					stackString.delete(0, 1);
			}

		}
	}


	String parseOneChar(String c) {

		if (字符表.containsKey(c)) {
			return (String) 字符表.get(c);

		}
		return c;
	}


}
