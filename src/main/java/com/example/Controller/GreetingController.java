package com.example.Controller;

import com.example.Anotation.*;;

/**
 * Controlador del sevicio
 */
@RestController
public class GreetingController {
	//Método static
    //Anotación quie define los métodos a ejecutar
	@GetMapping("/greeting")
	public static String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "Hola " + name;
	}
        
    @GetMapping("/pi")
	public static String pi(@RequestParam(value = "name", defaultValue = "World") String name) {
		return Double.toString(Math.PI);
	}
}
