package net.thumbtack.ptpb.db.item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    List<Item> getAllItems();

    Optional<Item> getItemById(long id);

    void insertItem(Item item);

    void deleteAllItems();

    List<Item> getItemsByProjectId(long id);

    void deleteItemById(long itemId);
}
