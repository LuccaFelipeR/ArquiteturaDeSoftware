package br.com.hub.adaptadores.entrada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @GetMapping("/status")
    public String status() {
        return "Hub de faturamento funcionando!";
    }
}
