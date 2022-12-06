package com.qa; //super class

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

//@Listeners(ExtentITestListenerAdapter.class)
public class BaseTest { 
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();//The protected modifier specifies that the member can only be accessed within its own package & in addition, by a subclass of its class in another package.
    protected static ThreadLocal <Properties> props = new ThreadLocal<Properties>();//creating object at class level
    protected static ThreadLocal <HashMap<String , String>> strings = new ThreadLocal<HashMap<String , String>>(); //creating object
    protected static ThreadLocal <String> platform = new ThreadLocal<String>(); //All global parameters are converted to ThreadLocal object 
    protected static ThreadLocal <String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();
    private static AppiumDriverLocalService server; //creating variable
    TestUtils utils = new TestUtils();
    
    public AppiumDriver getDriver() {
  	  return driver.get(); //getter method will return value for that particular thread that ensures thread safety , remove class level references & now use getter & setter method wherever using this global parameters
    }
    
    public void setDriver(AppiumDriver driver2) {
    	driver.set(driver2); //setter method will set value of object for current thread
    }
    
    public Properties getProps() {
    	  return props.get(); //it returns value for that particular thread that ensures thread safety
      }
      
     public void setProps(Properties props2) {
      	props.set(props2); //set value of object for current thread
      }
      
     public HashMap<String , String> getStrings() {
    	  return strings.get(); //it returns value for that particular thread that ensures thread safety
      }
      
     public void setStrings(HashMap<String , String> strings2) {
      	strings.set(strings2); //set value of object for current thread
      }
      
     public String getPlatform() {
    	  return platform.get(); //it returns value for that particular thread that ensures thread safety
      }
      
     public void setPlatform (String platform2) {
      	platform.set(platform2); //set value of object for current thread
      }
      
     public String getDateTime() {
      	  return dateTime.get();
        }
      
     public void setDateTime (String dateTime2) {
        	dateTime.set(dateTime2); //set value of object for current thread
        }
     
     public String getDeviceName() {
     	  return deviceName.get();
       }
     
    public void setDeviceName (String deviceName2) {
       	deviceName.set(deviceName2); //set value of object for current thread
       }
    
