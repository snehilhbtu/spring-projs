package com.example.SpringBootMethod;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@Controller       use RestController instead of these both
//@ResponseBody
@RestController
@RequestMapping("employee") //parent url
public class EmployeeController {

    @GetMapping("getEmployee")
    public ResponseEntity<Employee> getEmployee(){
        Employee employee=new Employee(1,"ram","kanpur");
        return  ResponseEntity.ok(employee);
    }

    @GetMapping("getEmployees")
    public ResponseEntity<List<Employee>> getEmployees(){
        List<Employee> employeeList=new ArrayList<Employee>();

        employeeList.add(new Employee(1,"ram","kanpur"));
        employeeList.add(new Employee(2,"rama","kanpurr"));
        employeeList.add(new Employee(3,"ram0","kanpurrr"));

        return  ResponseEntity.ok(employeeList);
    }

    @GetMapping("getEmployee/{id}/name")
    public ResponseEntity<Employee> getEmployeeWithId(@PathVariable("id") int id){


       Employee employee=new Employee(id,"name with id"+id,"kanpur");

        //response body with header
        return  ResponseEntity.ok()
                .header("IDE","IntelliJ")
                .body(employee);
    }

    @GetMapping("getEmployee/id/{id}/name/{name}")
    public ResponseEntity<Employee> getEmployeeWithId(@PathVariable("id") int id,@PathVariable("name") String name){

        Employee employee=new Employee(id,name,"kanpur");

        return  ResponseEntity.ok(employee);
    }

    //url will be "getEmployee/withIdNameAddress?id=1&name=snehil&address=lucknow"
    @GetMapping("getEmployee/withIdNameAddress")
    public ResponseEntity<Employee> getEmployeeWithId(@RequestParam int id,@RequestParam String name,@RequestParam String address){

        Employee employee=new Employee(id,name,address);

        return  ResponseEntity.ok(employee);
    }

    @PostMapping("create")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        System.out.println(employee.getId()+" "+employee.getName()+" "+employee.getAddress());

        //return ResponseEntity.ok(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,@PathVariable("id")int id){
        System.out.println("given id is "+id+" "+employee.getName()+" "+employee.getAddress());

        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("{id}/delete")
    public String deleteEmployee(@PathVariable("id")int id){
        System.out.println("given id to delete is "+id);

        return "deleted"+id;
    }

}
