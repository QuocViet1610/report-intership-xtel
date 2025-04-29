package com.example.project1.model.dto.request.product;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Slf4j
public class ProductBaseRequest {
    private String data;
    private List<MultipartFile> image;
    public ProductCreateRequest getData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            ProductCreateRequest productCreateRequest= objectMapper.readValue(data, ProductCreateRequest.class);
            return productCreateRequest;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ValidateException(Translator.toMessage("error.common.parameter.invalid"));
        }
    }
}
