package org.test.project.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.project.dao.ProductDao;
import org.test.project.domain.Category;
import org.test.project.domain.Product;
import org.test.project.domain.dto.ProductDto;
import org.test.project.support.Utils;
import org.test.project.support.db.CustomQueryRunner;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link ProductDao}
 * @author dmikhalishin@provectus-it.com
 * @see ProductDao
 */
@Singleton
public class ProductDaoImpl implements ProductDao {

    private final static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    private final DataSource dataSource;

    @Inject
    public ProductDaoImpl(final DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     * @see org.test.project.dao.ProductDao#getProducts()
     */
    @Override
    public Product getById(final Long id) {
        final ResultSetHandler<Product> handler = new ResultSetHandler<Product>() {
            @Override
            public Product handle(final ResultSet rs) throws SQLException {
                Product product = null;
                if(rs.next()){
                    product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setProductId(rs.getString("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCreated(rs.getTimestamp("created"));
                    return product;
                }
                if(rs.next())
                    throw new SQLException("More than one Product were returned by ID[" + id + "]");
                return product;
            }
        };

        final CustomQueryRunner run = new CustomQueryRunner(dataSource);
        try {
            return run.query("select id, product_id, name, description, price, created from product where id = ?", handler, id);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot load Product by ID[" + id + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see org.test.project.dao.ProductDao#getProducts()
     */
    @Override
    public List<Product> getProducts() {
        final ResultSetHandler<List<Product>> handler = new ResultSetHandler<List<Product>>() {
            @Override
            public List<Product> handle(final ResultSet rs) throws SQLException {
                final List<Product> result = new ArrayList<Product>();
                while(rs.next()){
                    final Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setProductId(rs.getString("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCreated(rs.getTimestamp("created"));
                    result.add(product);
                }
                return result;
            }
        };

        final CustomQueryRunner run = new CustomQueryRunner(dataSource);
        try {
            return run.query("select id, product_id, name, description, price, created from product", handler);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot load Product");
        }
    }

    /**
     * {@inheritDoc}
     * @see org.test.project.dao.ProductDao#getByCategory(org.test.project.domain.Category)
     */
    @Override
    public List<ProductDto> getByCategory(final Category category) {
        final ResultSetHandler<List<ProductDto>> handler = new ResultSetHandler<List<ProductDto>>() {
            @Override
            public List<ProductDto> handle(final ResultSet rs) throws SQLException {
                final List<ProductDto> result = new ArrayList<ProductDto>();
                while(rs.next()){
                    final ProductDto product = new ProductDto();
                    product.setId(rs.getLong("id"));
                    product.setProductId(rs.getString("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCreated(rs.getTimestamp("created"));
                    result.add(product);
                }
                return result;
            }
        };

        final CustomQueryRunner run = new CustomQueryRunner(dataSource);
        try {
            final StringBuilder sql = new StringBuilder();
            sql.append("select p.id, p.product_id, p.name, p.description, p.price, p.created from product p ");
            sql.append("join product_category pc on pc.product_id = p.id where pc.category_id = ?");
            return run.query(sql.toString(), handler, category.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot load Product");
        }
    }

    /**
     * {@inheritDoc}
     * @see ProductDao#create(org.test.project.domain.Product)
     */
    @Override
    public Product create(final Product product) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            final long id = run.insert("insert into product(product_id, name, description, price, created) VALUES(?, ?, ?, ?, ?)",
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCreated());
            product.setId(id);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot create new Product");
        }
        return product;
    }

    /**
     * {@inheritDoc}
     * @see ProductDao#update(org.test.project.domain.Product)
     */
    @Override
    public void update(final Product product) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            run.update("update product set product_id = ?, name = ?, description = ?, price = ? where id = ?",
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getId());
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot update Product[" + product.getId() + "]");
        }
    }

    /**
     * {@inheritDoc}
     * @see ProductDao#delete(Long)
     */
    @Override
    public void delete(final Long id) {
        try {
            final CustomQueryRunner run = new CustomQueryRunner(dataSource);
            run.update("delete from product where id = ?", id);
        }catch (SQLException e){
            logger.error(Utils.errorToString(e));
            throw new RuntimeException("Cannot delete Product[" + id + "]");
        }
    }
}
