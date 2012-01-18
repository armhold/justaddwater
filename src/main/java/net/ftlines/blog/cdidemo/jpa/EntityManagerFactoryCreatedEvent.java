package net.ftlines.blog.cdidemo.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * NB: this file is derived from the open-source project hosted at https://github.com/42Lines/blog-cdidemo
 */
public class EntityManagerFactoryCreatedEvent {
  private final EntityManagerFactory emf;

  public EntityManagerFactoryCreatedEvent(EntityManagerFactory emf) {
    this.emf = emf;
  }

  public EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }

}
