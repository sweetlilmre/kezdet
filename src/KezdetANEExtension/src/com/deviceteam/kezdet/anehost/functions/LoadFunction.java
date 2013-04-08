package com.deviceteam.kezdet.anehost.functions;


import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;
import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;

public class LoadFunction extends BaseFunction implements FREFunction
{
  public LoadFunction( KezdetANEHost loader )
  {
    super( loader, "KezdetANEHost-LoadFunction" );
  }

  @Override
  public FREObject call( FREContext ctx, FREObject[] args )
  {
    int pluginId = -1;
    HostResponseValues returnCode = HostResponseValues.UnknownError;
    try
    {
      String pluginFile = args[0].getAsString();
      String pluginName = args[1].getAsString();
      pluginId = _host.loadPlugin( ctx, pluginFile, pluginName );
      returnCode = HostResponseValues.OK;
    }
    catch( PluginLoadException e )
    {
      returnCode = HostResponseValues.PluginLoadError;
      LogE( "plugin load failed: " + e );
    }
    catch( PluginVerifyException e )
    {
      returnCode = HostResponseValues.PluginVerificationError;
      LogE( "verification failed: " + e );
    }
    catch( PluginCreateException e )
    {
      returnCode = HostResponseValues.PluginCreateError;
      LogE( "plugin creation failed: " + e );
    }
    catch( Exception e )
    {
      LogE( "unable to load plugin: " + e );
    }
    
    return( GenerateReturnObject( returnCode, pluginId ) );
  }
}
