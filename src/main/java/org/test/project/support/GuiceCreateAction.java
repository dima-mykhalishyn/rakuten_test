package org.test.project.support;

import org.apache.struts.action.Action;
import org.apache.struts.chain.commands.servlet.CreateAction;
import org.apache.struts.chain.contexts.ActionContext;

/**
 * {@link CreateAction} implementation that try load Action from guice injector
 * NOTE: We could initialize {@link Action} in default constructor, but it not looks good for me
 * @author dmikhalishin@provectus-it.com
 */
public class GuiceCreateAction extends CreateAction {

    /**
     * {@inheritDoc}
     * @see CreateAction#createAction(org.apache.struts.chain.contexts.ActionContext, String)
     */
    protected Action createAction(final ActionContext context, final String type) throws Exception {
        final Object action = GuiceInstanceHolder.getInjector().getInstance(Class.forName(type));
        return action == null ? super.createAction(context, type) : (Action)action;
    }

}
