package com.instadp.profilepicture.finalfoodappserver.Common;

import com.instadp.profilepicture.finalfoodappserver.Model.User;

/**
 * Created by gaurav on 8/2/2018.
 */

public class Common {
    public static User currentUser;
    public static final  String UPDATE ="Update";
    public static final  String DELETE ="Delete";
    public static String convertCodeToStatus(String code)
 {
     if(code.equals("0")){
     return "Placed";}

     else if(code.equals("1")) {
         return "On my Way";
     }
         else{
             return " Shipped";
         }
         }}

