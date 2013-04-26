package com.deviceteam.kezdet.anehost;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.UUID;

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
  private Context _context;
  private Boolean _inAssets = false;
  private Hashtable< String, UUID > _fileIdMap = new Hashtable< String, UUID >(); 
  
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

  public void initManager( Context context, Activity activity, String certPath, String jarLoction ) throws PluginVerifyException, IOException
  {
    _context = context;
    if( jarLoction.startsWith( "app:/" ) )
    {
      _inAssets = true;
      _jarLocation = jarLoction.replace( "app:/", "" );
    }
    else if( jarLoction.startsWith( "file://" ) )
    {
      _jarLocation = jarLoction.replace( "file://", "" );
    }
    else
    {
      _jarLocation = jarLoction;
    }
    ClassLoader parentClassloader = KezdetANEHost.class.getClassLoader();
    InputStream certificateStream = null;
    try
    {
      certificateStream = context.getAssets().open( certPath );
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

  
  public int loadPlugin( FREContext freContext, String pluginFile, String pluginClassName ) throws PluginLoadException, PluginVerifyException, PluginCreateException
  {
    InputStream jarStream = null;
    String path = combinePath( _jarLocation,  pluginFile );
    try
    {
      UUID containerId = null;
      if( !_fileIdMap.containsKey( pluginFile ) )
      {
        if( _inAssets )
        {
          jarStream = _context.getAssets().open( path );
        }
        else
        {
          jarStream = new FileInputStream( path );
        }
        containerId = _manager.registerContainer( jarStream );
      }
      else
      {
        containerId = _fileIdMap.get( pluginFile );
      }
      final FREContext ctx = freContext;
      final int id = _manager.loadPlugin( containerId, pluginClassName );
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
    catch( IOException e )
    {
      throw new PluginLoadException( "Unable to load plugin: " + path + " from( " + (_inAssets ? "assets" : "file system") + " )", e );
    }
    finally
    {
      if( jarStream != null )
      {
        try
        {
          jarStream.close();
        }
        catch( IOException e )
        {
        }
      }
    }
  }
}
