package net.thumbtack.ptpb.db.user;

import org.springframework.data.aerospike.repository.AerospikeRepository;

import java.util.List;

interface UserMapper extends AerospikeRepository<User, Long> {
    List<User> findByName(String name);

    List<User> findByNameAndPassword(String name, String password);
}