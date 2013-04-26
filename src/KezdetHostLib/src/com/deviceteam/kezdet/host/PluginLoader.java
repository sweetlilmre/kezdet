package com.deviceteam.kezdet.host;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.UUID;
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
  private Hashtable< UUID, ClassLoader > _loaderMap = new Hashtable< UUID, ClassLoader >();

  /**
   * PluginLoader constructor
   * @param parentLoader ClassLoader object to use as a loader context
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
  }
  

  /**
   * 
   * @param containerStream stream encapsulating the JAR file that contains the plugin to load, the stream will be closed by this method
   * @return UUID as a unique key identifier for the plugin container, pass this to {@link #loadPlugin}
   * @throws PluginLoadException if the plugin container cannot be loaded
   * @throws PluginVerifyException if the plugin container verification does not pass (incorrectly signed or cryptographic failure)
   * @throws PluginCreateException if the plugin ClassLoader could not be created
   */
  public UUID registerContainer( InputStream containerStream ) throws PluginLoadException, PluginVerifyException, PluginCreateException
  {
    try
    {
      File dexCache = new AppDataDirGuesser().guess();
      File result = File.createTempFile( "Generated", ".jar", dexCache );
      result.deleteOnExit();

      FileOutputStream fos = new FileOutputStream( result );
      CopyStream( containerStream, fos );
      fos.close();
      
      JarFile jf = new JarFile( result );
      JarVerifier.verify( jf, new X509Certificate[] { _verificationCert } );
      
      ClassLoader cl = (ClassLoader) Class.forName( "dalvik.system.DexClassLoader" )
          .getConstructor( String.class, String.class, String.class, ClassLoader.class )
          .newInstance( result.getPath(), dexCache.getAbsolutePath(), null, _parentLoader );
      
      UUID key = UUID.randomUUID();
      _loaderMap.put( key, cl );
      return( key );
    }
    catch( IOException e )
    {
      throw new PluginLoadException( String.format( "Failed to load plugin container" ), e );
    }
    catch( CertificateException e )
    {
      throw new PluginVerifyException( String.format( "Certificate issue while verifying plugin container" ), e );
    }
    catch( SecurityException e )
    {
      throw new PluginVerifyException( String.format( "Invalid JAR while verifying plugin container" ), e );
    }
    catch( ClassNotFoundException e )
    {
      throw new PluginCreateException( String.format( "Cannot find dalvik.system.DexClassLoader!" ), e );
    }
    catch( NoSuchMethodException e )
    {
      throw new PluginCreateException( String.format( "Cannot construct ClassLoader for plugin container" ), e );
    }
    catch( IllegalArgumentException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate ClassLoader in plugin container" ), e );
    }
    catch( IllegalAccessException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate ClassLoader in plugin container" ), e );
    }
    catch( InvocationTargetException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate ClassLoader in plugin container" ), e );
    }
    catch( InstantiationException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate ClassLoader in plugin container" ), e );
    }
  }
  
  /**
   * Loads a plugin from a container
   * @param containerId UUID representing the registered plugin container, see: {@link #registerContainer(InputStream)}
   * @param className name of the class in the JAR file that implements IPlugin
   * @return an IPlugin instance if the plugin is successfully loaded
   * @throws PluginLoadException if the plugin cannot be loaded
   * @throws PluginVerifyException if the plugin verification does not pass (incorrectly signed or cryptographic failure)
   * @throws PluginCreateException if the plugin class could not be created (incorrect name or other error)
   */
  public IPlugin loadPlugin( UUID containerId, String className ) throws PluginCreateException, PluginLoadException
  {
    try
    {
      ClassLoader cl = _loaderMap.get( containerId );
      if( cl == null )
      {
        throw new PluginLoadException( String.format( "Invalid plugin container: %s", containerId.toString() ) );
      }

      Class< ? > pluginClass = cl.loadClass( className );
      IPlugin plugin = (IPlugin) pluginClass.newInstance();
      return( plugin );
    }
    catch( SecurityException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in plugin", className ), e );
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
  }
}
