package com.example.project1.module.Order.service.Impl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.Order.AddressMapper;
import com.example.project1.model.dto.Order.AddressDto;
import com.example.project1.model.dto.request.Order.AddressCreateRequest;
import com.example.project1.model.enity.order.Address;
import com.example.project1.module.Order.repository.AddressRepository;
import com.example.project1.module.Order.service.AddressService;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressServiceImpl implements AddressService {

    AddressRepository addressRepository;
    AddressMapper addressMapper;
    TokenUtil tokenUtil;

    @Override
    public AddressDto create(AddressCreateRequest request) {
        Long userId = tokenUtil.getCurrentUserId();
        request.setUserId(userId);
        if (request.getIsDefault() == 1){
            addressRepository.updateAllAddressesToDefault();
        }
        Address address = addressRepository.save(addressMapper.toCreate(request));
        return addressMapper.toDto(address);
    }

    @Override
    public AddressDto update(Long id, AddressCreateRequest request) {
        if (Integer.valueOf(1).equals(request.getIsDefault()) ){
            addressRepository.updateAllAddressesToDefault();
        }
        return addressRepository.findById(id).map(address -> {
            Long userId = tokenUtil.getCurrentUserId();
            request.setUserId(userId);
            addressMapper.partialUpdate(address, request);
            addressRepository.save(address);
            return addressMapper.toDto(address);
        }).orElseThrow(() -> new ValidateException(Translator.toMessage("Địa chỉ không tồn tại")));
    }

    @Override
    public void delete(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ValidateException(Translator.toMessage("Địa chỉ không tồn tại ")));
        addressRepository.delete(address);
    }

    @Override
    public List<AddressDto> getAll() {
        Long userId = tokenUtil.getCurrentUserId();
        return addressMapper.toDto(addressRepository.findAllByUserId(userId));
    }

    @Override
    public AddressDto getDetail(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ValidateException(Translator.toMessage("Địa chỉ không tồn tại ")));
        return addressMapper.toDto(address);
    }
}
