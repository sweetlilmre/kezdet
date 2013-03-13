package com.deviceteam.kezdet.exception;

public class PluginLoadException extends Exception
{
  private static final long serialVersionUID = -2832748859048593185L;

  public PluginLoadException( String detailMessage )
  {
    super( detailMessage );
  }

  public PluginLoadException( String detailMessage, Throwable throwable )
  {
    super( detailMessage, throwable );
  }

  public PluginLoadException( Throwable throwable )
  {
    super( throwable );
  }

}
