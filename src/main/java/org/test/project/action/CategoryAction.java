package org.test.project.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.test.project.domain.Category;
import org.test.project.domain.TreeElement;
import org.test.project.form.CategoryForm;
import org.test.project.logic.CategoryLogic;
import org.test.project.logic.ProductLogic;
import org.test.project.support.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Actions for category page
 * @author dmikhalishin@provectus-it.com
 * @see DispatchAction
 */
@Singleton
public class CategoryAction extends DispatchAction {

    public CategoryLogic categoryLogic;
    public ProductLogic productLogic;

    @Inject
    private void inject(final CategoryLogic categoryLogic,
                        final ProductLogic productLogic){
        this.categoryLogic = categoryLogic;
        this.productLogic = productLogic;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward view(final ActionMapping mapping,
                              final ActionForm form,
                              final HttpServletRequest request,
                              final HttpServletResponse response) throws Exception {
        final CategoryForm categoryForm = (CategoryForm)form;
        categoryForm.setProducts(productLogic.getProducts());
        categoryForm.setTreeElements(categoryLogic.getByParent(null));
        return mapping.findForward("success");
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward loadData(final ActionMapping mapping,
                                  final ActionForm form,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response) throws Exception {
        final List<TreeElement> elements = categoryLogic.getByParent(request.getParameter("categoryId"));
        Utils.printResponse(response, treeToJson(elements));
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward create(final ActionMapping mapping,
                                final ActionForm form,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final CategoryForm categoryForm = (CategoryForm)form;
        categoryForm.validate(mapping, request);
        final Category category = categoryLogic.create(categoryForm.getCategory());
        final StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":").append(category.getId()).append("}");
        Utils.printResponse(response, builder.toString());
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward update(final ActionMapping mapping,
                                final ActionForm form,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final CategoryForm categoryForm = (CategoryForm)form;
        categoryLogic.update(categoryForm.getCategory());
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward delete(final ActionMapping mapping,
                                final ActionForm form,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        categoryLogic.delete(request.getParameter("id"));
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward addProduct(final ActionMapping mapping,
                                    final ActionForm form,
                                    final HttpServletRequest request,
                                    final HttpServletResponse response) throws Exception {
        categoryLogic.addProduct(request.getParameter("categoryId"), request.getParameter("productId"));
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ActionForward removeProduct(final ActionMapping mapping,
                                       final ActionForm form,
                                       final HttpServletRequest request,
                                       final HttpServletResponse response) throws Exception {
        categoryLogic.removeProduct(request.getParameter("categoryId"), request.getParameter("productId"));
        return null;
    }

    // ------------------------------------------------------------------------
    private String treeToJson(final List<TreeElement> elements){
        if (elements == null)
            return "[]";
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i=0;i<elements.size();i++) {
            final TreeElement element = elements.get(i);
            if(element == null) continue;
            builder.append("{")
                    .append("\"id\":\"").append(element.getId()).append("\",")
                    .append("\"text\":\"").append(StringEscapeUtils.escapeJavaScript(element.getDisplayName())).append("\",")
                    .append("\"expanded\":false,")
                    .append("\"type\":\"").append(element.getType()).append("\",")
                    .append("\"hasChildren\":").append(element.isHasChildElements())
                    .append("}");
            if(i + 1 != elements.size())
                builder.append(",");
        }
        builder.append("]");
        return builder.toString();
    }
}
