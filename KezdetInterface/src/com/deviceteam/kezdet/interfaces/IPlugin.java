package com.deviceteam.kezdet.interfaces;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;

public abstract interface IPlugin
{
  enum EventDataResponseType
  {
    NONE,
    BINARY,
    JSON
  };

  void initialise(Context context, Activity activity, IPluginCallback callback);
  void registerMethods( Hashtable< String, IInvokeMethod > methods );
  void dispose();

  EventDataResponseType getReponseType(); 
  byte[] getBinaryData() throws UnsupportedOperationException;
  String getJSONData() throws UnsupportedOperationException;
}
