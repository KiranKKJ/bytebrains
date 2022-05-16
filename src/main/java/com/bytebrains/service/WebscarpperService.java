package com.bytebrains.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;

@Service
public class WebscarpperService {
	private String LEADERSHIP_TEAM = " Leadership team";
	private String COMPANY_NAME = "CP&y Inc";

	public void webscrap() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			final HtmlPage googleSearchPage = webClient.getPage("https://www.google.com");

			HtmlElement inputField = googleSearchPage.getElementByName("q");
			inputField.type("CP&y Inc Leadership team");

			HtmlPage searchResultPage = googleSearchPage.getElementByName("btnK").click();
			System.out.println("=======================");
			System.out.println("=======================");
			System.out.println("=======================");
			System.out.println("=======================");
			System.out.println("=======================");
			List<String> resultList = searchResultPage.querySelectorAll("div > a").stream().filter(e -> e.hasAttributes())
			.map(e -> e.getAttributes().getNamedItem("href"))
			.filter(e -> e != null && e.getNodeValue().indexOf("leadership") >= 0).map(e -> e.getTextContent())
			.collect(Collectors.toList());
			System.out.println(resultList);
			
			for (String str: resultList) {
				HtmlPage page1 = webClient.getPage(str);
				System.out.println(page1.getTitleText());
				break;
			}

		}

	}

}
