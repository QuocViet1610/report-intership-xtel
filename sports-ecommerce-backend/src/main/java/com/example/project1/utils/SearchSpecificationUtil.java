package com.example.project1.utils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

public final class SearchSpecificationUtil {

    private SearchSpecificationUtil() {
        throw new RuntimeException("Class is static");
    }

    /* starting where 1=1 and load class type */
    public static <T> Specification<T> alwaysTrue() {
        return (r, q, b) -> b.equal(b.literal(1), 1);
    }

    /* Condition: 1 = 0 */
    public static <T> Specification<T> alwaysFalse() {
        return (r, q, b) -> b.notEqual(b.literal(1), 1);
    }

    /* Condition: key LIKE value */
    public static <T> Specification<T> likeField(String key, String value) {
        return !StringUtils.isBlank(value) ? (r, q, b) -> b.like(b.upper(r.get(key)), "%" + likeSpecialToStr(value).toUpperCase() + "%") : null;
    }

    public static <T> Specification<T> likeFieldCategory(String key, String value) {
        return !StringUtils.isBlank(value) ? (r, q, b) -> b.or(
                b.like(b.upper(r.get(key)), "%," + likeSpecialToStr(value).toUpperCase() + ",%"),
                b.like(b.upper(r.get(key)), likeSpecialToStr(value).toUpperCase() + ",%"),
                b.like(b.upper(r.get(key)), "%," + likeSpecialToStr(value).toUpperCase())
        ) : null;
    }

    public static <T> Specification<T> equalIdConditionOr(String key, Long value) {
        return value != null ? (r, q, b) -> b.or(
                b.equal(r.get(key), value)
        ) : null;
    }




    /* Condition: key IN value */
    public static <T> Specification<T> inField(String key, Collection<?> values) {
        return !CollectionUtils.isEmpty(values) ? (r, q, b) -> r.get(key).in(values) : null;
    }

    /* Condition: key NOT IN value */
    public static <T> Specification<T> notInField(String key, Collection<?> values) {
        return !CollectionUtils.isEmpty(values) ? (r, q, b) -> r.get(key).in(values).not() : null;
    }

    /* Condition: key LIKE value -> ignore case */
    public static <T> Specification<T> inFieldIgnoreCase(String key, Collection<String> values) {
        return !CollectionUtils.isEmpty(values) ? (r, q, b) -> b.upper(r.get(key)).in(values.stream().map(String::toUpperCase)) : null;
    }

    /* Condition: key > value */
    public static <T, V extends Number> Specification<T> ge(String key, V value) {
        return value != null ? (r, q, b) -> b.ge(r.get(key), value) : null;
    }

    /* Condition: key < value */
    public static <T, V extends Number> Specification<T> le(String key, V value) {
        return value != null ? (r, q, b) -> b.le(r.get(key), value) : null;
    }

    /* Condition: key >= value */
    public static <T, V extends Comparable<? super V>> Specification<T> gee(String key, V value) {
        return value != null ? (r, q, b) -> b.greaterThanOrEqualTo(r.get(key), value) : null;
    }

    /* Condition: key <= value */
    public static <T, V extends Comparable<? super V>> Specification<T> lee(String key, V value) {
        return value != null ? (r, q, b) -> b.lessThanOrEqualTo(r.get(key), value) : null;
    }

    /* Condition: key between start AND end */
    public static <T, V extends Comparable<? super V>> Specification<T> between(String key, V startPoint, V endPoint) {
        return startPoint != null && endPoint != null && startPoint.compareTo(endPoint) < 0 ? (r, q, b) -> b.between(r.get(key), startPoint, endPoint) : null;
    }

    /* Condition: key = value */
    public static <T> Specification<T> equal(String key, Object value) {
        return Objects.nonNull(value) ? (r, q, b) -> b.equal(r.get(key), value) : null;
    }

    public static <T> Specification<T> isNull(String key) {
        return (r, q, b) -> b.isNull(r.get(key));
    }

    public static <T> Specification<T> isNotNull(String key) {
        return (r, q, b) -> b.isNotNull(r.get(key));
    }

    /* Condition: key = value -> ignore case */
    public static <T> Specification<T> equalIgnoreCase(String key, String value) {
        return !StringUtils.isBlank(value) ? (r, q, b) -> b.equal(b.upper(r.get(key)), value.toUpperCase()) : null;
    }

    public static final char KEY_ESCAPE = '\\';

    public static String likeSpecialToStr(String str) {
//        str = str.replace("_", KEY_ESCAPE + "_");
//        str = str.replace("%", KEY_ESCAPE + "%");
        return str.trim();
    }

    /* Condition: key >= value */
    public static <T, V extends Comparable<? super V>> Specification<T> gee(String key, LocalDateTime value) {
        return value != null ? (r, q, b) -> b.greaterThanOrEqualTo(r.get(key), value) : null;
    }

    /* Condition: key <= value */
    public static <T, V extends Comparable<? super V>> Specification<T> lee(String key, LocalDateTime value) {
        return value != null ? (r, q, b) -> b.lessThanOrEqualTo(r.get(key), value) : null;
    }

    public static <T> Specification<T> likeFieldStartWith(String key, String value) {
        return !StringUtils.isBlank(value) ? (r, q, b) -> b.like(b.upper(r.get(key)), likeSpecialToStr(value).toUpperCase() + "%") : null;
    }

    public static <T> Specification<T> likeFieldEndWith(String key, String value) {
        return !StringUtils.isBlank(value) ? (r, q, b) -> b.like(b.upper(r.get(key)), "%"+likeSpecialToStr(value).toUpperCase()) : null;
    }


    public static <T> Specification<T> or(Map<String ,String> condition){
        if(condition.isEmpty()) return null;
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> list = new ArrayList<>();
            condition.forEach((key, value) -> predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(key)), "%" + likeSpecialToStr(value).toUpperCase() + "%")));
            list.add(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };

    }

    public static <T> Specification<T> join(Map<String, Map<String, String>> conditions, JoinType joinType, String joinField) {
        return (root, query, criteriaBuilder) -> {
            Join<Objects, Objects> join = root.join(joinField, joinType);
            List<Predicate> list = new ArrayList<>();
            conditions.forEach((type, condition) -> {
                switch (type) {
                    case "inField" -> condition.forEach((key, value) -> {
                        list.add(join.get(key).in(DataUtils.jsonToList(value, String.class)));
                    });
                    case "equal" -> condition.forEach((key, value) -> {
                        list.add(criteriaBuilder.equal(join.get(key), value));
                    });
                    case "like" -> condition.forEach((key, value) -> {
                        list.add(criteriaBuilder.like(criteriaBuilder.upper(join.get(key)), "%" + likeSpecialToStr(value).toUpperCase() + "%"));
                    });
                    case "likeAndOr" -> {
                        List<Predicate> predicates = new ArrayList<>();
                        condition.forEach((key, value) -> predicates.add(criteriaBuilder.like(criteriaBuilder.upper(join.get(key)), "%" + likeSpecialToStr(value).toUpperCase() + "%")));
                        list.add(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
                    }
                }
            });
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }
}
