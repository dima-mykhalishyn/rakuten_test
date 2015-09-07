package org.test.project.guice;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.test.project.support.GuiceInstanceHolder;

/**
 * Guice Servlet configuration
 * @author dmikhalishin@provectus-it.com
 * @see GuiceServletContextListener
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    /**
     * {@inheritDoc}
     * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
     */
    @Override
    protected Injector getInjector() {
        return GuiceInstanceHolder.getInjector();
    }

}
