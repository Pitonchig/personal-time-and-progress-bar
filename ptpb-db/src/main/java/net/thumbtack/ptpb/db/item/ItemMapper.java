package net.thumbtack.ptpb.db.item;

import org.springframework.data.aerospike.repository.AerospikeRepository;

interface ItemMapper extends AerospikeRepository<Item, Long> {


}