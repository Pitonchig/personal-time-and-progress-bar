package net.thumbtack.ptpb.db.project;

import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface ProjectMapper extends AerospikeRepository<Project, String > {
    Iterable<Project> findByUserId(String userId);
}
