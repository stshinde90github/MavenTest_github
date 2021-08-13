package com.MAVENTEST;

import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v91.emulation.Emulation;
import org.openqa.selenium.devtools.v91.fetch.Fetch;
import org.openqa.selenium.devtools.v91.network.Network;
import org.openqa.selenium.devtools.v91.network.model.Request;
import org.openqa.selenium.devtools.v91.network.model.Response;

//import io.restassured.response.Response;
import io.github.bonigarcia.wdm.WebDriverManager;

public class NetworkMocking {

	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver = new ChromeDriver();
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		devTools.send(Fetch.enable(Optional.empty(), Optional.empty()));
		devTools.addListener(Fetch.requestPaused(), request->{
			if(request.getRequest().getUrl().contains("shetty")) {
				String mockURL= request.getRequest().getUrl().replace("=shetty", "=BadGuy");
				System.out.println(mockURL);
				devTools.send(Fetch.continueRequest(request.getRequestId(), Optional.of(mockURL), Optional.of(request.getRequest().getMethod()),
						Optional.empty(), Optional.empty()));
			}
			else {
				devTools.send(Fetch.continueRequest(request.getRequestId(), Optional.of(request.getRequest().getUrl()), Optional.of(request.getRequest().getMethod()),
						Optional.empty(), Optional.empty()));
			}
		});
		
		driver.get("https://www.rahulshettyacademy.com/angularAppdemo/");
		driver.manage().timeouts().getImplicitWaitTimeout();
		driver.findElement(By.xpath("//button[contains(text(), ' Virtual Library')] ")).click();
		Thread.sleep(4000);
		String textPresent  = driver.findElement(By.cssSelector("p")).getText();
	    System.out.println(textPresent);
	}

}
