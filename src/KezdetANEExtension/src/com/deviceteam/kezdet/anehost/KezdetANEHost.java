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


public class KezdetANEHost implements FREExtension
{
  public static final String TAG = "KezdetANEHost";
  private PluginManager _manager;
  private String _jarLocation;
  
  public PluginManager get_pluginManager()
  {
    return( _manager );
  }

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
      certificateStream = context.getAssets().open( "certificates/kezdet-public-test-certificate.cer" );
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
      final int id = _manager.loadPlugin( fis, pluginClassName );
      _manager.initPlugin( id, new IPluginCallback()
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
}
