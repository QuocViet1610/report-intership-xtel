package com.example.project1.test.product;
import com.example.project1.cache.BaseCacheManager;
import com.example.project1.cache.CacheUtility;
import com.example.project1.model.enity.product.Product;
import com.example.project1.module.product.repository.ProductRepository;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.SearchSpecificationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductTestServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CacheUtility cacheUtility;
    private final BaseCacheManager baseCacheManager;

    @Override
    public List<Product> getAll() {
//        return productRepository.findAll();
        return productRepository.findAllWithVariants();
    }

    public List<Object[]> getAllNativeQuery() {
        return productRepository.findProductRevenueAndAvgRatingByCategory();
    }

    public List<Product> getAllJPA() {
        return productRepository.findProductRevenueAndAvgRatingByCategoryJPQL();
    }

    public Object Productspecification() {
        Specification<Product> conditions = Specification.where(SearchSpecificationUtil.<Product>alwaysTrue())
                .and(SearchSpecificationUtil.equal("isActive", 1));
        return productRepository.findAll(conditions);
    }

    public List<Product> getProductCache() {
        List<Product> product = baseCacheManager.getList("Product", Product.class);
        if (DataUtils.isNullOrEmpty(product)) {
            product = productRepository.findProductRevenueAndAvgRatingByCategoryJPQL();
            baseCacheManager.setCache("Product", product);
        }
        return product;
    }

}
