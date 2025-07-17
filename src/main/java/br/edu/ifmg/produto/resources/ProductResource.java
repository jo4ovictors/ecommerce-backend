package br.edu.ifmg.produto.resources;


import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.ProductListDTO;
import br.edu.ifmg.produto.dtos.StoreDTO;
import br.edu.ifmg.produto.services.AuthService;
import br.edu.ifmg.produto.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/product")
@Tag(name = "Product", description = "Controller/Resource for products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "List all products",
            description = "Returns a paginated list of all products.")
    @ApiResponse(
            responseCode = "200",
            description = "List retrieved successfully",
            content = @Content(mediaType = "application/json"))
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<ProductDTO>> findAll(@ParameterObject Pageable pageable) {
        Page<ProductDTO> page = productService.findAll(pageable);
        page.forEach(this::addProductLinks);
        return ResponseEntity.ok().body(page);
    }

    @Operation(
            summary = "Find product by ID",
            description = "Returns the product with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProductDTO> findById(
            @Parameter(description = "ID of the product to retrieve") @PathVariable Long id) {
        ProductDTO dto = productService.findById(id);
        addProductLinks(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
            summary = "Insert a new product",
            description = "Creates a new product. Only ADMIN or SELLER users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<ProductDTO> insert(
            @Parameter(description = "Product data to insert") @Valid @RequestBody ProductDTO dto) {
        dto = productService.insert(dto);
        addProductLinks(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(
            summary = "Update product",
            description = "Updates the product with the given ID. Only ADMIN or SELLER users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<ProductDTO> update(
            @Parameter(description = "ID of the product to update") @PathVariable Long id,
            @Parameter(description = "Updated product data") @Valid @RequestBody ProductDTO dto) {
        dto = productService.update(id, dto);
        addProductLinks(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
            summary = "Delete product",
            description = "Deletes the product with the given ID. Only ADMIN or SELLER users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the product to delete") @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Find products by category",
            description = "Returns a paginated list of products filtered by category ID and/or name.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/category", produces = "application/json")
    public ResponseEntity<Page<ProductListDTO>> findAllProductsByCategory(
            @Parameter(description = "Category ID to filter by", required = false)
            @RequestParam(value = "categoryId", required = false) Long categoryId,

            @Parameter(description = "Product name to search", required = false)
            @RequestParam(value = "name", required = false) String name,

            @ParameterObject Pageable pageable) {

        Page<ProductListDTO> dtos = productService.findAllProductsByCategory(categoryId, name, pageable);
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Get products by authenticated store",
            description = "Returns products from the authenticated store. Only ADMIN or SELLER users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/store")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SELLER')")
    public ResponseEntity<Page<ProductDTO>> getProductsByStoreId(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sorting field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sorting direction: asc or desc") @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        StoreDTO dto = authService.getAuthenticatedStore();

        Page<ProductDTO> products = productService.getProductsByStoreId(dto.getId(), pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Search products by name",
            description = "Returns a paginated list of products filtered by name.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<Page<ProductListDTO>> search(
            @Parameter(description = "Product name to search", required = false)
            @RequestParam(value = "name", required = false) String name,
            @ParameterObject Pageable pageable) {

        Page<ProductListDTO> dtos = productService.search(name, pageable);
        return ResponseEntity.ok(dtos);
    }

    private void addProductLinks(ProductDTO dto) {
        dto.add(linkTo(methodOn(ProductResource.class).findById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ProductResource.class).findAll(Pageable.unpaged())).withRel("all-products"));
        dto.add(linkTo(methodOn(ProductResource.class).delete(dto.getId())).withRel("delete"));
        dto.add(linkTo(methodOn(ProductResource.class).update(dto.getId(), dto)).withRel("update"));
    }

}
