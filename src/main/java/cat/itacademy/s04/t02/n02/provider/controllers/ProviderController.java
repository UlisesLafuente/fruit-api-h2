package cat.itacademy.s04.t02.n02.provider.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitDto;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderDto;
import cat.itacademy.s04.t02.n02.provider.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService){
        this.providerService=providerService;
    }

    @PostMapping
    public ResponseEntity<ProviderDto> addProvider(@Valid @RequestBody ProviderDto providerDto){
        ProviderDto created = providerService.addProvider(providerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProviderDto>> getAllProviders(){
        List<ProviderDto> providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDto> getProviderById(@PathVariable Long id) {
        ProviderDto provider = providerService.getProviderById(id);
        return ResponseEntity.ok(provider);
    }

    @GetMapping("/{id}/fruits")
    public ResponseEntity<List<FruitDto>> getFruitsByProviderId(@PathVariable Long id) {
        List<FruitDto> fruits = providerService.getFruitsByProviderId(id);
        return ResponseEntity.ok(fruits);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDto> updateProvider(@PathVariable Long id,
                                                @Valid @RequestBody ProviderDto providerDto) {
        ProviderDto updated = providerService.updateProvider(id, providerDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }

}
