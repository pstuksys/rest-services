package com.paulius.demo.controller;

import com.paulius.demo.exception.EmployeeNotFoundException;
import com.paulius.demo.model.Employee;
import com.paulius.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repository;

    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }
    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee employee){
        return repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable("id")Long id){
        return repository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));
    }
    @PutMapping("/employees/{id}")
    Employee editEmployee(@RequestBody Employee newEmployee,
                          @PathVariable("id")Long id){
        return repository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return repository.save(employee);
                }).orElseGet(()->{
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
        });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id){
        repository.deleteById(id);
    }
}
