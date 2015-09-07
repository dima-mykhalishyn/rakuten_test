package org.test.project.domain.dto;

import org.test.project.domain.Category;
import org.test.project.domain.TreeElement;
import org.test.project.domain.enums.Type;

import java.util.List;

/**
 * {@link Category} DTO
 * @author dmikhalishin@provectus-it.com
 * @see Category
 * @see TreeElement
 */
public class CategoryDto extends Category implements TreeElement {

    private boolean hasChildElements;

    /**
     * {@inheritDoc}
     * @see TreeElement#isHasChildElements()
     */
    public boolean isHasChildElements() {
        return hasChildElements;
    }

    public void setHasChildElements(boolean hasChildElements) {
        this.hasChildElements = hasChildElements;
    }

    /**
     * {@inheritDoc}
     * @see TreeElement#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return getName();
    }

    /**
     * {@inheritDoc}
     * @see TreeElement#getType()
     */
    @Override
    public Type getType() {
        return Type.CATEGORY;
    }
}
