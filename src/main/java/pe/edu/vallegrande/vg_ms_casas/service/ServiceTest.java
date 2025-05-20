package pe.edu.vallegrande.vg_ms_casas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.vallegrande.vg_ms_casas.model.Home;
import pe.edu.vallegrande.vg_ms_casas.repository.HomeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private HomeRepository homeRepository;

    @InjectMocks
    private HomeService homeService;

    @Test
    public void testFindAll() {
        System.out.println("✅ Iniciando testFindAll()...");

        // Configuración: Crear datos de prueba y simular el comportamiento del repositorio
        Home home1 = new Home(1, "Home 1", "Address 1", "A");
        Home home2 = new Home(2, "Home 2", "Address 2", "A");
        when(homeRepository.findAll()).thenReturn(Flux.just(home1, home2));

        // Ejecución: Llamar al método del servicio
        Flux<Home> result = homeService.findAll();

        // Verificación: Asegurarse de que se devuelven ambos hogares
        StepVerifier.create(result)
                .expectNext(home1)
                .expectNext(home2)
                .verifyComplete();

        System.out.println("✔️ testFindAll() PASÓ: Verificó correctamente la lista completa de hogares.");
    }

    @Test
    public void testFindById() {
        System.out.println("✅ Iniciando testFindById()...");

        // Configuración: Simular un hogar específico por ID
        Home home = new Home(1, "Home 1", "Address 1", "A");
        when(homeRepository.findById(1)).thenReturn(Mono.just(home));

        // Ejecución: Buscar hogar por ID
        Mono<Home> result = homeService.findById(1);

        // Verificación: Comprobar que se devuelve el hogar correcto
        StepVerifier.create(result)
                .expectNext(home)
                .verifyComplete();

        System.out.println("✔️ testFindById() PASÓ: Hogar encontrado correctamente por ID.");
    }

    @Test
    public void testSave() {
        System.out.println("✅ Iniciando testSave()...");

        // Configuración: Simular el guardado de un nuevo hogar
        Home newHome = new Home(null, "New Home", "New Address", "A");
        Home savedHome = new Home(1, "New Home", "New Address", "A");
        when(homeRepository.save(newHome)).thenReturn(Mono.just(savedHome));

        // Ejecución: Guardar el hogar
        Mono<Home> result = homeService.save(newHome);

        // Verificación: Asegurarse de que el hogar se guardó con ID asignado
        StepVerifier.create(result)
                .expectNext(savedHome)
                .verifyComplete();

        System.out.println("✔️ testSave() PASÓ: Hogar guardado correctamente con ID generado.");
    }

    @Test
    public void testDelete() {
        System.out.println("✅ Iniciando testDelete()...");

        // Configuración: Simular la desactivación de un hogar
        Home home = new Home(1, "Home 1", "Address 1", "A");
        Home inactiveHome = new Home(1, "Home 1", "Address 1", "I");
        when(homeRepository.findById(1)).thenReturn(Mono.just(home));
        when(homeRepository.save(inactiveHome)).thenReturn(Mono.just(inactiveHome));

        // Ejecución: Desactivar hogar por ID
        Mono<Void> result = homeService.delete(1);

        // Verificación: Asegurarse de que se completó sin errores
        StepVerifier.create(result)
                .verifyComplete();

        System.out.println("✔️ testDelete() PASÓ: Hogar desactivado correctamente (estado = 'I').");
    }
}
