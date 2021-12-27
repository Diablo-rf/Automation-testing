package Test.Data.Automation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class Search_Comparison {

	@Test(priority = 0, enabled = true)
	public void Actual_Google() throws IOException, InterruptedException {

		System.setProperty("webdriver.chrome.driver", "C://Drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		CSVWriter writer = new CSVWriter(new FileWriter("test-output//actual_google.csv"));
		List list = new ArrayList();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		driver.get("https://google.com");
		WebElement searchfield = driver.findElement(By.name("q"));
		searchfield.sendKeys("site:https://point.md/");
		searchfield.sendKeys(Keys.ENTER);

		int linknum = 1;
		String header[] = { "Link number", "URL", "Actual Title", "Actual Description" };
		list.add(header);

		for (int loop = 0; loop < 3; loop++) {

			List<WebElement> Titles = driver.findElements(By.xpath("//h3[@class='LC20lb MBeuO DKV0Md']"));
			List<WebElement> Desc = driver
					.findElements(By.xpath("//div[@class='VwiC3b yXK7lf MUxGbd yDYNvb lyLwlc lEBKkf']"));
			List<WebElement> URLs = driver.findElements(By.xpath("//div[@class='yuRUbf']/a"));

			int linksnum = Titles.size();

			for (int count = 0; count < linksnum; count++) {

				String data[] = { linknum + "", URLs.get(count).getAttribute("href"), Titles.get(count).getText(),
						Desc.get(count).getText() };
				list.add(data);

				linknum++;
			}

			Thread.sleep(2000);
			if (linknum >= 30) {
				break;
			}

			driver.findElement(By.xpath("//span[contains(text(),'Înainte')]")).click(); // To change depending on browser language
		}

		writer.writeAll(list);
		writer.flush();
		writer.close();
		driver.close();

	}

	@Test(priority = 1, enabled = true)
	public void Expected_Point() throws CsvValidationException, IOException {

		System.setProperty("webdriver.chrome.driver", "C://Drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		CSVWriter writer = new CSVWriter(new FileWriter("test-output//expected_point.csv"));
		List list = new ArrayList();
		
		
		String header[] = { "Link number", "URL", "Expected Title", "Expected Description" };
		list.add(header);

		int linknum = 1;
		CSVReader reader = null;
		
		try {
			reader = new CSVReader(new FileReader("test-output//actual_google.csv"));
			String[] URLs = reader.readNext();

			while ((URLs = reader.readNext()) != null) {
				
				String URL = URLs[1];
				driver.get(URL);
				String Desc = driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content");
				String data[] = {linknum+"", driver.getCurrentUrl(), driver.getTitle(), Desc};  
				list.add(data);
				
				linknum++;
			}
		} catch (IOException error) {
			error.printStackTrace();
		}
		
		writer.writeAll(list);
		writer.flush();
		writer.close();
		reader.close();
		driver.close();
		
		
		
	}

	@Test (priority = 2, enabled = true)
	public void Test_results() throws IOException, CsvValidationException {
		
		CSVWriter writer = new CSVWriter(new FileWriter("test-output//result.csv"));	
		CSVReader actualf = null;
		CSVReader expectedf = null;
		
		List list = new ArrayList();
		
		try {	
			actualf = new CSVReader(new FileReader("test-output//actual_google.csv"));
			expectedf = new CSVReader(new FileReader("test-output//expected_point.csv"));
			
			String[] actual = actualf.readNext();
			String[] expected = expectedf.readNext();
				
			int num = 1;
			
			while ((actual = actualf.readNext()) != null) {
				expected = expectedf.readNext();
				
				String header[] = {num + "", actual[1], "Expected Result", "Actual Result", "Status"};
				list.add(header);

				String aTitle = actual[2];	
				String eTitle = expected[2];
				
				if(eTitle.equals(aTitle)) {
				String Title[] = {" ", "Title", eTitle, aTitle, "Passed"};
				list.add(Title);
				}
				else {
					String Title[] = {" ", "Title", eTitle, aTitle, "Failed"};
					list.add(Title);
				}				
				
				String aDesc = actual[3];
				String eDesc = expected[3];
				
				if(eDesc.equals(aDesc)) {
					String Desc[] = {" ", "Description", eDesc, aDesc, "Passed"};
					list.add(Desc);
						}
						else {
							String Desc[] = {" ", "Description", eDesc, aDesc, "Failed"};	
							list.add(Desc);
						}			
				num++;
			}		
			
		} catch(IOException error) {
			error.printStackTrace();		
		}
		
		writer.writeAll(list, true);
		writer.flush();
		writer.close();
		actualf.close();
		expectedf.close();
		
		System.out.println("С НАСТУПАЮЩИМ НОВЫМ ГОДОМ Avantaj Prim !");
	}
	
	
}
