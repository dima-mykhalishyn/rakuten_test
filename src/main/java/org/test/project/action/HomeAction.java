package org.test.project.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Home action
 * @author dmikhalishin@provectus-it.com
 * @see Action
 */
public class HomeAction extends Action {

    /**
     * {@inheritDoc}
     * @see Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(final ActionMapping mapping,
                                 final ActionForm form,
                                 final HttpServletRequest request,
                                 final HttpServletResponse response) throws Exception {
        // add some staff for form, but for now - do nothing
        return mapping.findForward("success");
    }
}
