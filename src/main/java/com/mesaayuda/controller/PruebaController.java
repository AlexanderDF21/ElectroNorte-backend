package com.mesaayuda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PruebaController {

    @GetMapping("/prueba")
    public String prueba() {
        return "API de Mesa de Ayuda funcionando correctamente";
    }
}