package pe.edu.vallegrande.vg_ms_casas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.vg_ms_casas.dto.ProductDTO;
import pe.edu.vallegrande.vg_ms_casas.model.Consumption;
import pe.edu.vallegrande.vg_ms_casas.repository.ConsumptionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Service
public class ConsumptionService {

    @Autowired
    private ConsumptionRepository consumptionRepository;

    // Mantener tu WebClient como lo tenías
    private final WebClient productWebClient = WebClient.builder()
            .baseUrl("https://ms-product-ix0t.onrender.com/NPH/products")
            .defaultHeader("Content-Type", "application/json")
            .build();

    public Flux<Consumption> findAll() {
        return consumptionRepository.findAll();
    }

    public Mono<Consumption> findById(Integer id) {
        return consumptionRepository.findByIdWithNames(id); // Ahora incluirá productId
    }

    public Mono<Consumption> save(Consumption consumption) {
        if (consumption.getStatus() == null) {
            consumption.setStatus("A");
        }
        return consumptionRepository.save(consumption);
    }

    // Mantener los demás métodos igual
    public Mono<Consumption> update(Integer id, Consumption consumption) {
        return consumptionRepository.updateConsumption(
                id,
                consumption.getDate(),
                consumption.getId_home(),
                consumption.getProductId(), // Asegurar que se use productId
                consumption.getQuantity(),
                consumption.getWeight(),
                consumption.getPrice(),
                consumption.getSalevalue()
        ).then(findById(id));
    }

    public Mono<Void> delete(Integer id) {
        return consumptionRepository.inactivateConsumption(id);
    }

    public Mono<Void> restore(Integer id) {
        return consumptionRepository.restoreConsumption(id);
    }

    public Flux<Consumption> findActive() {
        return consumptionRepository.findByStatusWithNames("A");
    }

    public Flux<Consumption> findInactive() {
        return consumptionRepository.findByStatusWithNames("I");
    }

    // Mantener tu método original para obtener productos
    public Mono<ProductDTO> getProductFromExternal(Long productId) {
        return productWebClient.get()
                .uri("/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }
}