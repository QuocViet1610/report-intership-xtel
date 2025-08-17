package com.example.project1.module.product.service.impl;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.ProductMapper;
import com.example.project1.mapper.product.ProductVariantMapper;
import com.example.project1.mapper.product.ProductViewMapper;
import com.example.project1.model.config.MinioConfig;
import com.example.project1.model.dto.product.ProductDto;
import com.example.project1.model.dto.request.product.*;
import com.example.project1.model.dto.view.product.ProductView;
import com.example.project1.model.dto.view.product.ProductViewDto;
import com.example.project1.model.enity.order.OrderDetail;
import com.example.project1.model.enity.product.*;
import com.example.project1.module.Order.repository.OrderDetailRepository;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.*;
import com.example.project1.module.product.service.ProductService;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.MinioUtils;
import com.example.project1.utils.SearchSpecificationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final MinioConfig minioConfig;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final GenderRepository genderRepository;
    private final ProductImageRepository productImageRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductVariantMapper productVariantMapper;
    private final ProductVariantRepository productVariantRepository;
    private final ProductViewRepository productViewRepository;
    private final ProductViewMapper productViewMapper;
    private final OrderDetailRepository orderRepository;
    private final ProductAttributeValueViewRepos productAttributeValueViewRepos;
//    private final ProductView
    private String bucketName ;
    private String folderLocal;
    private String keyName ;

    @PostConstruct
    void started() {
        bucketName = minioConfig.getMinioBucketName();
        keyName = minioConfig.getMinioProductKeyName();
        folderLocal = minioConfig.getMinioProductFolder();
    }
    private void validateLogic(ProductCreateRequest request, boolean isCreated) {
        request.validate();
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->  new ValidateException(Translator.toMessage("Thể loại không tồn tại")));

        if (!DataUtils.isNullOrEmpty(request.getBrandId())){
        if (brandRepository.findById(request.getBrandId()).isEmpty()) {
            throw new ValidateException(Translator.toMessage("Thương hiệu không tồn tại"));
        }
        }
        if (genderRepository.findById(request.getGenderId()).isEmpty()) {
            throw new ValidateException(Translator.toMessage("Đối Tượng không tồn tại"));
        }
        if (request.getWeight() == null){
            request.setWeight(BigDecimal.ZERO);
        }

        if (DataUtils.isNullOrEmpty(request.getProductVariantCreateRequests())) {
            request.setStock(0L);
            request.setCostPrice(BigDecimal.ZERO);
            request.getProductVariantCreateRequests().forEach(
                    productVariantCreateRequest -> {
                        productVariantCreateRequest.validate();
                    }
            );
        }
