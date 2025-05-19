package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.StoreDTO;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Retrieves all stores from the repository and converts them to DTOs.
     * <p>
     * This method fetches all {@code Store} entities, maps each to a {@code StoreDTO},
     * and returns them as a list.
     *
     * @return a list of all stores represented as {@code StoreDTO}s
     */
    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a store by its ID and converts it to a DTO.
     * <p>
     * If a store with the specified ID exists in the repository, it is returned
     * as a {@code StoreDTO}. Otherwise, a {@code RuntimeException} is thrown.
     *
     * @param id the ID of the store to retrieve
     * @return the {@code StoreDTO} corresponding to the given ID
     * @throws RuntimeException if no store is found with the specified ID
     */
    public StoreDTO getStoreById(Long id) {
        return storeRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
    }

    /**
     * Retrieves an existing store by its name or creates and saves a new one if it does not exist.
     * <p>
     * If a store with the specified name exists in the repository, it is returned.
     * Otherwise, a new {@code Store} is created with the provided name and saved.
     *
     * @param name the name of the store
     * @return the existing or newly created {@code Store}
     */
    public Store getOrCreateStore(String name) {
        Optional<Store> existingStore = storeRepository.findByName(name);
        if (existingStore.isPresent()) {
            return existingStore.get();
        } else {
            Store newStore = new Store();
            newStore.setName(name);
            return storeRepository.save(newStore);
        }
    }

    private StoreDTO mapToDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setName(store.getName());
        return dto;
    }
}