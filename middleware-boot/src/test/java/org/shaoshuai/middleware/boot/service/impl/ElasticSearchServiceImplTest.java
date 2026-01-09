package org.shaoshuai.middleware.boot.service.impl;

import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shaoshuai.middleware.boot.pojo.Student;
import org.shaoshuai.middleware.boot.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static co.elastic.clients.elasticsearch._types.Result.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author yan
 * @Date 2026/1/6
 */
@SpringBootTest
@Slf4j
public class ElasticSearchServiceImplTest {
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Test
    public void testCreate() {
        try {
            CreateIndexResponse response = elasticSearchService.createStudentIndex();
            log.info("response: {}",response);
            Assertions.assertTrue(response.acknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDelete(){
        try {
            DeleteIndexResponse response = elasticSearchService.deleteStudentIndex();
            log.info("response: {}",response);
            Assertions.assertTrue(response.acknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddWithAddOrReplace(){
        try {
            IndexResponse response = elasticSearchService.addOrReplace(new Student("yan", 21, "2024-12-26T19:07:45Z"));
            log.info("response: {}",response);
            Assertions.assertSame(Created, response.result());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testReplaceWithAddOrReplace(){
        try {
            IndexResponse response = elasticSearchService.addOrReplace(new Student("n1V1oZsBFvv1ty-ybgFT","yan", 21, "2024-12-26T19:07:45Z"));
            log.info("response: {}",response);
            Assertions.assertSame(Updated,response.result());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdate(){
        try {
            UpdateResponse<Student> response = elasticSearchService.update(new Student("n1V1oZsBFvv1ty-ybgFT", "yan", 22, "2024-12-26T19:07:45Z"));
            log.info("response: {}",response);
            assertThat(response.result()).isIn(Updated,NoOp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSearch(){
        try {
            SearchResponse<Student> response = elasticSearchService.searchName("yan");
            log.info("response: {}",response);
            log.info("total: {}",response.hits().total());
            List<Student> students = response.hits().hits().stream().map(Hit::source).toList();
            log.info("students: {}",students);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteById(){
        try {
            DeleteResponse response = elasticSearchService.deleteById("n1V1oZsBFvv1ty-ybgFT");
            log.info("response: {}",response);
            Assertions.assertSame(Deleted,response.result());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
