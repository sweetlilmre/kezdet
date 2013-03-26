package com.deviceteam.kezdet.anehost;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.host.PluginManager;
import com.deviceteam.kezdet.interfaces.IPluginCallback;
import com.deviceteam.kezdet.interfaces.exception.BadPluginException;


public class KezdetANEHost implements FREExtension
{
  public static final String TAG = "KezdetANEHost";
  private PluginManager _manager;
  private String _jarLocation;
  

  @Override
  public FREContext createContext(String arg)
  {
    return( new KezdetANEHostContext( this ) );
  }

  @Override
  public void initialize()
  {
    _manager = new PluginManager();
  }

  @Override
  public void dispose()
  {
    _manager.dispose();
  }

  public void initManager( Context context, Activity activity, String jarLoction ) throws PluginVerifyException, IOException
  {
    _jarLocation = jarLoction;
    ClassLoader parentClassloader = KezdetANEHost.class.getClassLoader();
    InputStream certificateStream = null;
    try
    {
      certificateStream = context.getAssets().open( "certificates/kezdet-public.cer" );
      _manager.init( context, activity, parentClassloader, certificateStream );
    }
    finally
    {
      if( certificateStream != null )
      {
        certificateStream.close();
      }
    }
  }
  
  private String combinePath( String path1, String path2 )
  {
    File file1 = new File( path1 );
    File file2 = new File( file1, path2 );
    return( file2.getPath() );
  }

  
  public int loadPlugin( FREContext freContext, String pluginFile, String pluginClassName ) throws FileNotFoundException, PluginLoadException, PluginVerifyException, PluginCreateException
  {
    FileInputStream fis = null;
    try
    {
      fis = new FileInputStream( combinePath( _jarLocation,  pluginFile ) );
      final FREContext ctx = freContext;
      final int id = _manager.getNextPluginId();
      _manager.loadPlugin(id, fis, pluginClassName, new IPluginCallback()
      {
        @Override
        public void onPluginCallback( String message, String param )
        {
          ctx.dispatchStatusEventAsync( Integer.toString( id ) + ":" + message, param );
        }
      } );
      return( id );
    }
    finally
    {
      if( fis != null )
      {
        try
        {
          fis.close();
        }
        catch( IOException e )
        {
        }
      }
    }
  }

  public String invokePluginMethod( int pluginId, String methodName, String params ) throws IndexOutOfBoundsException, NoSuchMethodException, BadPluginException
  {
    return( _manager.invokePluginMethod( pluginId, methodName, params ) );
  }

  public void clearPluginData( int pluginId ) throws IndexOutOfBoundsException, BadPluginException
  {
    _manager.clearPluginData( pluginId );
  }

  public String getResponseType( int pluginId ) throws IndexOutOfBoundsException, BadPluginException
  {
    return( _manager.getResponseType( pluginId ) );
  }
  
  public String getJSONResponse( int pluginId ) throws IndexOutOfBoundsException, UnsupportedOperationException, BadPluginException
  {
    return( _manager.getJSONResponse( pluginId ) );
  }
  
  public byte[] getBinaryResponse( int pluginId ) throws IndexOutOfBoundsException, UnsupportedOperationException, BadPluginException
  {
    return( _manager.getBinaryResponse( pluginId ) );
  }
}
