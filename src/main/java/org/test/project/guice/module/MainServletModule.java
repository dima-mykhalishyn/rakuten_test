package org.test.project.guice.module;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.apache.struts.action.ActionServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * Main {@link ServletModule}
 * @author dmikhalishin@provectus-it.com
 * @see ServletModule
 */
public class MainServletModule extends ServletModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.servlet.ServletModule#configureServlets()
     */
    @Override
    protected void configureServlets() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("config", "/WEB-INF/struts/struts-config.xml");
        params.put("chainConfig","/WEB-INF/struts/chain-config.xml");
        bind(ActionServlet.class).in(Singleton.class);
        serve("*.do").with(ActionServlet.class, params);
    }
}
