package com.kftsoftwares.boutique.Interface;

import com.kftsoftwares.boutique.Models.CartViewModel;

import java.util.ArrayList;

/**
 * Created by apple on 22/02/18.
 */

public interface WishListInterfaceForActivity {



    public void moveToWishList(String clothId, int position, ArrayList<CartViewModel> cartViewModels);
}
