package com.ajay.MyApp.controllers;

import com.ajay.MyApp.model.Post;
import com.ajay.MyApp.model.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("posts", postRepository.findAll());
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addPost() {
        ModelAndView mav = new ModelAndView("add_post");
        mav.addObject("post", new Post());
        return mav;
    }

    @PostMapping("/add")
    public String savePostToDb(@ModelAttribute Post post) {
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deletePost(@RequestParam int id) {
        postRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/populate")
    public String populate() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Post>> response = restTemplate.exchange(
                "https://jsonplaceholder.typicode.com/posts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Post>>() {}
        );
        postRepository.saveAll(response.getBody());
        return "redirect:/";
    }

    @GetMapping("/deleteAll")
    public String deleteAll() {
        postRepository.deleteAll();
        return "redirect:/";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
