package org.test.project.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.project.dao.CategoryDao;
import org.test.project.dao.ProductDao;
import org.test.project.domain.Category;
import org.test.project.domain.Product;
import org.test.project.domain.TreeElement;
import org.test.project.domain.dto.CategoryDto;
import org.test.project.support.Utils;
import org.test.project.support.db.CustomQueryRunner;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link CategoryDao}
 * @author dmikhalishin@provectus-it.com
 * @see CategoryDao
 */
@Singleton
public class CategoryDaoImpl implements CategoryDao {

    private final static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    private final DataSource dataSource;
    private final ProductDao productDao;

    @Inject
    public CategoryDaoImpl(final DataSource dataSource, final ProductDao productDao){
        this.dataSource = dataSource;
        this.productDao = productDao;
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#getById(Long)
     */
    @Override
    public Category getById(final Long id) {
        final ResultSetHandler<Category> handler = new ResultSetHandler<Category>() {
            @Override
            public Category handle(final ResultSet rs) throws SQLException {
                Category category = null;
                if(rs.next()){
                    category = new Category();
                    category.setId(rs.getLong("id"));
                    category.setParentId(rs.getLong("parent_id"));
                    category.setName(rs.getString("name"));
                    return category;
                }
                if(rs.next())
                    throw new SQLException("More than one Category were returned by ID[" + id + "]");
                return category;
            }
        };

        final CustomQueryRunner run = new CustomQueryRunner(dataSource);
        try {
            return run.query("select id, parent_id, name from category where id = ?", handler, id);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot load Category by ID[" + id + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#getByName(String, Long)
     */
    @Override
    public Category getByName(final String name, final Long parentId) {
        final ResultSetHandler<Category> handler = new ResultSetHandler<Category>() {
            @Override
            public Category handle(final ResultSet rs) throws SQLException {
                Category category = null;
                if(rs.next()){
                    category = new Category();
                    category.setId(rs.getLong("id"));
                    category.setParentId(rs.getLong("parent_id"));
                    category.setName(rs.getString("name"));
                    return category;
                }
                if(rs.next())
                    throw new SQLException("More than one Category were returned by name[" + name + "]");
                return category;
            }
        };

        final CustomQueryRunner run = new CustomQueryRunner(dataSource);
        try {
            return run.query("select id, parent_id, name from category where name = ? and parent_id = ?", handler, name, parentId);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot load Category by name[" + name + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#getByParent(org.test.project.domain.Category)
     */
    @Override
    public List<TreeElement> getByParent(final Category category) {
        final ResultSetHandler<List<TreeElement>> handler = new ResultSetHandler<List<TreeElement>>() {
            @Override
            public List<TreeElement> handle(final ResultSet rs) throws SQLException {
                final List<TreeElement> result = new ArrayList<TreeElement>();
                while (rs.next()){
                    final CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setId(rs.getLong("id"));
                    categoryDto.setParentId(rs.getLong("parent_id"));
                    categoryDto.setName(rs.getString("name"));
                    categoryDto.setHasChildElements(rs.getBoolean("hasCategory") || rs.getBoolean("hasProduct"));
                    result.add(categoryDto);
                }
                // add products
                if(category.getId() != null)
                    result.addAll(productDao.getByCategory(category));
                return result;
            }
        };

        final CustomQueryRunner run = new CustomQueryRunner(dataSource);
        try {
            final StringBuilder sql = new StringBuilder();
            sql.append("select c.id, c.parent_id, c.name, count(cc.id) > 0 hasCategory, count(pc.id) > 0 hasProduct from category c ")
                    .append("left join category cc on cc.parent_id = c.id ")
                    .append("left join product_category pc on pc.category_id = c.id ");
            if(category.getId() == null)
                sql.append("where c.parent_id is null ");
            else
                sql.append("where c.parent_id = ? ");
            sql.append("group by c.id, c.parent_id, c.name");
            if(category.getId() == null)
                return run.query(sql.toString(), handler);
            else
                return run.query(sql.toString(), handler, category.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot load Category by parent");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#create(org.test.project.domain.Category)
     */
    @Override
    public Category create(final Category category) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            final long id = run.insert("insert into category(parent_id, name) VALUES(?, ?)",
                    category.getParentId(),
                    category.getName());
            category.setId(id);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot create new Category");
        }
        return category;
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#update(org.test.project.domain.Category)
     */
    @Override
    public void update(final Category category) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            run.update("update category set name = ? where id = ?",
                    category.getName(),
                    category.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot update Category[" + category.getId() + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#delete(Long)
     */
    @Override
    public void delete(final Long id) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            run.update("delete from category where id = ?", id);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot delete Category[" + id + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#containProduct(org.test.project.domain.Category, org.test.project.domain.Product)
     */
    @Override
    public boolean containProduct(final Category category, final Product product) {
        try {
            final ResultSetHandler<Boolean> handler = new ResultSetHandler<Boolean>() {
                @Override
                public Boolean handle(final ResultSet rs) throws SQLException {
                    return rs.next();
                }
            };
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            return run.query("select product_id from product_category where product_id = ? and category_id = ?", handler,
                    product.getId(),
                    category.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot check Product[" + product.getId() + "] in Category[" + category.getId() + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#addProduct(org.test.project.domain.Category, org.test.project.domain.Product)
     */
    @Override
    public void addProduct(final Category category, final Product product) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            run.update("insert into product_category(category_id, product_id) VALUES(?, ?)",
                    category.getId(),
                    product.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot attach Product[" + product.getId() + "] to Category[" + category.getId() + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see CategoryDao#removeProduct(org.test.project.domain.Category, org.test.project.domain.Product)
     */
    @Override
    public void removeProduct(final Category category, final Product product) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            run.update("delete from product_category where category_id = ? and product_id = ?",
                    category.getId(),
                    product.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot remove Product[" + product.getId() + "] from Category[" + category.getId() + "]");
        }
    }
}
