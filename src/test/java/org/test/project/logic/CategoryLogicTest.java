package org.test.project.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.test.project.dao.CategoryDao;
import org.test.project.dao.ProductDao;
import org.test.project.domain.Category;
import org.test.project.domain.Product;
import org.test.project.domain.TreeElement;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class CategoryLogicTest {

    private CategoryLogic categoryLogic;
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Before
    public void before(){
        categoryDao = createMock(CategoryDao.class);
        productDao = createMock(ProductDao.class);
        categoryLogic = new CategoryLogic(categoryDao, productDao);
    }

    @Test
    public void testGetByParentValid(){
        final List<TreeElement> result = new ArrayList<TreeElement>();
        expect(categoryDao.getByParent(anyObject(Category.class))).andReturn(result);
        replay(categoryDao, productDao);
        Assert.assertEquals(categoryLogic.getByParent(null), result);
        verify(categoryDao, productDao);
    }

    @Test
    public void testGetByParentValid1(){
        final Category category = new Category();
        expect(categoryDao.getById(1L)).andReturn(category);
        final List<TreeElement> result = new ArrayList<TreeElement>();
        expect(categoryDao.getByParent(category)).andReturn(result);
        replay(categoryDao, productDao);
        Assert.assertEquals(categoryLogic.getByParent("1"), result);
        verify(categoryDao, productDao);
    }

    @Test
    public void testCreateValid(){
        final Category category = new Category();
        expect(categoryDao.create(category)).andReturn(category);
        replay(categoryDao, productDao);
        categoryLogic.create(category);
        verify(categoryDao, productDao);
    }

    @Test
    public void testUpdateValid(){
        final Category category = new Category();
        category.setId(1L);
        category.setName("test");
        category.setParentId(2L);
        expect(categoryDao.getById(1L)).andReturn(category);
        expect(categoryDao.getByName(category.getName(), category.getParentId())).andReturn(null);
        categoryDao.update(category);
        expectLastCall();
        replay(categoryDao, productDao);
        categoryLogic.update(category);
        verify(categoryDao, productDao);
    }

    @Test
    public void testDeleteValid(){
        categoryDao.delete(1L);
        expectLastCall();
        replay(categoryDao, productDao);
        categoryLogic.delete("1");
        verify(categoryDao, productDao);
    }

    @Test
    public void testAddProductValid(){
        final Category category = new Category();
        final Product product = new Product();
        expect(categoryDao.getById(1L)).andReturn(category);
        expect(productDao.getById(1L)).andReturn(product);
        expect(categoryDao.containProduct(category, product)).andReturn(false);
        categoryDao.addProduct(category, product);
        expectLastCall();
        replay(categoryDao, productDao);
        categoryLogic.addProduct("1", "1");
        verify(categoryDao, productDao);
    }

    @Test
    public void testRemoveProductValid(){
        final Category category = new Category();
        final Product product = new Product();
        expect(categoryDao.getById(1L)).andReturn(category);
        expect(productDao.getById(1L)).andReturn(product);
        categoryDao.removeProduct(category, product);
        expectLastCall();
        replay(categoryDao, productDao);
        categoryLogic.removeProduct("1", "1");
        verify(categoryDao, productDao);
    }

    // ========================================================================
    @Test
    public void testGetByParentInvalid(){
        replay(categoryDao, productDao);
        try {
            categoryLogic.getByParent("sada");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Invalid Category ID. It should be numeric");
        }

    }

    @Test
    public void testGetByParentInvalid1(){
        expect(categoryDao.getById(23L)).andReturn(null);
        replay(categoryDao, productDao);
        try {
            categoryLogic.getByParent("23");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category[23] was not found");
        }
    }

    @Test
    public void testCreateInvalid(){
        replay(categoryDao, productDao);
        try {
            categoryLogic.create(null);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category cannot be null");
        }
    }

    @Test
    public void testUpdateInvalid(){
        replay(categoryDao, productDao);
        try {
            categoryLogic.update(null);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category cannot be null");
        }
    }

    @Test
    public void testUpdateInvalid1(){
        final Category category = new Category();
        category.setId(1L);
        expect(categoryDao.getById(1L)).andReturn(null);
        replay(categoryDao, productDao);
        try {
            categoryLogic.update(category);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category[1] was not found");
        }
    }

    @Test
    public void testUpdateInvalid2(){
        final Category category = new Category();
        category.setId(1L);
        category.setName("test");
        category.setParentId(2L);
        expect(categoryDao.getById(1L)).andReturn(category);
        expect(categoryDao.getByName(category.getName(), category.getParentId())).andReturn(category);
        replay(categoryDao, productDao);
        try {
            categoryLogic.update(category);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category name[test] already used");
        }
    }

    @Test
    public void testDeleteInvalid(){
        replay(categoryDao, productDao);
        try {
            categoryLogic.delete("invalid");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category ID should be numeric");
        }
    }

    @Test
    public void testAddProductInvalid(){
        replay(categoryDao, productDao);
        try {
            categoryLogic.addProduct(null, "1");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Invalid Category ID. It should be numeric");
        }
        try {
            categoryLogic.addProduct("1", null);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Invalid Product ID. It should be numeric");
        }
    }

    @Test
    public void testAddProductInvalid1(){
        final Category category = new Category();
        expect(categoryDao.getById(2L)).andReturn(null);
        expect(productDao.getById(2L)).andReturn(null);
        replay(categoryDao, productDao);
        try {
            categoryLogic.addProduct("2", "2");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category[2] was not found");
        }
        verify(categoryDao, productDao);
        before();
        expect(categoryDao.getById(2L)).andReturn(category);
        expect(productDao.getById(2L)).andReturn(null);
        replay(categoryDao, productDao);
        try {
            categoryLogic.addProduct("2", "2");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Product[2] was not found");
        }
        verify(categoryDao, productDao);
    }

    @Test
    public void testRemoveProductInvalid(){
        replay(categoryDao, productDao);
        try {
            categoryLogic.removeProduct(null, "1");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Invalid Category ID. It should be numeric");
        }
        try {
            categoryLogic.removeProduct("1", null);
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Invalid Product ID. It should be numeric");
        }
    }

    @Test
    public void testRemoveProductInvalid1(){
        final Category category = new Category();
        expect(categoryDao.getById(2L)).andReturn(null);
        expect(productDao.getById(2L)).andReturn(null);
        replay(categoryDao, productDao);
        try {
            categoryLogic.removeProduct("2", "2");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Category[2] was not found");
        }
        verify(categoryDao, productDao);
        before();
        expect(categoryDao.getById(2L)).andReturn(category);
        expect(productDao.getById(2L)).andReturn(null);
        replay(categoryDao, productDao);
        try {
            categoryLogic.removeProduct("2", "2");
        } catch (RuntimeException e){
            Assert.assertEquals(e.getMessage(), "Product[2] was not found");
        }
        verify(categoryDao, productDao);
    }

}
