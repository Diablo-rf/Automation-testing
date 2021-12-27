package Test.Data.Automation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import com.opencsv.CSVWriter;

public class Filters_Testing {

	@Test
	public void StepsToReproduce() throws InterruptedException, IOException {

		System.setProperty("webdriver.chrome.driver", "C://Drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		CSVWriter writer = new CSVWriter(new FileWriter("test-output//Filters_test_results.csv"));
		List list = new ArrayList();

		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		String header1[] = { "Step number", "Steps To Reproduce", "Expected Result", "Status" };
		list.add(header1);

		driver.get("https://999.md/ru/list/transport/cars");
		String prestep1[] = { "1", "Go to: https://999.md/ru/list/transport/cars ", "Website should open", "Passed" };
		list.add(prestep1);

		driver.findElement(By.cssSelector("#option-34")).click();
		String prestep2[] = { "2", "Select Filter: BMW ", "Filter should be selected", "Passed" };
		list.add(prestep2);
		Thread.sleep(1500);

		driver.findElement(By.cssSelector("#option-776")).click();
		String prestep3[] = { "3", "Select Filter: Sell ", "Filter should be selected", "Passed" };
		list.add(prestep3);
		Thread.sleep(1500);

		driver.findElement(By.cssSelector("#option-18592")).click();
		String prestep4[] = { "4", "Select Filter: Republic of Moldova ", "Filter should be selected", "Passed" };
		list.add(prestep4);
		Thread.sleep(1500);

		if (driver.getPageSource().contains("Confort")) {
			driver.findElement(By.xpath("//span[contains(text(),'Confort')]")).click();
			String prestep5[] = { "3", "Select Option: Comfort ", "Option should expand", "Passed" };
			list.add(prestep5);
			Thread.sleep(1500);
			driver.findElement(By.xpath("//input[@id='feature-130']")).click(); // Filter - Conditioner
			String prestep6[] = { "3", "Select Filter: Conditioner ", "Filter should be selected", "Passed" };
			list.add(prestep6);
		} else {
			driver.findElement(By.xpath("//span[contains(text(),'Комфорт')]")).click();
			String prestep5[] = { "3", "Select Option: Comfort ", "Option should expand", "Passed" };
			list.add(prestep5);
			Thread.sleep(1500);
			driver.findElement(By.xpath("//input[@id='feature-130']")).click();
			String prestep6[] = { "3", "Select Filter: Conditioner ", "Filter should be selected", "Passed" };
			list.add(prestep6);
		}

		Thread.sleep(1500);
		int linkstocheck = 52;
		int testcount = 1;

		for (int loop = 0; loop <= 1; loop++) {		
			List<WebElement> links = driver.findElements(By.xpath("//li[@class='ads-list-photo-item   ']"));
		
			for (int count = 2; count < linkstocheck; count++) {
			String test[] = { "Test number " + testcount + "" };
			list.add(test);
			String header2[] = { "Step number", "Steps To Reproduce", "Expected Result", "Status" };
			list.add(header2);
			
			links.get(count).click();			
			
			String Lang = driver.switchTo().frame("topbar-panel").findElement(By.xpath("//button[@class='user-item-btn']")).getText();
			
			driver.switchTo().defaultContent();

			String step1[] = { "1", "Go to: " + driver.getCurrentUrl(), "Page should open", "Passed " };
			list.add(step1);

			System.out.println("Test_Number " + testcount + " - Page URL:" + driver.getCurrentUrl());
			
			//Thread.sleep(1500);

			boolean brand = driver.findElements(By.xpath("//span[contains(text(),'BMW')]")).size() > 0;
			boolean LocRU = false;
			boolean TypeRU = false;
			boolean CondRU = false;
			boolean TypeRO = false;
			boolean CondRO = false;
			boolean LocRO = false;

			if (Lang.equals("русский")) {
				TypeRU = driver.findElements(By.xpath("//span[contains(text(),'Продам')]")).size() > 0;
				LocRU = driver.findElements(By.xpath("//span[contains(text(),'Республика Молдова')]")).size() > 0;
				CondRU = driver.findElements(By.xpath("//span[contains(text(),'Кондиционер')]")).size() > 0;
			} else {
				TypeRO = driver.findElements(By.xpath("//span[contains(text(),'Vând')]")).size() > 0;
				LocRO = driver.findElements(By.xpath("//span[contains(text(),'Republica Moldova')]")).size() > 0;
				CondRO = driver.findElements(By.xpath("//span[contains(text(),'Aer condiționat')]")).size() > 0;
			}

	
			if (brand) {
				String step2[] = { "2", "Check for Car Brand", "BMW", "Passed" };
				list.add(step2);
			} else {
				String step22[] = { "2", "Check for Car Brand", "BMW", "Failed" };
				list.add(step22);
			}
			if (TypeRU || TypeRO) {
				String step3[] = { "3", "Check for Offer Type", "Sell", "Passed" };

				list.add(step3);
			} else {
				String step33[] = { "3", "Check for Offer Type", "Sell", "Failed" };
				list.add(step33);
			}
			if (LocRU || LocRO) {
				String step4[] = { "4", "Check for Location", "Republic of Moldova", "Passed" };
				list.add(step4);
			} else {
				String step44[] = { "4", "Check for Location", "Republic of Moldova", "Failed" };
				list.add(step44);
			}
			if (CondRU || CondRO) {
				String step5[] = { "5", "Check for Filter Option", "Conditioner", "Passed" };
				list.add(step5);
			} else {
				String step55[] = { "5", "Check for Filter Option", "Conditioner", "Failed" };
				list.add(step55);
			}

			if (brand && (TypeRU || TypeRO) && (LocRU || LocRO) && (CondRU || CondRO)) {
				String passed[] = { "Test PASSED" };
				list.add(passed);
			} else {
				String failed[] = { "Test FAILED" };
				list.add(failed);
			}
			
			 driver.navigate().back();
			 links = driver.findElements(By.xpath("//li[@class='ads-list-photo-item   ']"));
			Thread.sleep(1500);
			testcount++;			 
			}
			
			if (testcount == 100) {
				break;
				}
				
			driver.findElement(By.linkText("2")).click();		
			Thread.sleep(1500);		
		}

		writer.writeAll(list);
		writer.flush();
		
		driver.close();
		
   System.out.println("С НАСТУПАЮЩИМ НОВЫМ ГОДОМ Avantaj Prim !");
		
	}
}

