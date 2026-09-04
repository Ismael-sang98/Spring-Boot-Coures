package com.isameldev.skybook.vol.service;

import com.isameldev.skybook.vol.dto.VolDTO;
import com.isameldev.skybook.vol.dto.VolResponseDTO;
import com.isameldev.skybook.vol.entity.Vol;
import com.isameldev.skybook.vol.repository.VolRepository;
import org.hibernate.query.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class VolService {

    private final VolRepository repository;

    public VolService(VolRepository repository) {
        this.repository = repository;
    }

    public Vol newVol (VolDTO volDTO){
        Vol vol = new Vol();
        vol.setDestination(volDTO.destination());
        vol.setPrix(volDTO.prix());
        vol.setPlacesDisponibles(volDTO.placeDisponible());

        return  repository.save(vol);
    }

    private List<VolResponseDTO> globalVol(List<Vol> vols){
        List<VolResponseDTO>  listVol = new ArrayList<>();
        for (Vol vol : vols){
            VolResponseDTO volResponseDTO = new VolResponseDTO(
                    vol.getDestination(),
                    vol.getPrix(),
                    vol.getPlacesDisponibles()
            );
            listVol.add(volResponseDTO);
        }
        return listVol;
    }
    public List<VolResponseDTO> getVol(){
        List<Vol> vols = repository.findAll();
        return globalVol(vols);
    }

    public List<VolResponseDTO> getByDestinationVol(String destination){
        List<Vol> vols =  repository.findByDestination(destination);
        return globalVol(vols);
    }

}
