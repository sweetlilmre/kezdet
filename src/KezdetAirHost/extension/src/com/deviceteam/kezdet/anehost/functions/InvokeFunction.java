package com.deviceteam.kezdet.anehost.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;
import com.deviceteam.kezdet.interfaces.exception.BadPluginException;

public class InvokeFunction extends BaseFunction implements FREFunction
{
  public InvokeFunction( KezdetANEHost host )
  {
    super( host, "KezdetANEHost-InvokePluginFunction" );
  }
  
  @Override
  public FREObject call( FREContext arg0, FREObject[] arg1 )
  {
    String returnValue = null;
    HostResponseValues returnCode = HostResponseValues.UnknownError; 
    
    try
    {
      int pluginId = arg1[0].getAsInt();
      String methodName = arg1[1].getAsString();
      
      String params = null;
      if( arg1[2] != null )
      {
        params = arg1[2].getAsString();
      }

      returnValue = _host.get_pluginManager().invokePluginMethod( pluginId, methodName, params );
      returnCode = HostResponseValues.OK;
    }
    catch( IndexOutOfBoundsException e)
    {
      returnCode = HostResponseValues.InvalidPluginId;
      LogE( "called on non-existant plugin: ", e );
    }
    catch( NoSuchMethodException e )
    {
      returnCode = HostResponseValues.NoSuchMethod;
      LogE( "attempt to invoke unknown method: ", e );
    }
    catch( BadPluginException e )
    {
      returnCode = HostResponseValues.BadPlugin;
      LogE( " plugin behaved badly ", e );
    }
    catch( Exception e )
    {
      LogE( "unknown exception ", e );
    }
    
    return( GenerateReturnObject( returnCode, returnValue ) );
  }

}
