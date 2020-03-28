package net.thumbtack.ptpb.db.item;


import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {
        DbConfiguration.class,
        DbProperties.class,
        ItemDaoImpl.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemDaoTest {

    private final ItemDao itemDao;

    @BeforeEach
    void setup() {
        itemDao.deleteAllItems();
    }

    @Test
    void testInsertAndGetItemById() {
        long itemId = System.nanoTime();
        long projectId = System.nanoTime();

        Item item = Item.builder()
                .id(itemId)
                .projectId(projectId)
                .userName("user")
                .content("test item")
                .due(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .isCompleted(false)
                .priority(1)
                .build();
        itemDao.insertItem(item);

        Optional<Item> result = itemDao.getItemById(itemId);
        assertTrue(result.isPresent());
        assertEquals(item, result.get());
    }

    @Test
    void testGetAllItems() {
        List<Item> items = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            long itemId = System.nanoTime();
            long projectId = System.nanoTime();
            Item item = Item.builder()
                    .id(itemId)
                    .projectId(projectId)
                    .userName("user")
                    .content(String.format("test item %d", i))
                    .due(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                    .isCompleted(false)
                    .priority(1)
                    .build();
            items.add(item);
        }

        items.forEach(itemDao::insertItem);

        Item notInsertedItem = Item.builder()
                .id(System.nanoTime())
                .projectId(System.nanoTime())
                .userName("user")
                .content("not inserted test item")
                .due(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .isCompleted(false)
                .priority(1)
                .build();

        List<Item> results = itemDao.getAllItems();
        assertAll(
                () -> assertTrue(items.containsAll(results)),
                () -> assertEquals(items.size(), results.size()),
                () -> assertFalse(results.contains(notInsertedItem))
        );
    }

    @Test
    void testDeleteAllItems() {
        int count = 10;
        assertEquals(0, itemDao.getAllItems().size());

        for (int i = 0; i < count; i++) {
            long itemId = System.nanoTime();
            long projectId = System.nanoTime();
            Item item = Item.builder()
                    .id(itemId)
                    .projectId(projectId)
                    .userName("user")
                    .content(String.format("test item", i))
                    .due(LocalDateTime.now().plusDays(1))
                    .isCompleted(false)
                    .priority(1)
                    .build();
            itemDao.insertItem(item);
        }
        assertEquals(count, itemDao.getAllItems().size());
        itemDao.deleteAllItems();
        assertEquals(0, itemDao.getAllItems().size());
    }

}
