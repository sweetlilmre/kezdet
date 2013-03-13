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

  public String invoke( Hashtable< String, IInvokeMethod > methods, String methodName, String jsonArgs ) throws NoSuchMethodException
  {
    if( methods.containsKey( methodName ) )
    {
      return( methods.get( methodName ).invoke( jsonArgs ) );
    }
    throw( new NoSuchMethodException( "No such method: " + methodName ) );
  }

  private Hashtable< String, IInvokeMethod > loadPlugin( InputStream jarStream, String pluginName, IPluginCallback callback )
  {
    IPlugin plugin;
    try
    {
      plugin = _loader.loadPlugin( jarStream, pluginName );
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

    Context context = getApplicationContext();
    X509Certificate verificationCert = null;
    
    try
    {
      InputStream is = context.getAssets().open( "kezdet-public.cer" );
      CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
      verificationCert = (X509Certificate) cf.generateCertificate( is );      
      is.close();
    }
    catch( IOException e )
    {
      Log.e( TAG, e.toString() );
      return;
    }
    catch( CertificateException e )
    {
      Log.e( TAG, e.toString() );
      return;
    }
    
    _loader = new PluginLoader( KezdetHostActivity.class.getClassLoader(), verificationCert );
    InputStream is = null;
    try
    {
      is = context.getAssets().open( "KezdetTestDex.jar" );
      _batteryMethods = loadPlugin( is, "com.deviceteam.kezdet.plugin.KezdetPlugin", new IPluginCallback()
      {
        @Override
        public void onPluginCallback( String message, String param )
        {
          DisplayResult( message, param );
        }
      } );
    }
    catch( IOException e )
    {
      Log.e( TAG, e.toString() );
      return;
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

    try
    {
      invoke( _batteryMethods, "batteryLevel", "1000000" );
    }
    catch( Exception e )
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
