package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.dtos.ProductListDTO;
import br.edu.ifmg.produto.dtos.StoreDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.entities.Store;
import br.edu.ifmg.produto.repository.ProductRepository;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("[!] -> Product not found!"));
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDTOToEntity(dto, entity);

        Store store = authService._getAuthenticatedStore();

        entity.setStore(store);

        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("[!] -> Product not found!"));
        copyDTOToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("[!] -> Product not found!"));

        Store store = product.getStore();
        if (store != null && store.getProducts() != null) {
            store.getProducts().remove(product);
        }

        productRepository.delete(product);
    }


    private void copyDTOToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());
        dto.getCategories().forEach(c -> entity.getCategories().add(new Category(c)));
    }

    private void copyDTOToEntityInsert(ProductDTO dto, Product entity, StoreDTO storeDTO) {
        this.copyDTOToEntity(dto, entity);
        entity.setStore(new Store(storeDTO));

    }

    @Transactional
    public Page<ProductListDTO> findAllProductsByCategory(
            Long categoryId,
            String name,
            Pageable pageable) {

        Page<Product> page = productRepository
                .findAllByCategoryAndName(categoryId, name, pageable);

        return page.map(ProductListDTO::new);
    }

    public Page<ProductListDTO> search(String name, Pageable pageable) {
        System.out.println("Searching products with name: " + name);
        Page<Product> page = productRepository.searchByName(name, pageable);

        return page.map(ProductListDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByStoreId(
            Long storeId,
            Pageable pageable) {

        Page<Product> page = productRepository
                .findByStoreId(storeId, pageable);

        return page.map(ProductDTO::new);
    }

}
