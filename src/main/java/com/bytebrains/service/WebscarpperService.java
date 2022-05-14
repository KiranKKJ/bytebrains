package com.bytebrains.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Service
public class WebscarpperService {

	public void webscrap() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			final HtmlPage page1 = webClient.getPage("https://www.google.com");
			System.out.println(page1.asXml());
		}

	}

}
