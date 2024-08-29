package miu.edu.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miu.edu.productservice.dtos.ProductRequest;
import miu.edu.productservice.dtos.ProductResponse;
import miu.edu.productservice.model.Product;
import miu.edu.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product with {} has been saved ", product.getId());

    }

    public List<ProductResponse> findAllProducts() {

        List<Product> products = productRepository.findAll();
        return products.stream().map(this::toProductResponse).toList();
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
