package net.ftlines.blog.cdidemo.jpa;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * NB: this file is derived from the open-source project hosted at https://github.com/42Lines/blog-cdidemo
 */
public class EntityManagerProducer {
  @Inject
  EntityManagerFactory emf;

  @Produces
  @ConversationScoped
  public EntityManager create() {
    return emf.createEntityManager();
  }

  public void destroy(@Disposes EntityManager em) {
    em.close();
  }

}
