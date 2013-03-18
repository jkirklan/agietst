/**
 * 
 */
package com.redhat.agie.services;

import static org.junit.Assert.*;

import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.agie.services.rest.JaxRsActivator;

/**
 * @author bashburn
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AgieTestPollerServiceTest {
  @Deployment(testable = false)
  public static Archive<?> createTestArchive() {
    MavenDependencyResolver mvnResolver = DependencyResolvers.use(MavenDependencyResolver.class)
        .loadMetadataFromPom("pom.xml");
    return ShrinkWrap.create(WebArchive.class, "services-test.war")
        .addClass(JaxRsActivator.class)
        .addClasses(AgieTestLocationUpdate.class, 
            AgieTestPollerService.class, 
            AgieTestPollResult.class, 
            AgieTestLocationRepository.class,
            QpidConnectionManager.class,
            ServiceResponse.class)
        .addAsResource("qpid-jndi.properties")
        .addAsLibraries(mvnResolver.artifact("org.jboss.resteasy:resteasy-jackson-provider").resolveAsFiles())
        .addAsLibraries(mvnResolver.artifact("org.jboss.resteasy:resteasy-jaxb-provider").resolveAsFiles())
        .addAsLibraries(mvnResolver.artifact("com.redhat.mrgm:qpid-client").resolveAsFiles())
        .addAsLibraries(mvnResolver.artifact("com.redhat.mrgm:qpid-common").resolveAsFiles())
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }
  @ArquillianResource
  URL deploymentUrl;
  
  @Test
  public void execute() throws Exception {
    String baseUrl = deploymentUrl.toString() + "rest/";
    RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    AgieTestPollerServiceInterface service = ProxyFactory.create(AgieTestPollerServiceInterface.class, baseUrl);
    seedMessages(service);
    ClientResponse<String> regResponse = service.register();
    assertTrue(regResponse.getStatus() == 200);
    String id = regResponse.getEntity();
    ClientResponse<AgieTestPollResult> response = service.execute(id);
    assertTrue(response.getStatus() == 200);
    AgieTestPollResult result = response.getEntity();
    assertFalse(result.isError());
    assertTrue(result.getMessages().size() >= 1);
    response = service.execute(id);
    assertTrue(response.getStatus() == 200);
    result = response.getEntity();
    assertFalse(result.isError());
    assertTrue(result.getMessages().size() == 1);
    assertTrue(Float.toString(result.getMessages().get(0).getY()), result.getMessages().get(0).getY() > 1.0f);
  }

  private void seedMessages(AgieTestPollerServiceInterface service) {
    service.pushMessage(new AgieTestLocationUpdate(0, 0));
    service.pushMessage(new AgieTestLocationUpdate(0, 2));
  }

  @Path("/agie-test-poller")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  private interface AgieTestPollerServiceInterface {
    @GET
    @Path("/register")
    public ClientResponse<String> register();
    @GET
    @Path("/{id}")
    public ClientResponse<AgieTestPollResult> execute(@PathParam("id") String id);
    @Path("/push")
    @POST
    public AgieTestLocationUpdate pushMessage(AgieTestLocationUpdate loc);
  }
}
