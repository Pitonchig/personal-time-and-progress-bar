package net.thumbtack.ptpb.db.project;

import org.springframework.data.aerospike.repository.AerospikeRepository;

interface ProjectMapper extends AerospikeRepository<Project, Long> {

}