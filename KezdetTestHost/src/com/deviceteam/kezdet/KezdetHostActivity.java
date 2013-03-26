package com.deviceteam.kezdet;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.host.PluginManager;
import com.deviceteam.kezdet.interfaces.IPluginCallback;
import com.deviceteam.kezdet.interfaces.exception.BadPluginException;
import com.google.gson.Gson;

public class KezdetHostActivity extends Activity
{
  public static String TAG = "KezdetHostActivity";
  private Gson _gson = new Gson();

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_kezdet_host );

    Context context = getApplicationContext();

    PluginManager _manager = new PluginManager( );
    InputStream is = null;
    
    try
    {
      is = context.getAssets().open( "certificates/kezdet-public.cer" );
      _manager.init( context, this, KezdetHostActivity.class.getClassLoader(), is );
      is.close();
      
      is = context.getAssets().open( "KezdetTestDex.jar" );
      int pluginId = _manager.getNextPluginId();
      
      _manager.loadPlugin( pluginId, is, "com.deviceteam.kezdet.plugin.KezdetPlugin", new IPluginCallback()
      {
        @Override
        public void onPluginCallback( String message, String param )
        {
          DisplayResult( message, param );
        }
      } );
      is.close();
 
      String battLevel = _manager.invokePluginMethod( pluginId, "batteryLevel", "1000000" );
      Log.d( TAG, "Battery Level is: " + battLevel );
      
      is = context.getAssets().open( "KezdetURLLoaderE.jar" );
      
      pluginId = _manager.getNextPluginId();
      _manager.loadPlugin( pluginId, is, "com.deviceteam.networking.URLLoaderPlugin", new IPluginCallback()
      {
        @Override
        public void onPluginCallback( String message, String param )
        {
          DisplayResult( message, param );
        }
      } );
      is.close();


      _manager.invokePluginMethod( pluginId, "init", _gson.toJson( Integer.valueOf( 1 ) ) );
      
      _manager.invokePluginMethod( pluginId, "load", "{'contentType':null,'followRedirects':true,'userAgent':'Mozilla/5.0 (Windows; U; en-US) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.4','data':null,'manageCookies':true,'requestHeaders':[],'method':'GET','cacheResponse':true,'idleTimeout':0,'authenticate':true,'url':'http://nanodroid.gameassists.co.uk/apks/saucissontest.apk','useCache':true,'digest':null}" );
      
      
    }
    catch( PluginVerifyException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( IOException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( PluginLoadException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( PluginCreateException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( NoSuchMethodException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( IndexOutOfBoundsException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( BadPluginException e )
    {
      Log.e( TAG, e.toString() );
    }
    finally
    {
      if( is != null )
      {
        try
        {
          is.close();
        }
        catch( IOException e )
        {
        }
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    getMenuInflater().inflate( R.menu.activity_kezdet_host, menu );
    return true;
  }

  public void DisplayResult( String arg0, String arg1 )
  {
    TextView txt1 = (TextView) findViewById( R.id.txtUpdate1 );
    txt1.setText( arg0 );
    TextView txt2 = (TextView) findViewById( R.id.txtUpdate2 );
    txt2.setText( arg1 );
  }

}
