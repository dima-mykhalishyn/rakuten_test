package org.test.project.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.junit.Assert;
import org.junit.Test;
import org.test.project.domain.Category;

import javax.servlet.http.HttpServletRequest;

import static org.easymock.EasyMock.*;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class CategoryFormTest {

    @Test
    public void validateTest(){
        final Category category = new Category();
        category.setName("test");
        final CategoryForm categoryForm = new CategoryForm();
        categoryForm.setCategory(category);
        final ActionMapping mapping = new ActionMapping();
        mapping.setParameter("action");
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("create");
        replay(request);
        final ActionErrors errors = categoryForm.validate(mapping, request);
        Assert.assertTrue(errors.isEmpty());
        verify(request);
    }

    @Test
    public void validateTestInvalidName(){
        final Category category = new Category();
        category.setName(null /*invalid*/);
        final CategoryForm categoryForm = new CategoryForm();
        categoryForm.setCategory(category);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getParameter(null)).andReturn(null);
        replay(request);
        final ActionMapping mapping = new ActionMapping();
        ActionErrors errors = categoryForm.validate(mapping, request);
        Assert.assertTrue(errors.isEmpty());
        verify(request);
        // ====================================================================
        mapping.setParameter("action");
        request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("update");
        replay(request);
        errors = categoryForm.validate(mapping, request);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(errors.size(), 1);
        Assert.assertEquals((errors.get().next()).toString(), "errors.required[Name]");
        verify(request);
        // ====================================================================
        category.setName("   " /*invalid*/);
        categoryForm.setCategory(category);
        request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("update");
        replay(request);
        errors = categoryForm.validate(mapping, request);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(errors.size(), 1);
        Assert.assertEquals((errors.get().next()).toString(), "errors.required[Name]");
        verify(request);
        // ====================================================================
        final StringBuilder builder = new StringBuilder();
        for (int i=0;i<256;i++)builder.append(i);
        category.setName(builder.toString() /*invalid*/);
        categoryForm.setCategory(category);
        request = createMock(HttpServletRequest.class);
        expect(request.getParameter(mapping.getParameter())).andReturn("update");
        replay(request);
        errors = categoryForm.validate(mapping, request);
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(errors.size(), 1);
        Assert.assertEquals((errors.get().next()).toString(), "errors.name[Name]");
        verify(request);
    }
}
