package com.example.demo.repository;

import com.example.demo.entity.Poem;
import com.example.demo.entity.VueJs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by linziyu on 2018/5/19.
 * dao层
 *
 */
public interface VueJsRepository extends ElasticsearchRepository<VueJs, Long>
{
    @Override
    Iterable<VueJs> findAll();

    Page<VueJs> findByMessageLike(String message, Pageable pageable);
}
