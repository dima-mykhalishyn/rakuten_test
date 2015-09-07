package org.test.project.dao;

import org.test.project.domain.Category;
import org.test.project.domain.Product;
import org.test.project.domain.TreeElement;

import java.util.List;

/**
 * {@link Category} DAO
 * @author dmikhalishin@provectus-it.com
 */
public interface CategoryDao {

    /**
     * Retrieves the {@link Category} by id
     * @param id the {@code Category} unique id. Cannot be {@code null}
     * @return the {@code Category} object
     */
    Category getById(Long id);

    /**
     * Retrieves the {@link Category} by name and parent id
     * @param name the {@code Category} name. Cannot be {@code null}
     * @param parentId the {@code Category} parent id. Can be {@code null}
     * @return the {@code Category} object
     */
    Category getByName(String name, Long parentId);

    /**
     * Retrieves the {@link TreeElement} by parent {@link Category}
     * @param category the parent {@code Category}. Cannot be {@code null}
     * @return the list {@code TreeElement} objects
     */
    List<TreeElement> getByParent(Category category);

    /**
     * Create the {@link Category}
     * @param category the {@code Category} that will be created. Cannot be {@code null}
     * @return the created {@code Category}
     */
    Category create(Category category);

    /**
     * Update the {@link Category}
     * @param category the {@code Category} that will be updated. Cannot be {@code null}
     */
    void update(Category category);

    /**
     * Delete the {@link Category} by id
     * @param id the {@code Category} id that will be deleted. Cannot be {@code null}
     */
    void delete(Long id);

    /**
     * Is {@link Product} attached to {@link Category}
     * @param category the {@code Category} object. Cannot be {@code null}
     * @param product the {@code Product} object. Cannot be {@code null}
     * @return the true if attached, else - otherwise
     */
    boolean containProduct(Category category, Product product);

    /**
     * Add {@link Product} to {@link Category}
     * @param category the {@code Category} object. Cannot be {@code null}
     * @param product the {@code Product} object. Cannot be {@code null}
     */
    void addProduct(Category category, Product product);

    /**
     * Remove {@link Product} from {@link Category}
     * @param category the {@code Category} object. Cannot be {@code null}
     * @param product the {@code Product} object. Cannot be {@code null}
     */
    void removeProduct(Category category,Product product);
}