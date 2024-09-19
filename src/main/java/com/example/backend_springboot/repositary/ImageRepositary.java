package com.example.backend_springboot.repositary;

import com.example.backend_springboot.model.Image;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ImageRepositary {

    private List<Image> imageList = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong(1); // To simulate auto-increment IDs

    // Get all images
    public List<Image> findAll() {
        return new ArrayList<>(imageList);
    }

    // Save a new image
    public Image save(Image image) {
        if (image.getId() == null) {
            image.setId(idCounter.getAndIncrement());
        } else {
            // Remove the existing image if updating
            imageList.removeIf(img -> img.getId().equals(image.getId()));
        }
        imageList.add(image);
        return image;
    }

    // Find image by ID
    public Optional<Image> findById(Long id) {
        return imageList.stream()
                .filter(image -> image.getId().equals(id))
                .findFirst();
    }

    // Delete an image
    public void delete(Image image) {
        imageList.removeIf(img -> img.getId().equals(image.getId()));
    }
}
