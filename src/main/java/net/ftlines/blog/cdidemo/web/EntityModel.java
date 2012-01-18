package net.ftlines.blog.cdidemo.web;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.ftlines.wicket.cdi.CdiContainer;

import org.apache.wicket.model.IModel;

public class EntityModel<T> implements IModel<T> {

  @Inject
  private EntityManager em;

  private Object id;
  private Class<?> type;

  private transient T entity;

  public EntityModel(Class<T> type, Object id) {
    CdiContainer.get().getNonContextualManager().inject(this);
    this.type = type;
    this.id = id;
  }

  public EntityModel(T entity) {
    CdiContainer.get().getNonContextualManager().inject(this);
    setObject(entity);
  }

  public T getObject() {
    if (entity == null && id != null) {
      entity = (T) em.find(type, id);
    }
    return entity;
  }

  public final void setObject(T other) {
    type = other.getClass();
    id = (Serializable) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(other);
    entity = other;
  }

  public void detach() {
    entity = null;
  }

}
