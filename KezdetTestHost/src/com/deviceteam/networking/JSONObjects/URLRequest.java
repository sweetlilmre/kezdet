package com.deviceteam.networking.JSONObjects;

public class URLRequest
{
  // {"contentType":null,"followRedirects":true,"userAgent":"Mozilla/5.0 (Windows; U; en-US) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.4","data":null,"manageCookies":true,"requestHeaders":[],"method":"GET","cacheResponse":true,"idleTimeout":0,"authenticate":true,"url":"http://www.google.com","useCache":true,"digest":null}

  public String contentType;
  public boolean followRedirects;
  public String userAgent;
  public String data;
  public boolean manageCookies;
  public NameValue[] requestHeaders;
  public String method;
  public boolean cacheResponse;
  public int idleTimeout;
  public boolean authenticate;
  public String url;
  public boolean useCache;
  // digest":null

}
