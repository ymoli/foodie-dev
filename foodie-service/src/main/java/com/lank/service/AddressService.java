package com.lank.service;

import com.lank.pojo.UserAddress;
import com.lank.pojo.bo.AddressBo;

import java.util.List;

public interface AddressService {

    //根据用户id查询地址列表
    public List<UserAddress> queryAll(String userId);
    //用户新增地址
    public void addNewUserAddress(AddressBo addressBo);
    //用户修改地址
    public void updateUserAddress(AddressBo addressBo);
    //根据用户id和地址id删除对应的地址信息
    public void deleteUserAddress(String userId,String addressId);
    //修改默认地址
    public void updateUserAddressToBeDefault(String userId,String addressId);
}
