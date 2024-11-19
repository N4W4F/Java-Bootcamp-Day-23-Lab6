package com.example.employeemanagement.Controller;

import com.example.employeemanagement.ApiResponse.ApiResponse;
import com.example.employeemanagement.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get-employees")
    public ResponseEntity getEmployees() {
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add-employee")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee has been added successfully"));
    }

    @PutMapping("/update-employee/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        for (int i = 0; i < employees.size(); i++)
            if (employees.get(i).getId().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.status(200).body(new ApiResponse("Employee with ID: " + id + " has been updated successfully"));
            }
        return ResponseEntity.status(404).body(new ApiResponse("Employee with ID: " + id + " not found"));
    }

    @DeleteMapping("/delete-employee/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {
        for (Employee e : employees)
            if (e.getId().equals(id)) {
                employees.remove(e);
                return ResponseEntity.status(200).body(new ApiResponse("Employee with ID: " + id + " has been deleted successfully"));
            }
        return ResponseEntity.status(404).body(new ApiResponse("Employee with ID: " + id + " not found"));
    }

    @GetMapping("/get-by-position")
    public ResponseEntity getEmployeeByPosition(@RequestBody String position) {
        ArrayList<Employee> positionEmployees = new ArrayList<>();

        for (Employee e : employees)
            if(e.getPosition().equalsIgnoreCase(position))
                positionEmployees.add(e);

        if (positionEmployees.isEmpty())
            return ResponseEntity.status(404).body(new ApiResponse("There is no employee with this position"));

        return ResponseEntity.status(200).body(positionEmployees);
    }

    @GetMapping("/get-by-age")
    public ResponseEntity getEmployeeByAge(@RequestParam @Valid int minAge, @RequestParam @Valid int maxAge) {
        ArrayList<Employee> newEmployees = new ArrayList<>();

        for (Employee e : employees)
            if (e.getAge() >= minAge && e.getAge() <= maxAge)
                newEmployees.add(e);

        if (newEmployees.isEmpty())
            return ResponseEntity.status(404).body(new ApiResponse("There is no employee within this range of age"));

        return ResponseEntity.status(200).body(newEmployees);
    }

    @PutMapping("/apply-for-leave")
    public ResponseEntity applyForLeave(@RequestBody Employee employee) {
        for (Employee e : employees) {
            if (e.equals(employee)) {
                if (e.getAnnualLeave() > 0) {
                    if (!e.isOnLeave()) {
                        e.setOnLeave(true);
                        e.setAnnualLeave(e.getAnnualLeave() - 1);
                        return ResponseEntity.status(200).body(new ApiResponse("Annual Leave has been accepted, total days remaining: " + e.getAnnualLeave()));
                    } else return ResponseEntity.status(400).body(new ApiResponse("Employee is already on a leave"));
                } else return ResponseEntity.status(400).body(new ApiResponse("Employee is out of annual leaves"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/get-no-leaves")
    public ResponseEntity getEmployeesWithNoLeave() {
        ArrayList<Employee> newEmployees = new ArrayList<>();

        for (Employee e : employees)
            if (!e.isOnLeave())
                newEmployees.add(e);

        return ResponseEntity.status(200).body(newEmployees);
    }

    @PutMapping("/promote-employee/{id}")
    public ResponseEntity promoteEmployee(@PathVariable String id, @RequestBody @Valid Employee requester, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        if (requester.getPosition().equals("Supervisor")) {
            for (Employee e : employees) {
                if (e.getId().equals(id) && e.getAge() >= 30 && !e.isOnLeave()) {
                    e.setPosition("Supervisor");
                    return ResponseEntity.status(200).body("Employee with ID: " + id + " has been promoted successfully");
                }
            }
        } else return ResponseEntity.status(400).body(new ApiResponse("Only supervisors can promote employees"));
        return ResponseEntity.status(400).body(new ApiResponse("You cannot promote this employee in the meantime"));
    }
}
