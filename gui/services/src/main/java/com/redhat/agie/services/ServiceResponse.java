/**
 * 
 */
package com.redhat.agie.services;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author bashburn
 *
 */
@XmlRootElement
public class ServiceResponse {
  @JsonProperty("message")
  private String message;
  
  public ServiceResponse() {}
  public ServiceResponse(String message) {
    this.message = message;
  }
  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }
  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
