package com.deviceteam.kezdet.anehost;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.deviceteam.kezdet.anehost.utils.PluginInfo;
import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.helpers.KezdetInterfaceMap;
import com.deviceteam.kezdet.host.PluginManager;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.deviceteam.kezdet.interfaces.IPluginCallback;


public class KezdetANEHost implements FREExtension
{
  public static final String TAG = "KezdetANEHost";
  private PluginManager _manager;
  private ArrayList< PluginInfo > _pluginList  = new ArrayList< PluginInfo >();
  private String _jarLocation;
  

  @Override
  public FREContext createContext(String arg)
  {
    Log.d(TAG, "createContext()");
    return( new KezdetANEHostContext( this ) );
  }

  @Override
  public void initialize()
  {
    Log.d(TAG, "initialise()");
    _manager = new PluginManager();
  }

  @Override
  public void dispose()
  {
    Log.d(TAG, "dispose()");
    _manager.dispose();
  }

  public void initManager( Context context, Activity activity, String jarLoction ) throws PluginVerifyException, IOException
  {
    _jarLocation = jarLoction;
    ClassLoader parentClassloader = KezdetANEHost.class.getClassLoader();
    InputStream certificateStream = context.getAssets().open( "certificates/kezdet-public.cer" );
    _manager.init( context, activity, parentClassloader, certificateStream );
  }
  
  private String combinePath( String path1, String path2 )
  {
    File file1 = new File( path1 );
    File file2 = new File( file1, path2 );
    return( file2.getPath() );
  }

  public int loadPlugin( FREContext freContext, String pluginFile, String pluginClassName ) throws FileNotFoundException, PluginLoadException, PluginVerifyException, PluginCreateException
  {
    FileInputStream fis = new FileInputStream( combinePath( _jarLocation,  pluginFile ) );
    final int id = _pluginList.size() + 1;
    final FREContext ctx = freContext;
    IPlugin plugin = _manager.loadPlugin( fis, pluginClassName, new IPluginCallback()
    {
      @Override
      public void onPluginCallback( String message, String param )
      {
        ctx.dispatchStatusEventAsync( Integer.toString( id ) + ":" + message, param );
      }
    } );
    
    KezdetInterfaceMap methodMap = new KezdetInterfaceMap();
    plugin.registerMethods( methodMap );
    PluginInfo info = new PluginInfo( plugin, methodMap );

    _pluginList.add( info );
    
    return( id );
  }

  public String invokePluginMethod( int pluginId, String methodName, String params ) throws IndexOutOfBoundsException, NoSuchMethodException
  {
    if( pluginId < 1 || pluginId > _pluginList.size() )
    {
      throw new IndexOutOfBoundsException("PluginId: " + pluginId + " does not exist");
    }
    Log.d( TAG, "about to invoke: " + methodName + ", with args: " + params );
    KezdetInterfaceMap methods = _pluginList.get( pluginId - 1 ).get_methods();
    return( _manager.invoke( methods, methodName, params ) );
  }

  public void clearPluginData( int pluginId ) throws IndexOutOfBoundsException
  {
    if( pluginId < 1 || pluginId > _pluginList.size() )
    {
      throw new IndexOutOfBoundsException("PluginId: " + pluginId + " does not exist");
    }
    _pluginList.get( pluginId - 1 ).get_plugin().clearResponseData();
  }

  public String getResponseType( int pluginId ) throws IndexOutOfBoundsException
  {
    if( pluginId < 1 || pluginId > _pluginList.size() )
    {
      throw new IndexOutOfBoundsException("PluginId: " + pluginId + " does not exist");
    }
    return( _pluginList.get( pluginId - 1 ).get_plugin().getReponseType().toString() );
  }
  
  public String getJSONResponse( int pluginId ) throws IndexOutOfBoundsException
  {
    if( pluginId < 1 || pluginId > _pluginList.size() )
    {
      throw new IndexOutOfBoundsException("PluginId: " + pluginId + " does not exist");
    }
    return( _pluginList.get( pluginId - 1 ).get_plugin().getJSONData() );
  }
  
  public byte[] getBinaryResponse( int pluginId ) throws IndexOutOfBoundsException
  {
    if( pluginId < 1 || pluginId > _pluginList.size() )
    {
      throw new IndexOutOfBoundsException("PluginId: " + pluginId + " does not exist");
    }
    return( _pluginList.get( pluginId - 1 ).get_plugin().getBinaryData() );
  }
}
