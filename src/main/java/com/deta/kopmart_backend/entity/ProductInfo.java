package com.deta.kopmart_backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@NoArgsConstructor
public class ProductInfo implements Serializable {

    @Id
    private String productId;

    @NotNull
    private String productName;

    @NotNull
    private BigDecimal productPrice;

    @NotNull
    @Min(0)
    private Integer productStock;

    @NotNull
    private String productDescription;

    @NotNull
    private String productIcon;

    @NotNull
    @ColumnDefault("0")
    private Integer productStatus;

    @NotNull
    @ColumnDefault("0")
    private Integer categoryType;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

}
