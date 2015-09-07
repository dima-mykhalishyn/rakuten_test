package org.test.project.support;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.project.guice.module.DaoModule;
import org.test.project.guice.module.MainServletModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Guice instance holder
 * @author dmikhalishin@provectus-it.com
 */
public class GuiceInstanceHolder {
    private final static Logger logger = LoggerFactory.getLogger(GuiceInstanceHolder.class);

    private static Injector injector;

    public synchronized static Injector getInjector(){
        if(injector == null) {
            initInjector();
        }
        return injector;
    }

    private static void initInjector(){
        try {
            logger.info("*******Start initialize the Guice*******************");
            final List<Module> moduleInstances = getModules();
            injector = Guice.createInjector(moduleInstances);
            logger.info("*******Guice successfully initialized*******");
        } catch (Exception e) {
            logger.error(Utils.errorToString(e));
            throw  new RuntimeException(e);
        }
    }

    private static List<Module> getModules(){
        final List<Module> moduleInstances = new ArrayList<Module>();
        moduleInstances.add(new MainServletModule());
        moduleInstances.add(new DaoModule());
        return moduleInstances;
    }
}
