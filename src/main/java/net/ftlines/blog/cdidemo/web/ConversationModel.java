package net.ftlines.blog.cdidemo.web;

import java.util.UUID;

import javax.inject.Inject;

import net.ftlines.blog.cdidemo.cdi.ConversationStore;
import net.ftlines.wicket.cdi.CdiContainer;

import org.apache.wicket.model.IModel;

public class ConversationModel<T> implements IModel<T> {

  @Inject
  ConversationStore store;

  private UUID key;
  private transient T instance;

  public ConversationModel(T instance) {
    CdiContainer.get().getNonContextualManager().inject(this);
    setObject(instance);
  }

  public T getObject() {
    if (instance == null) {
      instance = (T) store.get(key);
    }
    return instance;
  }

  public void setObject(T object) {
    if (key != null) {
      store.remove(key);
    }
    key = store.put(object);
  }

  public void detach() {
    instance = null;
  }

}
