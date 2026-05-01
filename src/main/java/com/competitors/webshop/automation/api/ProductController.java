package com.competitors.webshop.automation.api;

import com.competitors.webshop.automation.api.dto.ProductMatchResult;
import com.competitors.webshop.automation.api.dto.SaveDTO;
import com.competitors.webshop.automation.api.dto.SearchProductRequest;
import com.competitors.webshop.automation.model.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/search")
    public ResponseEntity<List<ProductMatchResult>> search(@Valid @RequestBody SearchProductRequest request) {
        List<Product> results = productService.findSimilar(request.productName());
        List<ProductMatchResult> response = results.stream()
                .map(r -> new ProductMatchResult(r.getPayload().getProductName(), r.getScore()))
                .toList();
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/save")
//    public void save(@Valid @RequestBody List<SaveDTO> products) {
//       products.forEach(product -> productService.saveCompetitorProduct(product.name()));
//    }
}