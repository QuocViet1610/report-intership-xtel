package com.example.project1.test.category;
import com.example.project1.cache.CacheUtility;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.product.CategoryMapper;
import com.example.project1.model.Enum.ActionType;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.CategoryCreateRequest;
import com.example.project1.model.enity.product.Category;
import com.example.project1.module.product.repository.CategoryRepository;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.MinioUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryTestServiceImpl implements CategoryTestService {

    CacheUtility cacheUtility;
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    private void validateLogic(CategoryCreateRequest request, boolean isCreated) {
        request.validate();

        request.setName(request.getName().trim());
        if (isCreated) {
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
    public List<CategoryDto> getAll() {
        return cacheUtility.getCategory();
    }

    @Override
    public CategoryDto create(CategoryCreateRequest createRequest) {
        Category category = categoryRepository.save(categoryMapper.toCreate(createRequest));
        cacheUtility.setCategory(category, ActionType.CREATE.getStatus());
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long id, CategoryCreateRequest createRequest) {
        return categoryRepository.findById(id).map(category -> {

            createRequest.setId(id);
            this.validateLogic(createRequest, false);

            // doi parent id, full_parent_id, level
            if (createRequest.getParentId() != null && createRequest.getParentId() != 0) {
                Category parentCategory = categoryRepository.findById(createRequest.getParentId())
                        .orElseThrow(() -> new ValidateException(Translator.toMessage("Thể loại cha không tồn tại")));

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
    public void delete(Long id) {

    }


}