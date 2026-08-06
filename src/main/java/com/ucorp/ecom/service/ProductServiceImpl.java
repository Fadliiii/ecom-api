package com.ucorp.ecom.service;

import com.ucorp.ecom.exceptions.APIException;
import com.ucorp.ecom.exceptions.ResourceNotFoundException;
import com.ucorp.ecom.model.Category;
import com.ucorp.ecom.model.Product;
import com.ucorp.ecom.payload.ProductDTO;
import com.ucorp.ecom.payload.ProductResponseDTO;
import com.ucorp.ecom.repository.CategoryRepository;
import com.ucorp.ecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO add(Long categoryId, ProductDTO productDTO) {
        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        boolean isProductNotPresent=true;

        List<Product>products = category.getProducts();
        for(Product product:products){
            System.out.println(product.getProductName());
        }
        for (int i =0; i< products.size();i++) {
            if (products.get(i).getProductName().equals(productDTO.getProductName())) {
                System.out.println(productDTO.getProductName()+" sesudah masuk scope for");
                isProductNotPresent=false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    (product.getDiscount()* 0.01) * product.getPrice();
            product.setSpecialPrice(specialPrice);
            product.setImage("default.png");
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(product,ProductDTO.class);
        }else {
            throw  new APIException("Product alredy exist");
        }

    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
      Page<Product> page = productRepository.findAll(pageable);
      List<Product> products = page.getContent();

//        if (products.isEmpty()) {
//            throw  new APIException("No products found");
//        }
        List<ProductDTO> productResponseDTOs = products.stream()
              .map(product -> modelMapper.map(product,ProductDTO.class))
              .toList();
      ProductResponseDTO productResponseDTO = new ProductResponseDTO();
      productResponseDTO.setContent(productResponseDTOs);
      productResponseDTO.setPageNumber(page.getNumber());
      productResponseDTO.setPageSize(page.getSize());
      productResponseDTO.setTotalPages(page.getTotalPages());
      productResponseDTO.setTotalElements(page.getTotalElements());
      productResponseDTO.setLastPage(page.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchByCategory(Long categoryId,int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageable);
          List<Product> products =  productPage.getContent();
        if (products.isEmpty()) {
            throw  new APIException("No products found for category = "+category.getCategoryName() );
        }
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setPageSize(productPage.getSize());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productsPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%', pageable);
        List<Product>products = productsPage.getContent();
        if (products.isEmpty()) {
            throw  new APIException("No products found for given keyword= "+keyword);
        }
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);
        productResponseDTO.setPageNumber(productsPage.getNumber());
        productResponseDTO.setPageSize(productsPage.getSize());
        productResponseDTO.setTotalElements(productsPage.getTotalElements());
        productResponseDTO.setTotalPages(productsPage.getTotalPages());
        productResponseDTO.setLastPage(productsPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductDTO updateProducts(Long productId, ProductDTO productDTO) {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        Product product = modelMapper.map(productDTO, Product.class);
        if (productDTO.getProductName() != null) {
            productFromDb.setProductName(product.getProductName());
        }
        if(productDTO.getDescription() != null) {
            productFromDb.setDescription(product.getDescription());
        }
        if (productDTO.getQuantity() != null) {
            productFromDb.setQuantity(product.getQuantity());
        }
        if (productDTO.getPrice() != null) {
            productFromDb.setPrice(product.getPrice());
        }
        if (productDTO.getDiscount() != null) {
            productFromDb.setDiscount(product.getDiscount());
            double specialPrice = productFromDb.getPrice() -
                    (product.getDiscount()* 0.01) * productFromDb.getPrice();
            productFromDb.setSpecialPrice(specialPrice);
        }




        Product savedProduct = productRepository.save(productFromDb);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
       Product product = productRepository.findById(productId)
               .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
       productRepository.delete(product);
       return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //get product from db
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        //upload image to server
        //get the file name of uploaded image
        String fileName = fileService.uploadImage(path,image);

        //updating the new file name to the product
        productFromDb.setImage(fileName);

        //save updated product
        Product updatedProduct = productRepository.save(productFromDb);

        //return DTO after mapping product to dto
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }


}
