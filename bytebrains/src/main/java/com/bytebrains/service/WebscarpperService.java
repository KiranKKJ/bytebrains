package com.bytebrains.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Service
public class WebscarpperService {
	private String LEADERSHIP_TEAM = " OFFICIAL Leadership team";

	public String webscrap(final String COMPANY_NAME)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			final HtmlPage googleSearchPage = webClient.getPage("https://www.google.com");

			HtmlElement inputField = googleSearchPage.getElementByName("q");
			String searchString = COMPANY_NAME.trim() + LEADERSHIP_TEAM;
			inputField.type(searchString);

			HtmlPage searchResultPage = googleSearchPage.getElementByName("btnK").click();
			System.out.println("=======================");
			System.out.println("=======================");
			System.out.println("Searching for: " + searchString);
			System.out.println("=======================");
			System.out.println("=======================");
			List<String> resultList = searchResultPage.querySelectorAll("div > a").stream()
					.filter(e -> e.hasAttributes()).map(e -> e.getAttributes().getNamedItem("href"))
					.filter(e -> e != null && (e.getNodeValue().indexOf("leadership") >= 0
							|| e.getNodeValue().indexOf("management") >= 0))
					.map(e -> e.getTextContent()).collect(Collectors.toList());
			System.out.println(resultList);

			if (resultList != null && !resultList.isEmpty()) {
				return resultList.get(0);
			} else {
				return "Not Found";
			}

		}

	}

}
