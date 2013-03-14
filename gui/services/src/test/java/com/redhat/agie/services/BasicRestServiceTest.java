package com.redhat.agie.services;

import static org.junit.Assert.*;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.agie.services.rest.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
public class BasicRestServiceTest {
  @Deployment(testable = false)
  public static Archive<?> createTestArchive() {
    return ShrinkWrap.create(WebArchive.class, "services-test.war")
        .addClasses(JaxRsActivator.class, BasicRestService.class)
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }
  @ArquillianResource
  URL deploymentUrl;
  
  @Test
  public void execute() throws Exception {
    String baseUrl = deploymentUrl.toString() + "rest/";
    ClientRequest request = new ClientRequest(baseUrl + "basic/");
    ClientResponse<String> response = request.get(String.class);
    assertTrue(response.getStatus() == 200);
    assertTrue("SUCCESS".equals(response.getEntity()));
  }
}
