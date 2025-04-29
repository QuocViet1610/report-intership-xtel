package com.example.project1.module.product.service.impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.BrandMapper;
import com.example.project1.model.config.MinioConfig;
import com.example.project1.model.dto.product.BrandDto;
import com.example.project1.model.dto.request.product.BrandBaseRequest;
import com.example.project1.model.dto.request.product.BrandCreateRequest;
import com.example.project1.model.dto.request.product.BrandSearchRequest;
import com.example.project1.model.enity.product.Brand;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.repository.BrandRepository;
import com.example.project1.module.product.service.BrandService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final MinioConfig minioConfig;
    private String bucketName ;
    private String folderLocal;
    private String keyName ;

    @PostConstruct
    void started() {
        bucketName = minioConfig.getMinioBucketName();
        keyName = minioConfig.getMinioBrandKeyName();
        folderLocal = minioConfig.getMinioBrandFolder();
    }

    private void validateLogic(BrandCreateRequest request, boolean isCreated) {
        if (isCreated) {
            Optional<Brand> existingBrand = brandRepository.findByName(request.getName());

            if (existingBrand.isPresent() &&
                    request.getName().equalsIgnoreCase(existingBrand.get().getName())) {

                throw new ValidateException(Translator.toMessage("Thương hiệu đã tồn tại"));
            }
        } else {
            Optional<Brand> existingBrandExit = brandRepository.findByNameAndIdNot(request.getName(), request.getId());

            if (existingBrandExit.isPresent() &&
                    request.getName().equalsIgnoreCase(existingBrandExit.get().getName())) {

                throw new ValidateException(Translator.toMessage("Thương hiệu đã tồn tại"));
            }
        }

    }

    @Override
    public void delete(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ValidateException(Translator.toMessage("thể loại không tồn tại ")));
        MinioUtils.deleteFileMinio(bucketName, brand.getLogo());
        brandRepository.delete(brand);
    }

    @Override
    public Object search(BrandSearchRequest searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("name", searchRequest.getSearchText());
        }
            Specification<Brand> conditions = Specification.where(SearchSpecificationUtil.<Brand>alwaysTrue())
                    .and(SearchSpecificationUtil.or(mapCondition));
            if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
                Page<Brand> page = brandRepository.findAll(conditions, pageable);
                return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
            }
            return brandRepository.findAll(conditions, pageable.getSort());
        }

    @Override
    public BrandDto create(BrandBaseRequest request) {
        BrandCreateRequest createRequest = request.getData();
        this.validateLogic(createRequest, true);
        if(!DataUtils.isNull(request.getImage())){
            createRequest.setLogo(MinioUtils.uploadToMinioAndGetUrl(request.getImage(), folderLocal, bucketName, keyName));
        }
        Brand brand = brandRepository.save(brandMapper.toCreate(createRequest));
        return brandMapper.toDto(brand);
    }

    @Override
    public BrandDto update(BrandBaseRequest request, Long id) {
        return brandRepository.findById(id).map(brand -> {
            BrandCreateRequest createRequest= request.getData();
            createRequest.setId(id);
            this.validateLogic(createRequest, false);
            if (!DataUtils.isNullOrEmpty(request.getImage())){
                createRequest.setLogo(MinioUtils.uploadToMinioAndGetUrl(request.getImage(), folderLocal, bucketName, keyName));
                MinioUtils.deleteFileMinio(bucketName, brand.getLogo());
            }else {
                createRequest.setLogo(brand.getLogo());
            }
            brandMapper.partialUpdate(brand, createRequest);
            brandRepository.save(brand);
            return brandMapper.toDto(brand);
        }).orElseThrow(() -> new ValidateException(Translator.toMessage("error.category.id.not_exist")));
    }

    @Override
    public List<BrandDto> getAll() {
        return brandMapper.toDto(brandRepository.findAll());
    }
}