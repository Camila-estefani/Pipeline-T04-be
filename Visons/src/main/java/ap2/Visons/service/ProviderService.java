package ap2.Visons.service;

import ap2.Visons.model.Provider;
import java.util.List;
import java.util.Optional;

public interface ProviderService {

    List<Provider> findAll();

    List<Provider> findByState(String state);

    Optional<Provider> findById(Integer id);

    Provider save(Provider provider);

    Provider update(Integer id, Provider provider);

    Provider delete(Integer id);

    Provider restore(Integer id);

}
