package org.test.project.guice.module;

import com.google.inject.AbstractModule;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.test.project.dao.CategoryDao;
import org.test.project.dao.ProductDao;
import org.test.project.dao.impl.CategoryDaoImpl;
import org.test.project.dao.impl.ProductDaoImpl;
import org.test.project.support.Utils;

import javax.sql.DataSource;

/**
 * Guice DAO module
 * @author dmikhalishin@provectus-it.com
 * @see AbstractModule
 */
public class DaoModule extends AbstractModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        bind(DataSource.class).toInstance(initDataSource());
        bind(ProductDao.class).to(ProductDaoImpl.class);
        bind(CategoryDao.class).to(CategoryDaoImpl.class);
    }

    /**
     * Initialize the {@link DataSource}
     */
    private DataSource initDataSource(){
        try {
            return BasicDataSourceFactory.createDataSource(Utils.getProperties());
        } catch (Exception e){
            // if there is an exceptions, then we cannot continue working
            throw new RuntimeException(e);
        }
    }
}
