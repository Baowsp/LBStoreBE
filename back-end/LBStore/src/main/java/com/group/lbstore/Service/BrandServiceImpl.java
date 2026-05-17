package com.group.lbstore.Service;

import com.group.lbstore.Model.Brand;
import com.group.lbstore.Repository.BrandRepository;
import com.group.lbstore.Service.BrandService;
import com.group.lbstore.Service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Brand createBrand(Brand brand) {
        if (brand.getLogoFile() != null && !brand.getLogoFile().isEmpty()) {
            String logoUrl = fileStorageService.storeFile(brand.getLogoFile());
            brand.setLogoUrl(logoUrl);
        }
        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Brand updateBrand(Long id, Brand brandDetails) {
        Brand existingBrand = getBrandById(id).orElseThrow();
        if (brandDetails.getLogoFile() != null && !brandDetails.getLogoFile().isEmpty()) {
            String logoUrl = fileStorageService.storeFile(brandDetails.getLogoFile());
            existingBrand.setLogoUrl(logoUrl);
        }
        existingBrand.setName(brandDetails.getName());
        existingBrand.setDescription(brandDetails.getDescription());
        return brandRepository.save(existingBrand);
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}