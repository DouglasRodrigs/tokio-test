package com.example.api.web.rest;

import java.util.List;

import com.example.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.service.CustomerService;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerService service;
	private CustomerRepository repository;

	@Autowired
	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping
	public List<Customer> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
	}
	// Insert new customer
	@PostMapping
	public Customer create(@RequestBody @Valid Customer customer){
		return repository.save(customer);
	}

	// Update customer
	@PutMapping(value="/{id}")
	public ResponseEntity updateBot(@PathVariable("id") long id,
									@RequestBody @Valid Customer customer) {
		return repository.findById(id)
				.map(response -> {
					response.setName(customer.getName());
					response.setEmail(customer.getEmail());
					Customer  customerUpdated = repository.save(response);
					return ResponseEntity.ok().body(customerUpdated);
				}).orElse(ResponseEntity.notFound().build());
	}

	// Delete customer
	@DeleteMapping(path ={"/{id}"})
	public ResponseEntity<?> delete(@PathVariable long id) {
		return repository.findById(id)
				.map(response -> {
					repository.deleteById(id);
					return ResponseEntity.ok().build();
				}).orElse(ResponseEntity.notFound().build());
	}

}
