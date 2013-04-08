package com.deviceteam.kezdet.interfaces;

public interface IInvokeMethod
{
  /**
   * Standard plugin method signature
   * @param jsonArgs parameters passed to the method in JSON object format
   * @return a string representing a JSON object as return value 
   */
  String invoke( String jsonArgs );
}
