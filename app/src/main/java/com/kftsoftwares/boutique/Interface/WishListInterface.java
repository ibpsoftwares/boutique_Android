package com.kftsoftwares.boutique.Interface;

import com.kftsoftwares.boutique.Models.CartViewModel;

import java.util.ArrayList;

/**
 * Created by apple on 22/02/18.
 */

public interface WishListInterface  {


    public void updateWishList(String value, int position, ArrayList<CartViewModel> cartViewModels);

    public void moveToWishList( String clothId, int position, ArrayList<CartViewModel> cartViewModels);
}
