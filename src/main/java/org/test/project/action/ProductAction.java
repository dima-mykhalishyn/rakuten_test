package org.test.project.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.test.project.domain.Product;
import org.test.project.form.ProductForm;
import org.test.project.logic.ProductLogic;
import org.test.project.support.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Actions for product page
 * @author dmikhalishin@provectus-it.com
 * @see DispatchAction
 */
@Singleton
public class ProductAction extends DispatchAction {

    private ProductLogic productLogic;

    @Inject
    public void inject(final ProductLogic productLogic){
        this.productLogic = productLogic;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward view(final ActionMapping mapping,
                              final ActionForm form,
                              final HttpServletRequest request,
                              final HttpServletResponse response) throws Exception {
        final ProductForm productForm = (ProductForm) form;
        productForm.setProductList(productLogic.getProducts());
        return mapping.findForward("success");
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward create(final ActionMapping mapping,
                                final ActionForm form,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final ProductForm productForm = (ProductForm) form;
        final Product created = productLogic.createProduct(productForm.getProduct());
        final StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append("\"id\":").append(created.getId())
                .append(", \"created\":\"").append(Utils.dateToSting(created.getCreated()))
                .append("\"}");
        Utils.printResponse(response, builder.toString());
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward update(final ActionMapping mapping,
                                final ActionForm form,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final ProductForm productForm = (ProductForm) form;
        productLogic.updateProduct(productForm.getProduct());
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward delete(final ActionMapping mapping,
                                final ActionForm form,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final String id = request.getParameter("id");
        productLogic.deleteProductById(id);
        return null;
    }
}
