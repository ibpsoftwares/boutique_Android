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

    private static final String HOST_URL = "http://kftsoftwares.com/ecom/recipes/";


    public static final String TOKEN = "ZWNvbW1lcmNl";

    public static final String SIGN_UP = HOST_URL + "signup/"+TOKEN;

    public static final String LOGIN = HOST_URL + "login/"+TOKEN;

    public static final String FORGET_PASSWORD = HOST_URL + "forget/"+TOKEN;

    public static final String GET_CATEGORIES = HOST_URL + "viewCategories/"+TOKEN;

    public static final String GET_ALL_CATEGORIES_BY_ID = HOST_URL + "getbycategory/";

    public static final String GET_ALL_PRODUCTS = HOST_URL + "getallcloths/"+TOKEN;

     public static final String SEARCH_BY_NAME = HOST_URL + "searchbyname/";

     public static final String GET_WISH_LIST = HOST_URL + "ViewWishlist/";

     public static final String ADD_WISH_LIST = HOST_URL + "addToWishList/";

     public static final String REMOVE_FROM_WISHLIST = HOST_URL + "rmwishlist/";

     public static final String GET_SINGLE_PRODUCT = HOST_URL + "getcloth/";

     public static final String ADD_TO_CART = HOST_URL + "addToCart/";
     public static final String VIEW_CART = HOST_URL + "ViewCart/";

     public static final String REMOVE_FROM_CART = HOST_URL + "rmcart/";

     public static final String PRICE_RANGE = HOST_URL + "getpricerange/";

     public static final String SORT_DATA = HOST_URL + "sort/";

     public static final String MOVETOWISHLIST = HOST_URL + "movetocart/";

     public static final String GET_BANNER_IMAGES = HOST_URL + "get_banner_images/"+TOKEN;
}
