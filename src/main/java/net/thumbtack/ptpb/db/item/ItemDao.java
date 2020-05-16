package net.thumbtack.ptpb.db.item;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.project.Project;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemDao {
    private final ItemMapper itemMapper;

    public void insertItem(Item item) {
        itemMapper.save(item);
    }

    public List<Item> getItemsByProjectId(String id) {
        List<Item> items = new LinkedList<>();
        itemMapper.findByProjectId(id).forEach(items::add);
        return items;
    }

    public Optional<Item> getItemById(String itemId) {
        return itemMapper.findById(itemId);
    }

    public void updateItem(Item item) {
        itemMapper.save(item);
    }

    public void deleteItemById(String itemId) {
        itemMapper.deleteById(itemId);
    }
}