    public BaseTest() { //creating constructor
    	PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this); //this command will initialize UI elements for Page factory,this is super class all POM files will extend this class,UI elements defined in pages will be automatically initialized using this command 
    }
    
    @BeforeMethod
    public void beforeMethod() {
    	((CanRecordScreen) getDriver()).startRecordingScreen();
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) { //creating directory structure need ITest Result from testng & pass that as argument
    	String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
    	
    	if(result.getStatus()== 2) {	
    		Map <String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
        	
        	String dir = "videos" + File.separator + params.get("platformName") + "_" + params.get("platformVersion") + "_"
        			+ params.get("deviceName") + File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();
        	
        	File videodir = new File(dir); //creating directory structure
        	
        	if(!videodir.exists()) { //if not exist then it will create directory if exist then it will just overwrite the files
        		videodir.mkdirs();
        	}
        	
        	try {
    			FileOutputStream stream = new FileOutputStream(videodir + File.separator + result.getName()+ ".mp4");
    			stream.write(Base64.getDecoder().decode(media));
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
       
    	}
    	
    }
    @BeforeSuite
    public void beforeSuite() throws Exception, Exception{
    	ThreadContext.put("ROUTINGKEY", "ServerLogs");
    	server = getAppiumService();
    	if(!checkIfAppiumServerIsRunning(4723)) {
    		server.start();
        	server.clearOutPutStreams();
        	utils.log().info("Appium server started");
    	}else {
    		utils.log().info("Appium server already Running");
    	}
    }
    
    public boolean checkIfAppiumServerIsRunning(int port) throws Exception{ //takes port number as argument
    	boolean isAppiumServerRunning = false;
    	ServerSocket socket; //creates socket object
    	try {
    		socket = new ServerSocket(port); //tries to open port , if AppiumServer is already running on this port then throws IO Exception
    		socket.close(); //close the socket
    	}catch (IOException e) { //it will catch exception & set flag to true
    		System.out.println("1");
    		isAppiumServerRunning = true; //set flag to true
    	}finally {
    		socket = null;
    	}
    	return isAppiumServerRunning; //return this flag to calling method
    }
    
    @AfterSuite
    public void afterSuite(){
    	server.stop();
    	utils.log().info("Appium server stopped");
    }
    
    public AppiumDriverLocalService getAppiumServerDefault() { //creating method to get AppiumDriverLocalService object
    	return AppiumDriverLocalService.buildDefaultService();
    }
    
    public AppiumDriverLocalService getAppiumService() {     	
    	//HashMap<String, String> environment = new HashMap<String, String>(); //created HASHMAP object
    	//environment.put("PATH","C:/Python310/Scripts/;C:/Python310/;C:/Program Files (x86)/Common Files/Oracle/Java/javapath;C:/WINDOWS/system32;C:/WINDOWS;C:/WINDOWS/System32/Wbem;C:/WINDOWS/System32/WindowsPowerShell/v1.0/;C:/WINDOWS/System32/OpenSSH/;C:/Users/kajal/AppData/Local/Android/Sdk/platform-tools;C:/Users/kajal/AppData/Local/Android/Sdk/tools;C:/Users/kajal/AppData/Local/Android/Sdk/tools/bin;C:/apache-maven-3.8.5/bin;C:/Program Files/Java/jdk1.8.0_331/bin;C:/Program Files/Git/cmd;C:/Program Files/nodejs/;C:/ProgramData/chocolatey/bin;;C:/Program Files/Docker/Docker/resources/bin;C:/ProgramData/DockerDesktop/version-bin;C:/Program Files/Java/jdk1.8.0_331/bin;C:/Users/kajal/AppData/Roaming/npm;%USERPROFILE%/AppData/Local/Microsoft/WindowsApps" + System.getenv("PATH"));
    	//environment.put("ANDROID_HOME","C:/Users/kajal/AppData/Local/Android/Sdk");
    	return AppiumDriverLocalService.buildService(new AppiumServiceBuilder() //creating new instance of AppiumServiceBuilder using below options to build the service
    			.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
    			.withAppiumJS(new File("C:\\Users\\kajal\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
    			.usingPort(4723)
    			.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
    	//		.withEnvironment(environment)
    			.withLogFile(new File("ServerLogs/server.log")));
    }
    
  @Parameters({"platformName" , "deviceName", "emulator" , "udid" , "systemPort" , "chromeDriverPort"}) //using @Parameters annotations provided by testng.xml file to read parameters
  @BeforeTest // It will be executed before any of the test class executed
  public void beforeTest(String platformName , String deviceName , String emulator , String udid , String systemPort , String chromeDriverPort) throws Exception { //passing parameters as an argument to this method
	  
	  setDateTime(utils.dateTime());
	  setPlatform(platformName);//platformName variable is local to beforeTest method & local variables guarantee Thread safety but we need to pass
	  //value of local variable to global variable using setter method & our global variable we have defined it using ThreadLocal class which ensures thread safety for global variables
	  setDeviceName(deviceName);
	  URL url;
	    InputStream inputstream = null; //converting class variables to local variables inside method
	    InputStream stringis = null;
	    Properties props=new Properties();
	    AppiumDriver driver;//declaring local
	    
	    String strFile = "logs" + File.separator + platformName + "_" + deviceName; //file path
		File logFile = new File(strFile); //creating file object
		if (!logFile.exists()) {
			logFile.mkdirs(); //if directory does not exist then it will create
		}
		//route logs to separate file for each thread
		ThreadContext.put("ROUTINGKEY", strFile);//using ThreadContext class from log4j & put method to set the value for routing key variable & 
		//value for Routing key variable is file path & file path will be different for each thread
		utils.log().info("log path: " + strFile);
		
	  try {
		  props=new Properties();//creating new instances of properties object
		  String propFileName = "config.properties";//creating string with properties file name
		  String xmlFileName = "strings/strings.xml";// creating string object for XML file name
		  
		  inputstream = getClass().getClassLoader().getResourceAsStream(propFileName);//creating inputstream object for properties file
		  props.load(inputstream);//to load properties file
		  setProps(props);
		  
		  stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);//reading file as input Stream
		  setStrings(utils.parseStringXML(stringis));//calling parse string xml methods & method returns HashMap
		  
		  DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		  desiredCapabilities.setCapability("platformName", platformName);
		  desiredCapabilities.setCapability("deviceName", deviceName);
		  desiredCapabilities.setCapability("udid" , udid);
		  //url = new URL(props.getProperty("appiumURL") + "4723/wd/hub");
		  
		  //switch(deviceName) {
		  //case "Moto X Force":
			  desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
			  desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
			  desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
			  if(emulator.equalsIgnoreCase("true")) { //if using emulator in place of real device
			       desiredCapabilities.setCapability("avd" , deviceName);
			       }
			  desiredCapabilities.setCapability("systemPort", systemPort);
			  desiredCapabilities.setCapability("chromeDriverPort",chromeDriverPort);
			  //URL appurl = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));//getResource method will get the complete path for test resources package & then it will append path from config.properties file
			  //String appurl = getClass().getResource(props.getProperty("androidAppLocation")).getFile(); //defining it as string not URL
			  String appurl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
			  utils.log().info("appUrl is " + appurl);
			  desiredCapabilities.setCapability("app", appurl);
			  
			  url = new URL(props.getProperty("appiumURL") + "4723/wd/hub");
			  driver = new AndroidDriver(url, desiredCapabilities);
			  
		  /*break;
		  case "OPPOA57":
			  desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
			  desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
			  desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
			  if(emulator.equalsIgnoreCase("true")) { //if using emulator in place of real device
			       desiredCapabilities.setCapability("avd" , deviceName);
			       }
			  desiredCapabilities.setCapability("systemPort", systemPort);
			  desiredCapabilities.setCapability("chromeDriverPort",chromeDriverPort);
			  //URL appurl = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));//getResource method will get the complete path for test resources package & then it will append path from config.properties file
			  //String appurl = getClass().getResource(props.getProperty("androidAppLocation")).getFile(); //defining it as string not URL
			  String appurl2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
			  utils.log().info("appUrl is " + appurl2);
			  desiredCapabilities.setCapability("app", appurl2);
			  
			  url = new URL(props.getProperty("appiumURL") + "4724/wd/hub");
			  driver = new AndroidDriver(url, desiredCapabilities);
			  
		      break;
		   default:
			   throw new Exception("Invalid DeviceName! - " + deviceName);
		  }*/
		  setDriver(driver);//passing driver to global variable  
	  }catch(Exception e) {
		  e.printStackTrace();
	  } finally {
		  if (inputstream != null) {
			  inputstream.close();
		  }
		  if (stringis != null) {
			  stringis.close();
		  }
	  }
  }
  
  
  
  public void waitForVisibility(MobileElement e) { //creating method using explicit wait to check the element visibility
	  WebDriverWait wait = new WebDriverWait(getDriver(),TestUtils.WAIT);
	  wait.until(ExpectedConditions.visibilityOf(e));//this will wait 10 sec for element to be visible if not visible it will throw timeout exception
  }
  
  public void click(MobileElement e) {   //creating methods for driver commands click
	  waitForVisibility(e); // will check visibility if visible then will click on element
	  e.click();
  }
  
  public void sendKeys(MobileElement e , String txt) {   //creating methods for driver commands sendKeys
	  waitForVisibility(e); // will check visibility if visible then will click on element
	  e.sendKeys(txt);
  }
  
  public String getAttribute(MobileElement e , String attribute) {   //creating methods for driver commands getAttribute
	  waitForVisibility(e); // will check visibility if visible then will click on element
	  return e.getAttribute(attribute);
  }
  
  public void closeApp() {
	  ((InteractsWithApps)getDriver()).closeApp();
  }
  
  public void launchApp() {
	  ((InteractsWithApps)getDriver()).launchApp();
  }
  
  public MobileElement scrollToElement() {
	  return (MobileElement) ((FindsByAndroidUIAutomator)getDriver()).findElementByAndroidUIAutomator( //It will return mobile element since driver is Appium driver if we need to find elemeny by AndroidUIAutomator framework then we need to cast driver using FindsByAndroidUIAutomator class
			  "new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView(" //then we need to find parent element i.e scrollable element"
			  		 + "new UiSelector().description(\"test-Price\"));");//then we need to find child element i.e scrollTo element , parent & child element both can be found using resource id or text or class name or content description which is basically accessibility id but cannot use xpath to find element in this method
  }

  @AfterTest // It will be executed after all of the test class executed
  public void afterTest() {
	  getDriver().quit();
  }

}
