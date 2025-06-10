package com.example.project1.module;

import com.example.project1.utils.DataUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageableCustom extends PageRequest{

//    private static int size;
//    private static int page;
//    private static List<String> sort;

    private final boolean isFindAll;

    public PageableCustom(int page, int size, boolean isFindAll) {
        super(page, size, Sort.unsorted());
        this.isFindAll = isFindAll;
    }

    public PageableCustom(int page, int size, Sort sort, boolean isFindAll) {
        super(page, size, sort);
        this.isFindAll = isFindAll;
    }

    public static PageableCustom setPageableCustom(int page, int size, List<String> sorts){
        List<Sort.Order> orders = new ArrayList<>();
        if(!DataUtils.isNullOrEmpty(sorts)){
            String[] strSort;
            for (String s : sorts) {
                strSort = s.split("\\.", 2);
                orders.add(new Sort.Order("desc".equalsIgnoreCase(strSort[1]) ? Sort.Direction.DESC : Sort.Direction.ASC, strSort[0]));
            }
        }else{
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        }
        return new PageableCustom(page, size, Sort.by(orders), false);
    }

    public static PageableCustom setPageableCustom(int page, int size, List<String> sorts, boolean isFindAll){
        List<Sort.Order> orders = new ArrayList<>();
        if(!DataUtils.isNullOrEmpty(sorts)){
            String[] strSort;
            for (String s : sorts) {
                strSort = s.split("\\.", 2);
                orders.add(new Sort.Order("desc".equalsIgnoreCase(strSort[1]) ? Sort.Direction.DESC : Sort.Direction.ASC, strSort[0]));
            }
        }else{
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        }
        return new PageableCustom(page, size, Sort.by(orders), isFindAll);
    }

    public boolean isFindAll() {
        return isFindAll;
    }
}
