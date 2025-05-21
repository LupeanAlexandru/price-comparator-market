package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.StoreDTO;
import com.example.price_comparator_market.exception.StoreNotFoundException;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * Retrieves all stores from the repository and converts them to DTOs.
     * <p>
     * This method fetches all {@code Store} entities, maps each to a {@code StoreDTO},
     * and returns them as a list.
     *
     * @return a list of all {@code StoreDTO}s
     */
    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a store by its ID and converts it to a DTO.
     * <p>
     * If a store with the specified ID exists, it is returned as a {@code StoreDTO}.
     * Otherwise, a {@code StoreNotFoundException} is thrown.
     *
     * @param id the ID of the store to retrieve
     * @return the {@code StoreDTO} corresponding to the given ID
     * @throws StoreNotFoundException if no store is found with the specified ID
     */

    public StoreDTO getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + id));
        return mapToDTO(store);
    }

    private StoreDTO mapToDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        return dto;
    }
}