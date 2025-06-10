package com.example.project1.module.product.service.impl;

import com.example.project1.model.enity.product.Gender;
import com.example.project1.module.product.repository.GenderRepository;
import com.example.project1.module.product.service.GenderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    @Override
    public List<Gender> getAll() {
        return genderRepository.findAll();
    }
}