// kiem tra thuoc tinh
        if (!DataUtils.isNullOrEmpty(request.getAttributeCreatRequests())) {

            List<Long> requestedAttributeIds = request.getAttributeCreatRequests()
                    .stream()
                    .map(AttributeProductCreatRequest::getAttributeId)
                    .toList();

            List<Long> existingAttributeIds = attributeRepository.findAllById(requestedAttributeIds)
                    .stream()
                    .map(Attribute::getId)
                    .toList();

             List<Long> missingAttributeIds = requestedAttributeIds.stream()
                    .filter(id -> !existingAttributeIds.contains(id))
                    .toList();
            if (!missingAttributeIds.isEmpty()) {
                throw new ValidateException(Translator.toMessage("Thuộc tính không tồn tại: " + missingAttributeIds));
            }

            for (AttributeProductCreatRequest attribute: request.getAttributeCreatRequests()){
                if (DataUtils.isNullOrEmpty(attribute.getAttributeValueIds())){
                    throw new ValidateException(Translator.toMessage("Giá trị thuộc tính không được để trống"));
                }
            }

            for (AttributeProductCreatRequest attributeRequest : request.getAttributeCreatRequests()) {
                Long attributeId = attributeRequest.getAttributeId();
                List<Long> requestedValueIds = attributeRequest.getAttributeValueIds();

                if (!requestedValueIds.isEmpty()) {
                    List<AttributeValue> existingValues = attributeValueRepository.findAllById(requestedValueIds);
                    List<Long> existingValueIds = existingValues.stream()
                            .map(AttributeValue::getId)
                            .toList();

                    List<Long> missingValueIds = requestedValueIds.stream()
                            .filter(id -> !existingValueIds.contains(id))
                            .toList();
                    if (!missingValueIds.isEmpty()) {
                        throw new ValidateException(Translator.toMessage("Giá trị thuộc tính không tồn tại: " + missingValueIds));
                    }

                    // Lấy danh sách attributeId của các AttributeValue
                    List<Long> attributeIdsFromValues = existingValues.stream()
                            .map(AttributeValue::getAttributeId)
                            .toList();

                    List<Long> invalidAttributeValues = attributeIdsFromValues.stream()
                            .filter(id -> !id.equals(attributeId))
                            .toList();
                    if (!invalidAttributeValues.isEmpty()) {
                        throw new ValidateException(Translator.toMessage("Giá trị thuộc tính không thuộc về thuộc tính đã chọn: " + invalidAttributeValues));
                    }
                }
            }

//            if (DataUtils.isNullOrEmpty(request.getProductVariantCreateRequests())){
//                throw new ValidateException(Translator.toMessage("Biến thể không được để trống"));
//            }
        }

        if (isCreated) {
            if (productRepository.findByName(request.getName()).isPresent()) {
                throw new ValidateException(Translator.toMessage("Sản phẩm đã tồn tại "));
            }
            request.setCode(DataUtils.createCodeByKey(category.getName(), generateProductCode()));

        } else {
            if (productRepository.findByNameAndIdNot(request.getName(), request.getId()).isPresent()) {
                throw new ValidateException(Translator.toMessage("Sản phẩm đã tồn tại "));
            }
        }
    }

    public String generateProductCode() {
        Long maxProductId = productRepository.findMaxId();
        Long newProductCodeNumber = (maxProductId != null) ? maxProductId + 1 : 1;
        return String.format("%05d", newProductCodeNumber);
    }

    private void generateVariantCode(Product product, ProductVariantCreateRequest variantRequest, ProductVariant productVariant) {
        String baseCode = product.getCode();

        List<String> attributeValues = attributeValueRepository.findAllById(variantRequest.getVariantAttributeIds()).stream()
                .map(AttributeValue::getName)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        String variantCode = baseCode + "-" + String.join("-", attributeValues);
        variantCode = removeDiacritics(variantCode);
        productVariant.setCode(variantCode);

        String variantName = product.getName() + "-" + String.join("-", attributeValues);

        productVariant.setName(variantName);
    }

    public static String removeDiacritics(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm không tồn tại")));
        List<OrderDetail> order = orderRepository.findAllByProductId(id);
        if (order.size() > 0){
            product.setIsActive(0);
            productRepository.save(product);
        }
        else {
            List<ProductImage> productImages = productImageRepository.findAllByProductId(product.getId());

            if (!productImages.isEmpty()) {
                for (ProductImage productImage : productImages) {
                    MinioUtils.deleteFileMinio(bucketName, productImage.getImageUrl());
                }
            }
            productRepository.delete(product);
        }
    }

    @Override
    public Object search(ProductSearchRequest searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("productName", searchRequest.getSearchText());
            mapCondition.put("productCode", searchRequest.getSearchText());
//            mapCondition.put("attributeValueIds", searchRequest.getAttributeSearch());
        }
        Specification<ProductView> conditions = Specification.where(SearchSpecificationUtil.<ProductView>alwaysTrue())
                .and(SearchSpecificationUtil.equal("brandId", searchRequest.getBrandId()))
                .and(SearchSpecificationUtil.equal("productIsActive", searchRequest.getProductIsActive()))
                .and(SearchSpecificationUtil.equal("categoryId", searchRequest.getCategoryId()))
                .and(SearchSpecificationUtil.equal("genderId", searchRequest.getGenderId()))
                .and(SearchSpecificationUtil.likeFieldAttribute("attributeValueIds", searchRequest.getAttributeSearch()))
                .and(
                        Specification.<ProductView>where(SearchSpecificationUtil.equal("categoryId", searchRequest.getCategorySearch()))
                                .or(SearchSpecificationUtil.likeFieldCategory("fullParentId", searchRequest.getFullParentId()))
                )
