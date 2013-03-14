/**
 * 
 */
package com.redhat.agie.services;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.ManagedBean;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author bashburn
 *
 */
@ManagedBean
public class QpidConnectionManager {
  public ConnectionFactory lookupConnectionFactory() throws IOException, NamingException {
    Properties props = loadProps();
    Context ctx = new InitialContext(props);
    return (ConnectionFactory)ctx.lookup("qpidConnectionFactory");
  }
  /**
   * @return
   * @throws IOException
   */
  private Properties loadProps() throws IOException {
    Properties props = new Properties();
    props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("qpid-jndi.properties"));
    return props;
  }
  public Destination lookupDestination(String jndiName) throws IOException, NamingException {
    Properties props = loadProps();
    Context ctx = new InitialContext(props);
    return (Destination)ctx.lookup("directExchange");
  }
}
