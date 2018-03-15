package com.kftsoftwares.boutique.utils;

/**
 * Created by apple on 19/02/18.
 */

public class Constants {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String User_ID = "idKey";
    public static final String Email = "emailKey";
    public static final String Checked = "check";
    public static final String DEVICE_UD_ID = "deviceUID";

    private static final String HOST_URL = "http://kftsoftwares.com/ecom/recipes/";


    public static final String TOKEN = "ZWNvbW1lcmNl";
    public static final String UPDATED_TOKEN = "Bearer ZWNvbW1lcmNl";



    public static final String SIGN_UP = HOST_URL + "signup";

    public static final String LOGIN = HOST_URL + "login";

    public static final String FORGET_PASSWORD = HOST_URL + "forget/"+TOKEN;

    public static final String GET_CATEGORIES = HOST_URL + "viewCategories";

    public static final String GET_ALL_CATEGORIES_BY_ID = HOST_URL + "getByCategory";

    public static final String GET_ALL_PRODUCTS = HOST_URL + "getAllCloths";

     public static final String GET_WISH_LIST = HOST_URL + "viewWishlist";

     public static final String ADD_WISH_LIST = HOST_URL + "addToWishList";

     public static final String REMOVE_FROM_WISHLIST = HOST_URL + "rmWishlist/";

     public static final String GET_SINGLE_PRODUCT = HOST_URL + "getCloth";

     public static final String ADD_TO_CART = HOST_URL + "addToCart/";

     public static final String VIEW_CART = HOST_URL + "ViewCart/";

     public static final String REMOVE_FROM_CART = HOST_URL + "rmCart/";

     public static final String PRICE_RANGE = HOST_URL + "getPriceRange/";

     public static final String MOVETOWISHLIST = HOST_URL + "moveToCart/";

     public static final String CHANGE_PASSWORD = HOST_URL + "changePassword/";

     public static final String GET_BANNER_IMAGES = HOST_URL + "getBannerImages";
}
