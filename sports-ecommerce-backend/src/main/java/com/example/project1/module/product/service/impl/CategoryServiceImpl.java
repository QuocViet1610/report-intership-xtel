package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.CategoryMapper;
import com.example.project1.model.config.MinioConfig;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.CategoryBaseRequest;
import com.example.project1.model.dto.request.product.CategoryCreateRequest;
import com.example.project1.model.dto.request.product.CategorySearchRequest;
import com.example.project1.model.dto.view.product.CategoryView;
import com.example.project1.model.enity.product.Category;
import com.example.project1.model.enity.product.Product;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.CategoryRepository;
import com.example.project1.module.product.repository.CategoryViewRepository;
import com.example.project1.module.product.repository.ProductRepository;
import com.example.project1.module.product.service.CategoryService;
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

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final MinioConfig minioConfig;
    private final CategoryViewRepository categoryViewRepository;
    private final ProductRepository productRepository;
    private String bucketName ;
    private String folderLocal;
    private String keyName ;

    @PostConstruct
    void started() {
        bucketName = minioConfig.getMinioBucketName();
        keyName = minioConfig.getMinioCategoryKeyName();
        folderLocal = minioConfig.getMinioCategoryFolder();
    }

    private void validateLogic(CategoryCreateRequest request, boolean isCreated) {
        request.validate();


        request.setName(request.getName().trim());
        if (isCreated) {
            if (request.getParentId() != null) {
                Category parentCategory = categoryRepository.findById(request.getParentId())
                        .orElseThrow(() -> new ValidateException(Translator.toMessage("Thể loại cha không tồn tại")));
                if (parentCategory.getLevel() != null){
                    request.setLevel(parentCategory.getLevel() + 1);
                }
                if (parentCategory.getFullParentId() != null) {
                    request.setFullParentId(parentCategory.getFullParentId() + "," + parentCategory.getId());

                } else {
                    request.setFullParentId(String.valueOf(parentCategory.getId()));
                }
            } else {
                request.setParentId(0L);
                request.setLevel(1L);
                request.setFullParentId(null);
            }


            List<Category> existingCategory = categoryRepository.findByNameIgnoreCase(request.getName().trim());
            if (existingCategory.size() >= 1 ) {
                throw new ValidateException(Translator.toMessage("Thể loại đã tồn tại"));
            }


        } else {
            Optional<Category> existingCategoryExit = categoryRepository.findByNameIgnoreCaseAndIdNot(request.getName(), request.getId());
            if (existingCategoryExit.isPresent()) {
                throw new ValidateException(Translator.toMessage("Thể loại đã tồn tại"));
            }

        }
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ValidateException(Translator.toMessage("Thể loại không tồn tại ")));
        MinioUtils.deleteFileMinio(bucketName, category.getImage());
        List<Long> fullParentIds = categoryRepository.findIdsByFullParentIdContaining(String.valueOf(id));

//        String[] parentIds = fullParentId.split(",");
//        Long[] parentIdsLong = new Long[parentIds.length];
//        for (int i = 0; i < parentIds.length; i++) {
//            parentIdsLong[i] = Long.parseLong(parentIds[i]);
//        }
//        List<Long> parentIdsList = Arrays.asList(parentIdsLong);


        List<Product> productIds = productRepository.findAllByCategoryId(fullParentIds);
        List<Product> productId = productRepository.findAllByCategoryId(id);
        if (productIds.size() > 0 || productId.size() > 0){
            throw new ValidateException(Translator.toMessage("Không thể xoá thể loại vì đang có sản phẩm thuộc Thể loại này"));
        }

        categoryRepository.deleteByIds(fullParentIds);
        categoryRepository.delete(category);
    }

    @Override
    public Object search(CategorySearchRequest searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("categoryName", searchRequest.getSearchText());
        }
            Specification<CategoryView> conditions = Specification.where(SearchSpecificationUtil.<CategoryView>alwaysTrue())
                    .and(SearchSpecificationUtil.or(mapCondition));
            if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
                Page<CategoryView> page = categoryViewRepository.findAll(conditions, pageable);
                return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
            }
            return categoryViewRepository.findAll(conditions, pageable.getSort());
        }

    @Override
    public CategoryDto create(CategoryBaseRequest request) {
        CategoryCreateRequest createRequest = request.getData();
        this.validateLogic(createRequest, true);
        if(!DataUtils.isNullOrEmpty(request.getImage())){
            createRequest.setImage(MinioUtils.uploadToMinioAndGetUrl(request.getImage(), folderLocal, bucketName, keyName));
        }

        Category category = categoryRepository.save(categoryMapper.toCreate(createRequest));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(CategoryBaseRequest request, Long id) {
        return categoryRepository.findById(id).map(category -> {
            CategoryCreateRequest createRequest= request.getData();
            createRequest.setId(id);
            this.validateLogic(createRequest, false);
            if (!DataUtils.isNullOrEmpty(request.getImage())){
                createRequest.setImage(MinioUtils.uploadToMinioAndGetUrl(request.getImage(), folderLocal, bucketName, keyName));
                MinioUtils.deleteFileMinio(bucketName, category.getImage());
            }else {
                createRequest.setImage(category.getImage());
            }
            // doi parent id, full_parent_id, level
            if (createRequest.getParentId() != null && createRequest.getParentId() != 0) {
                Category parentCategory = categoryRepository.findById(createRequest.getParentId())
                        .orElseThrow(() -> new ValidateException(Translator.toMessage("Thể loại cha không tồn tại")));
                if (parentCategory.getLevel() != null){
                    createRequest.setLevel(parentCategory.getLevel() + 1);
                }
                if (parentCategory.getFullParentId() != null) {
                    createRequest.setFullParentId(parentCategory.getFullParentId() + "," + parentCategory.getId());

                } else {
                    createRequest.setFullParentId(String.valueOf(parentCategory.getId()));
                }
            } else {
                createRequest.setParentId(0L);
                createRequest.setLevel(1L);
                createRequest.setFullParentId(null);
            }
            // Thay doi level cua child
            Long level = createRequest.getLevel() - category.getLevel();
            categoryRepository.updateLevelForCategories(level,category.getId());
            //Thay doi fullParentChild
            if (createRequest.getFullParentId() != null){
                categoryRepository.updateFullParentId(createRequest.getFullParentId()+",",category.getId());
            }else {
                categoryRepository.updateFullParentId("",category.getId());
            }


            categoryMapper.partialUpdate(category, createRequest);
            categoryRepository.save(category);
            return categoryMapper.toDto(category);
        }).orElseThrow(() -> new ValidateException(Translator.toMessage("Thể loại không tồn tại")));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }
}