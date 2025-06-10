package com.example.project1.model.dto.request.product;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Data
@Slf4j
public class BrandBaseRequest {
    private String data;
    private MultipartFile image;
    public BrandCreateRequest getData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(data, BrandCreateRequest.class);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ValidateException(Translator.toMessage("error.common.parameter.invalid"));
        }
    }
}
