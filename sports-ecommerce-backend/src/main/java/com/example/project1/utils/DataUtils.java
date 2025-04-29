package com.example.project1.utils;

import com.example.project1.middleware.annotation.Nested;
import com.example.project1.model.config.LocalDateAdapter;
import com.example.project1.model.config.OffsetDateTimeAdapter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataUtils {

    public static void trimValues(Object model) {
        List<Field> allField = Arrays.stream(model.getClass().getDeclaredFields()).collect(Collectors.toList());
        if (model.getClass().getSuperclass() != null) {
            List<Field> allFieldParent = Arrays.stream(model.getClass().getSuperclass().getDeclaredFields()).collect(Collectors.toList());
            allField.addAll(allFieldParent);
        }
        for (Field field : allField) {
            try {
                field.setAccessible(true);
                Object value = field.get(model);
                if (value != null) {
                    if (value instanceof String) {
                        String strValue = (String) value;
                        field.set(model, strValue.trim());
                    }else if(value instanceof List){
                        try{
                            List<String> strings = (List<String>) value;
                            strings = strings.stream().map(String::trim).collect(Collectors.toList());
                            field.set(model, strings);
                        }catch (Exception e){

                        }
                    }
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static void trimNested(Object nestedObject) {
        if (nestedObject != null) {
            DataUtils.trimValues(nestedObject);
            Field[] fields = nestedObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Nested) {
                        Nested nested = (Nested) annotation;
                        field.setAccessible(true);
                        if (nested.isCollection()) {
                            Collection<Object> collection = (Collection<Object>) ReflectionUtils.getField(field, nestedObject);
                            if (collection != null)
                                collection.forEach(DataUtils::trimNested);
                        } else {
                            Object value = ReflectionUtils.getField(field, nestedObject);
                            if (value != null) {
                                DataUtils.trimValues(value);
                                trimNested(value);
                            }
                        }
                    }
                }
            }
        }
    }

    public static <T> List<String> validate(T obj) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!CollectionUtils.isEmpty(violations)) {
            return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value.trim());
    }
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    public static <T> List<T> jsonToList(String json, Class<T> classOutput) {
        if (isNullOrEmpty(json)) {
            return new ArrayList<>();
        }
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        try {
            return objectMapper
                    .readValue(json, typeFactory.constructCollectionType(List.class, classOutput));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public static boolean isNullOrEmpty(final Object obj) {
        return obj == null || obj.toString().isEmpty();
    }

    public static <T> T jsonToObject(String jsonData, Class<T> classOutput) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(jsonData, classOutput);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return jsonToObjectFronGson(jsonData, classOutput);
        } catch (Throwable i){
            return jsonToObjectFronGson(jsonData, classOutput);
        }
    }

    private static <T> T jsonToObjectFronGson(String jsonData, Class<T> classOutput) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                    .create();
            return gson.fromJson(jsonData, classOutput);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static String convertNameToCode(String str) {
        str = str.trim();
        str = str.toLowerCase();
        str = str.replaceAll(" +", " ");
        str = str.replaceAll("\\s", "_");
        str = str.replace("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replace("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replace("ì|í|ị|ỉ|ĩ", "i");
        str = str.replace("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replace("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replace("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replace("đ", "d");
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(nfdNormalizedString).replaceAll("");
        return str;
    }

    public static boolean notNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }


    public static String createCodeByKey(String key, String codeNumber) {
        key = getCode(key);
        String transactionCode = key + codeNumber;

        return transactionCode;
    }

    public static String createCode() {
        String code = String.format("%06d", new Random().nextInt(1000000));
        return code;
    }
    public static String getCode(String name) {
        String[] words = name.split("\\s+");
        StringBuilder initials = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
            if (initials.length() == 2) break;
        }

        return initials.toString().toUpperCase();
    }

    private static String minioHost;
    private static String bucketName;
    @Value("${app.minio.bucketName}")
    public void setBucketName(String bucketName) {
        DataUtils.bucketName = bucketName;
    }


    @Value("${app.minio.host}")
    public void setMinioHost(String minioHost) {
        DataUtils.minioHost = minioHost;
    }


//    public static String convertUrl(String url) {
//        String tmp_Url = "";
//        if (!DataUtils.isNullOrEmpty(url)) {
//            if (url.contains(minioHost) || url.contains("https://assets.plugshare.com"))
//                tmp_Url = url;
//            else
//                tmp_Url = minioHost+ bucketName+"/" + url;
//        }
//        return tmp_Url;
//    }

    public static String convertUrl(String url) {
        String modifiedString = url.replace("/", "//");
        String tmp_Url = "src//upload//"+modifiedString;

        return tmp_Url;
    }
}
