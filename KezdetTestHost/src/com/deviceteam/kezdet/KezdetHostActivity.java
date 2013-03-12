package com.deviceteam.kezdet;

import java.util.Hashtable;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.host.PluginLoader;
import com.deviceteam.kezdet.interfaces.IInvokeMethod;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.deviceteam.kezdet.interfaces.IPluginCallback;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class KezdetHostActivity extends Activity
{

  public static String TAG = "KezdetHostActivity";

  private PluginLoader _loader;

  Hashtable< String, IInvokeMethod > _gyroMethods = new Hashtable< String, IInvokeMethod >();

  public String invoke( Hashtable< String, IInvokeMethod > methods, String methodName, String jsonArgs ) throws NoSuchMethodException
  {
    if( methods.containsKey( methodName ) )
    {
      return( methods.get( methodName ).invoke( jsonArgs ) );
    }
    throw( new NoSuchMethodException( "No such method: " + methodName ) );
  }

  private Hashtable< String, IInvokeMethod > loadPlugin( String jarName, String pluginName, IPluginCallback callback )
  {
    IPlugin plugin;
    try
    {
      plugin = _loader.loadPlugin( jarName, pluginName );
      plugin.initialise( getApplicationContext(), this, callback );

      Hashtable< String, IInvokeMethod > methods = new Hashtable< String, IInvokeMethod >();
      plugin.registerMethods( methods );
      return( methods );
    }
    catch( PluginLoadException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( PluginVerifyException e )
    {
      Log.e( TAG, e.toString() );
    }
    catch( PluginCreateException e )
    {
      Log.e( TAG, e.toString() );
    }
    return( null );
  }

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_kezdet_host );

    _loader = new PluginLoader( getApplicationContext(), KezdetHostActivity.class.getClassLoader() );
    try
    {
      _loader.init();
    }
    catch( PluginVerifyException e )
    {
      Log.e( TAG, e.toString() );
    }

    _gyroMethods = loadPlugin( "KezdetTestDex.jar", "com.deviceteam.kezdet.plugin.KezdetPlugin", new IPluginCallback()
    {
      @Override
      public void onPluginCallback( String message, String param )
      {
        DisplayResult( message, param );
      }
    } );



    try
    {
      invoke( _gyroMethods, "start", "1000000" );
    }
    catch( Exception e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
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
    String[] strs = arg1.split( "&" );
    TextView txt1 = (TextView) findViewById( R.id.txtUpdate1 );
    txt1.setText( strs[0] );
    TextView txt2 = (TextView) findViewById( R.id.txtUpdate2 );
    txt2.setText( strs[1] );
  }

}
