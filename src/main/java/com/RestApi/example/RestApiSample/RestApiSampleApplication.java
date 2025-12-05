package com.RestApi.example.RestApiSample;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.data.repository.CrudRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@SpringBootApplication
public class RestApiSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiSampleApplication.class, args);
	}

}

@Component
class DataLoader {
	private final CoffeeRepository coffeeRepository;

	public DataLoader(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	@PostConstruct
	private void loadData() {
		coffeeRepository.saveAll(List.of(
			new Coffee("Cappuccino"),
			new Coffee("Latte"),
			new Coffee("Espresso")
		));
	}
}

@RestController
@RequestMapping("/coffees")
class RestApiSampleController {
	private final CoffeeRepository coffeeRepository;

	public RestApiSampleController(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;

		this.coffeeRepository.saveAll(List.of(
			new Coffee("Cappuccino"),
			new Coffee("Latte"),
			new Coffee("Espresso")
		));
	}
	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffeeRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable("id") String id) {
		return coffeeRepository.findById(id);
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		return coffeeRepository.save(coffee);
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable("id") String id, @RequestBody Coffee coffee) {
		return (coffeeRepository.existsById(id))
				? new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK)
				: new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable("id") String id) {
		coffeeRepository.deleteById(id);
	}
}

interface CoffeeRepository extends CrudRepository<Coffee, String> {}

@Entity
class Coffee {
	@Id
	private String id;
	private String name;

	public Coffee() {}

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee (String name) {
		this(UUID.randomUUID().toString(), name);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}