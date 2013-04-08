package com.deviceteam.kezdet.interfaces.exception;

public class PluginInvocationException extends Exception
{
  private static final long serialVersionUID = -4622925546979208751L;

  public PluginInvocationException( String detailMessage )
  {
    super( detailMessage );
  }

  public PluginInvocationException( String detailMessage, Throwable throwable )
  {
    super( detailMessage, throwable );
  }

  public PluginInvocationException( Throwable throwable )
  {
    super( throwable );
  }
}
