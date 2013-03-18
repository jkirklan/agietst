/**
 * 
 */
package com.redhat.agie.services;

import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bashburn
 * 
 */
@Path("/agie-test-poller")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgieTestPollerService {
  private static final Logger logger = LoggerFactory.getLogger(AgieTestPollerService.class);

  private AgieTestLocationUpdate lastLocation = new AgieTestLocationUpdate(0f, 0f);
  
  @Inject
  private AgieTestLocationRepository repository;
  
  @Inject
  private QpidConnectionManager connectionManager;
  
  @GET
  @Path("/register")
  public ServiceResponse register() {
    String id = repository.genId();
    repository.store(id, lastLocation);
    return new ServiceResponse(id);
  }
  
  @Path("/push")
  @POST
  public AgieTestLocationUpdate pushMessage(AgieTestLocationUpdate loc) {
    try {
      ConnectionFactory connFactory = connectionManager.lookupConnectionFactory();
      Destination dest = connectionManager.lookupDestination("directExchange");
      Connection conn = connFactory.createConnection();
      try {
        conn.start();
        Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer prod = sess.createProducer(dest);
        BytesMessage msg = sess.createBytesMessage();
        msg.writeBytes(loc.toJson().getBytes());
        prod.send(msg);
      } finally {
        conn.close();
      }
    } catch(Exception e) {
      logger.error(e.getMessage());
    }
    return loc;
  }

  @GET
  @Path("/{id}")
  public AgieTestPollResult execute(@PathParam("id") String id) {
    AgieTestPollResult result = new AgieTestPollResult();
    try {
      ConnectionFactory connFactory = connectionManager.lookupConnectionFactory();
      Destination dest = connectionManager.lookupDestination("directExchange");
      Connection conn = connFactory.createConnection();
      try {
        conn.start();
        Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer cons = sess.createConsumer(dest);
        BytesMessage msg = (BytesMessage)cons.receiveNoWait();
        while(msg != null) {
          try {
            byte[] bytes = new byte[new Long(msg.getBodyLength()).intValue()];
            msg.readBytes(bytes);
            this.lastLocation = AgieTestLocationUpdate.fromJson(new String(bytes)).transformToMap();
            repository.store(id, lastLocation);
            result.addMessage(this.lastLocation);
          } catch(Exception e) {
            logger.error("ERROR", e);
          }
          msg = (BytesMessage)cons.receiveNoWait();
        }
        if(result.getMessages().size() == 0) {
          result.getMessages().add(this.repository.retrieve(id));
        }
      } finally {
        conn.close();
      }
    } catch(Exception e) {
      logger.error("ERROR", e);
      result.setError(true);
      result.setErrorMessage(e.getMessage());
    }
    return result;
  }
  
  /**
   * @return the lastLocation
   */
  public AgieTestLocationUpdate getLastLocation() {
    return lastLocation;
  }
}
