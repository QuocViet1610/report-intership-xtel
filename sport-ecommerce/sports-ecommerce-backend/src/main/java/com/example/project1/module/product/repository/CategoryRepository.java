package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    List<Category> findByNameIgnoreCase(@Param("name") String name);

    Optional<Category> findByNameIgnoreCaseAndIdNot(String name, Long id);



    @Query(value = "SELECT id FROM category WHERE FIND_IN_SET(:value, full_parent_id) > 0", nativeQuery = true)
    List<Long> findIdsByFullParentIdContaining(@Param("value") String value);

    @Modifying
    @Query("DELETE FROM Category c WHERE c.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);


    @Modifying
    @Query(value = "UPDATE category c SET c.full_parent_id = CONCAT(:param1, SUBSTRING(c.full_parent_id, LOCATE(CAST(:param2 AS CHAR), c.full_parent_id))) WHERE FIND_IN_SET(CAST(:param2 AS CHAR), c.full_parent_id) > 0", nativeQuery = true)
    void updateFullParentId(@Param("param1") String fullParentId, @Param("param2") Long id);

    @Modifying
    @Query(value = "UPDATE category c SET c.level = c.level + :increment WHERE FIND_IN_SET(CAST(:param AS CHAR), c.full_parent_id) > 0", nativeQuery = true)
    void updateLevelForCategories(@Param("increment") Long increment, @Param("param") Long id);


}
