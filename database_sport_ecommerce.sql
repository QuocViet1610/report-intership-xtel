use test;
create database sprort_ecommerce;
use sport_ecommerce;
CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(255),
  avatar VARCHAR(255),
  phone VARCHAR(20),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by VARCHAR(64),
  updated_by VARCHAR(64),
  password_changed_at DATETIME,
  is_active INT DEFAULT 1,
  google_id VARCHAR(255) DEFAULT NULL COMMENT 'ID tài khoản Google',
  facebook_id VARCHAR(255) DEFAULT NULL COMMENT 'ID tài khoản Facebook',
  PRIMARY KEY (id)
);
ALTER TABLE users
MODIFY COLUMN google_account_id VARCHAR(225) DEFAULT NULL COMMENT 'ID tài khoản Google',
MODIFY COLUMN facebook_account_id VARCHAR(225) DEFAULT NULL COMMENT 'ID tài khoản Facebook';


-- Table: user_verification
CREATE TABLE user_verification (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  otp VARCHAR(6) NOT NULL,
  expiry_time DATETIME NOT NULL
);

-- Table: role
CREATE TABLE role (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE user_role (
  id INT NOT NULL AUTO_INCREMENT,
  role_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (id)
);

-- Table: permission
CREATE TABLE permission (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,           
  description TEXT DEFAULT NULL  
);


CREATE TABLE role_permission (
  id INT NOT NULL AUTO_INCREMENT,
  role_id INT NOT NULL,
  permission_id INT NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE user_role
ADD CONSTRAINT fk_userrole_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_role
ADD CONSTRAINT fk_userrole_role
FOREIGN KEY (role_id) REFERENCES role(id);


ALTER TABLE role_permission
ADD CONSTRAINT fk_rolepermission_role
FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE role_permission
ADD CONSTRAINT fk_rolepermission_permission
FOREIGN KEY (permission_id) REFERENCES permission(id);


-- Table: product
CREATE TABLE product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  category_id INT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  cost_price DECIMAL(10, 2) NOT NULL,
  description LONGTEXT,
  is_active BOOLEAN DEFAULT TRUE,
  brand_id INT,
  gender_id INT,
  total_sold INT DEFAULT 0,
  total_rating DECIMAL(3, 2) DEFAULT 0.00,
  stock INT DEFAULT 0,
  discount_price DECIMAL(10, 2),
  weight DECIMAL(10, 2) DEFAULT 0.00,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by VARCHAR(64),
  updated_by VARCHAR(64),
  rating_count INT DEFAULT 0
);

-- Table: category
CREATE TABLE category (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  parent_id INT DEFAULT NULL,
  full_parent_id VARCHAR(255) DEFAULT NULL,
  image VARCHAR(255) DEFAULT NULL,
  level INT DEFAULT 1
);

-- Table: product_rating
CREATE TABLE product_rating (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_id INT NOT NULL,
  user_id INT NOT NULL,
  rating INT CHECK (rating BETWEEN 1 AND 5),
  comment TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by VARCHAR(64),
  updated_by VARCHAR(64)
);
ALTER TABLE product_rating
ADD COLUMN isActive int NOT NULL DEFAULT 1;
ALTER TABLE product_rating
CHANGE COLUMN isActive is_active INT NOT NULL DEFAULT 1;

-- Table: gender
CREATE TABLE gender (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- Table: product_image
CREATE TABLE product_image (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_id INT,
  image_url VARCHAR(500),
  is_primary BOOLEAN
);

-- Table: brand
CREATE TABLE brand (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  logo VARCHAR(255)
);

-- Table: attribute
CREATE TABLE attribute (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  display_order INT DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by VARCHAR(64),
  updated_by VARCHAR(64)
);

-- Table: attribute_value
CREATE TABLE attribute_value (
  id INT PRIMARY KEY AUTO_INCREMENT,
  attribute_id INT NOT NULL,
  name VARCHAR(255) NOT NULL
);

-- Table: product_attribute
CREATE TABLE product_attribute (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  attribute_id INT NOT NULL
);

-- Table: product_attribute_value
CREATE TABLE product_attribute_value (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  attribute_value_id INT NOT NULL
);

-- Table: product_variants
CREATE TABLE product_variants (
  id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL UNIQUE,
  product_id INT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  cost_price DECIMAL(10, 2) NOT NULL,
  price_old DECIMAL(10, 2),
  quantity INT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by VARCHAR(64),
  updated_by VARCHAR(64),
  image VARCHAR(255)
);

-- Table: product_variant_attribute
CREATE TABLE product_variant_attribute (
  id INT PRIMARY KEY AUTO_INCREMENT,
  product_variant_id INT NOT NULL,
  attribute_value_id INT NOT NULL
);

-- Table: Coupons
CREATE TABLE Coupons (
  coupon_id INT AUTO_INCREMENT PRIMARY KEY,
  name varchar(225), 
  coupon_code VARCHAR(100) UNIQUE NOT NULL,
  discount_value DECIMAL(10, 2) NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  usage_limit INT DEFAULT 1,
  times_used INT DEFAULT 0
);

-- Table: Cart
CREATE TABLE Cart (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL
);

-- Table: Cart_Items
CREATE TABLE Cart_Items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cart_id INT NOT NULL,
  product_id INT NOT NULL,
  product_variant_id INT,
  quantity INT NOT NULL
);

-- Table: addresses_ship
CREATE TABLE addresses_ship (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(15) NOT NULL,
  province_name VARCHAR(100) NOT NULL,
  province_id INT,
  district_name VARCHAR(100) NOT NULL,
  district_id INT,
  ward_name VARCHAR(100) NOT NULL,
  ward_id INT,
  country VARCHAR(100) NOT NULL,
  is_default INT,
  note LONGTEXT,
  address_text TEXT,
  address_type VARCHAR(100) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: Orders
CREATE TABLE Orders (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  total_price DECIMAL(10, 2) NOT NULL,
  total_discount DECIMAL(10, 2) DEFAULT 0,
  final_price DECIMAL(10, 2) NOT NULL,
  status_id int,
  shipping_address TEXT, 
  coupon_id INT,
  payment_method VARCHAR(100) NOT NULL,
  shipping_method VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  vnp_txn_ref VARCHAR(100)
);

ALTER TABLE Orders
ADD COLUMN order_code VARCHAR(50) NOT NULL;

ALTER TABLE Orders
MODIFY COLUMN order_code VARCHAR(50) NULL;
ALTER TABLE Orders
ADD COLUMN total_product INT DEFAULT 0;


-- Table: Order_detail
CREATE TABLE Order_detail (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  product_variant_id INT,
  quantity INT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  total DECIMAL(10, 2) NOT NULL
);

-- Table: Blogs
CREATE TABLE Blogs (
  post_id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  user_id INT
);

-- Table: Comments
CREATE TABLE Comments (
  comment_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  post_id INT,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Adding foreign key for users table
ALTER TABLE product
  ADD CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE SET NULL,
  ADD CONSTRAINT fk_gender FOREIGN KEY (gender_id) REFERENCES gender(id) ON DELETE SET NULL;

-- Adding foreign key for product_rating table
ALTER TABLE product_rating
  ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Adding foreign key for product_image table
ALTER TABLE product_image
  ADD CONSTRAINT fk_product_image FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE;

-- Adding foreign key for attribute_value table
ALTER TABLE attribute_value
  ADD CONSTRAINT fk_attribute FOREIGN KEY (attribute_id) REFERENCES attribute(id) ON DELETE CASCADE;

-- Adding foreign key for product_attribute table
ALTER TABLE product_attribute
  ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_attribute FOREIGN KEY (attribute_id) REFERENCES attribute(id) ON DELETE CASCADE;

-- Adding foreign key for product_attribute_value table
ALTER TABLE product_attribute_value
  ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_attribute_value FOREIGN KEY (attribute_value_id) REFERENCES attribute_value(id) ON DELETE CASCADE;

-- Adding foreign key for product_variants table
ALTER TABLE product_variants
  ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE;

-- Adding foreign key for product_variant_attribute table
ALTER TABLE product_variant_attribute
  ADD CONSTRAINT fk_product_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_attribute_value FOREIGN KEY (attribute_value_id) REFERENCES attribute_value(id) ON DELETE CASCADE;

-- Adding foreign key for Coupons table
ALTER TABLE Coupons
  ADD CONSTRAINT fk_coupon FOREIGN KEY (coupon_id) REFERENCES Coupons(coupon_id) ON DELETE CASCADE;

-- Adding foreign key for Cart table
ALTER TABLE Cart
  ADD CONSTRAINT fk_user_cart FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Adding foreign key for Cart_Items table
ALTER TABLE Cart_Items
  ADD CONSTRAINT fk_cart FOREIGN KEY (cart_id) REFERENCES Cart(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE;

-- Adding foreign key for addresses_ship table
ALTER TABLE addresses_ship
  ADD CONSTRAINT fk_user_address FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Adding foreign key for Orders table
ALTER TABLE Orders
  ADD CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_coupon FOREIGN KEY (coupon_id) REFERENCES Coupons(coupon_id) ON DELETE SET NULL;

-- Adding foreign key for Order_detail table
ALTER TABLE Order_detail
  ADD CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE;

-- Adding foreign key for Blogs table
ALTER TABLE Blogs
  ADD CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Adding foreign key for Comments table
ALTER TABLE Comments
  ADD CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES Blogs(post_id) ON DELETE CASCADE;

CREATE TABLE Category_blog (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE Blogs
ADD COLUMN category_id INT,
ADD CONSTRAINT FK_Blogs_Category FOREIGN KEY (category_id) REFERENCES Category_blog(category_id);
