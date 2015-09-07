package org.test.project.logic;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang.StringUtils;
import org.test.project.dao.CategoryDao;
import org.test.project.dao.ProductDao;
import org.test.project.domain.Category;
import org.test.project.domain.Product;
import org.test.project.domain.TreeElement;
import org.test.project.domain.dto.CategoryDto;

import java.util.List;

/**
 * Category business logic
 * @author dmikhalishin@provectus-it.com
 */
@Singleton
public class CategoryLogic {

    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Inject
    public CategoryLogic(final CategoryDao categoryDao,
                         final ProductDao productDao){
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    /**
     * Retrieves the {@link TreeElement} by parent {@link Category}
     * @param categoryId the parent {@code Category} id. Can be {@code null}
     * @return the list {@code TreeElement} objects
     */
    public List<TreeElement> getByParent(final String categoryId){
        if(!StringUtils.isBlank(categoryId) && !StringUtils.isNumeric(categoryId))
            throw new RuntimeException("Invalid Category ID. It should be numeric");
        if(StringUtils.isBlank(categoryId))
            return categoryDao.getByParent(new Category());
        else {
            final Category category = categoryDao.getById(Long.valueOf(categoryId));
            if(category == null)
                throw new RuntimeException("Category[" + categoryId + "] was not found");
            return categoryDao.getByParent(category);
        }
    }

    /**
     * Create the {@link Category}
     * @param category the {@code Category} that will be created. Cannot be {@code null}
     * @return the created {@code Category}
     */
    public Category create(final Category category){
        if(category == null)
            throw new RuntimeException("Category cannot be null");
        return categoryDao.create(category);
    }

    /**
     * Update the {@link Category}
     * @param category the {@code Category} that will be updated. Cannot be {@code null}
     */
    public void update(final Category category){
        if(category == null)
            throw new RuntimeException("Category cannot be null");
        final Category fromDB = categoryDao.getById(category.getId());
        if(fromDB == null)
            throw new RuntimeException("Category[" + category.getId() + "] was not found");
        if(categoryDao.getByName(category.getName(), fromDB.getParentId()) != null)
            throw new RuntimeException("Category name[" + category.getName() + "] already used");
        categoryDao.update(category);
    }

    /**
     * Delete the {@link Category} by id
     * @param id the {@code Category} id that will be deleted. Cannot be {@code null}
     */
    public void delete(final String id){
        if(!StringUtils.isNumeric(id))
            throw new RuntimeException("Category ID should be numeric");
        categoryDao.delete(Long.valueOf(id));
    }

    /**
     * Add {@link Product} to {@link Category}
     * @param categoryId the {@code Category} id object. Cannot be {@code blank}
     * @param productId the {@code Product} id object. Cannot be {@code blank}
     */
    public void addProduct(final String categoryId, final String productId){
        if(StringUtils.isBlank(categoryId) || !StringUtils.isNumeric(categoryId))
            throw new RuntimeException("Invalid Category ID. It should be numeric");
        if(StringUtils.isBlank(productId) || !StringUtils.isNumeric(productId))
            throw new RuntimeException("Invalid Product ID. It should be numeric");
        final Category category = categoryDao.getById(Long.valueOf(categoryId));
        final Product product = productDao.getById(Long.valueOf(productId));
        if(category == null)
            throw new RuntimeException("Category[" + categoryId + "] was not found");
        if(product == null)
            throw new RuntimeException("Product[" + productId + "] was not found");
        if(!categoryDao.containProduct(category, product))
            categoryDao.addProduct(category, product);
    }

    /**
     * Remove {@link Product} from {@link Category}
     * @param categoryId the {@code Category} id object. Cannot be {@code blank}
     * @param productId the {@code Product} id object. Cannot be {@code blank}
     */
    public void removeProduct(final String categoryId, final String productId){
        if(StringUtils.isBlank(categoryId) || !StringUtils.isNumeric(categoryId))
            throw new RuntimeException("Invalid Category ID. It should be numeric");
        if(StringUtils.isBlank(productId) || !StringUtils.isNumeric(productId))
            throw new RuntimeException("Invalid Product ID. It should be numeric");
        final Category category = categoryDao.getById(Long.valueOf(categoryId));
        final Product product = productDao.getById(Long.valueOf(productId));
        if(category == null)
            throw new RuntimeException("Category[" + categoryId + "] was not found");
        if(product == null)
            throw new RuntimeException("Product[" + productId + "] was not found");
        categoryDao.removeProduct(category, product);
    }


}
