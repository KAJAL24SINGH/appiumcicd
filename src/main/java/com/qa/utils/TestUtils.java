package com.qa.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qa.BaseTest;


public class TestUtils {
	public static final long WAIT = 20; // creating static final variable The static keyword is used to represent the class member.It is basically used with methods and variables to indicate that it is a part of the class, not the object.

	
	public HashMap <String , String> parseStringXML(InputStream file) throws Exception{ //method name is parseStringXML it will return HashMap with key value pair it will read the XML file as input Stream, we can pass InputStream as file object as well
		HashMap <String , String> stringMap = new HashMap <String , String>(); //defined HashMap as stringMap
		//Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //Initialize DocumentBuilderFactory
		DocumentBuilder builder = factory.newDocumentBuilder(); 
		
		//Build Document
		Document document = builder.parse(file); // using build parsing XML file
		
		//Normalize the XML structure.It's just too important !!
		document.getDocumentElement().normalize();
		
		//Here comes the root node
		Element root = document.getDocumentElement(); // root element is resources element from strings.xml file
		
		//Get all elements
		NodeList nList = document.getElementsByTagName("string"); //reading strings using string tag & collecting in NodeList i.e it search for string tag from strings.xml file & store in NodeList
		
		for (int temp = 0; temp < nList.getLength(); temp++ ) { //iterating NodeList
			Node node = nList.item(temp); //storing individual item in node
			
			if(node.getNodeType()== Node.ELEMENT_NODE) { // checking whether node is elemnt or not
				Element eElement = (Element) node;
				//storing each element key value in map
				stringMap.put(eElement.getAttribute("name"), eElement.getTextContent()); //if node is element then will get name(key) & text content from strings.xml file (value) for HashMap , Extracting strings & putting it in HashMap
			}
		}
		return stringMap; //returning HashMap object
	}
	
	public String dateTime() {
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date(); //create new date object & format it in above format
		//System.out.println(dateformat.format(date));//now will use single Appium server instances so no need of this line call log method at all places
		return dateformat.format(date); //returns date as string
		
	}
	
	public void log(String txt) {//output log in separate file for each devices
		BaseTest base = new BaseTest();//takes txt as input
		String msg = Thread.currentThread().getId() + ":" + base.getPlatform() + ":" + base.getDeviceName() + ":" //constructs a message using details
				+ Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;
		
		System.out.println(msg);
		
		String strFile = "logs" + File.separator + base.getPlatform() + "_" + base.getDeviceName() //then it creates this directory structure
				+ File.separator + base.getDateTime();

		File logFile = new File(strFile);

		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		
		FileWriter fileWriter = null; //using FileWriter it creates log.txt file & then it writes message in log file
		try {
			fileWriter = new FileWriter(logFile + File.separator + "log.txt",true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.println(msg);
	    printWriter.close(); //closes the log file
	}
	
	public Logger log() {
		return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
	}
}
