package org.test.project.domain.dto;

import org.test.project.domain.Product;
import org.test.project.domain.TreeElement;
import org.test.project.domain.enums.Type;

/**
 * {@link Product} DTO
 * @author dmikhalishin@provectus-it.com
 * @see Product
 * @see TreeElement
 */
public class ProductDto extends Product implements TreeElement {

    /**
     * {@inheritDoc}
     * @see TreeElement#isHasChildElements()
     */
    public boolean isHasChildElements() {
        return false; /* product don't have child elements */
    }

    /**
     * {@inheritDoc}
     * @see TreeElement#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return getProductId();
    }

    /**
     * {@inheritDoc}
     * @see TreeElement#getType()
     */
    @Override
    public Type getType() {
        return Type.PRODUCT;
    }
}
