package com.borghol.omcom.basketService;

import com.borghol.omcom.basketService.model.ItemLocation;
import com.borghol.omcom.basketService.model.UserBasketItem;
import com.borghol.omcom.basketService.model.UserType;
import com.borghol.omcom.basketService.repository.UserBasketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class BasketServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BasketServiceApplication.class, args);
	}

	@Autowired
	private UserBasketRepository userBasketRepository;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		UserBasketItem item = UserBasketItem.builder().count(1).userId("borghol").itemId("someRandomItem2").location(ItemLocation.BASKET).userType(UserType.USER).build();
		userBasketRepository.putItem(item).flatMap(res -> {
			res.attributes().forEach((name, value) -> {
				System.out.println(name + ":" + value.s());
			});
			return userBasketRepository.findAllByUserId("borghol");
		}).map(res -> {
			res.items().forEach(map -> {
				map.forEach((action, val) -> System.out.println(action + ": " + val.s()));
			});
			return Mono.empty();
		}).subscribe();

		// userBasketRepository.getTable()
		// 	.map(x -> {
		// 		x.table().keySchema().forEach(key -> {
		// 			System.out.println("key: " + key.attributeName());
		// 		});

		// 		x.table().globalSecondaryIndexes().forEach(action -> {
		// 			System.out.println(action.indexName());
		// 		});
		// 		return x;
		// 	}).subscribe();
	}

}
