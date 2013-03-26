package com.deviceteam.kezdet.interfaces.exception;

public class BadPluginException extends Exception
{
  private static final long serialVersionUID = -4554516897440174105L;

  public BadPluginException()
  {
  }

  public BadPluginException( String detailMessage )
  {
    super( detailMessage );
  }

  public BadPluginException( Throwable throwable )
  {
    super( throwable );
  }

  public BadPluginException( String detailMessage, Throwable throwable )
  {
    super( detailMessage, throwable );
  }
}
