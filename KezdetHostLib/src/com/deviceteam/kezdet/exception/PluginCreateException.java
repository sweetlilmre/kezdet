package com.deviceteam.kezdet.exception;

public class PluginCreateException extends Exception
{
  private static final long serialVersionUID = 4076653849808322777L;

  public PluginCreateException( String detailMessage )
  {
    super( detailMessage );
  }

  public PluginCreateException( String detailMessage, Throwable throwable )
  {
    super( detailMessage, throwable );
  }

  public PluginCreateException( Throwable throwable )
  {
    super( throwable );
  }

}
