package ap2.Visons.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ap2.Visons.model.Provider;
import ap2.Visons.repository.ProviderRepository;
import ap2.Visons.service.ProviderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProviderServiceImpl implements ProviderService {

    // ✅ Inyección del repository
    private final ProviderRepository providerRepository;

    @Autowired
    public ProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    // 🛠️🔍 Implementación del método Listar Todos (Solo Activos)
    @Override
    public List<Provider> findAll() {
        log.info("Listando proveedores activos");
        return providerRepository.findByIsActive(true);
    }

    // 🛠️🔍 Implementación del método Listar por Estado
    @Override
    public List<Provider> findByState(String state) {
        log.info("Listando proveedores por estado: {}", state);
        boolean active = "A".equalsIgnoreCase(state) || "1".equals(state) || "true".equalsIgnoreCase(state);
        return providerRepository.findByIsActive(active);
    }

    // 🛠️🔍 Implementación del método Listar por ID
    @Override
    public Optional<Provider> findById(Integer id) {
        log.info("Buscando proveedor por ID: {}", id);
        return providerRepository.findById(id);
    }

    // 🛠️✅ Implementación del método Registrar
    @Override
    public Provider save(Provider provider) {
        log.info("Registrando proveedor: {}", provider);
        LocalDateTime now = LocalDateTime.now();
        provider.setProviderId(null);
        provider.setIsActive(true);
        provider.setCreatedAt(now);
        provider.setUpdatedAt(null);
        provider.setDeletedAt(null);
        provider.setRestoredAt(null);
        return providerRepository.save(provider);
    }

    // 🛠️✏️ Implementación del método Actualizar
    @Override
    public Provider update(Integer id, Provider provider) {
        log.info("Editando proveedor: {}", id);
        Provider existing = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + id));

        existing.setCompanyName(provider.getCompanyName());
        existing.setTaxId(provider.getTaxId());
        existing.setProductType(provider.getProductType());
        existing.setUpdatedAt(LocalDateTime.now());

        return providerRepository.save(existing);
    }

    // 🛠️❌ Implementación del método Eliminar (Cambio de Estado) por ID
    @Override
    public Provider delete(Integer id) {
        log.info("Eliminando lógico proveedor: {}", id);
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + id));
        provider.setDeletedAt(LocalDateTime.now());
        provider.setIsActive(false);
        return providerRepository.save(provider);
    }

    // 🛠️♻️ Implementación del método Restaurar (Cambio de Estado) por ID
    @Override
    public Provider restore(Integer id) {
        log.info("Restaurando lógico proveedor: {}", id);
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + id));
        provider.setRestoredAt(LocalDateTime.now());
        provider.setIsActive(true);
        return providerRepository.save(provider);
    }

}
