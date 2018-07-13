package core;

import org.openqa.selenium.*;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Safari {
	static WebDriver driver = new SafariDriver();
	
	public static String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, 
	InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
	}

	public static boolean isPresent(final By by) {
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		if (!driver.findElements(by).isEmpty())
			return true;
		else
			return false;
	}

	public static void main(String[] args) throws InterruptedException, InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);

		String mac_address;
		String cmd_mac = "ifconfig en0";
		String cmd_win = "cmd /C for /f \'usebackq tokens=1\' %a in ('getmac ^| findstr Device') do echo %a";
		String url = "http://facebook.com/";
		String email_address = "kotik_good@mail.ru";
		String encrypted_password = "xNYuVbynhPCkZGI20ulIVQ==";
		
		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) 
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next().split(" ")[1];
		
		else
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next().split(" ")[4];
		
		String user_password = decrypt(encrypted_password, new SecretKeySpec(Arrays.copyOf(mac_address.getBytes("UTF-8"), 16), "AES"));
	

		if (!System.getProperty("os.name").toUpperCase().contains("MAC"))
			throw new IllegalArgumentException("Safari is available only on Mac");

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		WebDriverWait wait = new WebDriverWait(driver, 15);

		driver.get(url);

		wait.until(ExpectedConditions.titleIs("Facebook - Log In or Sign Up"));
		String title = driver.getTitle();

//		By email = By.id("email");
		By email = By.xpath(".//*[@id='email']");
//		By password = By.id("pass");
		By password = By.xpath(".//*[@id='pass']");
		By login = By.id("loginbutton");
		By Copyright = By.xpath("//span[contains(text(),'Facebook © 2018')]");
		By timeline = By.xpath("//*[@id='u_0_a']/div[1]/div[1]/div/a/span/span");
		By friends = By.xpath("//span[text()='215']");
//		By account = By.id("userNavigationLabel");
		By account = By.xpath(".//*[@id='userNavigationLabel']");
		By logout = By.xpath("//span[text()='Log Out']");

		String copyright = wait.until(ExpectedConditions.presenceOfElementLocated(Copyright)).getText();
		Dimension copyright_Size = driver.findElement(Copyright).getSize();
		Point copyright_Location = driver.findElement(Copyright).getLocation();

		System.out.println("01. Browser: Safari");
		System.out.println("02. Title of the page: " + title);
		System.out.println("03. Copyright: " + copyright);
		System.out.println("04. Element [Copyright Text]: " + (isPresent(Copyright) ? "Exists" : " Not exist "));
		System.out.println("05. Size of [Copyright Text]: " + copyright_Size);
		System.out.println("06. Location of [Copyright Text]: " + copyright_Location);

		wait.until(ExpectedConditions.presenceOfElementLocated(email)).clear();
		wait.until(ExpectedConditions.presenceOfElementLocated(email)).sendKeys(email_address);
		Dimension email_Size = driver.findElement(email).getSize();
		Point email_Location = driver.findElement(email).getLocation();

		wait.until(ExpectedConditions.presenceOfElementLocated(password)).clear();
		wait.until(ExpectedConditions.presenceOfElementLocated(password)).sendKeys(user_password);
		Dimension pass_Size = driver.findElement(password).getSize();
		Point pass_Location = driver.findElement(password).getLocation();

		System.out.println("07. Element [Email Field]: " + (isPresent(email) ? "Exists" : " Not exist "));
		System.out.println("08. Size of [Email Field]: " + email_Size);
		System.out.println("09. Location of [Email Field]: " + email_Location);
		System.out.println("10. Element [Password Field]: " + (isPresent(password) ? "Exists" : " Not exist "));
		System.out.println("11. Size of [Password Field]: " + pass_Size);
		System.out.println("12. Location of [Password Field]: " + pass_Location);

		wait.until(ExpectedConditions.presenceOfElementLocated(login));
		Dimension login_Size = driver.findElement(login).getSize();
		Point login_Location = driver.findElement(login).getLocation();

		System.out.println("13. Element [Log In Button]: " + (isPresent(login) ? "Exists" : " Not exist "));
		System.out.println("14. Size of [Log In Button]: " + login_Size);
		System.out.println("15. Location of [Log In Button]: " + login_Location);

		wait.until(ExpectedConditions.elementToBeClickable(login)).click();

		Thread.sleep(4000);
		driver.findElement(timeline);
		Dimension timeline_Size = driver.findElement(timeline).getSize();
		Point timeline_Location = driver.findElement(timeline).getLocation();

		System.out.println("16. Element [Timeline Button]: " + (isPresent(timeline) ? "Exists" : " Not exist "));
		System.out.println("17. Size of [Timeline Button]: " + timeline_Size);
		System.out.println("18. Location of [Timeline Button]: " + timeline_Location);

		wait.until(ExpectedConditions.visibilityOfElementLocated(timeline)).click();
		
		Thread.sleep(4000);
		String friends_total = wait.until(ExpectedConditions.presenceOfElementLocated(friends)).getText();
		Dimension friends_Size = driver.findElement(friends).getSize();
		Point friends_Location = driver.findElement(friends).getLocation();

		System.out.println("19. Element [Friends Button]: " + (isPresent(friends) ? "Exists" : " Not exist "));
		System.out.println("20. Size of [Friends Button]: " + friends_Size);
		System.out.println("21. Location of [Friends Button]: " + friends_Location);
		System.out.println("22. You have " + friends_total + " friends");

		wait.until(ExpectedConditions.presenceOfElementLocated(account));
		Dimension account_Size = driver.findElement(timeline).getSize();
		Point account_Location = driver.findElement(timeline).getLocation();

		System.out
				.println("23. Element [Account Settings Button]: " + (isPresent(timeline) ? "Exists" : " Not exist "));
		System.out.println("24. Size of [Account Settings Button]: " + account_Size);
		System.out.println("25. Location of [Account Settings Button]: " + account_Location);
		
		WebElement settings = driver.findElement(account);	
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", settings); 		
		
		driver.findElement(logout);
		Dimension logout_Size = driver.findElement(logout).getSize();
		Point logout_Location = driver.findElement(logout).getLocation();

		System.out.println("26. Element [Log Out Button]: " + (isPresent(timeline) ? "Exists" : " Not exist "));
		System.out.println("27. Size of [Log Out Button]: " + logout_Size);
		System.out.println("28. Location of [Log Out Button]: " + logout_Location);

		WebElement logout_click = driver.findElement(logout);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout_click);

		driver.quit();

	}
}
