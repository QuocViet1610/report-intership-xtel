package com.example.project1.mapper.Blog;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.blog.BlogDto;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.respone.CartItemResponse;
import com.example.project1.model.enity.blog.Blog;
import com.example.project1.model.enity.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class BlogMapper implements EntityMapper<BlogDto, Blog> {

}
