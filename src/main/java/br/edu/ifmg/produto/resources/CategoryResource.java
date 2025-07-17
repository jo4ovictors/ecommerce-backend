package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/category")
@Tag(name = "Category", description = "Operations related to product categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @Operation(
            summary = "List all categories",
            description = "Returns a list of all categories.")
    @ApiResponse(
            responseCode = "200",
            description = "List of categories",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDTO.class)))
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Find category by ID",
            description = "Returns the category with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CategoryDTO> findById(
            @Parameter(description = "ID of the category to retrieve") @PathVariable Long id) {
        CategoryDTO dto = categoryService.findById(id);
        addCategoryLinks(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
            summary = "Insert a new category",
            description = "Creates a new category. Only ADMIN users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> insert(
            @Parameter(description = "Category data to insert") @Valid @RequestBody CategoryDTO dto) {
        dto = categoryService.insert(dto);
        addCategoryLinks(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Operation(
            summary = "Update category",
            description = "Updates the category with the given ID. Only ADMIN users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping(value = "/{id}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> update(
            @Parameter(description = "ID of the category to update") @PathVariable Long id,
            @Parameter(description = "Updated category data") @Valid @RequestBody CategoryDTO dto) {
        dto = categoryService.update(id, dto);
        addCategoryLinks(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(
            summary = "Delete category",
            description = "Deletes the category with the given ID. Only ADMIN users can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the category to delete") @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void addCategoryLinks(CategoryDTO dto) {
        dto.add(linkTo(methodOn(CategoryResource.class).findById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(CategoryResource.class).findAll()).withRel("all-categories"));
        dto.add(linkTo(methodOn(CategoryResource.class).delete(dto.getId())).withRel("delete"));
        dto.add(linkTo(methodOn(CategoryResource.class).update(dto.getId(), dto)).withRel("update"));
    }

}
