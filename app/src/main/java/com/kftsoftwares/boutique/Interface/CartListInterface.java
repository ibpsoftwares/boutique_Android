package com.kftsoftwares.boutique.Interface;

import com.kftsoftwares.boutique.Models.CartViewModel;

import java.util.ArrayList;

/**
 * Created by apple on 22/02/18.
 */

public interface CartListInterface {


    public void updateCartList(String value);

    void updatePrice(ArrayList<CartViewModel> arrayList);
}
