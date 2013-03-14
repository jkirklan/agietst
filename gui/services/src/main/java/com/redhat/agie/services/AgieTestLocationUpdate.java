/**
 * 
 */
package com.redhat.agie.services;

import java.io.StringWriter;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author bashburn
 *
 */
@XmlRootElement
public class AgieTestLocationUpdate {
  @JsonProperty("x")
  private float x;
  @JsonProperty("y")
  private float y;
  
  public AgieTestLocationUpdate() {}
  public AgieTestLocationUpdate(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  /**
   * @return the x
   */
  public float getX() {
    return x;
  }
  /**
   * @param x the x to set
   */
  public void setX(float x) {
    this.x = x;
  }
  /**
   * @return the y
   */
  public float getY() {
    return y;
  }
  /**
   * @param y the y to set
   */
  public void setY(float y) {
    this.y = y;
  }
  
  /**
   * @return
   * @throws Exception 
   */
  public String toJson() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, this);
    return writer.toString();
  }
  
  /**
   * Location Notes:
   * 34L - x:123, y:395 - relative: x:7,   y:1
   * 34C - x:190, y:433 - relative: x:5.5, y:6
   * 34R - x:219, y:530 - relative: x:2,   y:8
   * 
   * 16R - x:123, y:70  - relative: x:7,   y:24.8
   * 16C - x:190, y:70  - relative: x:5.5, y:24.8
   * 16L - x:219, y:70  - relative: x:2,   y:24.8
   * 
   * f(x) = x'
   * g(y) = y'
   * 
   * f(123) = 7
   * f(190) = 5.5
   * f(219) = 2
   * 
   * g(70)  = 24.8
   * g(395) = 1
   * g(433) = 6
   * g(530) = 8
   * 
   * @return
   */
  public AgieTestLocationUpdate transformToMap() {
    float newX = (this.x * 19) + 84f;
    float newY = 550 - (this.y * 19);
    return new AgieTestLocationUpdate(newX, newY);
  }
  
  
  public static AgieTestLocationUpdate fromJson(String json) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(json, AgieTestLocationUpdate.class);
  }

}
