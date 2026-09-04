package com.isameldev.skybook.vol.controller;

import com.isameldev.skybook.vol.dto.VolDTO;
import com.isameldev.skybook.vol.dto.VolResponseDTO;
import com.isameldev.skybook.vol.entity.Vol;
import com.isameldev.skybook.vol.service.VolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vols")
public class VolController {

    private final VolService service;

    public VolController(VolService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Vol> postVol(@RequestBody VolDTO volDTO){
        return ResponseEntity.ok(service.newVol(volDTO));
    }

    @GetMapping
    public ResponseEntity<List<VolResponseDTO>> getVol(){
        return ResponseEntity.ok(service.getVol());
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<VolResponseDTO>> getByDestinationVol(@RequestParam String destination){
        return ResponseEntity.ok(service.getByDestinationVol(destination));
    }

}

