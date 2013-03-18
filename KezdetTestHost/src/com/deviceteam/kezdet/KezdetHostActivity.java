package com.deviceteam.kezdet;

import java.io.IOException;
import java.io.InputStream;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.helpers.KezdetInterfaceMap;
import com.deviceteam.kezdet.host.PluginLoader;
import com.deviceteam.kezdet.host.PluginManager;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.deviceteam.kezdet.interfaces.IPluginCallback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class KezdetHostActivity extends Activity
{

  public static String TAG = "KezdetHostActivity";

  KezdetInterfaceMap _batteryMethods = new KezdetInterfaceMap();

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_kezdet_host );

    Context context = getApplicationContext();

    PluginManager _manager = new PluginManager( );
    
    try
    {
      InputStream is = context.getAssets().open( "certificates/kezdet-public.cer" );
      _manager.init( context, this, KezdetHostActivity.class.getClassLoader(), is );
      
      is = context.getAssets().open( "KezdetTestDex.jar" );
      IPlugin plugin = _manager.loadPlugin( is, "com.deviceteam.kezdet.plugin.KezdetPlugin", new IPluginCallback()
      {
        @Override
        public void onPluginCallback( String message, String param )
        {
          DisplayResult( message, param );
        }
      } );
 
      KezdetInterfaceMap _batteryMethods = new KezdetInterfaceMap();
      plugin.registerMethods( _batteryMethods );
      
      _manager.invoke( _batteryMethods, "batteryLevel", "1000000" );
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
