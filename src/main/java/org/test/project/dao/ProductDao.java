package org.test.project.dao;

import org.test.project.domain.Category;
import org.test.project.domain.Product;
import org.test.project.domain.dto.ProductDto;

import java.util.List;

/**
 * {@link Product} DAO
 * @author dmikhalishin@provectus-it.com
 */
public interface ProductDao {

    /**
     * Retrieves the {@link Product} by id
     * @param id the {@code Product} unique id. Cannot be {@code null}
     * @return the {@code Product} object
     */
    Product getById(Long id);

    /**
     * Return list of {@link Product}
     * @return list of {@code Product}
     */
    List<Product> getProducts();

    /**
     * Return list of {@link ProductDto} by {@link Category}
     * @param category the product {@code Category}. Cannot be {@code null}
     * @return list of {@code ProductDto} by {@code Category}
     */
    List<ProductDto> getByCategory(Category category);

    /**
     * Create new {@link Product}
     * @param product the {@code Product} that will be created. Cannot be {@code null}
     * @return created {@code Product}
     */
    Product create(Product product);

    /**
     * Update {@link Product}
     * @param product the {@code Product} that will be updated. Cannot be {@code null}
     */
    void update(Product product);

    /**
     * Delete {@link Product} by Id
     * @param id the {@code Product} id that will be deleted. Cannot be {@code null}
     */
    void delete(Long id);

}
