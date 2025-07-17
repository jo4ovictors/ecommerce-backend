package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.StoreDTO;
import br.edu.ifmg.produto.services.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/store")
@Tag(name = "Store", description = "Controller/Resource for stores")
public class StoreResource {

    @Autowired
    private StoreService storeService;

    @Operation(
            summary = "Get top stores",
            description = "Returns a list of top-rated or featured stores for homepage display.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of top stores",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StoreDTO.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/home", produces = "application/json")
    public ResponseEntity<List<StoreDTO>> getTopStores() {
        List<StoreDTO> stores = storeService.getTopStores();
        return ResponseEntity.ok(stores);
    }

}
