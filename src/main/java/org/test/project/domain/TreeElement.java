package org.test.project.domain;

import org.test.project.domain.enums.Type;

/**
 * Tree element
 * @author dmikhalishin@provectus-it.com
 */
public interface TreeElement {

    /**
     * ID of element
     */
    public Long getId();

    /**
     * Does element contains child objects
     */
    boolean isHasChildElements();
    /**
     * Return display name what will be show in tree
     */
    String getDisplayName();

    /**
     * Retrieves the element {@link Type}
     */
    Type getType();
}
