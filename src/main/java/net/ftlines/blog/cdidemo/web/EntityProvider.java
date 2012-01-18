package net.ftlines.blog.cdidemo.web;

import net.ftlines.wicket.cdi.CdiContainer;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

public abstract class EntityProvider<T> extends SortableDataProvider<T> {

  public EntityProvider() {
    CdiContainer.get().getNonContextualManager().inject(this);
  }

  public IModel<T> model(T entity) {
    return new EntityModel<T>(entity);
  }

}
