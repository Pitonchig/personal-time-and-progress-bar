package net.thumbtack.ptpb.db.session;

import org.springframework.data.aerospike.repository.AerospikeRepository;

interface SessionMapper extends AerospikeRepository<Session, Integer> {

}