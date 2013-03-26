package com.deviceteam.kezdet.anehost.utils;

public enum HostResponseValues
{
  OK(0),
  UnknownError(1),
  CertificateVerifyError(2),
  CertificateLoadError(3),
  PluginLoadError(4),
  PluginVerificationError(5),
  PluginCreateError(6),
  InvalidPluginId(7),
  NoSuchMethod(8),
  UnsupportedOperation(9),
  BadPlugin(10);

  private final int _value;

  public int get_value()
  {
    return( _value );
  }
  
  HostResponseValues( int value )
  {
    _value = value;
  }
}
