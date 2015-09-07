package org.test.project.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Product domain object
 * @author dmikhalishin@provectus-it.com
 * @see Serializable
 */
public class Product implements Serializable{

    private Long id;
    private String productId;
    private String name;
    private String description;
    private Double price;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
