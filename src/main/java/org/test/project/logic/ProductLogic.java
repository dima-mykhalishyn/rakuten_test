package org.test.project.logic;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang.StringUtils;
import org.test.project.dao.ProductDao;
import org.test.project.domain.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Product business logic
 * @author dmikhalishin@provectus-it.com
 */
@Singleton
public class ProductLogic {

    private final ProductDao productDao;

    @Inject
    public ProductLogic(final ProductDao productDao){
        this.productDao = productDao;
    }

    /**
     * Return list of {@link Product}
     * @return list of {@code Product}
     */
    public List<Product> getProducts(){
        return productDao.getProducts();
    }

    /**
     * Create new {@link Product}
     * @param product the {@code Product} that will be created. Cannot be {@code null}
     * @return created {@code Product}
     */
    public Product createProduct(final Product product){
        if(product == null)
            throw new RuntimeException("Product cannot be null");
        product.setCreated(new Date());
        return productDao.create(product);
    }

    /**
     * Update {@link Product}
     * @param product the {@code Product} that will be updated. Cannot be {@code null}
     */
    public void updateProduct(final Product product){
        if(product == null)
            throw new RuntimeException("Product cannot be null");
        if(productDao.getById(product.getId()) == null)
            throw new RuntimeException("Product[" + product.getId() + "] was not found");
        productDao.update(product);
    }

    /**
     * Delete {@link Product} by Id
     * @param id the {@code Product} id that will be deleted. Cannot be {@code null}
     */
    public void deleteProductById(final String id){
        if(!StringUtils.isNumeric(id))
            throw new RuntimeException("Product ID should be numeric");
        productDao.delete(Long.valueOf(id));
    }
}
