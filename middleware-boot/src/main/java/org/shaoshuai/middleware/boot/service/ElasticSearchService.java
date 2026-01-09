package org.shaoshuai.middleware.boot.service;

import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import org.shaoshuai.middleware.boot.pojo.Student;

import java.io.IOException;

public interface ElasticSearchService {
    CreateIndexResponse createStudentIndex() throws IOException;

    DeleteIndexResponse deleteStudentIndex() throws IOException;

    IndexResponse addOrReplace(Student student) throws IOException;

    UpdateResponse<Student> update(Student student) throws IOException;

    SearchResponse<Student> searchName(String name) throws IOException;

    DeleteResponse deleteById(String rowId) throws IOException;
}
