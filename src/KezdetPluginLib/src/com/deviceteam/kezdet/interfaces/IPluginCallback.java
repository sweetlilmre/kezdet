package com.deviceteam.kezdet.interfaces;

public interface IPluginCallback
{
  /**
   * Callback interface a plugin uses to notify the host of activity
   * @param message String value used to indicate a message 
   * @param param String value used to indicate a parameter to the message 
   */
  void onPluginCallback(String message, String param);
}
