/**
 * 
 */
package com.redhat.agie.services;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author bashburn
 *
 */
@XmlRootElement
public class AgieTestPollResult {
  @JsonProperty("error")
  private boolean error = false;
  @JsonProperty("errorMessage")
  private String errorMessage;
  @JsonProperty("messages")
  private ArrayList<AgieTestLocationUpdate> messages = new ArrayList<AgieTestLocationUpdate>();

  /**
   * @param message
   */
  public void setErrorMessage(String message) {
    this.errorMessage = message;
  }
  /**
   * @param error the error to set
   */
  public void setError(boolean error) {
    this.error = error;
  }
  
  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }
  /**
   * @return the error
   */
  public boolean isError() {
    return error;
  }

  /**
   * @param agieTestLocationUpdate
   */
  public void addMessage(AgieTestLocationUpdate agieTestLocationUpdate) {
    this.messages.add(agieTestLocationUpdate);
  }
  
  /**
   * @return the messages
   */
  public ArrayList<AgieTestLocationUpdate> getMessages() {
    return messages;
  }
  /**
   * @param messages the messages to set
   */
  public void setMessages(ArrayList<AgieTestLocationUpdate> messages) {
    this.messages = messages;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("AgieTestPollResult[\n");
    buf.append("error[").append(error).append("]\n");
    buf.append("errorMessage[").append(errorMessage).append("]\n");
    buf.append("messagesSize[").append(messages.size()).append("]\n");
    buf.append("]");
    return buf.toString();
  }
}
