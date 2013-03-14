package com.deviceteam.kezdet;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.host.PluginLoader;
import com.deviceteam.kezdet.host.PluginManager;
import com.deviceteam.kezdet.interfaces.IInvokeMethod;
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

  private PluginLoader _loader;

  Hashtable< String, IInvokeMethod > _batteryMethods = new Hashtable< String, IInvokeMethod >();

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_kezdet_host );

    Context context = getApplicationContext();

    PluginManager _manager = new PluginManager( context, this );
    
    try
    {
      InputStream is = context.getAssets().open( "kezdet-public.cer" );
      _manager.init( KezdetHostActivity.class.getClassLoader(), is );
      
      is = context.getAssets().open( "KezdetTestDex.jar" );
      _batteryMethods = _manager.loadPlugin( is, "com.deviceteam.kezdet.plugin.KezdetPlugin", new IPluginCallback()
      {
        @Override
        public void onPluginCallback( String message, String param )
        {
          DisplayResult( message, param );
        }
      } );
      
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
