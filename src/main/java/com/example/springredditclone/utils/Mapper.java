package com.example.springredditclone.utils;
import org.modelmapper.ModelMapper;

public class Mapper {

    private static final ModelMapper mapper = new ModelMapper();

    private Mapper(){
    }

    public static ModelMapper getMapper(){
        return mapper;
    }

}
