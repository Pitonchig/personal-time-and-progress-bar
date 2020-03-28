package net.thumbtack.ptpb.db.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {

    private final ItemMapper itemMapper;

    @Override
    public List<Item> getAllItems() {
        List<Item> items = new LinkedList<>();
        itemMapper.findAll().forEach(items::add);
        return items;
    }

    @Override
    public Optional<Item> getItemById(long id) {
        return itemMapper.findById(id);
    }

    @Override
    public void insertItem(Item item) {
        itemMapper.save(item);
    }

    @Override
    public void deleteAllItems() {
        itemMapper.deleteAll();
    }
}
