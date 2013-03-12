package com.deviceteam.kezdet.exception;

public class PluginVerifyException extends Exception
{
  private static final long serialVersionUID = 2252667237932482515L;

  public PluginVerifyException( String detailMessage )
  {
    super( detailMessage );
  }

  public PluginVerifyException( String detailMessage, Throwable throwable )
  {
    super( detailMessage, throwable );
  }

  public PluginVerifyException( Throwable throwable )
  {
    super( throwable );
  }
}
