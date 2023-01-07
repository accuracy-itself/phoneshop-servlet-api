package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Date;

public class PriceHistory {
    private BigDecimal price;
    private Date date;

    public PriceHistory(BigDecimal price, Date date){
        this.date = date;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }
}
