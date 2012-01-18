package net.ftlines.blog.cdidemo.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * NB: this file is derived from the open-source project hosted at https://github.com/42Lines/blog-cdidemo
 */
public class EntityMangerFactoryProducer {

  @Inject
  Event<EntityManagerFactoryCreatedEvent> created;

  @Produces
  @ApplicationScoped
  public EntityManagerFactory create() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("justaddwater");
    created.fire(new EntityManagerFactoryCreatedEvent(emf));
    return emf;
  }

  public void destroy(@Disposes EntityManagerFactory factory) {
    factory.close();
  }

}
