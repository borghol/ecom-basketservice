package com.borghol.omcom.basketService.repository;

import com.borghol.omcom.basketService.model.UserBasket;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserBasketRepository extends CrudRepository<UserBasket, String> {
}