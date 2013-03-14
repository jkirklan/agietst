/**
 * 
 */
package com.redhat.agie.services;

import java.util.HashMap;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author bashburn
 *
 */
@ApplicationScoped
public class AgieTestLocationRepository {
  private final HashMap<String, AgieTestLocationUpdate> lastLocations = new HashMap<String, AgieTestLocationUpdate>();
  
  public String genId() { return UUID.randomUUID().toString(); }
  
  public void store(String id, AgieTestLocationUpdate location) {
    this.lastLocations.put(id, location);
  }
  public AgieTestLocationUpdate retrieve(String id) { return this.lastLocations.get(id); }
}
