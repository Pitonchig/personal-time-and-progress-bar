package net.thumbtack.ptpb.db.task;

import org.springframework.data.aerospike.repository.AerospikeRepository;

interface TaskMapper extends AerospikeRepository<Task, Integer> {


}