//                .or(SearchSpecificationUtil.between("productPrice", searchRequest.getMinPrice(), searchRequest.getMaxPrice()))
                .and(SearchSpecificationUtil.or(mapCondition));
        if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
            Page<ProductView> page = productViewRepository.findAll(conditions, pageable);
            return new PageImpl<>(productViewMapper.toDto(page.getContent()), pageable, page.getTotalElements());
        }
        return productViewMapper.toDto(productViewRepository.findAll(conditions, pageable.getSort()));
    }
    @Override
    public ProductDto create(ProductBaseRequest request) {
        ProductCreateRequest createRequest = request.getData();
        this.validateLogic(createRequest, true);
        Product product = productRepository.save(productMapper.toCreate(createRequest));
        List<String> imageUrls = new ArrayList<>();
        saveProductImages(request.getImage(), product,imageUrls );
        if (!DataUtils.isNullOrEmpty(createRequest.getAttributeCreatRequests())){
            List<ProductAttribute> productAttributes = new ArrayList<>();
            List<ProductAttributeValue> productAttributeValues = new ArrayList<>();
            for (AttributeProductCreatRequest attributeRequest : createRequest.getAttributeCreatRequests()) {
                ProductAttribute productAttribute = new ProductAttribute();
                productAttribute.setAttributeId(attributeRequest.getAttributeId());
                productAttribute.setProductId(product.getId());
                productAttributes.add(productAttribute);

                if (!DataUtils.isNullOrEmpty(attributeRequest.getAttributeValueIds())) {
                    for (Long attributeValueId : attributeRequest.getAttributeValueIds()) {
                        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                        productAttributeValue.setProductId(product.getId());
                        productAttributeValue.setAttributeValueId(attributeValueId);
                        productAttributeValues.add(productAttributeValue);
                    }
                }
            }

            productAttributeRepository.saveAll(productAttributes);
            productAttributeValueRepository.saveAll(productAttributeValues);
            List<Long> productAttributeValueIds = productAttributeValues.stream()
                    .map(ProductAttributeValue::getAttributeValueId)
                    .collect(Collectors.toList());
            if (!DataUtils.isNullOrEmpty(createRequest.getProductVariantCreateRequests())){
                List<ProductVariant> productVariants =  createRequest.getProductVariantCreateRequests()
                        .stream().map(
                                variantRequest -> {
                                    ProductVariant productVariant = productVariantMapper.toCreate(variantRequest);
                                    productVariant.setProductId(product.getId());

                                    generateVariantCode(product, variantRequest, productVariant);

                                    for (Long attributeValue: variantRequest.getVariantAttributeIds()){
                                        if (!productAttributeValueIds.contains(attributeValue)){
                                            throw new ValidateException(Translator.toMessage("Thuộc tính của biến thể không tồn tại"));
                                        }
                                    }
                                    Set<ProductVariantAttribute> variantAttributes = createVariantAttributes(variantRequest, productVariant);
                                    productVariant.setVariantAttributes(variantAttributes);

                                    return productVariant;
                                }) .collect(Collectors.toList());

                List<ProductVariant> productVariantListSave =  productVariantRepository.saveAll(productVariants);
            }
        }
        return productMapper.toDto(product);
    }

    private Set<ProductVariantAttribute> createVariantAttributes(ProductVariantCreateRequest variantRequest, ProductVariant variant) {
        return variantRequest.getVariantAttributeIds().stream()
                .map(attributeValueId -> {
                    ProductVariantAttribute variantAttribute = new ProductVariantAttribute();
                    variantAttribute.setProductVariant(variant);
                    variantAttribute.setAttributeValueId(attributeValueId);
                    return variantAttribute;
                })
                .collect(Collectors.toSet());
    }

    private void saveProductImages(List<MultipartFile> images, Product product, List<String> imageUrls) {
        if (!DataUtils.isNull(images)) {
            if (images.size() > 10) {
                throw new ValidateException(Translator.toMessage("Bạn chỉ có thể tải lên tối đa 10 ảnh"));
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);

                ProductImage productImage = new ProductImage();
                String imageUrl = MinioUtils.uploadToMinioAndGetUrl(image, folderLocal, bucketName, keyName);
                productImage.setImageUrl(imageUrl);
                productImage.setProductId(product.getId());
                imageUrls.add(imageUrl);

                productImage.setIsPrimary(i == 0 ? 1 : 0);

                productImages.add(productImage);
            }

            productImageRepository.saveAll(productImages);
        }
    }
    @Override
    public ProductDto update(ProductBaseRequest request, Long id) {
        if (!DataUtils.isNullOrEmpty(request.getImage())) {
            List<MultipartFile> images = request.getImage();
            for (MultipartFile image : images) {
                if (image.isEmpty()) {
                    request.setImage(null);
                }

                String filename = image.getOriginalFilename();
                if (filename == null || filename.trim().isEmpty()) {
                    request.setImage(null);
                }
            }
        }
        return productRepository.findById(id).map(product -> {

            ProductCreateRequest createRequest= request.getData();
            createRequest.setId(id);
            this.validateLogic(createRequest, false);
            productMapper.partialUpdate(product, createRequest);
            List<String> imageUrls = new ArrayList<>();
            saveProductImages(request.getImage(), product, imageUrls);
            productRepository.save(product);
            return productMapper.toDto(product);
        }).orElseThrow(() -> new ValidateException(Translator.toMessage("error.category.id.not_exist")));
    }

    @Override
    public List<ProductViewDto> getAll() {
//        productAttributeValueViewRepos.findAll();
        return  productViewMapper.toDto(productViewRepository.findAll());
    }

    @Override
    public ProductViewDto getDetail(Long id) {
        ProductView productView = productViewRepository.findById(id)  .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm không tồn tại")));
        return productViewMapper.toDto(productView);
    }

    public Object kinhDoanh(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Sản phẩm không tồn tại")));

        // Đảo trạng thái: 0 -> 1 hoặc 1 -> 0
        int currentStatus = product.getIsActive();
        int newStatus = (currentStatus == 0) ? 1 : 0;

        // Cập nhật trạng thái mới
        product.setIsActive(newStatus);

        // Lưu thay đổi
        productRepository.save(product);

        return product;

    }
}
