package net.ftlines.blog.cdidemo.cdi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ConversationScoped;

/**
 * NB: this file is derived from the open-source project hosted at https://github.com/42Lines/blog-cdidemo
 */
@ConversationScoped
public class ConversationStore implements Serializable {
  private Map<UUID, Object> store = new HashMap<UUID, Object>();

  public UUID put(Object value) {
    UUID key = UUID.randomUUID();
    store.put(key, value);
    return key;
  }

  public Object get(UUID key) {
    return store.get(key);
  }

  public Object remove(UUID key) {
    return store.remove(key);
  }
}
