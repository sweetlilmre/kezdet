package com.deviceteam.kezdet.airhost
{
  public interface IPluginCallback
  {
    function handleEvent( message : String, param : String ) : void;
  }
}