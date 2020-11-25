package com.example.springredditclone;

import com.example.springredditclone.dtos.PostDto;
import com.example.springredditclone.dtos.SubredditDto;
import com.example.springredditclone.responses.SubredditResponse;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class SpringRedditCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedditCloneApplication.class, args);
    }

    @Bean("CustomizedMapper")
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<List<PostDto>, Integer> subredditPostsListToPostsCountConverter =
                new AbstractConverter<>() {
                    @Override
                    protected Integer convert(List<PostDto> posts) {
                        if (posts != null) {
                            return posts.size();
                        } else {
                            return 0;
                        }
                    }
                };
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(SubredditDto.class, SubredditResponse.class).addMappings(mapper -> {
            mapper.using(subredditPostsListToPostsCountConverter)
                    .map(SubredditDto::getPosts, SubredditResponse::setPostsCount);
        });

        return modelMapper;
    }

}
