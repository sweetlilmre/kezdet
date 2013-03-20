package com.deviceteam.kezdet.anehost.functions;


import java.nio.ByteBuffer;

import android.util.Log;

import com.adobe.fre.FREByteArray;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;

public class BaseFunction
{
  protected KezdetANEHost _host;
  protected final String TAG;
  
  public BaseFunction( KezdetANEHost loader, String tag )
  {
    super();
    _host = loader;
    TAG = tag;
  }

  FREObject GenerateReturnObject( HostResponseValues returnCode, String returnValue )
  {
    FREObject obj = null;
    try
    {
      obj = FREObject.newObject( "Object", null );
      obj.setProperty( "code", FREObject.newObject( returnCode.get_value() ) );
      obj.setProperty( "value", FREObject.newObject( returnValue ) );
    }
    catch( Exception e )
    {
      Log.e( TAG, "Error creating plugin return object: " + e );
    }
    return( obj );
  }

  FREObject GenerateReturnObject( HostResponseValues returnCode, int returnValue )
  {
    FREObject obj = null;
    try
    {
      obj = FREObject.newObject( "Object", null );
      obj.setProperty( "code", FREObject.newObject( returnCode.get_value() ) );
      obj.setProperty( "value", FREObject.newObject( returnValue ) );
    }
    catch( Exception e )
    {
      Log.e( TAG, "Error creating plugin return object: " + e );
    }
    return( obj );
  }
  
  FREObject GenerateReturnObject( HostResponseValues returnCode, byte[] returnValue )
  {
    FREObject obj = null;
    try
    {
      obj = FREObject.newObject( "Object", null );
      obj.setProperty( "code", FREObject.newObject( returnCode.get_value() ) );
      
      FREByteArray data = FREByteArray.newByteArray();
      data.setProperty( "length", FREObject.newObject( returnValue.length ) );
      data.acquire();
      ByteBuffer buf = data.getBytes();
      buf.put( returnValue );
      data.release();
      
      obj.setProperty( "value", data );
    }
    catch( Exception e )
    {
      Log.e( TAG, "Error creating plugin return object: " + e );
    }
    return( obj );
  }  
}
