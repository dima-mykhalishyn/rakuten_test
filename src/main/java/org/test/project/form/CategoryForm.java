package org.test.project.form;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.test.project.domain.Category;
import org.test.project.domain.TreeElement;
import org.test.project.domain.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Form for category page
 * @author dmikhalishin@provectus-it.com
 * @see ActionForm
 */
public class CategoryForm extends ActionForm {

    private Category category = new Category();

    private List<TreeElement> treeElements;
    private List<Product> products;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<TreeElement> getTreeElements() {
        return treeElements;
    }

    public void setTreeElements(List<TreeElement> treeElements) {
        this.treeElements = treeElements;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * {@inheritDoc}
     * @see ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request) {
        final ActionErrors errors = new ActionErrors();
        // NOTE: run validation only for create / update action
        final String action = request.getParameter(mapping.getParameter());
        if(category != null && StringUtils.isNotBlank(action)
                && (action.equals("create") || action.equals("update"))){
            if(StringUtils.isBlank(category.getName()))
                errors.add("name", new ActionMessage("errors.required","Name"));
            else if(category.getName().length() > 255)
                errors.add("name", new ActionMessage("errors.name","Name"));
        }
        return errors;
    }
}
