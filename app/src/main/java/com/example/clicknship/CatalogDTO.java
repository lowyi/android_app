package com.example.clicknship;

public class CatalogDTO {
    private String imgUrlCat;
    private String nameCat;
    private String descriptionCat;
    private String priceCat;

    public CatalogDTO(String imgUrl, String name, String description, String price) {
        imgUrlCat = imgUrl;
        nameCat = name;
        descriptionCat = description;
        priceCat = price;
    }

    public String getImgUrl() {
        return imgUrlCat;
    }
    public String getName() {
        return nameCat;
    }
    public String getDescription() {
        return descriptionCat;
    }
    public String getPrice() {
        return priceCat;
    }

}
