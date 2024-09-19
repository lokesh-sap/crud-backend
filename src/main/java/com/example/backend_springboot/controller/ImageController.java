package com.example.backend_springboot.controller;

import com.example.backend_springboot.model.Image;
import com.example.backend_springboot.repositary.ImageRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/employees")
public class ImageController {

    @Autowired
   private ImageRepositary imageRepositary;

    @GetMapping("/images")
    public List<Image> getImages() {
        return imageRepositary.findAll();
    }
}
