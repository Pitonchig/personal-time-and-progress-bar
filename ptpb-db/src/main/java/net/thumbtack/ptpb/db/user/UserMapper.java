package net.thumbtack.ptpb.db.user;

import org.springframework.data.aerospike.repository.AerospikeRepository;

interface UserMapper extends AerospikeRepository<User, Integer> {


}