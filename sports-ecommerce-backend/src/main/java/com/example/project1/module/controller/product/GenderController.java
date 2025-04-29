package com.example.project1.module.controller.product;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.enity.product.Gender;
import com.example.project1.module.product.repository.GenderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gender")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenderController {

    private final GenderRepository genderRepository;

    @GetMapping()
    public ResponseResult<List<Gender>> updateCategory() {
        return ResponseResult.ofSuccess(genderRepository.findAll());
    }
}
