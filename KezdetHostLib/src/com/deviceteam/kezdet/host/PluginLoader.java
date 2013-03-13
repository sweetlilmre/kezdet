package com.deviceteam.kezdet.host;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.jar.JarFile;

import org.whipplugin.data.bundle.JarVerifier;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.google.dexmaker.AppDataDirGuesser;

public class PluginLoader
{
  private X509Certificate _verificationCert;
  private ClassLoader _parentLoader;

  /**
   * PluginLoader constructor
   * @param parentLoader Classloader object to use as a loader context
   * @param verificationCert public X509 certificate used to verify that the plugin has been correctly signed
   */
  public PluginLoader( ClassLoader parentLoader, X509Certificate verificationCert )
  {
    _parentLoader = parentLoader;
    _verificationCert = verificationCert;
  }

  private void CopyStream( InputStream is, OutputStream os ) throws IOException
  {
    byte[] b = new byte[1024];
    int noOfBytes = 0;

    while( ( noOfBytes = is.read( b ) ) != -1 )
    {
      os.write( b, 0, noOfBytes );
    }
    os.close();
  }

  /**
   * Loads a plugin
   * @param pluginJarStream stream encapsulating the JAR file that contains the pluign to load
   * @param className name of the class in the JAR file that implements IPlugin
   * @return an IPlugin instance if the plugin is successfully loaded
   * @throws PluginLoadException if the plugin cannot be loaded
   * @throws PluginVerifyException if the plugin verification does not pass (incorrectly signed or cryptographic failure)
   * @throws PluginCreateException if the plugin class could not be created (incorrect name or other error)
   */
  public IPlugin loadPlugin( InputStream pluginJarStream, String className ) throws PluginLoadException, PluginVerifyException, PluginCreateException
  {
    try
    {
      File dexCache = new AppDataDirGuesser().guess();
      File result = File.createTempFile( "Generated", ".jar", dexCache );
      result.deleteOnExit();

      FileOutputStream fos = new FileOutputStream( result );

      CopyStream( pluginJarStream, fos );
      JarFile jf = new JarFile( result );
      JarVerifier.verify( jf, new X509Certificate[] { _verificationCert } );

      ClassLoader cl = (ClassLoader) Class.forName( "dalvik.system.DexClassLoader" )
          .getConstructor( String.class, String.class, String.class, ClassLoader.class )
          .newInstance( result.getPath(), dexCache.getAbsolutePath(), null, _parentLoader );

      Class< ? > pluginClass = cl.loadClass( className );
      IPlugin plugin = (IPlugin) pluginClass.newInstance();
      return( plugin );

    }
    catch( IOException e )
    {
      throw new PluginLoadException( String.format( "Failed to load plugin for: %s", className ), e );
    }
    catch( CertificateException e )
    {
      throw new PluginVerifyException( String.format( "Certificate issue while verifying plugin for: %s", className ), e );
    }
    catch( SecurityException e )
    {
      throw new PluginVerifyException( String.format( "Invalid JAR while verifying plugin for: %s", className ), e );
    }
    catch( ClassNotFoundException e )
    {
      throw new PluginCreateException( String.format( "Cannot find class %s in plugin", className ), e );
    }
    catch( InstantiationException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in plugin", className ), e );
    }
    catch( IllegalArgumentException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in plugin", className ), e );
    }
    catch( IllegalAccessException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in plugin", className ), e );
    }
    catch( InvocationTargetException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in plugin", className ), e );
    }
    catch( NoSuchMethodException e )
    {
      throw new PluginCreateException( String.format( "Cannot find method in class %s in plugin", className ), e );
    }
  }
}
