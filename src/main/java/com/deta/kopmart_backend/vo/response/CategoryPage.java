package com.deta.kopmart_backend.vo.response;

import com.deta.kopmart_backend.entity.ProductInfo;
import org.springframework.data.domain.Page;

/**
 * @author deta
 * @description Class for Category Pagination
 */
public class CategoryPage {
    private String category;
    private Page<ProductInfo> page;

    /**
     * @param category Category name
     * @param page Page of ProductInfo
     * @description Constructor for CategoryPage
     */
    public CategoryPage(String category, Page<ProductInfo> page) {
        this.category = category;
        this.page = page;
    }

    /**
     * @return Category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category Category name
     * @description Set Category name
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return Page of ProductInfo
     */
    public Page<ProductInfo> getPage() {
        return page;
    }

    /**
     * @param page Page of ProductInfo
     * @description Set Page of ProductInfo
     */
    public void setPage(Page<ProductInfo> page) {
        this.page = page;
    }
}
