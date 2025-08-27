package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.exception.IllegalArgumentException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repo;

    public ProductServiceImpl (ProductRepository repo) {
        this.repo = repo;
    }
    @Override
    public ProductDTO create(ProductDTO dto) {
        Product p = repo.save(ProductMapper.toEntity(dto));
        return ProductMapper.toDTO(p);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        p.setName(dto.getName());
        p.setSku(dto.getSku());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        return ProductMapper.toDTO(p);
    }

    @Override
    public Page<ProductDTO> list(Pageable pageable) {

        return repo.findAll(pageable).map(ProductMapper::toDTO);
    }

    @Override
    public ProductDTO get(Long id) {
        Product p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: "+id));
        return ProductMapper.toDTO(p);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void decreaseStock(Product product, int qty) {
        int newStock = product.getStock() - qty;
        if (newStock < 0) throw new IllegalArgumentException("Insufficient stock for SKU " + product);
        product.setStock(newStock);
    }
}
