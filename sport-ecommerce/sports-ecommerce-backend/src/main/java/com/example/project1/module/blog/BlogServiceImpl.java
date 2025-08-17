package com.example.project1.module.blog;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.Blog.BlogMapper;
import com.example.project1.model.config.MinioConfig;
import com.example.project1.model.dto.blog.BlogDto;
import com.example.project1.model.dto.request.Blog.BlogBaseRequest;
import com.example.project1.model.dto.request.Blog.BlogSearch;
import com.example.project1.model.enity.blog.Blog;
import com.example.project1.model.enity.blog.BlogView;
import com.example.project1.module.PageableCustom;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.MinioUtils;
import com.example.project1.utils.SearchSpecificationUtil;
import com.example.project1.utils.TokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional

public class BlogServiceImpl implements BlogService{

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final TokenUtil tokenUtil;
    private final MinioConfig minioConfig;
    private String bucketName ;
    private String folderLocal;
    private String keyName ;
    private final BlogViewRepository blogViewRepository;

    @PostConstruct
    void started() {
        bucketName = minioConfig.getMinioBucketName();
        keyName = minioConfig.getMinioBlogKeyName();
        folderLocal = minioConfig.getMinioBlogFolder();
    }

    public Object createBlog(BlogDto blogDto){
        Blog blog = blogMapper.toEntity(blogDto);
        blog.setCreatedAt(OffsetDateTime.now());

        blog.setUpdatedAt(OffsetDateTime.now());
        blog.setUserId(tokenUtil.getCurrentUserId());
        blogRepository.save(blog);

        return blog;
    }


    public Object create(BlogBaseRequest request) {
        BlogDto createRequest = request.getData();
        createRequest.setUserId(tokenUtil.getCurrentUserId());
        createRequest.setCreatedAt(OffsetDateTime.now());

        createRequest.setUpdatedAt(OffsetDateTime.now());
                if(!DataUtils.isNullOrEmpty(request.getImage())){
            createRequest.setImage(MinioUtils.uploadToMinioAndGetUrl(request.getImage(), folderLocal, bucketName, keyName));
        }
        Blog blog = blogRepository.save(blogMapper.toEntity(createRequest));
        return blog;
    }

    @Override
    public Object getDetail(Long id) {
        BlogView blog = blogViewRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("bài viết không tồn tại ")));
        return blog;
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("bài viết không tồn tại ")));
        blogRepository.delete(blog);
    }

    public void updateBlog(BlogDto blogDto, Long id){
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("bài viết không tồn tại ")));

        Blog blogUpdate = blogMapper.toEntity(blogDto);
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blog.setCategoryId(blogDto.getCategoryId());
        blog.setUpdatedAt(OffsetDateTime.now());
        blog.setUserId(tokenUtil.getCurrentUserId());
        blogRepository.save(blog);
    }


    public Object search(BlogSearch searchRequest, PageableCustom pageable) {
        Map<String, String> mapCondition = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchRequest.getSearchText())) {
            mapCondition.put("title", searchRequest.getSearchText());
        }

        Specification<BlogView> conditions = Specification.where(SearchSpecificationUtil.<BlogView>alwaysTrue())
                .and(SearchSpecificationUtil.equal("categoryId", searchRequest.getStatusId()))
                .and(SearchSpecificationUtil.or(mapCondition));
        if (!DataUtils.isNullOrEmpty(pageable) && !pageable.isFindAll()) {
            Page<BlogView> page = blogViewRepository.findAll(conditions, pageable);
            return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
        }
        return blogViewRepository.findAll(conditions, pageable.getSort());
    }
}
