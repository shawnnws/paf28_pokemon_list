package com.edu.suss_paf.paf_day28_pokemonworkshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.edu.suss_paf.paf_day28_pokemonworkshop.repository.PokemonRepository;

@Controller
@RequestMapping
public class PokemonController {
    
    @Autowired
    PokemonRepository pokemonRepository;

    @GetMapping("/")
    public ModelAndView display() {
        List<Document> pokemonTypes = new ArrayList<Document>();
        pokemonTypes = pokemonRepository.listPokemonTypes();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("display");
        mav.addObject("pokemonTypes", pokemonTypes);
        mav.setStatus(HttpStatusCode.valueOf(200));
        return mav;
    }

    @GetMapping("/{type}")
    public ModelAndView displayPokemonByType(@PathVariable String type) {

        List<Document> pokemonByType = new ArrayList<Document>();
        pokemonByType = pokemonRepository.listPokemonByType(type);
        System.out.println("Type: ---------" + pokemonByType);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("type");
        mav.addObject("pokemonByType", pokemonByType);
        mav.setStatus(HttpStatusCode.valueOf(200));
        return mav;
    }
}
