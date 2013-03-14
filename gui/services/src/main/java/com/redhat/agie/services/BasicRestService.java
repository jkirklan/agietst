package com.redhat.agie.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/basic")
public class BasicRestService {
  @GET
  public String execute() {
    return "SUCCESS";
  }
}

