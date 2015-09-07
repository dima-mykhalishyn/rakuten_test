package org.test.project.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.junit.Assert;
import org.junit.Test;
import org.test.project.domain.Product;

import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.verify;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class ProductFormTest {
    @Test
    public void validateTest(){
        final Product product = new Product();
        product.setName("test");
        product.setPrice(1d);
        product.setProductId("test");
        final ProductForm productForm = new ProductForm();
        productForm.setProduct(product);
        final ActionMapping mapping = new ActionMapping();
        mapping.setParameter("action");
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("create");
        replay(request);
        final ActionErrors errors = productForm.validate(mapping, request);
        Assert.assertTrue(errors.isEmpty());
        verify(request);
    }

    @Test
    public void validateTestInvalidName(){
        final Product product = new Product();
        product.setName(null /*invalid*/);
        product.setPrice(null);
        product.setProductId(null /*invalid*/);
        final ProductForm productForm = new ProductForm();
        productForm.setProduct(product);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getParameter(null)).andReturn(null);
        replay(request);
        final ActionMapping mapping = new ActionMapping();
        ActionErrors errors = productForm.validate(mapping, request);
        Assert.assertTrue(errors.isEmpty());
        verify(request);
        // ====================================================================
        mapping.setParameter("action");
        request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("update");
        replay(request);
        errors = productForm.validate(mapping, request);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(errors.size(), 3);
        Iterator it = errors.get();
        Assert.assertEquals(it.next().toString(), "errors.required[Name]");
        Assert.assertEquals(it.next().toString(), "errors.required[Product ID]");
        Assert.assertEquals(it.next().toString(), "errors.required[Price]");
        verify(request);
        // ====================================================================
        product.setPrice(1d);
        product.setName("   " /*invalid*/);
        product.setProductId("   " /*invalid*/);
        productForm.setProduct(product);
        request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("update");
        replay(request);
        errors = productForm.validate(mapping, request);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(errors.size(), 2);
        it = errors.get();
        Assert.assertEquals((it.next()).toString(), "errors.required[Name]");
        Assert.assertEquals((it.next()).toString(), "errors.required[Product ID]");
        verify(request);
        // ====================================================================
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<256;i++)builder.append(i);
        product.setName(builder.toString() /*invalid*/);
        product.setProductId(builder.toString() /*invalid*/);
        product.setPrice(-1d);
        productForm.setProduct(product);
        request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("update");
        replay(request);
        errors = productForm.validate(mapping, request);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(errors.size(), 3);
        it = errors.get();
        Assert.assertEquals((it.next()).toString(), "errors.name[Name]");
        Assert.assertEquals((it.next()).toString(), "errors.name[Product ID]");
        Assert.assertEquals((it.next()).toString(), "errors.price[Price]");
    }
}
