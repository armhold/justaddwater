package net.ftlines.blog.cdidemo.cdi;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.ftlines.wicket.cdi.NonContextual;

import org.jboss.weld.environment.servlet.Listener;

/**
 * NB: this file is derived from the open-source project hosted at https://github.com/42Lines/blog-cdidemo
 */
public class ApplicationLifecycleNotifier implements ServletContextListener {

  @Inject
  Event<ApplicationStartedEvent> started;

  public void contextInitialized(ServletContextEvent sce) {
    BeanManager manager = (BeanManager) sce.getServletContext().getAttribute(Listener.BEAN_MANAGER_ATTRIBUTE_NAME);
    NonContextual.of(getClass(), manager).inject(this);
    started.fire(new ApplicationStartedEvent());
  }

  public void contextDestroyed(ServletContextEvent sce) {
  }

}
