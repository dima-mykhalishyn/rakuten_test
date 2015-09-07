Test Project for Rakuten Deutschland GmbH (2013)

===============================================================================
Create a simple web application for managing products.
Basic conditions:
The web application should be written in Java.
MVC-Framework: Struts 1.3.10
Database: MySQL
Dataaccess: JDBC
For DI: Google guice
Servlet-Container: Tomcat 7

Front-Controller Pattern should be used:
This means no jsp will be called directly. In front of every JSP should be a "Pre"-Action.
i.e. For the "home" page following actions and JSP should be created:
-HomePreAction forwards to home.jsp
-home.jsp the form should point to HomePostAction
-HomePostAction forwards to the next PreAction

Step 1: create an administration for managing products
  Products have a name, price, description, productid, creation timestamp
  Following functions should be implemented:
  Create new product
  Update existing procuct
  Delete existing product
  For updating and deletion of products a search by productid, name should be implemented
  Create a Custom-Tag to show the creation timestamp in a human readable format.

Step 2: create an administration for managing product categories
  A category has a name and child categories
  Following functions should be implemented:
  Create new categories
  Update existing categories (only name)
  Delete existing categories which are empty; a category is defined as empty if no child categories exists and after step 3 no products
  are assigned to this category
  The UI should show a tree representation of the categories
  Would be great if the tree is initial closed to the root nodes and can be opened and closed (i.e JavaScript, or post-action or best would be by ajax)

Step 3: create an administration for assigning products to categories
  Following functions should be implemented:
  Assign products to a category
  Remove assignment for a product to a category
