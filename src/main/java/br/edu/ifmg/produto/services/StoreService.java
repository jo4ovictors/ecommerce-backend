package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.StoreDTO;
import br.edu.ifmg.produto.entities.Store;
import br.edu.ifmg.produto.repository.StoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public List<StoreDTO> findAll() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreDTO> getTopStores() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rating"));
        return storeRepository.findAll(pageRequest).getContent()
                .stream()
                .map(StoreDTO::new)
                .toList();
    }


    private StoreDTO convertToDTO(Store store) {
        return new StoreDTO();
    }
}
