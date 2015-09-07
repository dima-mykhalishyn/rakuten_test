package org.test.project.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.test.project.dao.ProductDao;
import org.test.project.domain.Category;
import org.test.project.domain.Product;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class ProductLogicTest {

    private ProductLogic productLogic;
    private ProductDao productDao;

    @Before
    public void before(){
        productDao = createMock(ProductDao.class);
        productLogic = new ProductLogic(productDao);
    }

    @Test
    public void testGetProductsValid(){
        final List<Product> products = new ArrayList<Product>();
        expect(productDao.getProducts()).andReturn(products);
        replay(productDao);
        Assert.assertEquals(productLogic.getProducts(), products);
        verify(productDao);
    }

    @Test
    public void testCreateValid(){
        final Product product = new Product();
        expect(productDao.create(product)).andReturn(product);
        replay(productDao);
        productLogic.createProduct(product);
        verify(productDao);
    }

    @Test
    public void testUpdateValid(){
        final Product product = new Product();
        product.setId(1L);
        expect(productDao.getById(1L)).andReturn(product);
        productDao.update(product);
        expectLastCall();
        replay(productDao);
        productLogic.updateProduct(product);
        verify(productDao);
    }

    @Test
    public void testDeleteValid(){
        productDao.delete(1L);
        expectLastCall();
        replay(productDao);
        productLogic.deleteProductById("1");
        verify(productDao);
    }


    // ========================================================================
    @Test
    public void testCreateInvalid(){
        replay(productDao);
        try {
            productLogic.createProduct(null);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Product cannot be null");
        }
    }

    @Test
    public void testUpdateInvalid(){
        replay(productDao);
        try {
            productLogic.updateProduct(null);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Product cannot be null");
        }
    }

    @Test
    public void testUpdateInvalid1(){
        final Product product = new Product();
        product.setId(1L);
        expect(productDao.getById(1L)).andReturn(null);
        replay(productDao);
        try {
            productLogic.updateProduct(product);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Product[1] was not found");
        }
    }

    @Test
    public void testDeleteInvalid(){
        replay(productDao);
        try {
            productLogic.deleteProductById("invalid");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Product ID should be numeric");
        }
    }

}
