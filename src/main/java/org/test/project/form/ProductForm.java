package org.test.project.form;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.test.project.domain.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Form for product page
 * @author dmikhalishin@provectus-it.com
 * @see ActionForm
 */
public class ProductForm extends ActionForm {

    private Product product = new Product();
    private List<Product> productList;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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
        if(product != null && StringUtils.isNotBlank(action)
                && (action.equals("create") || action.equals("update"))){
            if(StringUtils.isBlank(product.getName()))
                errors.add("name", new ActionMessage("errors.required","Name"));
            else if(product.getName().length() > 255)
                errors.add("name", new ActionMessage("errors.name","Name"));
            if(StringUtils.isBlank(product.getProductId()))
                errors.add("productId", new ActionMessage("errors.required","Product ID"));
            else if(product.getProductId().length() > 255)
                errors.add("productId", new ActionMessage("errors.name","Product ID"));
            if(product.getPrice() == null)
                errors.add("price", new ActionMessage("errors.required","Price"));
            else if(product.getPrice() < 0)
                errors.add("price", new ActionMessage("errors.price","Price"));
        }
        return errors;
    }

}
