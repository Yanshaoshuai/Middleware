package org.shaoshuai.middleware.boot.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import org.shaoshuai.middleware.boot.pojo.Student;
import org.shaoshuai.middleware.boot.service.ElasticSearchService;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author yan
 * @Date 2026/1/6
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchServiceImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public CreateIndexResponse createStudentIndex() throws IOException {
        return elasticsearchClient.indices().create(c -> {
            TypeMapping typeMapping = new TypeMapping.Builder()
                    .properties("name", p -> p.text(t -> t.fields("keyword", k -> k.keyword(kk -> kk))))
                    .properties("age", p -> p.integer(i -> i))
                    .properties("createdDate", p -> p.date(d -> d))
                    .build();
            return c.mappings(typeMapping).index("student");
        });
    }

    @Override
    public DeleteIndexResponse deleteStudentIndex() throws IOException {
        return elasticsearchClient.indices().delete(d -> d.index("student"));
    }

    @Override
    public IndexResponse addOrReplace(Student student) throws IOException {
        return elasticsearchClient.index(i -> i.index("student").id(student.getRowId()).document(student));
    }

    @Override
    public UpdateResponse<Student> update(Student student) throws IOException {
        return elasticsearchClient.update(u -> u.index("student").id(student.getRowId()).doc(student), Student.class);
    }

    @Override
    public SearchResponse<Student> searchName(String name) throws IOException {
        return elasticsearchClient.search(s -> s.index("student").query(
                q -> q.bool(b -> b.must(m -> m.term(t -> t.field("name").value(name))))
        ), Student.class);
    }

    @Override
    public DeleteResponse deleteById(String rowId) throws IOException {
        return elasticsearchClient.delete(d -> d.index("student").id(rowId));
    }
}
