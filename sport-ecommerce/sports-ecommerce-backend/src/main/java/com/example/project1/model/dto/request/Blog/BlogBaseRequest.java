package com.example.project1.model.dto.request.Blog;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.model.dto.blog.BlogDto;
import com.example.project1.model.dto.request.product.CategoryCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Data
@Slf4j
public class BlogBaseRequest {
    private String data;
    private MultipartFile image;
    public BlogDto getData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(data, BlogDto.class);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ValidateException(Translator.toMessage("error.common.parameter.invalid"));
        }
    }
}
